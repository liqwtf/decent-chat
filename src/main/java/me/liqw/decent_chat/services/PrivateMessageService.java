package me.liqw.decent_chat.services;

import me.liqw.decent_chat.DecentChat;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrivateMessageService {
    private static final DecentChat instance = DecentChat.getInstance();
    private static final Map<UUID, UUID> lastMessageSender = new HashMap<>();

    public static void recordMessage(Player receiver, Player sender) {
        lastMessageSender.put(receiver.getUniqueId(), sender.getUniqueId());
    }

    public static UUID getLastSender(Player receiver) {
        return lastMessageSender.get(receiver.getUniqueId());
    }

    public static Player getLastSenderPlayer(Player receiver) {
        UUID senderUuid = getLastSender(receiver);

        if (senderUuid == null) {
            return null;
        }

        Server server = instance.getServer();

        return server.getPlayer(senderUuid);
    }

    public static void clearLastSender(Player player) {
        lastMessageSender.remove(player.getUniqueId());
    }
}
