package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory;


import com.pixelmonmod.pixelmon.Pixelmon;
import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PixelmonBoosterFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.InfoCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.SettingsBoostCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin.AdminGlobalBoostCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin.AdminModifyBoostCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin.AdminReloadCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterLanguageConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners.BattleEndListener;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners.PlayerActionListener;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PixelmonBoosterFactoryImpl extends PixelmonBoosterFactory {
	private final LoggerUtil moduleLogger;
	private Path directory;
	private final JavaPlugin plugin;
	private PixelmonBoosterConfig config;
	private ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterPlayerData> playerDataConfigNode;
	private ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterConfig> configNode;
	private PixelmonBoosterPlayerData playerData;
	private Locale<PixelmonBoosterLanguageConfig> locale;
	private BoosterService boosterService;
	private CommandManager commandManager;

	@Getter
	public static PixelmonBoosterFactoryImpl instance;


	public PixelmonBoosterFactoryImpl(IModuleFactory<? extends JavaPlugin> module) {
		PixelmonBoosterFactoryImpl.instance = this;
		this.moduleLogger = new LoggerUtil(module.getPlugin().getServer(), "PixelmonBooster");
		this.plugin = (JavaPlugin) module;

		moduleLogger.info("Initializing PixelmonBooster module...");
		Path directory = module.getDirectory().resolve("modules").resolve("PixelmonBooster");
		if (!directory.toFile().exists()) {
			boolean success = directory.toFile().mkdir();
			if (!success) {
				moduleLogger.error("Error creating directory: " + directory);
				return;
			}

			moduleLogger.info("Created directory: " + directory);
		}

		this.directory = directory;

		moduleLogger.info("Loading configs...");
		ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterLanguageConfig> locale = new ConfigFactory<>(
				this,
				"language.yaml"
		);
		this.configNode = new ConfigFactory<>(
				this,
				"main.yaml"
		);
		this.playerDataConfigNode = new ConfigFactory<>(this, "player-data.yaml");

		this.config = this.configNode.setHeader("PixelmonBooster Config").build(PixelmonBoosterConfig.class);
		this.playerData = this.playerDataConfigNode.build(PixelmonBoosterPlayerData.class);
		this.locale = new Locale<>(
				locale.setHeader("PixelmonBooster Language Config")
						.build(PixelmonBoosterLanguageConfig.class),
				s -> s.getGeneralConfig().getPrefix()
		);
		moduleLogger.info("Loaded configs!");
		this.boosterService = new BoosterService(this);

		moduleLogger.info("Registering commands...");
		this.commandManager = new CommandManager(this.getPlugin());
		this.commandManager.mainCommand("pixelmonbooster")
				.subCommands(
						new InfoCommand(this),
						new SettingsBoostCommand(this),
						new AdminModifyBoostCommand(this),
						new AdminGlobalBoostCommand(this),
						new AdminReloadCommand(this)
				);

		moduleLogger.info("Registering listeners...");
		this.plugin.getServer().getPluginManager().registerEvents(new PlayerActionListener(), this.getPlugin());

		moduleLogger.info("Registering events...");
		BattleEndListener listener = new BattleEndListener(this);
		Pixelmon.EVENT_BUS.addListener(listener::onPlayerBeatTrainer);


		moduleLogger.info("Registered commands!");

		moduleLogger.info("Initialized PixelmonBooster module!");

	}

	@Override
	public CommandManager getCommandManager() {
		return this.commandManager;
	}

	@Override
	public void unload() {
		this.moduleLogger.info("Unloading PixelmonBooster module...");
		this.moduleLogger.info("Unloaded PixelmonBooster module!");
	}

	public void reload() {
		moduleLogger.info("Loading configs...");
		ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterLanguageConfig> locale = new ConfigFactory<>(
				this,
				"language.yaml"
		);
		this.configNode = new ConfigFactory<>(
				this,
				"main.yaml"
		);

		this.config = this.configNode.setHeader("PixelmonBooster Config").build(PixelmonBoosterConfig.class);
		this.locale = new Locale<>(
				locale.setHeader("PixelmonBooster Language Config")
						.build(PixelmonBoosterLanguageConfig.class),
				s -> s.getGeneralConfig().getPrefix()
		);
		moduleLogger.info("Loaded configs!");
	}
}
