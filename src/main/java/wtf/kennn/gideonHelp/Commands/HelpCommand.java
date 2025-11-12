package wtf.kennn.gideonHelp.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;
import wtf.kennn.gideonHelp.GideonHelp;
import wtf.kennn.gideonHelp.Menu.HelpMenu;
import wtf.kennn.gideonHelp.Util.ChatUtil;

@CommandAlias("help|ayuda")
@Description("Shows the help system based on the configured mode.")
public class HelpCommand extends BaseCommand {

    @Default
    public void onHelp(Player player) {
        var plugin = GideonHelp.getInstance();
        var config = plugin.getConfig();

        // Read the help mode
        String mode = config.getString("mode", "TEXT").toUpperCase();

        switch (mode) {
            case "MENU" -> {
                // Open the inventory menu
                new HelpMenu(player).open();
            }
            case "COMMAND" -> {
                // Execute custom command defined in config.yml
                String cmd = config.getString("execute-command", "menuhelp");
                player.closeInventory();
                player.performCommand(cmd);
            }
            case "TEXT" -> {
                // Send text help
                ChatUtil.sendTextHelp(player);
            }
            default -> {
                // Invalid mode, fallback to text
                player.sendMessage("§cInvalid mode in config.yml. Using TEXT as fallback.");
                ChatUtil.sendTextHelp(player);
            }
        }
    }
}
