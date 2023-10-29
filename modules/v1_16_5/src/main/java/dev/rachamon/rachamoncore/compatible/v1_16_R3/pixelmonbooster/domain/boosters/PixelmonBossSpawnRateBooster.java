package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;

import java.util.Random;

public class PixelmonBossSpawnRateBooster extends BoosterBase {

	public PixelmonBossSpawnRateBooster(PixelmonBoosterFactoryImpl module) {
		super(module, BoosterType.BOSS);
	}

	public boolean success() {
		PixelmonBoosterConfig.ChanceBooster config = (PixelmonBoosterConfig.ChanceBooster) this.getConfig();
		return ((double) 1 / config.getChance()) >= new Random().nextFloat();
	}
}
