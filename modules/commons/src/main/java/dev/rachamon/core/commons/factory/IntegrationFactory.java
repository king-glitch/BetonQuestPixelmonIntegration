package dev.rachamon.core.commons.factory;

import br.com.finalcraft.evernifecore.version.MCVersion;
import dev.rachamon.core.commons.utils.LoggerUtil;
import dev.rachamon.core.commons.utils.ReflectionUtil;

public abstract class IntegrationFactory {
    public static IntegrationFactory create(LoggerUtil logger, boolean isDebug) {
        return (IntegrationFactory) ReflectionUtil.getConstructor("dev.rachamon.betonquestpixelmonintegration.compatible." + MCVersion.getCurrent().name() + ".reforged.factory.IntegrationFactoryImpl", LoggerUtil.class, Boolean.class).invoke(logger, isDebug);
    }
}
