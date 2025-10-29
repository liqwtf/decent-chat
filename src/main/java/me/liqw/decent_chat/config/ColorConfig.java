package me.liqw.decent_chat.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ColorConfig {
    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration config;

    public ColorConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "colors.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save colors.yml: " + e.getMessage());
        }
    }

    public void setColor(UUID playerId, String color) {
        config.set(String.valueOf(playerId), color);
        save();
    }

    public String getColor(UUID playerId, String fallback) {
        String value = config.getString(String.valueOf(playerId));
        if (value == null) {
            return fallback;
        }
        return value;
    }
}
