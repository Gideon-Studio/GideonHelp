package wtf.kennn.gideonHelp.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;
import wtf.kennn.gideonHelp.GideonHelp;
import wtf.kennn.gideonHelp.Util.ChatUtil;

@CommandAlias("ghelp")
@CommandPermission("gideonhelp.admin")
@Description("Administrative commands for GideonHelp.")
public class AdminCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        player.sendMessage("§7§m----------------------------------");
        player.sendMessage("§bGideonHelp §7Admin Commands:");
        player.sendMessage(" §8• §a/ghelp reload §7→ Reloads all config files");
        player.sendMessage(" §8• §a/ghelp mode <TEXT|MENU|COMMAND> §7→ Change help display mode");
        player.sendMessage(" §8• §a/ghelp info §7→ View current configuration info");
        player.sendMessage("§7§m----------------------------------");
    }

    @Subcommand("reload")
    @Description("Reloads config.yml and menu.yml")
    public void onReload(Player player) {
        GideonHelp.getInstance().reloadAll();
        ChatUtil.log("&aConfiguration files successfully reloaded by &b" + player.getName());
        player.sendMessage("§aAll configuration files have been reloaded!");
    }

    @Subcommand("mode")
    @Syntax("<TEXT|MENU|COMMAND>")
    @CommandCompletion("@helpModes") // 💡 usa el completador que registramos en el main
    @Description("Changes the help display mode.")
    public void onChangeMode(Player player, String mode) {
        var plugin = GideonHelp.getInstance();
        var config = plugin.getConfig();

        String newMode = mode.toUpperCase();

        if (!newMode.equals("TEXT") && !newMode.equals("MENU") && !newMode.equals("COMMAND")) {
            player.sendMessage("§cInvalid mode! §7Available: §fTEXT, MENU, COMMAND");
            return;
        }

        config.set("mode", newMode);
        plugin.saveConfig();

        String msg = config.getString("messages.mode-changed", "&aMode changed to &b%mode%");
        msg = msg.replace("%mode%", newMode);
        player.sendMessage(ChatUtil.color(msg));
        ChatUtil.log("&e" + player.getName() + " &7changed GideonHelp mode to &b" + newMode);
    }

    @Subcommand("info")
    @Description("Shows the current plugin status and mode.")
    public void onInfo(Player player) {
        var config = GideonHelp.getInstance().getConfig();

        String mode = config.getString("mode", "TEXT").toUpperCase();
        boolean tabEnabled = config.getBoolean("tab-complete-blocker.enabled", true);
        boolean cmdEnabled = config.getBoolean("command-blocker.enabled", true);

        player.sendMessage("§7§m----------------------------------");
        player.sendMessage("§bGideonHelp §7Status:");
        player.sendMessage(" §8• §7Current mode: §a" + mode);
        player.sendMessage(" §8• §7TabCompleteBlocker: " + (tabEnabled ? "§aEnabled" : "§cDisabled"));
        player.sendMessage(" §8• §7CommandBlocker: " + (cmdEnabled ? "§aEnabled" : "§cDisabled"));
        player.sendMessage("§7§m----------------------------------");
    }
}
