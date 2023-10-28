package dev.rachamon.rachamoncore.api.factory.modules;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.api.utils.ReflectionUtil;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BetonQuestPixelmonIntegrationFactory implements IModuleFactory<BetonQuestPixelmonIntegrationFactory> {
	public static BetonQuestPixelmonIntegrationFactory create(IModuleFactory<? extends JavaPlugin> plugin) {
		return (BetonQuestPixelmonIntegrationFactory) ReflectionUtil.getConstructor(
				"dev.rachamon.rachamoncore.compatible." + MCVersion.getCurrent()
						.name() + ".betonquestpixelmonintegration.factory.BetonQuestObjectiveFactoryImpl",
				IModuleFactory.class
		).invoke(plugin);
	}
}
