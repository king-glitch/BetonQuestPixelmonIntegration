package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
@Getter
public class PixelmonBoosterConfig {
    @Setting(value = "general")
    @Comment("General Settings")
    private final GeneralConfig generalConfig = new GeneralConfig();
    @Setting(value = "boosters")
    @Comment("Boosters")
    private final Map<String, Booster> boosters = new HashMap<String, Booster>() {{
        put(BoosterType.EV.toString(), new ModifierBooster("{current}*2"));
        put(BoosterType.EXP.toString(), new ModifierBooster("{current}*2"));
        put(BoosterType.DROP.toString(), new ModifierBooster("{current}+2"));
        put(BoosterType.HATCH.toString(), new ModifierBooster("{current}-50"));
        put(BoosterType.CAPTURE.toString(), new ModifierBooster("{current}+5"));
        put(BoosterType.BATTLE_WINNING.toString(), new ModifierBooster("{current}*2"));

        put(BoosterType.BOSS.toString(), new ChanceBooster(0.10));
        put(BoosterType.POKEMON_SPAWN.toString(), new ChanceBooster(0.10));
        put(BoosterType.SHINY_RATE.toString(), new ChanceBooster(0.000244F));
        put(BoosterType.HIDDEN_ABILITY.toString(), new ChanceBooster(0.000244F));
    }};

    @Getter
    @ConfigSerializable
    public static class GeneralConfig {
        @Setting(value = "is-debug")
        @Comment("is debug mode [default: false]")
        protected boolean isDebug = false;
        @Setting(value = "interval-seconds")
        @Comment("interval seconds [default: 60]")
        protected int intervalSeconds = 60;
    }

    @Getter
    @ConfigSerializable
    public static class Booster {


    }

    @Getter
    @ConfigSerializable
    public static class ChanceBooster extends Booster {
        @Setting(value = "chance")
        protected double chance = 0.20;
        public ChanceBooster(double chance) {
            this.chance = chance;
        }
    }

    @Getter
    @ConfigSerializable
    public static class ModifierBooster extends Booster {
        @Setting(value = "modifier-eval")
        protected String modifierEval = "{current}+1";
        public ModifierBooster(String modifierEval) {
            this.modifierEval = modifierEval;
        }
    }
}
