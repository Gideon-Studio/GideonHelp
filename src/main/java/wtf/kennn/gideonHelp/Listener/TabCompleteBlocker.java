package wtf.kennn.gideonHelp.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import wtf.kennn.gideonHelp.GideonHelp;


import java.util.List;

public class TabCompleteBlocker implements Listener {

    @EventHandler
    public void onTabComplete(PlayerCommandSendEvent event) {
        var player = event.getPlayer();
        var config = GideonHelp.getInstance().getConfig();

        // Check if feature is enabled
        if (!config.getBoolean("tab-complete-blocker.enabled", true)) return;

        // ✅ Admins bypass this system
        if (player.hasPermission("gideonhelp.admin")) return;

        List<String> allowed = config.getStringList("tab-complete-blocker.allowed-commands");

        // Remove any command not on the whitelist
        event.getCommands().removeIf(cmd -> !allowed.contains(cmd.toLowerCase()));
    }
}
