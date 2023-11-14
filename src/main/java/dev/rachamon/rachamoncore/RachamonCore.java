package dev.rachamon.rachamoncore;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.ModuleFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.config.LanguageConfig;
import dev.rachamon.rachamoncore.config.MainConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

@Getter
public class RachamonCore extends JavaPlugin implements IModuleFactory<RachamonCore> {
	public static RachamonCore instance;
	public LoggerUtil moduleLogger;
	public MainConfig pluginConfig;
	public LanguageConfig pluginLanguage;
	public final JavaPlugin plugin = this;

	@Override
	public void onLoad() {
		instance = this;
		moduleLogger = new LoggerUtil(Bukkit.getServer(), "RachamonCore");
		moduleLogger.info("Loading plugin...");
	}

	@Override
	public void onEnable() {
		this.load();
	}

	@Override
	public void onDisable() {
		this.unload();
	}


	@Override
	public Path getDirectory() {
		return this.getDataFolder().toPath();
	}

	@Override
	public CommandManager getCommandManager() {
		return null;
	}

	@Override
	public void unload() {
		this.getModuleLogger().info("Unloading RachamonCore module...");
		this.getModuleLogger().info("Unloaded RachamonCore module!");
	}

	@Override
	public void load() {
		if (!this.getDataFolder().exists()) {
			boolean success = this.getDataFolder().mkdir();
			if (!success) {
				moduleLogger.error("Failed to create plugin directory!");
				this.getPluginLoader().disablePlugin(this);
				return;
			}

			moduleLogger.info("Plugin directory created!");

			// create modules directory
			boolean successModules = new File(this.getDataFolder(), "modules").mkdir();
			if (!successModules) {
				moduleLogger.error("Failed to create plugin modules directory!");
				this.getPluginLoader().disablePlugin(this);
				return;
			}

			moduleLogger.info("Plugin modules directory created!");
		}

		moduleLogger.info("Loading configs...");

		this.pluginConfig = new ConfigFactory<RachamonCore, MainConfig>(this, "main.yaml").setHeader(
				"RachamonCore Config").build(MainConfig.class);
		this.pluginLanguage = new ConfigFactory<RachamonCore, LanguageConfig>(this, "language.yaml").setHeader(
				"RachamonCore Language Config").build(LanguageConfig.class);

		moduleLogger.info("Configs loaded!");

		moduleLogger.setDebug(this.pluginConfig.generalConfig.isDebug());

		moduleLogger.info("Registering modules...");
		if (this.getPluginConfig().getModulesConfig().isBetonquestPixelmonIntegrationEnable()) {
			if (Bukkit.getPluginManager()
					.getPlugin("BetonQuest") != null && Objects.requireNonNull(Bukkit.getPluginManager()
					.getPlugin("BetonQuest")).isEnabled()) {
				ModuleFactory.create(this, ".betonquestpixelmonintegration.BetonQuestPixelmonIntegration");
			}
		}

		if (this.getPluginConfig().getModulesConfig().isPaletteTokensEnable()) {
			ModuleFactory.create(this, ".palettetokens.PaletteTokens");
		}

		if (this.getPluginConfig().getModulesConfig().isPixelmonBoosterEnable()) {
			ModuleFactory.create(this, ".pixelmonbooster.PixelmonBooster");
		}

		if (this.getPluginConfig().getModulesConfig().isPixelmonDropsLoggerEnable()) {
			ModuleFactory.create(this, ".pixelmondropslogger.PixelmonDropsLogger");
		}

		if (this.getPluginConfig().getModulesConfig().isBattlePassPixelmonIntegrationEnable()) {
			ModuleFactory.create(this, ".battlepasspixelmonintegration.BattlePassPixelmonIntegration");
		}

		moduleLogger.info("Features registered!");
		moduleLogger.info("Plugin started!");
	}

}