package me.liqw.decent_chat.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class NicknameConfig {
    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration config;

    public NicknameConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "nicknames.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save nicknames.yml: " + e.getMessage());
        }
    }

    public void setNickname(UUID playerId, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            config.set(String.valueOf(playerId), null);
        } else {
            config.set(String.valueOf(playerId), nickname);
        }

        save();
    }

    public void removeNickname(UUID playerId) {
        config.set(String.valueOf(playerId), null);

        save();
    }

    public String getNickname(UUID playerId, String fallback) {
        String value = config.getString(String.valueOf(playerId));
        if (value == null) {
            return fallback;
        }
        return value;
    }
}
