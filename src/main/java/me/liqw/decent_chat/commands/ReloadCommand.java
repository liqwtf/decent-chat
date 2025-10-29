package me.liqw.decent_chat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.utils.Constants;
import org.bukkit.command.CommandSender;

import static me.liqw.decent_chat.utils.Components.mm;

public class ReloadCommand {
    private static final DecentChat instance = DecentChat.getInstance();

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("reload").requires(source -> source.getSender().hasPermission("decent-chat.reload"))
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();

                    instance.reloadConfig();
                    sender.sendMessage(mm(Constants.CHAT_TITLE + " has been <green>reloaded</green>."));

                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
