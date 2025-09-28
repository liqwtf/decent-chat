package gg.liqw.decent_chat.listeners;

import gg.liqw.decent_chat.utils.PlayerTeam;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;

public class SleepListener implements Listener {
    private final FileConfiguration config;
    private final Server server;

    public SleepListener(Plugin plugin) {
        this.config = plugin.getConfig();
        this.server = plugin.getServer();
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            PlayerTeam playerTeam = new PlayerTeam();
            String message = config.getString("messages.sleep");

            if (message != null) {
                server.sendMessage(MiniMessage.miniMessage().deserialize(message, Placeholder.component("player", player.displayName().color(playerTeam.getTeamColor(player)))));
            }
        }
    }
}
