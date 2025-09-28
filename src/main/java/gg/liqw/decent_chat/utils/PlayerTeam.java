package gg.liqw.decent_chat.utils;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class PlayerTeam {
    public TextColor getTeamColor(Player player) {
        Team team = player.getScoreboard().getPlayerTeam(player);

        if (team != null) {
            return team.color();
        }

        return TextColor.color(0xFFFFFF);
    }
}
