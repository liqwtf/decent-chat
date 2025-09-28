package gg.liqw.decent_chat.listeners;

import gg.liqw.decent_chat.utils.PlayerTeam;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ActivityListener implements Listener {
    private final FileConfiguration config;
    private final PlayerTeam playerTeam;

    public ActivityListener(Plugin plugin) {
        this.config = plugin.getConfig();
        this.playerTeam = new PlayerTeam();

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String message = config.getString("messages.join", "<player> <yellow>joined the game");

        event.joinMessage(MiniMessage.miniMessage().deserialize(message, Placeholder.component("player", player.displayName().color(playerTeam.getTeamColor(player)))));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String message = config.getString("messages.quit", "<player> <yellow>left the game");

        event.quitMessage(MiniMessage.miniMessage().deserialize(message, Placeholder.component("player", player.displayName().color(playerTeam.getTeamColor(player)))));
    }
}
