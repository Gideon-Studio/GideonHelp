package wtf.kennn.gideonHelp.Util;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YamlUtil {

    private final JavaPlugin plugin;

    public YamlUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public YamlDocument load(String fileName) {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()) plugin.saveResource(fileName, false);
            return YamlDocument.create(file, plugin.getResource(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Could not load " + fileName, e);
        }
    }
}
