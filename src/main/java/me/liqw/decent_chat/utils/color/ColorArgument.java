package me.liqw.decent_chat.utils.color;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class ColorArgument implements CustomArgumentType.Converted<String, String> {
    private static final DynamicCommandExceptionType ERROR_INVALID_COLOR = new DynamicCommandExceptionType(
            color -> MessageComponentSerializer.message().serialize(Component.text(color + " is not a valid color!")));
    private static final DynamicCommandExceptionType ERROR_INVALID_GRADIENT = new DynamicCommandExceptionType(
            color -> MessageComponentSerializer.message()
                    .serialize(Component.text(color + " is not allowed in gradients!")));
    public static final String SEPARATOR = ",";

    private static final java.util.regex.Pattern HEX_PATTERN = java.util.regex.Pattern
            .compile("#([0-9a-fA-F]{3}|[0-9a-fA-F]{6})");

    private static String expandShortHex(String hexLower) {
        if (hexLower.length() == 4) { // #rgb -> #rrggbb
            char r = hexLower.charAt(1);
            char g = hexLower.charAt(2);
            char b = hexLower.charAt(3);
            return ("#" + r + r + g + g + b + b).toLowerCase(Locale.ROOT);
        }
        return hexLower.toLowerCase(Locale.ROOT);
    }

    @Override
    public String convert(String nativeType) throws CommandSyntaxException {
        String value = nativeType.toLowerCase(Locale.ROOT);

        if (!value.contains(SEPARATOR)) {
            try {
                Color.valueOf(value.toUpperCase(Locale.ROOT));
                return value;
            } catch (IllegalArgumentException ignored) {
                if (value.startsWith("#") && HEX_PATTERN.matcher(value).matches()) {
                    String lower = value.toLowerCase(Locale.ROOT);
                    return expandShortHex(lower);
                }

                throw ERROR_INVALID_COLOR.create(nativeType);
            }
        }

        String[] parts = value.split(SEPARATOR);
        if (parts.length < 2) {
            throw ERROR_INVALID_COLOR.create(nativeType);
        }

        String[] normalized = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String partLower = part.toLowerCase(Locale.ROOT);

            if (partLower.equals(Color.RAINBOW.toString())) {
                throw ERROR_INVALID_GRADIENT.create(part);
            }

            try {
                Color.valueOf(partLower.toUpperCase(Locale.ROOT));
                normalized[i] = partLower;
                continue;
            } catch (IllegalArgumentException ignored) {
                // not a named color -> maybe hex
            }

            if (partLower.startsWith("#") && HEX_PATTERN.matcher(partLower).matches()) {
                normalized[i] = expandShortHex(partLower);
                continue;
            }

            throw ERROR_INVALID_COLOR.create(part);
        }

        return String.join(SEPARATOR, normalized);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemainingLowerCase();

        String prefix;
        String lastToken;
        int lastColon = remaining.lastIndexOf(SEPARATOR);
        if (lastColon >= 0) {
            prefix = remaining.substring(0, lastColon + 1);
            lastToken = remaining.substring(lastColon + 1);
        } else {
            prefix = "";
            lastToken = remaining;
        }

        boolean isGradient = !prefix.isEmpty();

        for (Color color : Color.values()) {
            String name = color.toString();

            if (isGradient && color == Color.RAINBOW) {
                continue;
            }

            if (name.startsWith(lastToken)) {
                builder.suggest(prefix + name);
            }
        }

        if (lastToken.startsWith("#")) {
            String[] examples = new String[] { "#fff", "#ffffff", "#ff00ff" };
            for (String ex : examples) {
                if (ex.startsWith(lastToken)) {
                    builder.suggest(prefix + ex);
                }
            }

            if (HEX_PATTERN.matcher(lastToken).matches()) {
                builder.suggest(prefix + lastToken);
            }
        } else if (!isGradient) {
            builder.suggest(prefix + "#ffffff");
        }

        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.greedyString();
    }
}