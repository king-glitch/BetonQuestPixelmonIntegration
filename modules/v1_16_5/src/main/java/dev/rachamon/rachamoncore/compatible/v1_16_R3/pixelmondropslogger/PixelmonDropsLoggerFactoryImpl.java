package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger;

import com.pixelmonmod.pixelmon.Pixelmon;
import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.ModuleFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.commands.GiveCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerLanguage;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerLog;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.listeners.PixelmonDropListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PixelmonDropsLoggerFactoryImpl extends ModuleFactory {
	private final PixelmonDropsLoggerConfig config;
	private final ConfigFactory<PixelmonDropsLoggerFactoryImpl, PixelmonDropsLoggerLog> logDataNode;
	private final PixelmonDropsLoggerLog logData;
	private final Locale<PixelmonDropsLoggerLanguage> locale;

	@Getter
	public static PixelmonDropsLoggerFactoryImpl instance;

	public PixelmonDropsLoggerFactoryImpl(IModuleFactory<? extends JavaPlugin> module) {
		super(module.getPlugin(), "PixelmonDropsLogger");
		PixelmonDropsLoggerFactoryImpl.instance = this;


		this.getModuleLogger().info("Loading configs...");
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
		this.getModuleLogger().info("Loaded configs!");

		PixelmonDropListener listener = new PixelmonDropListener(
				this);
		Pixelmon.EVENT_BUS.addListener(listener::onDrop);

		this.getModuleLogger().info("Registered listeners!");

		this.getModuleLogger().info("Registering commands...");
		this.getCommandManager().mainCommand("pixelmondropslogger")
				.subCommands(new GiveCommand(this));
		this.getModuleLogger().info("Registered commands!");

		this.getModuleLogger().info("PixelmonDropsLogger module has been enabled!");

	}

	@Override
	public void unload() {

	}
}
