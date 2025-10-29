package me.liqw.decent_chat.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class Components {
    public static Component mm(String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    public static Component mm(String input, TagResolver... tagResolvers) {
        return MiniMessage.miniMessage().deserialize(input, tagResolvers);
    }
}
