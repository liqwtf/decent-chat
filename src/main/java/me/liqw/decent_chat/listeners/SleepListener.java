package me.liqw.decent_chat.listeners;

import me.liqw.decent_chat.DecentChat;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import static me.liqw.decent_chat.utils.Components.mm;

public class SleepListener implements Listener {
    private final static DecentChat instance = DecentChat.getInstance();

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            FileConfiguration config = instance.getConfig();
            String message = config.getString("system-messages.sleep");
            Server server = instance.getServer();

            if (message != null) {
                server.sendMessage(mm(message, Placeholder.component("player", player.displayName())));
            }
        }
    }
}
