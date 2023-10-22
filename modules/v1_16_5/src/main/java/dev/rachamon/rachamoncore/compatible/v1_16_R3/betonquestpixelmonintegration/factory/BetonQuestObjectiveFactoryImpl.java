package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.factory;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.modules.BetonQuestPixelmonIntegrationFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events.*;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.Objective;

import java.nio.file.Path;
import java.util.HashMap;

@Getter
public class BetonQuestObjectiveFactoryImpl extends BetonQuestPixelmonIntegrationFactory {
    private final LoggerUtil moduleLogger;
    private Path directory;
    public static BetonQuestObjectiveFactoryImpl instance;

    public BetonQuestObjectiveFactoryImpl(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        this.moduleLogger = logger;
        Path directory = plugin.getDirectory().resolve("modules").resolve("BetonQuestPixelmonIntegration");
        if (!directory.toFile().exists()) {
            boolean success = directory.toFile().mkdir();
            if (!success) {
                logger.error("Error creating directory: " + directory);
                return;
            }

            logger.info("Created directory: " + directory);
        }

        this.directory = directory;

        logger.info("Initializing BetonQuestPixelmonIntegration module...");
        HashMap<String, Class<? extends Objective>> objectives = new HashMap<String, Class<? extends Objective>>() {{
            put("pixelmon.apricorn.harvest", OnApricornHarvest.class);
            put("pixelmon.defeated", OnDefeated.class);
            put("pixelmon.hatch", OnHatchPokemonEgg.class);
            put("pixelmon.knockout", OnKnockout.class);
            put("pixelmon.knockout.wild", OnKnockoutWildPokemon.class);
            put("pixelmon.knockout.trainer", OnKnockoutTrainerPokemon.class);
            put("pixelmon.knockout.player", OnKnockoutPlayerPokemon.class);
            put("pixelmon.catch", OnPokemonCatchEvent.class);
            put("pixelmon.evolve.pre", OnPokemonEvolvePre.class);
            put("pixelmon.evolve.post", OnPokemonEvolvePost.class);
            put("pixelmon.trade.get", OnPokemonTradeGet.class);
            put("pixelmon.trade.give", OnPokemonTradeGive.class);
            put("pixelmon.trainer.win", OnTrainerWin.class);
            put("pixelmon.trainer.lose", OnTrainerLose.class);
        }};

        for (String objective : objectives.keySet()) {
            register(objective, objectives.get(objective));
        }

        logger.info("BetonQuestPixelmonIntegration module initialized!");
    }

    public void register(String objective, Class<? extends Objective> clazz) {
        try {
            moduleLogger.info("Registering objective: " + objective + "...");
            BetonQuest.getInstance().registerObjectives(objective, clazz);
            moduleLogger.info("Objective: " + objective + " registered!");
        } catch (Exception e) {
            moduleLogger.error("Error registering objective: " + objective + "!");
        }
    }

    @Override
    public BetonQuestPixelmonIntegrationFactory getInstance() {
        return this;
    }
}