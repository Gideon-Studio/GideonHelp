package wtf.kennn.gideonHelp;

import co.aikar.commands.PaperCommandManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.kennn.gideonHelp.Commands.AdminCommand;
import wtf.kennn.gideonHelp.Commands.HelpCommand;
import wtf.kennn.gideonHelp.Listener.CommandBlocker;
import wtf.kennn.gideonHelp.Listener.TabCompleteBlocker;
import wtf.kennn.gideonHelp.Util.YamlUtil;


import java.io.IOException;
import java.util.Arrays;

import static wtf.kennn.gideonHelp.Util.ChatUtil.log;

public class GideonHelp extends JavaPlugin {

    private static GideonHelp instance;
    private PaperCommandManager commandManager;
    private YamlDocument menuFile;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadMenuFile();

        commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerStaticCompletion(
                "helpModes",
                Arrays.asList("TEXT", "MENU", "COMMAND")
        );
        commandManager.registerCommand(new HelpCommand());
        commandManager.registerCommand(new AdminCommand());

        Bukkit.getPluginManager().registerEvents(new CommandBlocker(), this);
        Bukkit.getPluginManager().registerEvents( new TabCompleteBlocker(), this);

        log("       &b&lGIDEONHELP");
        log("");
        log("&fPlugin by &bGideon Studio");
        log("&fAuthor: &bKenn");
        log("&fStatus: &aEnabled");
        log("&fVersion: &a" + getDescription().getVersion());
        log("");

    }

    @Override
    public void onDisable() {
        log("       &b&lGIDEONHELP");
        log("");
        log("&fPlugin by  &bGideon Studio");
        log("&fAuthor: &bKenn");
        log("&fStatus: &cDisabled");
        log("&fVersion: &a" + getDescription().getVersion());
        log("");
    }

    public void loadMenuFile() {
        menuFile = new YamlUtil(this).load("menu.yml");
    }

    public void reloadAll() {
        reloadConfig();
        try {
            menuFile.reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GideonHelp getInstance() {
        return instance;
    }

    public YamlDocument getMenuFile() {
        return menuFile;
    }
}
