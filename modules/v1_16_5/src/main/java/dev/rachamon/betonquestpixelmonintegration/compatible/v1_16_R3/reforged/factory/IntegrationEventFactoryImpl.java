package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory;

import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events.*;
import dev.rachamon.core.commons.factory.IntegrationEventFactory;
import dev.rachamon.core.commons.utils.LoggerUtil;
import pl.betoncraft.betonquest.BetonQuest;

public class IntegrationEventFactoryImpl extends IntegrationEventFactory {
    LoggerUtil logger;

    public IntegrationEventFactoryImpl(LoggerUtil logger) {
        this.logger = logger;
        try {
            logger.info("Registering objective...");

            BetonQuest.getInstance().registerObjectives("pixelmon.catch", OnPokemonCatchEvent.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.apricorn.harvest", OnApricornHarvest.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.hatch", OnHatchPokemonEgg.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.knockout", OnKnockout.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.knockout.wild", OnKnockoutWildPokemon.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.knockout.trainer", OnKnockoutTrainerPokemon.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.knockout.player", OnKnockoutPlayerPokemon.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.defeated", OnDefeated.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.defeated.wild", OnDefeatedByWild.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.defeated.trainer", OnDefeatedByTrainer.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.defeated.player", OnDefeatedByPlayer.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.evolve.pre", OnPokemonEvolvePre.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.evolve.post", OnPokemonEvolvePost.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.trade.get", OnPokemonTradeGet.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.trade.give", OnPokemonTradeGive.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.tile", OnTileEntityVicinity.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.trainer.win", OnTrainerWin.class);
            BetonQuest.getInstance().registerObjectives("pixelmon.trainer.lose", OnTrainerLose.class);

            logger.info("Objective registered!");
        } catch (Exception e) {
            logger.error("Error registering objective!");
        }

    }

    public void unregister() {
        logger.info("Unregistering objective...");
    }
}
