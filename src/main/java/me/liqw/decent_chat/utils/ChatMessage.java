package me.liqw.decent_chat.utils;

import static me.liqw.decent_chat.utils.Components.mm;

import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.liqw.decent_chat.DecentChat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ChatMessage {
    private final static DecentChat instance = DecentChat.getInstance();
    private static FileConfiguration config = instance.getConfig();

    public static Component process(Player source, Component message, Audience viewer) {
        ConfigurationSection emojis = config.getConfigurationSection("emojis");
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

        if (emojis != null) {
            for (String emoji : emojis.getKeys(false)) {
                Pattern pattern = Pattern.compile(Pattern.quote(emoji), Pattern.CASE_INSENSITIVE);
                String replacement = emojis.getString(emoji);

                if (replacement != null) {
                    message = message.replaceText(builder -> builder.match(pattern).replacement(mm(replacement)));
                }
            }
        }

        if (config.getBoolean("chat-messages.mentions", true) && viewer instanceof Player mentioned) {
            Pattern pattern = Pattern.compile("@?\\b" + Pattern.quote(mentioned.getName()) + "\\b",
                    Pattern.CASE_INSENSITIVE);

            if (pattern.matcher(plainMessage).find() && mentioned != source) {
                message = message.replaceText(builder -> builder.match(pattern).replacement(
                        match -> mm("<yellow><player></yellow>", Placeholder.parsed("player", match.content()))));
                mentioned.playSound(
                        Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 0.5f, 1.0f));
            }
        }

        if (config.getBoolean("chat-messages.mentions", true) && source.hasPermission("decent-chat.mention-everyone")) {
            Pattern pattern = Pattern.compile("@everyone", Pattern.CASE_INSENSITIVE);

            if (pattern.matcher(plainMessage).find() && viewer != source) {
                message = message.replaceText(builder -> builder.match(pattern).replacement(
                        match -> mm("<yellow><everyone></yellow>", Placeholder.parsed("everyone", match.content()))));
                viewer.playSound(
                        Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 0.5f, 1.0f));
            }
        }

        Pattern itemPattern = Pattern.compile(Pattern.quote("<item>"), Pattern.CASE_INSENSITIVE);
        if (itemPattern.matcher(plainMessage).find()) {
            ItemStack held = source.getInventory().getItemInMainHand();

            if (held.getType().isAir()) {
                held = source.getInventory().getItemInOffHand();
            }

            if (held.getType().isAir()) {
                message = message
                        .replaceText(builder -> builder.match(itemPattern).replacement(mm("<gray>...</gray>")));
            } else {
                ItemStack displayItem = held.clone();
                displayItem.setAmount(1);
                Component item = Component.text().append(displayItem.displayName())
                        .hoverEvent(displayItem.asHoverEvent()).build();
                message = message.replaceText(builder -> builder.match(itemPattern).replacement(item));
            }
        }

        return message;
    }

    public static Component process(Player source, Component message) {
        return process(source, message, source);
    }
}
