package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster;


import com.pixelmonmod.pixelmon.Pixelmon;
import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.ModuleFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.InfoCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.SettingsBoostCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin.AdminGlobalBoostCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin.AdminModifyBoostCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin.AdminReloadCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterLanguageConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners.*;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PixelmonBooster extends ModuleFactory {
	private PixelmonBoosterConfig config;
	private ConfigFactory<PixelmonBooster, PixelmonBoosterPlayerData> playerDataConfigNode;
	private ConfigFactory<PixelmonBooster, PixelmonBoosterConfig> configNode;
	private final PixelmonBoosterPlayerData playerData;
	private Locale<PixelmonBoosterLanguageConfig> locale;
	private final BoosterService boosterService;
	@Getter
	public static PixelmonBooster instance;


	public PixelmonBooster(IModuleFactory<? extends JavaPlugin> module) {
		super(module.getPlugin(), "PixelmonBooster");
		PixelmonBooster.instance = this;


		this.getModuleLogger().info("Loading configs...");
		ConfigFactory<PixelmonBooster, PixelmonBoosterLanguageConfig> locale = new ConfigFactory<>(
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
		this.getModuleLogger().info("Loaded configs!");
		this.boosterService = new BoosterService(this);

		this.getModuleLogger().info("Registering commands...");
		this.getCommandManager().mainCommand("pixelmonbooster")
				.subCommands(
						new InfoCommand(this),
						new SettingsBoostCommand(this),
						new AdminModifyBoostCommand(this),
						new AdminGlobalBoostCommand(this),
						new AdminReloadCommand(this)
				);

		this.getModuleLogger().info("Registering listeners...");
		this.getPlugin().getServer().getPluginManager().registerEvents(new PlayerActionListener(), this.getPlugin());

		this.getModuleLogger().info("Registering events...");
		BattleEndListener battleEndListener = new BattleEndListener(this);
		PixelmonDropListener dropListener = new PixelmonDropListener(this);
		PixelmonSpawnListener spawnListener = new PixelmonSpawnListener(this);
		PixelmonHatchListener hatchListener = new PixelmonHatchListener(this);
		PixelmonExpListener expListener = new PixelmonExpListener(this);
		PixelmonCaptureListener captureListener = new PixelmonCaptureListener(this);

		Pixelmon.EVENT_BUS.addListener(battleEndListener::onPlayerBeatTrainer);
		Pixelmon.EVENT_BUS.addListener(dropListener::onDrop);
		Pixelmon.EVENT_BUS.addListener(spawnListener::onPixelmonSpawn);
		Pixelmon.EVENT_BUS.addListener(hatchListener::onBreed);
		Pixelmon.EVENT_BUS.addListener(expListener::onExpGain);
		Pixelmon.EVENT_BUS.addListener(captureListener::onGeneralCapture);
		Pixelmon.EVENT_BUS.addListener(captureListener::onRaidCapture);


		this.getModuleLogger().info("Registered commands!");

		this.getModuleLogger().info("Initialized PixelmonBooster module!");

	}

	@Override
	public void unload() {
		this.getModuleLogger().info("Unloading PixelmonBooster module...");
		this.getModuleLogger().info("Unloaded PixelmonBooster module!");
	}

	public void reload() {
		this.getModuleLogger().info("Loading configs...");
		ConfigFactory<PixelmonBooster, PixelmonBoosterLanguageConfig> locale = new ConfigFactory<>(
				this,
				"language.yaml"
		);

		this.config = this.configNode.setHeader("PixelmonBooster Config").build(PixelmonBoosterConfig.class);
		this.locale = new Locale<>(
				locale.setHeader("PixelmonBooster Language Config")
						.build(PixelmonBoosterLanguageConfig.class),
				s -> s.getGeneralConfig().getPrefix()
		);

		this.boosterService.reload();

		this.getModuleLogger().info("successfully reloaded configs!");
	}


}
