package me.liqw.decent_chat.services;

import me.liqw.decent_chat.DecentChat;
import me.liqw.decent_chat.config.ColorConfig;
// NicknameService intentionally not imported here to avoid circular/unused imports
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static me.liqw.decent_chat.utils.Components.mm;

public class DisplayNameService {
    private static final DecentChat instance = DecentChat.getInstance();

    public static Component raw(Player player) {
        ColorConfig colorConfig = instance.getColorConfig();
        String color = colorConfig.getColor(player.getUniqueId(), "white");

        String tag = "<" + color + ">";
        if (color.contains(":")) {
            tag = "<gradient:" + color + ">";
        }

        return mm("<tag><player>", Placeholder.parsed("player", player.getName()), Placeholder.parsed("tag", tag));
    }

    public static Component chat(Player player) {
        ColorConfig colorConfig = instance.getColorConfig();
        String nickname = NicknameService.raw(player);

        if (nickname != null) {
            FileConfiguration config = instance.getConfig();
            String template = config.getString("nick-identifier.chat", "<gray>*</gray><nickname>");

            String display = nickname;

            String color = colorConfig.getColor(player.getUniqueId(), "white");
            String tag = "<" + color + ">";
            if (color.contains(":")) {
                tag = "<gradient:" + color + ">";
            }

            Component nicknameComponent = mm("<tag>" + display, Placeholder.parsed("tag", tag));

            nicknameComponent = nicknameComponent.hoverEvent(
                    HoverEvent.showText(mm("Original name: <name>", Placeholder.parsed("name", player.getName()))));

            Component result = mm(template, Placeholder.component("nickname", nicknameComponent),
                    Placeholder.parsed("player", player.getName()));

            return result;
        }

        String color = colorConfig.getColor(player.getUniqueId(), "white");

        String tag = "<" + color + ">";
        if (color.contains(":")) {
            tag = "<gradient:" + color + ">";
        }

        return mm("<tag><player>", Placeholder.parsed("player", player.getName()), Placeholder.parsed("tag", tag));
    }

    public static Component tab(Player player) {
        ColorConfig colorConfig = instance.getColorConfig();
        String nickname = NicknameService.raw(player);

        org.bukkit.configuration.file.FileConfiguration config = instance.getConfig();
        String template = config.getString("nick-identifier.tab", "<nickname> <gray>(<player>)</gray>");

        if (nickname == null) {
            String color = colorConfig.getColor(player.getUniqueId(), "white");
            String tag = "<" + color + ">";
            if (color.contains(":")) {
                tag = "<gradient:" + color + ">";
            }

            return mm("<tag><player>", Placeholder.parsed("player", player.getName()), Placeholder.parsed("tag", tag));
        }

        String display = nickname;

        String color = colorConfig.getColor(player.getUniqueId(), "white");
        String tag = "<" + color + ">";
        if (color.contains(":")) {
            tag = "<gradient:" + color + ">";
        }

        Component nickComp = mm("<tag>" + display, Placeholder.parsed("tag", tag));

        Component result = mm(template, Placeholder.component("nickname", nickComp),
                Placeholder.parsed("player", player.getName()));

        return result;
    }

    public static Component nametag(Player player) {
        return raw(player);
    }
}
