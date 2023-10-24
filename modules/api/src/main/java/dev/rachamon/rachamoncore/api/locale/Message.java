package dev.rachamon.rachamoncore.api.locale;

import dev.rachamon.rachamoncore.api.utils.ColorUtil;
import dev.rachamon.rachamoncore.api.version.MCDetailedVersion;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;

public class Message {

    private String message;

    public Message(String message) {
        // if message contains regex like {#aabbcc} the message will be &x&a&a&b&b&c&c
        if (message.matches(".*\\{#[0-9a-fA-F]{6}}.*")) {
            // if the client version is lower than 1.16, the message will be converted to rgb
            if (MCVersion.isLower(MCDetailedVersion.v1_16_R1)) {
                // replace all {#aabbcc} to ColorUtil.fromRGB(aa,bb,cc)
                // where aa, bb, cc is the hex code must be converted to rgb

                int red = Integer.parseInt(message.substring(message.indexOf("{#") + 2, message.indexOf("{#") + 4), 16);
                int green = Integer.parseInt(message.substring(message.indexOf("{#") + 4, message.indexOf("{#") + 6), 16);
                int blue = Integer.parseInt(message.substring(message.indexOf("{#") + 6, message.indexOf("{#") + 8), 16);

                message = message.replaceAll("\\{#[0-9a-fA-F]{6}}", "&x" + ColorUtil.fromRGB(red, green, blue).getChatColor().toString());
            } else {
                message = message.replaceAll("\\{#[0-9a-fA-F]{6}}", "&x$0");
            }
        }

        this.message = message.replaceAll("&([0-9a-fA-Fk-oK-OrR])", "§$1");
    }


    public Message process(String placeholder, Object replacement) {
        final String place = Matcher.quoteReplacement(placeholder);
        this.message = message.replaceAll("%" + place + "%|\\{" + place + "\\}", replacement == null ? "" : Matcher.quoteReplacement(replacement.toString()));

        return this;
    }

    public void send(Player player) {
        player.sendMessage(this.message);
    }

    public void send(CommandSender sender) {
        sender.sendMessage(this.message);
    }


    public String get() {
        return this.message;
    }
}
