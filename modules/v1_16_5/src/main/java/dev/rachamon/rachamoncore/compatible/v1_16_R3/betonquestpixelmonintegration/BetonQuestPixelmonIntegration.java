package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ModuleFactory;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events.*;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.api.Objective;

import java.util.HashMap;

@Getter
public class BetonQuestPixelmonIntegration extends ModuleFactory {
	@Getter
	public static BetonQuestPixelmonIntegration instance;

	public BetonQuestPixelmonIntegration(IModuleFactory<? extends JavaPlugin> module) {
		super(module.getPlugin(), "BetonQuestPixelmonIntegration");
		BetonQuestPixelmonIntegration.instance = this;

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

		this.getModuleLogger().info("registered " + objectives.size() + " objectives!");
		this.getModuleLogger().info("BetonQuestPixelmonIntegration module initialized!");
	}

	public void register(String objective, Class<? extends Objective> clazz) {
		try {
			BetonQuest.getInstance().registerObjectives(objective, clazz);
		} catch (Exception e) {
			this.getModuleLogger().error("Error registering objective: " + objective + "!");
		}
	}

}