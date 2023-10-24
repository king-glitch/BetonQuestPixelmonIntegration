package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Getter
public class PixelmonBoosterLanguageConfig {

    @Setting(value = "general")
    @Comment("General Settings")
    private final GeneralConfig generalConfig = new GeneralConfig();

    @Getter
    @ConfigSerializable
    public static class GeneralConfig {
        @Setting(value = "prefix")
        @Comment("message prefix")
        protected String prefix = "&8[&cPixelmonBooster&8]&8:&7 ";
        @Setting("booster-activated")
        @Comment("Message when booster activated\n" + "variables:\n" + " - {booster}: booster name\n" + " - {time}: time")
        protected String boosterActivated = "&a{booster} booster activated for {time} seconds";
        @Setting("booster-deactivated")
        @Comment("Message when booster deactivated\n" + "variables:\n" + " - {booster}: booster name")
        protected String boosterDeactivated = "&c{booster} booster deactivated";
        @Setting("booster-already-activated")
        @Comment("Message when booster already activated\n" + "variables:\n" + " - {booster}: booster name")
        protected String boosterAlreadyActivated = "&c{booster} booster already activated";
        @Setting("booster-expired")
        @Comment("Message when booster expired\n" + "variables:\n" + " - {booster}: booster name")
        protected String boosterExpired = "&c{booster} booster expired";
    }
}
