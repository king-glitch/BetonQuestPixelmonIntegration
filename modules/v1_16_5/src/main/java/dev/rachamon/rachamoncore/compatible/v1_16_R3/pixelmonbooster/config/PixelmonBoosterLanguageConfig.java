package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
@Getter
public class PixelmonBoosterLanguageConfig {

	@Setting(value = "general")
	@Comment("General Settings")
	private final GeneralConfig generalConfig = new GeneralConfig();

	@Setting(value = "boosters")
	@Comment("Boosters Settings")
	private final BoostersConfig boosterConfig = new BoostersConfig();

	@Setting("global-booster-activation")
	@Comment("Global Booster")
	private final GlobalActivationConfig globalBoosterActivation = new GlobalActivationConfig();

	@Getter
	@ConfigSerializable
	public static class GeneralConfig {

		@Setting(value = "prefix")
		@Comment("message prefix")
		protected String prefix = "&8[&cPixelmonBooster&8]&8:&7 ";

		@Setting(value = "booster-resume")
		@Comment("Message when booster resume\n" + "variables:\n" + " - {booster}: booster name")
		protected String boosterResume = "&a{booster}&7 booster resume";

		@Setting("booster-already-resume")
		@Comment("Message when booster already resume\n" + "variables:\n" + " - {booster}: booster name")
		protected String boosterAlreadyResume = "&c{booster}&7 booster already resume";

		@Setting("booster-pause")
		@Comment("Message when booster pause\n" + "variables:\n" + " - {booster}: booster name")
		protected String boosterPause = "&c{booster}&7 booster pause";

		@Setting("booster-already-pause")
		@Comment("Message when booster already pause\n" + "variables:\n" + " - {booster}: booster name")
		protected String boosterAlreadyPause = "&c{booster}&7 booster already pause";

		@Setting("booster-expired")
		@Comment("Message when booster expired\n" + "variables:\n" + " - {booster}: booster name")
		protected String boosterExpired = "&c{booster}&7 booster expired";

		@Setting("booster-modification-added-success")
		@Comment("Message when booster modification added success\n" + "variables:\n" + " - {booster}: booster name\n" + " - {time}: time\n" + " - {player}: player name")
		protected String boosterModificationAddedSuccess = "&7successfully added &a{time} to &a{booster} &7booster for &a{player}";

		@Setting("booster-modification-set-success")
		@Comment("Message when booster modification set success\n" + "variables:\n" + " - {booster}: booster name\n" + " - {time}: time\n" + " - {player}: player name")
		protected String boosterModificationSetSuccess = "&7successfully set &a{time} to &a{booster} &7booster for &a{player}";

		@Setting("booster-modification-removed-success")
		@Comment("Message when booster modification removed success\n" + "variables:\n" + " - {booster}: booster name\n" + " - {time}: time\n" + " - {player}: player name")
		protected String boosterModificationRemovedSuccess = "&7successfully removed &a{time} to &a{booster} &7booster for &a{player}";

		@Setting("booster-info-template")
		@Comment("Message when booster info")
		protected List<String> boosterInfoTemplate = new ArrayList<String>() {{
			add("&7========== PixelmonBooster ==========");
			add("{data}");
			add("&7=====================================");
		}};

		@Setting("booster-info")
		@Comment("Message when booster info\n" + "variables:\n" + " - {booster}: booster name\n" + " - {time}: time\n" + " - {activated}: activated")
		protected String boosterInfo = "&8- &a{booster} &7booster, &a{time} &7left. &a{activated}";

		@Setting("booster-join-info")
		@Comment("Message when booster info")
		protected String boosterJoinInfo = "&7Currently you have &a{amount} &7boosters, &a{activated-amount} &7activated: &a{boosters}";

		@Setting("reload")
		@Comment("Message when reload config")
		protected String reload = "&7Reload config";
	}

	@Getter
	@ConfigSerializable
	public static class GlobalActivationConfig {
		@Setting("activated")
		@Comment("Message when global booster activated\n" + "variables:\n" + " - {booster}: booster name")
		protected String activated = "global booster &a{booster} activated";

		@Setting("deactivated")
		@Comment("Message when global booster deactivated\n" + "variables:\n" + " - {booster}: booster name")
		protected String deactivated = "global booster &c{booster}&7 deactivated";

		@Setting("already-activated")
		@Comment("Message when global booster already activated\n" + "variables:\n" + " - {booster}: booster name")
		protected String alreadyActivated = "global booster &c{booster}&7 already activated";

		@Setting("already-deactivated")
		@Comment("Message when global booster already deactivated\n" + "variables:\n" + " - {booster}: booster name")
		protected String alreadyDeactivated = "global booster &c{booster}&7 already deactivated";
	}

	@Getter
	@ConfigSerializable
	public static class BoostersConfig {
		@Setting("beat-trainer-win-money")
		@Comment("Message when beat trainer win money\n" + "variables:\n" + " - {money}: money")
		protected String beatTrainerWinMoney = "&7You beat trainer and get &a{money} &7more money";

		@Setting("exp-gain")
		@Comment("Message when exp gain\n" + "variables:\n" + " - {exp}: exp")
		protected String expGain = "&7You got &a{exp} &7more exp";

		@Setting("capture-rate")
		@Comment("Message when capture rate\n" + "variables:\n" + " - {rate}: rate")
		protected String captureRate = "&7You got &a{rate} &7more capture rate";

		@Setting("egg-steps")
		@Comment("Message when egg steps\n" + "variables:\n" + " - {steps}: steps")
		protected String eggSteps = "&7You got &a{steps} &7less egg steps";

		@Setting("pixelmon-extra-drops")
		@Comment("Message when pixelmon extra drops\n" + "variables:\n" + " - {data}: data")
		protected String pixelmonExtraDrops = "&7You got extra drops: {data}";

		@Setting("pixelmon-extra-drops-item-template")
		@Comment("Message when pixelmon extra drops\n" + "variables:\n" + " - {amount}: amount\n" + " - {item}: item name")
		protected String pixelmonExtraDropsItemTemplate = "&8x&a{amount} &2{item}&7";
	}

}
