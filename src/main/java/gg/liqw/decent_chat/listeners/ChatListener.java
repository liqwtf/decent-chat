package gg.liqw.decent_chat.listeners;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.regex.Pattern;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import gg.liqw.decent_chat.utils.PlayerTeam;

public class ChatListener implements Listener, ChatRenderer {
    private final FileConfiguration config;
    private final MiniMessage miniMessage;

    public ChatListener(Plugin plugin) {
        this.config = plugin.getConfig();
        this.miniMessage = MiniMessage.miniMessage();
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(this);
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        return MiniMessage.miniMessage().deserialize(config.getString("chat-format", "<player>: <message>"), Placeholder.component("player", processPlayer(source)), Placeholder.component("message", processMessage(source, message, viewer)));
    }

    private Component processPlayer(Player source) {
        PlayerTeam playerTeam = new PlayerTeam();

        return source.displayName().color(playerTeam.getTeamColor(source));
    }

    private Component processMessage(Player source, Component message, Audience viewer) {
        Component processedMessage = message;

        if (config.getBoolean("mentions", true) && viewer instanceof Player mentioned) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(mentioned.getName()) + "\\b", Pattern.CASE_INSENSITIVE);

            if (pattern.matcher(message.toString()).find() && mentioned != source) {
                processedMessage = processedMessage.replaceText(builder -> builder.match(pattern).replacement(Component.text(mentioned.getName()).color(NamedTextColor.YELLOW)));

                mentioned.playSound(Sound.sound(org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, Sound.Source.MASTER, 1.0f, 1.0f));
            }
        }

        ConfigurationSection emojis = config.getConfigurationSection("emojis");
        if (emojis != null) {
            for (String pattern : emojis.getKeys(false)) {
                String replacement = emojis.getString(pattern);
                if (replacement != null) {
                    processedMessage = processedMessage.replaceText(builder -> builder.matchLiteral(pattern).replacement(MiniMessage.miniMessage().deserialize(replacement)));
                }
            }
        }

        return processedMessage;
    }
}




