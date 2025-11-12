package wtf.kennn.gideonHelp.Menu;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import wtf.kennn.gideonHelp.GideonHelp;

import java.util.List;

public class HelpMenu implements Listener {

    private final Player player;
    private final Inventory inv;

    public HelpMenu(Player player) {
        this.player = player;
        YamlDocument menu = GideonHelp.getInstance().getMenuFile();

        String title = color(menu.getString("menu.title"));
        int size = menu.getInt("menu.size", 27);
        this.inv = Bukkit.createInventory(null, size, title);

        Bukkit.getPluginManager().registerEvents(this, GideonHelp.getInstance());
        setupItems(menu);
    }

    private void setupItems(YamlDocument menu) {
        // ✅ Obtener la sección de los ítems correctamente en BoostedYAML
        Section itemsSection = menu.getSection("menu.items");
        if (itemsSection == null) return;

        for (String key : itemsSection.getRoutesAsStrings(false)) {
            String path = "menu.items." + key;

            String materialName = menu.getString(path + ".material", "PAPER");
            Material mat = Material.matchMaterial(materialName.toUpperCase());
            if (mat == null) continue;

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(color(menu.getString(path + ".name", "&7Unnamed")));
            List<String> lore = menu.getStringList(path + ".lore").stream().map(this::color).toList();
            meta.setLore(lore);
            item.setItemMeta(meta);

            // ✅ Convertir el key (slot) a número de forma segura
            try {
                int slot = Integer.parseInt(key);
                if (slot >= 0 && slot < inv.getSize()) {
                    inv.setItem(slot, item);
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    public void open() {
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String title = color(GideonHelp.getInstance().getMenuFile().getString("menu.title", "&bHelp Menu"));
        if (!e.getView().getTitle().equals(title)) return;

        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) return;

        int slot = e.getSlot();
        String path = "menu.items." + slot;
        String command = GideonHelp.getInstance().getMenuFile().getString(path + ".command", "");

        if (command.equalsIgnoreCase("close")) {
            e.getWhoClicked().closeInventory();
            return;
        }

        if (!command.isEmpty()) {
            e.getWhoClicked().closeInventory();
            Bukkit.dispatchCommand(e.getWhoClicked(), command);
        }
    }

    private String color(String text) {
        return text == null ? "" : ChatColor.translateAlternateColorCodes('&', text);
    }
}
