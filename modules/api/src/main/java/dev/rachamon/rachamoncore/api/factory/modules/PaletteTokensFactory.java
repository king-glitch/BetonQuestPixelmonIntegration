package dev.rachamon.rachamoncore.api.factory.modules;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.api.utils.ReflectionUtil;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PaletteTokensFactory implements IModuleFactory<PaletteTokensFactory> {
    public static PaletteTokensFactory create(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        return (PaletteTokensFactory) ReflectionUtil.getConstructor("dev.rachamon.rachamoncore.compatible." + MCVersion.getCurrent().name() + ".palletetokens.factory.PaletteTokensFactoryImpl", IModuleFactory.class, LoggerUtil.class).invoke(plugin, logger);
    }
}
