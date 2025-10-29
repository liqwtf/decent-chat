package me.liqw.decent_chat.listeners;

import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.services.PrivateMessageService;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.liqw.decent_chat.utils.Components.mm;

public class PlayerQuitListener implements Listener {
    private final DecentChat instance = DecentChat.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = instance.getConfig();
        String message = config.getString("system-messages.quit");

        if (message == null) {
            event.quitMessage(null);
            return;
        }

        event.quitMessage(mm(message, Placeholder.component("player", player.displayName())));

        PrivateMessageService.clearLastSender(player);
    }
}
