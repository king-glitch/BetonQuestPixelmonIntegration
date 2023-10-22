package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config;

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
    private final GeneralConfig generalConfig = new GeneralConfig();

    @Getter
    @ConfigSerializable
    public static class GeneralConfig {
        @Setting(value = "is-debug")
        @Comment("is debug mode [default: false]")
        protected boolean isDebug = false;
    }
}
