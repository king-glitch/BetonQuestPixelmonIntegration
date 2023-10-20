package dev.rachamon.core.commons.factory;

import br.com.finalcraft.evernifecore.util.FCReflectionUtil;
import dev.rachamon.core.commons.utils.LoggerUtil;

public abstract class IntegrationEventFactory {
    public static IntegrationEventFactory create(LoggerUtil logger) {
        return (IntegrationEventFactory) FCReflectionUtil.getConstructor("dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory.IntegrationEventFactoryImpl", LoggerUtil.class).invoke(logger);
    }

    public abstract void unregister();
}
