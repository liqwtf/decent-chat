package me.liqw.decent_chat.listeners;

import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.services.DisplayNameService;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.liqw.decent_chat.utils.Components.mm;

public class PlayerJoinListener implements Listener {
    private final DecentChat instance = DecentChat.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = instance.getConfig();
        String message = config.getString("system-messages.join");

        player.displayName(DisplayNameService.chat(player));
        player.playerListName(DisplayNameService.tab(player));

        if (message == null) {
            event.joinMessage(null);
            return;
        }

        event.joinMessage(mm(message, Placeholder.component("player", player.displayName())));
    }
}
