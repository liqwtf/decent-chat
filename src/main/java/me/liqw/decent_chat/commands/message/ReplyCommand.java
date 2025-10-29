package me.liqw.decent_chat.commands.message;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.services.PrivateMessageService;
import me.liqw.decent_chat.utils.ChatMessage;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

import static me.liqw.decent_chat.utils.Components.mm;

public class ReplyCommand {
    public static Collection<String> ALIASES = List.of("r");
    private static final DecentChat instance = DecentChat.getInstance();

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("reply")
                .then(Commands.argument("message", StringArgumentType.greedyString()).executes(ctx -> {
                    Component message = Component.text(ctx.getArgument("message", String.class));
                    Player sender = (Player) ctx.getSource().getSender();
                    Player target = PrivateMessageService.getLastSenderPlayer(sender);

                    if (target == null) {
                        sender.sendMessage(mm("<red>You haven't received any messages recently."));
                        return Command.SINGLE_SUCCESS;
                    }

                    if (!target.isOnline()) {
                        sender.sendMessage(mm("<red>The player you're trying to reply to is no longer online."));
                        return Command.SINGLE_SUCCESS;
                    }

                    FileConfiguration config = instance.getConfig();

                    String toFormat = config.getString("chat-message.whisper.to",
                            "<dark_aqua>To <player><gray>: <message>");
                    String fromFormat = config.getString("chat-message.whisper.from",
                            "<dark_aqua>From <player><gray>: <message>");

                    sender.sendMessage(mm(toFormat, Placeholder.component("player", target.displayName()),
                            Placeholder.component("message", ChatMessage.process(sender, message))));
                    target.sendMessage(mm(fromFormat, Placeholder.component("player", sender.displayName()),
                            Placeholder.component("message", ChatMessage.process(sender, message))));
                    target.playSound(Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER,
                            0.5f, 1.0f));

                    PrivateMessageService.recordMessage(target, sender);

                    return Command.SINGLE_SUCCESS;
                })).build();
    }
}
