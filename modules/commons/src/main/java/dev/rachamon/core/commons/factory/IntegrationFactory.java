package dev.rachamon.core.commons.factory;

import dev.rachamon.core.commons.utils.LoggerUtil;
import dev.rachamon.core.commons.utils.ReflectionUtil;
import dev.rachamon.core.commons.version.MCVersion;

public abstract class IntegrationFactory {
    public static IntegrationFactory create(LoggerUtil logger, boolean isDebug) {
        return (IntegrationFactory) ReflectionUtil.getConstructor("dev.rachamon.betonquestpixelmonintegration.compatible." + MCVersion.getCurrent().name() + ".reforged.factory.IntegrationFactoryImpl", LoggerUtil.class, Boolean.class).invoke(logger, isDebug);
    }
}
