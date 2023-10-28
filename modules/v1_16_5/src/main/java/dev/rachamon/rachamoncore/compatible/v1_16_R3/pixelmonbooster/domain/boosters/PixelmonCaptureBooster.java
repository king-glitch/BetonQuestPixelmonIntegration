package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters;

import dev.rachamon.rachamoncore.api.utils.ExpressionEvaluator;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;

public class PixelmonCaptureBooster extends BoosterBase {
	public PixelmonCaptureBooster(PixelmonBoosterFactoryImpl module) {
		super(module, BoosterType.CAPTURE);
	}

	public int calculate(int value) {
		PixelmonBoosterConfig.ModifierBooster config = (PixelmonBoosterConfig.ModifierBooster) this.getConfig();

		return (int) ExpressionEvaluator.evaluateExpression(config.getModifierEval()
				.replaceAll("\\{current}", String.valueOf(value)));
	}
}
