package dev.rachamon.rachamoncore.api.factory.modules;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.api.utils.ReflectionUtil;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PixelmonDropsLoggerFactory implements IModuleFactory<PixelmonDropsLoggerFactory> {
	public static PixelmonDropsLoggerFactory create(IModuleFactory<? extends JavaPlugin> plugin) {
		return (PixelmonDropsLoggerFactory) ReflectionUtil.getConstructor("dev.rachamon.rachamoncore.compatible." + MCVersion.getCurrent()
						.name() + ".pixelmondropslogger.factory.PixelmonDropsLoggerFactoryImpl", IModuleFactory.class)
				.invoke(plugin);
	}

	public abstract LoggerUtil getModuleLogger();
}
