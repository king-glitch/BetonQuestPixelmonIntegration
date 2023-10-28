package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.utils;

import com.google.common.collect.Lists;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

import java.util.List;

public class SpecUtil {
	public static boolean match(PixelmonEntity pixelmon, List<PokemonSpecification> requirementSpecs) {
		for (PokemonSpecification requirementSpec : requirementSpecs) {
			if (requirementSpec != null && !requirementSpec.matches(pixelmon)) {
				return false;
			}
		}

		return true;
	}

	public static boolean match(Pokemon pokemon, List<PokemonSpecification> requirementSpecs) {
		for (PokemonSpecification requirementSpec : requirementSpecs) {
			if (requirementSpec != null && !requirementSpec.matches(pokemon)) {
				return false;
			}
		}

		return true;
	}

	public static List<PokemonSpecification> parseSpecs(String[] specs) {
		List<PokemonSpecification> requirementSpecs = Lists.newArrayList();
		for (String spec : specs) {
			requirementSpecs.add(PokemonSpecificationProxy.create(spec));
		}

		return requirementSpecs;
	}
}
