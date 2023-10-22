package dev.rachamon.rachamoncore.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * The type Main config.
 */
@Getter
@ConfigSerializable
public class MainConfig {
    @Setting(value = "general")
    @Comment("General Settings")
    public final GeneralConfig generalConfig = new GeneralConfig();
    @Setting(value = "modules")
    @Comment("Modules Settings")
    public final ModulesConfig modulesConfig = new ModulesConfig();

    @Getter
    @ConfigSerializable
    public static class GeneralConfig {
        @Setting(value = "is-debug")
        @Comment("is debug mode [default: false]")
        protected boolean isDebug = false;
    }

    @Getter
    @ConfigSerializable
    public static class ModulesConfig {
        @Setting(value = "palette-tokens-enable")
        @Comment("Enable PaletteTokens Module [default: true]")
        protected boolean paletteTokensEnable = true;
        @Setting(value = "pixelmon-booster-enable")
        @Comment("Enable PixelmonBooster Module [default: true]")
        protected boolean pixelmonBoosterEnable = true;
        @Setting(value = "betonquest-pixelmon-integration-enable")
        @Comment("Enable BetonQuest Pixelmon Integration Module [default: true]")
        protected boolean betonquestPixelmonIntegrationEnable = true;
    }

}
