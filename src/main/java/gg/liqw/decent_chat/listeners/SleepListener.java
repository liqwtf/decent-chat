package gg.liqw.decent_chat.listeners;

import gg.liqw.decent_chat.utils.PlayerTeam;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;

public class SleepListener implements Listener {
    private final Plugin plugin;

    public SleepListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            Player player = event.getPlayer();
            PlayerTeam playerTeam = new PlayerTeam();
            FileConfiguration config = plugin.getConfig();
            String message = config.getString("messages.sleep");

            if (message != null) {
                plugin.getServer().sendMessage(MiniMessage.miniMessage().deserialize(message, Placeholder.component("player", player.displayName().color(playerTeam.getTeamColor(player)))));
            }
        }
    }
}
