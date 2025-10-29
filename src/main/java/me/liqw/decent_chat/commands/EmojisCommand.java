package me.liqw.decent_chat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.utils.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class EmojisCommand {
    public static List<String> ALIASES = List.of("emotes");

    public static LiteralCommandNode<CommandSourceStack> create() {
        DecentChat instance = DecentChat.getInstance();

        return Commands.literal("emojis").executes(context -> {
            FileConfiguration config = instance.getConfig();
            ConfigurationSection emojiSection = config.getConfigurationSection("emojis");
            MiniMessage miniMessage = MiniMessage.miniMessage();
            CommandSender sender = context.getSource().getSender();

            Component emojis = miniMessage.deserialize(Constants.CHAT_TITLE + " Emojis:");

            if (emojiSection != null) {
                for (String emojiKey : emojiSection.getKeys(false)) {
                    String emojiValue = emojiSection.getString(emojiKey);

                    if (emojiValue != null) {
                        emojis = emojis.append(Component.empty().appendNewline().appendSpace()
                                .append(Component.text(emojiKey).color(NamedTextColor.AQUA))
                                .append(miniMessage.deserialize("<gray> - </gray>" + emojiValue)));
                    }
                }
            } else {
                emojis = emojis.append(miniMessage.deserialize("<red>This server does not have any emojis."));
            }

            sender.sendMessage(emojis);

            return Command.SINGLE_SUCCESS;
        }).build();
    }
}
