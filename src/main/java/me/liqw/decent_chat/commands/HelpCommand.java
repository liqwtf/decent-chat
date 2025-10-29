package me.liqw.decent_chat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.liqw.decent_chat.utils.Constants;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.List;

import static me.liqw.decent_chat.utils.Components.mm;

public class HelpCommand {
    public static Collection<String> ALIASES = List.of("chat");

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("decent-chat").executes(HelpCommand::response)
                .then(Commands.literal("help").executes(HelpCommand::response)).then(ReloadCommand.create())
                .then(ColorCommand.create()).build();
    }

    private static int response(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        Component help = mm(Constants.CHAT_TITLE + " Commands:");

        Collection<String> commands = List.of("/decent-chat reload", "/decent-chat help", "/color <color> [<player>]",
                "/emojis");

        for (String command : commands) {
            help = help.appendNewline().append(Component.empty().appendSpace().append(mm("<aqua>" + command)));
        }

        sender.sendMessage(help);

        return Command.SINGLE_SUCCESS;
    }
}
