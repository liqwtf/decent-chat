package gg.liqw.decent_chat;

import gg.liqw.decent_chat.listeners.ActivityListener;
import gg.liqw.decent_chat.listeners.ChatListener;
import gg.liqw.decent_chat.listeners.SleepListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DecentChat extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new ActivityListener(this), this);
        pluginManager.registerEvents(new SleepListener(this), this);
    }
}
