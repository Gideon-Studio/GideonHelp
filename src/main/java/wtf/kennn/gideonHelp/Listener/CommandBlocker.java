package wtf.kennn.gideonHelp.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import wtf.kennn.gideonHelp.GideonHelp;

import java.util.List;

public class CommandBlocker implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        var config = GideonHelp.getInstance().getConfig();

        // Feature toggle
        if (!config.getBoolean("command-blocker.enabled", true)) return;

        var player = event.getPlayer();

        // Admin bypass configurable
        boolean adminBypassEnabled = config.getBoolean("command-blocker.admin-bypass", true);
        if (adminBypassEnabled && player.hasPermission("gideonhelp.admin")) {
            return; // admins bypass the command blocker
        }

        // Normalize message (remove leading slash and lowercase)
        String full = event.getMessage();
        if (full.length() == 0) return;
        String message = full.startsWith("/") ? full.substring(1).toLowerCase() : full.toLowerCase();

        List<String> blocked = config.getStringList("command-blocker.blocked-commands");

        for (String blockedCmd : blocked) {
            if (blockedCmd == null) continue;
            String cmd = blockedCmd.toLowerCase().trim();
            if (cmd.isEmpty()) continue;

            // Match exact command or command + space (arguments)
            if (message.equals(cmd) || message.startsWith(cmd + " ")) {
                // Cancel and send message
                event.setCancelled(true);
                String msg = config.getString("command-blocker.message", "&cYou don't have permission to use this command.");
                player.sendMessage(msg.replace("&", "§"));
                break;
            }
        }
    }
}
