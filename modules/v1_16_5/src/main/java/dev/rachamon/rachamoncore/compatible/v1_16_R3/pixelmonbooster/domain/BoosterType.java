package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain;

import lombok.Getter;


@Getter
public enum BoosterType {
	EV("ev", BoosterConfigureType.MODIFIER),
	EXP("exp", BoosterConfigureType.MODIFIER),
	BOSS("boss", BoosterConfigureType.CHANCE),
	DROP("drop", BoosterConfigureType.MODIFIER),
	HATCH("hatch", BoosterConfigureType.MODIFIER),
	CAPTURE("capture", BoosterConfigureType.MODIFIER),
	SHINY_RATE("shiny", BoosterConfigureType.CHANCE),
	POKEMON_SPAWN("pokemon", BoosterConfigureType.CHANCE),
	HIDDEN_ABILITY("ha", BoosterConfigureType.CHANCE),
	BATTLE_WINNING("winning", BoosterConfigureType.MODIFIER);
	private final String name;
	private final BoosterConfigureType type;

	BoosterType(String name, BoosterConfigureType type) {
		this.name = name;
		this.type = type;
	}

	public static BoosterType fromString(String text) {
		for (BoosterType b : BoosterType.values()) {
			if (b.getName().equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}

	public static enum BoosterConfigureType {
		CHANCE,
		MODIFIER,
	}
}
