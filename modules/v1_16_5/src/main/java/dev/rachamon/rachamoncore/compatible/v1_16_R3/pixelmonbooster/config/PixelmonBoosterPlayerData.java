package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigSerializable
public class PixelmonBoosterPlayerData {
    @Setting(value = "data")
    private final Map<String, Map<String, BoosterData>> data = new HashMap<>();

    @Getter
    @ConfigSerializable
    public static class BoosterData {
        @Setting(value = "time-left")
        protected int timeLeft = 0;
        @Setting(value = "activated")
        protected boolean activated = true;

        public void setTimeLeft(int timeLeft) {
            this.timeLeft = timeLeft;
        }
        public void setActivated(boolean activated) {
            this.activated = activated;
        }
    }

    public void update(String uuid, String boosterType, BoosterData boosterData) {
        if (!data.containsKey(uuid)) {
            data.put(uuid, new HashMap<>());
        }

        data.get(uuid).put(boosterType, boosterData);
    }
}
