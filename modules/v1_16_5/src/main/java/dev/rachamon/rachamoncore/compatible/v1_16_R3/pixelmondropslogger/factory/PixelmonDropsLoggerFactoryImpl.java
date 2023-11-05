package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.factory;

import com.pixelmonmod.pixelmon.Pixelmon;
import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PixelmonDropsLoggerFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.commands.GiveCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerLanguage;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerLog;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.listeners.PixelmonDropListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PixelmonDropsLoggerFactoryImpl extends PixelmonDropsLoggerFactory {
	private final LoggerUtil moduleLogger;
	private Path directory;
	private final JavaPlugin plugin;
	private PixelmonDropsLoggerConfig config;
	private ConfigFactory<PixelmonDropsLoggerFactoryImpl, PixelmonDropsLoggerLog> logDataNode;
	private PixelmonDropsLoggerLog logData;
	private Locale<PixelmonDropsLoggerLanguage> locale;
	private CommandManager commandManager;

	@Getter
	public static PixelmonDropsLoggerFactoryImpl instance;

	public PixelmonDropsLoggerFactoryImpl(IModuleFactory<? extends JavaPlugin> module) {
		PixelmonDropsLoggerFactoryImpl.instance = this;
		this.moduleLogger = new LoggerUtil(module.getPlugin().getServer(), "PixelmonDropsLogger");
		this.plugin = (JavaPlugin) module;

		Path directory = module.getDirectory().resolve("modules").resolve("PixelmonDropsLogger");
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
		ConfigFactory<PixelmonDropsLoggerFactoryImpl, PixelmonDropsLoggerLanguage> locale = new ConfigFactory<>(
				this,
				"language.yaml"
		);

		ConfigFactory<PixelmonDropsLoggerFactoryImpl, PixelmonDropsLoggerConfig> config = new ConfigFactory<>(
				this,
				"main.yaml"
		);

		this.logDataNode = new ConfigFactory<>(this, "drops-log.yaml");

		this.logData = this.logDataNode.setHeader("Pixelmon Drops Logger Config")
				.build(PixelmonDropsLoggerLog.class);
		this.config = config.setHeader("Pixelmon Drops Logger Config")
				.build(PixelmonDropsLoggerConfig.class);
		this.locale = new Locale<>(
				locale.setHeader("Pixelmon Drops Logger Language Config")
						.build(PixelmonDropsLoggerLanguage.class),
				s -> s.getGeneralConfig().getPrefix()
		);
		moduleLogger.info("Loaded configs!");

		PixelmonDropListener listener = new PixelmonDropListener(
				this);
		Pixelmon.EVENT_BUS.addListener(listener::onDrop);

		moduleLogger.info("Registered listeners!");

		moduleLogger.info("Registering commands...");
		this.commandManager = new CommandManager(this.getPlugin());
		this.commandManager.mainCommand("pixelmondropslogger")
				.subCommands(new GiveCommand(this));
		moduleLogger.info("Registered commands!");

		moduleLogger.info("PixelmonDropsLogger module has been enabled!");

	}

	@Override
	public Path getDirectory() {
		return this.directory;
	}

	@Override
	public JavaPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public CommandManager getCommandManager() {
		return this.commandManager;
	}

	@Override
	public void unload() {

	}

	@Override
	public LoggerUtil getModuleLogger() {
		return this.moduleLogger;
	}
}
