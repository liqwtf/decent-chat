package me.liqw.decent_chat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.config.ColorConfig;
import me.liqw.decent_chat.services.DisplayNameService;
import me.liqw.decent_chat.services.NicknameService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

import static me.liqw.decent_chat.utils.Components.mm;

public class NicknameCommand {
    public static Collection<String> ALIASES = List.of("nick");
    private static final DecentChat instance = DecentChat.getInstance();
    private static final ColorConfig colorConfig = instance.getColorConfig();

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("nickname").executes(NicknameCommand::resetNickname)
                .then(Commands.literal("reset").executes(NicknameCommand::resetNickname))
                .then(Commands.literal("clear").executes(NicknameCommand::resetNickname))
                .then(Commands.argument("nickname", StringArgumentType.greedyString()).executes(ctx -> {
                    String nick = ctx.getArgument("nickname", String.class).trim();
                    Player sender = (Player) ctx.getSource().getSender();
                    String color = colorConfig.getColor(sender.getUniqueId(), "white");

                    if (nick.equalsIgnoreCase(sender.getName())) {
                        resetNickname(ctx);
                        return Command.SINGLE_SUCCESS;
                    }

                    if (nick.length() > 32) {
                        sender.sendMessage(mm("<red>Nickname is too long (max 32 characters)."));
                        return Command.SINGLE_SUCCESS;
                    }

                    NicknameService.setNickname(sender, nick);

                    String tag = "<" + color + ">";
                    if (color.contains(":")) {
                        tag = "<gradient:" + color + ">";
                    }

                    sender.sendMessage(mm("Set your nickname to <nick>",
                            Placeholder.component("nick", mm("<tag>" + nick, Placeholder.parsed("tag", tag)))));
                    sender.playerListName(DisplayNameService.tab(sender));

                    return Command.SINGLE_SUCCESS;
                })).build();
    }

    private static int resetNickname(CommandContext<CommandSourceStack> ctx) {
        Player sender = (Player) ctx.getSource().getSender();
        NicknameService.setNickname(sender, null);
        Component original = DisplayNameService.chat(sender);

        sender.displayName(original);
        sender.playerListName(DisplayNameService.tab(sender));
        sender.sendMessage(mm("Your nickname has been reset."));

        return Command.SINGLE_SUCCESS;
    }
}
