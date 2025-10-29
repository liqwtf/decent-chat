package me.liqw.decent_chat;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.liqw.decent_chat.commands.*;
import me.liqw.decent_chat.commands.message.MessageCommand;
import me.liqw.decent_chat.commands.message.ReplyCommand;
import me.liqw.decent_chat.config.ColorConfig;
import me.liqw.decent_chat.listeners.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DecentChat extends JavaPlugin {
    private static DecentChat instance;
    private final ColorConfig colorConfig = new ColorConfig(this);

    public static DecentChat getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        LifecycleEventManager<Plugin> manager = getLifecycleManager();
        PluginManager pluginManager = getServer().getPluginManager();

        saveDefaultConfig();

        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register(HelpCommand.create(), "List all available commands", HelpCommand.ALIASES);
            commands.register(MessageCommand.create(), "Privately message another player", MessageCommand.ALIASES);
            commands.register(ReplyCommand.create(), "Reply to the last received message", ReplyCommand.ALIASES);
            commands.register(ColorCommand.create(), "Change name color in chat", ColorCommand.ALIASES);
            commands.register(NicknameCommand.create(), "Set your nickname", NicknameCommand.ALIASES);
            commands.register(EmojisCommand.create(), "List all available emojis", EmojisCommand.ALIASES);
        });

        pluginManager.registerEvents(new AdvancementListener(), this);
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PrepareAnvilListener(), this);
        pluginManager.registerEvents(new SleepListener(), this);
        pluginManager.registerEvents(new WorldChangeListener(), this);
    }

    public ColorConfig getColorConfig() {
        return colorConfig;
    }
}
