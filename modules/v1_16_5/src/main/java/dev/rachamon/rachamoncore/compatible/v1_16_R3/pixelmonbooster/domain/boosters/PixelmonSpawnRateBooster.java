package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;

import java.util.Random;

public class PixelmonSpawnRateBooster extends BoosterBase {

	public PixelmonSpawnRateBooster(PixelmonBoosterFactoryImpl module) {
		super(module, BoosterType.POKEMON_SPAWN);
	}

	public boolean success() {
		PixelmonBoosterConfig.ChanceBooster config = (PixelmonBoosterConfig.ChanceBooster) this.getConfig();
		return config.getChance() >= new Random().nextDouble();
	}
}
