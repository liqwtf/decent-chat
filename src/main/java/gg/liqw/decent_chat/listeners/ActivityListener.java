package gg.liqw.decent_chat.listeners;

import gg.liqw.decent_chat.utils.PlayerTeam;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ActivityListener implements Listener {
    private final Plugin plugin;
    private final PlayerTeam playerTeam;

    public ActivityListener(Plugin plugin) {
        this.plugin = plugin;
        this.playerTeam = new PlayerTeam();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        String message = config.getString("messages.join");

        if (message == null) {
            event.joinMessage(null);
            return;
        }

        event.joinMessage(MiniMessage.miniMessage().deserialize(message, Placeholder.component("player", player.displayName().color(playerTeam.getTeamColor(player)))));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        String message = config.getString("messages.quit");

        if (message == null) {
            event.quitMessage(null);
            return;
        }

        event.quitMessage(MiniMessage.miniMessage().deserialize(message, Placeholder.component("player", player.displayName().color(playerTeam.getTeamColor(player)))));
    }
}
