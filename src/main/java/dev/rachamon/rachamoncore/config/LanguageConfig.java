package dev.rachamon.rachamoncore.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@Getter
@ConfigSerializable
public class LanguageConfig {

	@Comment("General Settings")
	@Setting("general")
	private final GeneralConfig generalConfig = new GeneralConfig();


	@Getter
	@ConfigSerializable
	public static class GeneralConfig {
		@Comment("Prefix for chat message")
		@Setting("prefix")
		protected String prefix = "&8[&c&lRachamonCore&8]&7 ";
	}
}

