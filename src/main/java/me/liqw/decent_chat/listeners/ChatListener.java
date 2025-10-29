package me.liqw.decent_chat.listeners;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.services.DisplayNameService;
import me.liqw.decent_chat.utils.ChatMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.liqw.decent_chat.utils.Components.mm;

public class ChatListener implements Listener, ChatRenderer {
    private final DecentChat instance = DecentChat.getInstance();
    private FileConfiguration config = instance.getConfig();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        config = instance.getConfig();

        event.renderer(this);
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        String format = config.getString("chat-message.format", "<player>: <message>");

        Component playerComp = DisplayNameService.chat(source);

        return mm(format, Placeholder.component("player", playerComp),
                Placeholder.component("message", ChatMessage.process(source, message, viewer)));
    }
}
