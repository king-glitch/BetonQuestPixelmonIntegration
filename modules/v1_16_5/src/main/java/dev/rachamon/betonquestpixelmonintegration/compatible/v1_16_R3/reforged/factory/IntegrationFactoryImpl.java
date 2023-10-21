package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory;

import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events.*;
import dev.rachamon.core.commons.factory.BetonQuestObjectiveFactory;
import dev.rachamon.core.commons.factory.IntegrationFactory;
import dev.rachamon.core.commons.utils.LoggerUtil;
import pl.betoncraft.betonquest.api.Objective;

public class IntegrationFactoryImpl extends IntegrationFactory {
    public static LoggerUtil logger;

    public IntegrationFactoryImpl(LoggerUtil logger, boolean isDebug) {
        IntegrationFactoryImpl.logger = logger;
        logger.setDebug(isDebug);

        try {
            BetonQuestObjectiveFactory<Objective> betonQuestObjectiveFactory = new BetonQuestObjectiveFactoryImpl(logger);

            betonQuestObjectiveFactory.register("pixelmon.apricorn.harvest", OnApricornHarvest.class);
            betonQuestObjectiveFactory.register("pixelmon.defeated", OnDefeated.class);
            betonQuestObjectiveFactory.register("pixelmon.hatch", OnHatchPokemonEgg.class);
            betonQuestObjectiveFactory.register("pixelmon.knockout", OnKnockout.class);
            betonQuestObjectiveFactory.register("pixelmon.knockout.wild", OnKnockoutWildPokemon.class);
            betonQuestObjectiveFactory.register("pixelmon.knockout.trainer", OnKnockoutTrainerPokemon.class);
            betonQuestObjectiveFactory.register("pixelmon.knockout.player", OnKnockoutPlayerPokemon.class);
            betonQuestObjectiveFactory.register("pixelmon.catch", OnPokemonCatchEvent.class);
            betonQuestObjectiveFactory.register("pixelmon.evolve.pre", OnPokemonEvolvePre.class);
            betonQuestObjectiveFactory.register("pixelmon.evolve.post", OnPokemonEvolvePost.class);
            betonQuestObjectiveFactory.register("pixelmon.trade.get", OnPokemonTradeGet.class);
            betonQuestObjectiveFactory.register("pixelmon.trade.give", OnPokemonTradeGive.class);
            betonQuestObjectiveFactory.register("pixelmon.trainer.win", OnTrainerWin.class);
            betonQuestObjectiveFactory.register("pixelmon.trainer.lose", OnTrainerLose.class);

        } catch (Exception e) {
            logger.error("Error registering objective!");
        }

    }

    public void unregister() {
        logger.info("Unregistering objective...");
    }
}
