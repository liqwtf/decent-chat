package me.liqw.decent_chat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.config.ColorConfig;
import me.liqw.decent_chat.services.DisplayNameService;
import me.liqw.decent_chat.utils.color.ColorArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static me.liqw.decent_chat.utils.Components.mm;

public class ColorCommand {
    public static Collection<String> ALIASES = List.of("colour");
    private static final DecentChat instance = DecentChat.getInstance();
    private static ColorConfig colorConfig = instance.getColorConfig();

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("color").then(Commands.argument("color", new ColorArgument()).executes(ctx -> {
            String color = ctx.getArgument("color", String.class);
            Player sender = (Player) ctx.getSource().getSender();

            applyColor(sender, sender, color, true);

            return Command.SINGLE_SUCCESS;
        })).then(Commands.argument("target", ArgumentTypes.player())
                .requires(source -> source.getSender().hasPermission("decent-chat.set-color"))
                .then(Commands.argument("color", new ColorArgument()).executes(ctx -> {
                    String color = ctx.getArgument("color", String.class);
                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target",
                            PlayerSelectorArgumentResolver.class);
                    Player sender = (Player) ctx.getSource().getSender();
                    Player target = targetResolver.resolve(ctx.getSource()).getFirst();

                    applyColor(sender, target, color, false);

                    return Command.SINGLE_SUCCESS;
                }))).build();
    }

    private static void applyColor(Player sender, Player target, String color, boolean self) {
        boolean isGradient = color.contains(ColorArgument.SEPARATOR);
        String processedColor = color;
        Component preview;

        if (isGradient) {
            processedColor = color.replaceAll(Pattern.quote(ColorArgument.SEPARATOR), ":");

            Component colors = Component.empty();
            String[] parts = color.split(ColorArgument.SEPARATOR);
            int count = parts.length;
            for (int i = 0; i < count; i++) {
                String c = parts[i];
                colors = colors.append(mm("<" + c + ">" + c + "</" + c + ">"));

                if (count > 1) {
                    if (i < count - 2) {
                        colors = colors.append(Component.text(", "));
                    } else if (i == count - 2) {
                        colors = colors.append(Component.text(" and "));
                    }
                }
            }

            preview = colors;
        } else {
            preview = mm("<" + color + ">" + color + "</" + color + ">");
        }

        colorConfig.setColor(target.getUniqueId(), processedColor);
        // update both chat display and player list name using nickname-aware components
        target.displayName(DisplayNameService.chat(target));
        target.playerListName(DisplayNameService.tab(target));

        if (self) {
            sender.sendMessage(mm("Set your color to <preview>", Placeholder.component("preview", preview)));
        } else {
            String playerSuffix = "'s";
            if (target.getName().endsWith("s")) {
                playerSuffix = "'";
            }

            sender.sendMessage(mm("Set <target> color to <preview>",
                    Placeholder.component("target", target.displayName().append(Component.text(playerSuffix))),
                    Placeholder.component("preview", preview)));
            target.sendMessage(mm("<sender> set your color to <preview>",
                    Placeholder.component("sender", sender.displayName()), Placeholder.component("preview", preview)));
        }
    }
}
