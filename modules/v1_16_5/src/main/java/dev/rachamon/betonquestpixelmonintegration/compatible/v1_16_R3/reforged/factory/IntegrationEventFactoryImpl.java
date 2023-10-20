package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory;

import dev.rachamon.core.commons.factory.IntegrationEventFactory;
import dev.rachamon.core.commons.utils.LoggerUtil;
import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events.OnPokemonCatchEvent;
import pl.betoncraft.betonquest.BetonQuest;

public class IntegrationEventFactoryImpl extends IntegrationEventFactory {
    LoggerUtil logger;
    public IntegrationEventFactoryImpl(LoggerUtil logger) {
        this.logger = logger;
        try {
            logger.info("Registering objective...");
            BetonQuest.getInstance().registerObjectives("pixelmon.catch", OnPokemonCatchEvent.class);
            logger.info("Objective registered!");
        } catch (Exception e) {
           logger.error("Error registering objective!");
        }

    }

    public void unregister() {
       logger.info("Unregistering objective...");
    } 
}
