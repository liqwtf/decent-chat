package me.liqw.decent_chat.listeners;

import io.papermc.paper.advancement.AdvancementDisplay;
import me.liqw.decent_chat.DecentChat;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import static me.liqw.decent_chat.utils.Components.mm;

public class AdvancementListener implements Listener {
    private final DecentChat instance = DecentChat.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        AdvancementDisplay advancementDisplay = event.getAdvancement().getDisplay();
        Player player = event.getPlayer();
        ;

        if (advancementDisplay != null && !advancementDisplay.isHidden()) {
            String messageType = switch (advancementDisplay.frame()) {
                case CHALLENGE -> "challenge";
                case GOAL -> "goal";
                case TASK -> "task";
            };

            String message = instance.getConfig().getString("system-messages.advancement." + messageType);

            if (message != null) {
                event.message(mm(message,
                        Placeholder.component("player", player.displayName()),
                        Placeholder.component("advancement", advancementDisplay.displayName())));
            } else {
                event.message(null);
            }
        } else {
            event.message(null);
        }
    }
}
