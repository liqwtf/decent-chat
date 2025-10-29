package me.liqw.decent_chat.listeners;

import me.liqw.decent_chat.DecentChat;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static me.liqw.decent_chat.utils.Components.mm;

public class WorldChangeListener implements Listener {
    private final DecentChat instance = DecentChat.getInstance();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        Server server = instance.getServer();
        FileConfiguration config = instance.getConfig();
        String message = config.getString("system-messages.world-change." + world);

        if (player.isInvisible() || (player.getInventory().getHelmet() != null
                && player.getInventory().getHelmet().getType().toString().equalsIgnoreCase("CARVED_PUMPKIN")))
            return;

        if (config.getString("system-messages.world-change") != null) {
            server.sendMessage(mm(message, Placeholder.component("player", player.displayName())));
        }
    }
}
