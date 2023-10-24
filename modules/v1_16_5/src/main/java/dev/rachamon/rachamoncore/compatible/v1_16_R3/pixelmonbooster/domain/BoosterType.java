package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain;

import lombok.Getter;

@Getter
public enum BoosterType {
    EV("ev"),
    EXP("exp"),
    BOSS("boss"),
    DROP("drop"),
    HATCH("hatch"),
    CAPTURE("capture"),
    SHINY_RATE("shiny"),
    POKEMON_SPAWN("pokemon"),
    HIDDEN_ABILITY("ha"),
    BATTLE_WINNING("winning");
    private final String name;

    BoosterType(String name) {
        this.name = name;
    }

    public static BoosterType fromString(String text) {
        for (BoosterType b : BoosterType.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
