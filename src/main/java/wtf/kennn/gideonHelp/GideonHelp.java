package wtf.kennn.gideonHelp;

import co.aikar.commands.PaperCommandManager;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.kennn.gideonHelp.Commands.AdminCommand;
import wtf.kennn.gideonHelp.Commands.HelpCommand;
import wtf.kennn.gideonHelp.Listener.CommandBlocker;
import wtf.kennn.gideonHelp.Listener.TabCompleteBlocker;
import wtf.kennn.gideonHelp.Util.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GideonHelp extends JavaPlugin {

    private static GideonHelp instance;
    private PaperCommandManager commandManager;
    private YamlDocument menuFile;

    @Override
    public void onEnable() {
        instance = this;

        // 🌟 Tu mensaje bonito
        ChatUtil.log("&b--------------------------------------------");
        ChatUtil.log("&a         GideonHelp is starting...");
        ChatUtil.log("&7        Thank you for using my plugin!");
        ChatUtil.log("&b--------------------------------------------");

        // ⚙️ CARGA CONFIG
        saveDefaultConfig(); // solo si NO existe

        // ⚙️ CARGA MENU.YML
        loadMenuFile();

        // 📦 REGISTRO DE COMANDOS Y EVENTOS
        registerCommands();
        registerListeners();

        ChatUtil.log("&aGideonHelp has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        ChatUtil.log("&cGideonHelp has been disabled.");
    }

    // -----------------------------------------------------
    //             CARGA INICIAL DEL MENU.YML
    // -----------------------------------------------------
    private void loadMenuFile() {
        File file = new File(getDataFolder(), "menu.yml");

        if (!file.exists()) {
            ChatUtil.log("&eCreating default menu.yml...");
            saveResource("menu.yml", false);
        }

        try {
            menuFile = YamlDocument.create(file, getResource("menu.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Could not load menu.yml!");
        }
    }

    // -----------------------------------------------------
    //                   RELOAD SIN BORRAR
    // -----------------------------------------------------
    public void reloadAll() {
        reloadConfig(); // NO reescribe config

        try {
            if (menuFile != null) {
                menuFile.reload(); // NO reescribe menu.yml
            } else {
                loadMenuFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("Could not reload menu.yml!");
        }

        // 🌟 Tu mensaje bonito después de recargar
        ChatUtil.log("&b--------------------------------------------");
        ChatUtil.log("&a   Configuration files reloaded successfully!");
        ChatUtil.log("&b--------------------------------------------");
    }

    // -----------------------------------------------------
    //                  COMANDOS
    // -----------------------------------------------------
    private void registerCommands() {
        commandManager = new PaperCommandManager(this);

        // Autocompletado TAB para /ghelp mode
        commandManager.getCommandCompletions().registerStaticCompletion(
                "helpModes",
                Arrays.asList("TEXT", "MENU", "COMMAND")
        );

        commandManager.registerCommand(new HelpCommand());
        commandManager.registerCommand(new AdminCommand());
    }

    // -----------------------------------------------------
    //                  LISTENERS
    // -----------------------------------------------------
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new CommandBlocker(), this);
        getServer().getPluginManager().registerEvents(new TabCompleteBlocker(), this);
    }

    // -----------------------------------------------------
    //                   GETTERS
    // -----------------------------------------------------
    public static GideonHelp getInstance() {
        return instance;
    }

    public YamlDocument getMenuFile() {
        return menuFile;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }
}
