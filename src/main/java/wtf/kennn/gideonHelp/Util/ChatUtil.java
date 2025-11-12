package wtf.kennn.gideonHelp.Util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import wtf.kennn.gideonHelp.GideonHelp;


import java.util.ArrayList;
import java.util.List;

public class ChatUtil {

    /**
     * Sends the /help text mode list to the player.
     */
    public static void sendTextHelp(Player player) {
        var plugin = GideonHelp.getInstance();
        var config = plugin.getConfig();

        String prefix = color(config.getString("prefix"));
        boolean clickable = config.getBoolean("clickable-text", true);

        player.sendMessage("§7§m----------------------------------");
        player.sendMessage(prefix + " §aAvailable Commands:");

        for (String line : config.getStringList("text-help")) {
            String colored = color(line);

            if (clickable) {
                // Create a clickable and hoverable text component
                TextComponent component = new TextComponent(colored);
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§7Click to run this command").create()));

                // Extract the first word (command)
                String[] parts = line.split(" ");
                if (parts.length > 0) {
                    String cmd = parts[0].replace("&", "").replace("§", "");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
                }

                player.spigot().sendMessage(component);
            } else {
                player.sendMessage(colored);
            }
        }

        player.sendMessage("§7§m----------------------------------");
    }

    /**
     * Translates color codes using '&'.
     */
    public static String color(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Translates a list of strings using '&' color codes.
     */
    public static List<String> color(List<String> input) {
        List<String> output = new ArrayList<>();
        if (input == null) return output;
        for (String line : input) {
            output.add(color(line));
        }
        return output;
    }

    /**
     * Sends a colored message to the console with prefix.
     */
    public static void log(String message) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(color(message));
    }
}
