package gg.liqw.decent_chat.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {
    private final Plugin plugin;

    public Reload(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String [] args) {
        plugin.reloadConfig();
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<aqua>Decent Chat</aqua> has been <green>reloaded</green>."));
        return true;
    }
}
