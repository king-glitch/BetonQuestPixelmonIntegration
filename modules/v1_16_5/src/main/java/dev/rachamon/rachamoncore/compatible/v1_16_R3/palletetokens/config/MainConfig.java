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

	@Setting(value = "token-template")
	@Comment("Token Template")
	private final TokenTemplate tokenTemplate = new TokenTemplate();

	@Getter
	@ConfigSerializable
	public static class GeneralConfig {
		@Setting(value = "is-debug")
		@Comment("is debug mode [default: false]")
		protected boolean isDebug = false;
		@Setting(value = "is-apply-texture-on-shiny-pokemon")
		@Comment("is apply texture on shiny pokemon [default: false]")
		protected boolean isApplyTextureOnShinyPokemon = false;
	}

	@Getter
	@ConfigSerializable
	public static class TokenTemplate {
		@Setting(value = "item-custom-model-data")
		@Comment("Item Custom Model Data")
		protected int customModelData = 0;
		@Setting(value = "item-material")
		@Comment("Item Material")
		protected String material = "PAPER";
		@Setting(value = "item-name")
		@Comment("Item Name")
		protected String pokemon = "&6{pokemon} &ePalette Token";
		@Setting(value = "item-lore")
		@Comment("Item Lore")
		protected String[] palette = new String[]{"&7Pokemon: &e{pokemon}", "&7Palette: &e{palette}", "", "&7Right click on a pokemon to apply this palette."};
	}
}
