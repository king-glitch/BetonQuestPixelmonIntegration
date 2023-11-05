package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Getter
public class PixelmonDropsLoggerConfig {
	@Setting(value = "general")
	@Comment("General Settings")
	protected final GeneralConfig generalConfig = new GeneralConfig();

	@Getter
	@ConfigSerializable
	public static class GeneralConfig {
		@Setting(value = "is-debug")
		@Comment("is debug mode [default: false]")
		protected boolean isDebug = false;

		@Setting(value = "only-boss")
		@Comment("only boss pokemon [default: true]")
		protected boolean onlyBoss = true;
	}
}
