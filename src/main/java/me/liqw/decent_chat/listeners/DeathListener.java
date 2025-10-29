package me.liqw.decent_chat.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.regex.Pattern;

public class DeathListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Component deathMessage = event.deathMessage();

        if (deathMessage != null) {
            Pattern playerPattern = Pattern.compile("\\b" + Pattern.quote(player.getName()) + "\\b",
                    Pattern.CASE_INSENSITIVE);

            deathMessage = deathMessage.color(NamedTextColor.YELLOW).replaceText(
                    TextReplacementConfig.builder().match(playerPattern).replacement(player.displayName()).build());

            Player killer = player.getKiller();
            if (killer != null) {
                Pattern killerPattern = Pattern.compile("\\b" + Pattern.quote(killer.getName()) + "\\b",
                        Pattern.CASE_INSENSITIVE);

                deathMessage = deathMessage
                        .replaceText(builder -> builder.match(killerPattern).replacement(killer.displayName()));
            }

            event.deathMessage(deathMessage);
        }
    }
}
