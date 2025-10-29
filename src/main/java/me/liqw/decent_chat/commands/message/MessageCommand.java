package me.liqw.decent_chat.commands.message;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
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

public class MessageCommand {
    public static Collection<String> ALIASES = List.of("msg", "w", "whisper", "tell", "dm");
    private static final DecentChat instance = DecentChat.getInstance();

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("message").then(Commands.argument("target", ArgumentTypes.player())
                .then(Commands.argument("message", StringArgumentType.greedyString()).executes(ctx -> {
                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target",
                            PlayerSelectorArgumentResolver.class);
                    Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                    Player sender = (Player) ctx.getSource().getSender();
                    Component message = Component.text(ctx.getArgument("message", String.class));
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
                }))).build();
    }
}
