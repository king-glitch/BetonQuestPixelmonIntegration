package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
@Getter
public class PixelmonBoosterConfig {
	@Setting(value = "general")
	@Comment("General Settings")
	private final GeneralConfig generalConfig = new GeneralConfig();
	@Setting(value = "boosters")
	@Comment("Boosters")
	private final BoostersConfig boosters = new BoostersConfig();

	@Getter
	@ConfigSerializable
	public static class GeneralConfig {
		@Setting(value = "is-debug")
		@Comment("is debug mode [default: false]")
		protected boolean isDebug = false;
		@Setting(value = "interval-seconds")
		@Comment("interval seconds [default: 60]")
		protected int intervalSeconds = 60;
		@Setting("allowed-random-pokemon-legendary")
		@Comment("Allowed random pokemon to be legendary [default: false]")
		protected boolean allowedRandomPokemonLegendary = false;

		@Setting("allowed-random-pokemon-ultra-beast")
		@Comment("Allowed random pokemon to be ultra beast [default: false]")
		protected boolean allowedRandomPokemonUltraBeast = false;

		@Setting("random-pokemon-blacklist")
		@Comment("Random pokemon blacklist [default: []]")
		protected List<String> randomPokemonBlacklist = List.of();
	}

	@Getter
	@ConfigSerializable
	public static class BoostersConfig {
		@Setting(value = "chance-boosters")
		@Comment("Chance Boosters ")
		protected Map<String, ChanceBooster> chanceBoosters = new HashMap<String, ChanceBooster>() {{
			put(BoosterType.BOSS.toString(), new ChanceBooster(128));
			put(BoosterType.POKEMON_SPAWN.toString(), new ChanceBooster(10));
			put(BoosterType.SHINY_RATE.toString(), new ChanceBooster(1000));
			put(BoosterType.HIDDEN_ABILITY.toString(), new ChanceBooster(1000));
		}};

		@Setting("modifier-boosters")
		@Comment("Modifier Boosters ")
		protected Map<String, ModifierBooster> modifierBoosters = new HashMap<String, ModifierBooster>() {{
			put(BoosterType.EV.toString(), new ModifierBooster("{current}*2"));
			put(BoosterType.EXP.toString(), new ModifierBooster("{current}*2"));
			put(BoosterType.DROP.toString(), new ModifierBooster("{current}+2"));
			put(BoosterType.HATCH.toString(), new ModifierBooster("{current}*0.1"));
			put(BoosterType.CAPTURE.toString(), new ModifierBooster("{current}+5"));
			put(BoosterType.BATTLE_WINNING.toString(), new ModifierBooster("{current}*2"));
		}};
	}

	public static class Booster {
	}

	@Getter
	@ConfigSerializable
	public static class ChanceBooster extends Booster {
		@Setting(value = "chance")
		@Comment("1 divided by chance [default: 2048]")
		protected double chance = 2048;
		@Setting(value = "global-activate")
		protected boolean globalActivate = false;

		public ChanceBooster() {
		}

		public ChanceBooster(double chance) {
			this.chance = chance;
		}

		public void setGlobalActivate(boolean globalActivate) {
			this.globalActivate = globalActivate;
		}
	}

	@Getter
	@ConfigSerializable
	public static class ModifierBooster extends Booster {
		@Setting(value = "modifier-eval")
		protected String modifierEval = "{current}+1";
		@Setting(value = "global-activate")
		protected boolean globalActivate = false;

		public ModifierBooster() {
		}

		public ModifierBooster(String modifierEval) {
			this.modifierEval = modifierEval;
		}

		public void setGlobalActivate(boolean globalActivate) {
			this.globalActivate = globalActivate;
		}
	}
}
