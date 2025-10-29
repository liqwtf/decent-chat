package me.liqw.decent_chat.services;

import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.config.NicknameConfig;
import org.bukkit.entity.Player;

public class NicknameService {
    private static final DecentChat instance = DecentChat.getInstance();
    private static final NicknameConfig nickConfig = new NicknameConfig(instance);

    public static void setNickname(Player player, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            nickConfig.removeNickname(player.getUniqueId());
        } else {
            nickConfig.setNickname(player.getUniqueId(), nickname);
        }

        player.displayName(DisplayNameService.chat(player));
        player.playerListName(DisplayNameService.tab(player));
    }

    public static String raw(Player player) {
        return nickConfig.getNickname(player.getUniqueId(), null);
    }
}
