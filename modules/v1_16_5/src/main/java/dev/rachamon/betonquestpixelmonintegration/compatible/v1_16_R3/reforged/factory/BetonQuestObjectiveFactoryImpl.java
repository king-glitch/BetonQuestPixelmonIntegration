package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory;

import dev.rachamon.core.commons.factory.BetonQuestObjectiveFactory;
import dev.rachamon.core.commons.utils.LoggerUtil;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.Objective;

public class BetonQuestObjectiveFactoryImpl extends BetonQuestObjectiveFactory<Objective> {

    private final LoggerUtil logger;

    public BetonQuestObjectiveFactoryImpl(LoggerUtil logger) {
        this.logger = logger;
    }

    @Override
    public void register(String objective, Class<? extends Objective> clazz) {
        try {
            logger.info("Registering objective: " + objective + "...");
            BetonQuest.getInstance().registerObjectives(objective, clazz);
            logger.info("Objective: " + objective + " registered!");
        } catch (Exception e) {
            logger.error("Error registering objective: " + objective + "!");
        }
    }
}