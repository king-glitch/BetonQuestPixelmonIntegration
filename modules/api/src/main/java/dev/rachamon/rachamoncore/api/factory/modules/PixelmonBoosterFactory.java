package dev.rachamon.rachamoncore.api.factory.modules;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.api.utils.ReflectionUtil;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PixelmonBoosterFactory implements IModuleFactory<PixelmonBoosterFactory> {
    public static PixelmonBoosterFactory create(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        return (PixelmonBoosterFactory) ReflectionUtil.getConstructor("dev.rachamon.rachamoncore.compatible." + MCVersion.getCurrent().name() + ".pixelmonbooster.factory.PixelmonBoosterFactoryImpl", IModuleFactory.class, LoggerUtil.class).invoke(plugin, logger);
    }
}
