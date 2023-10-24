package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BoosterService {
    @Getter
    private static Map<BoosterType, BoosterBase> boosters = new HashMap<>();
    public final PixelmonBoosterFactoryImpl instance;

    public BoosterService() {
        this.instance = (PixelmonBoosterFactoryImpl) PixelmonBoosterFactoryImpl.getInstance();
    }

    public boolean resume(Player player, BoosterType boosterType) throws IOException {
        if (!boosters.containsKey(boosterType)) {
            return false;
        }

        PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType);
        if (boosterData == null) {
            return false;
        }

        if (boosterData.getTimeLeft() < 1) {
            return false;
        }

        boosters.get(boosterType).add(player.getUniqueId().toString(), boosterData.getTimeLeft());

        boosterData.setActivated(true);

        instance.getPlayerDataConfig().save();
        return true;
    }

    public void pause(Player player, BoosterType boosterType) throws IOException {
        if (!boosters.containsKey(boosterType)) {
            return;
        }

        PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType);
        if (boosterData == null) {
            return;
        }

        boosterData.setActivated(false);

        instance.getPlayerDataConfig().save();

        boosters.get(boosterType).remove(player.getUniqueId().toString());

    }

    public void add(Player player, BoosterType boosterType, int seconds) throws IOException {
        if (!boosters.containsKey(boosterType)) {
            return;
        }

        PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType, true);

        boosterData.setTimeLeft(boosterData.getTimeLeft() + seconds);

        instance.getPlayerDataConfig().save();

        if (boosterData.isActivated()) {
            boosters.get(boosterType).add(player.getUniqueId().toString(), seconds);
            boosters.get(boosterType).start();
        }
    }

    public void set(Player player, BoosterType boosterType, int seconds) throws IOException {
        if (!boosters.containsKey(boosterType)) {
            return;
        }

        PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType, true);

        boosterData.setTimeLeft(seconds);

        instance.getPlayerDataConfig().save();

        if (boosterData.isActivated()) {
            boosters.get(boosterType).set(player.getUniqueId().toString(), seconds);
            boosters.get(boosterType).start();
        }
    }

    @Nullable
    public PixelmonBoosterPlayerData.BoosterData get(Player player, BoosterType boosterType) {
        if (!boosters.containsKey(boosterType)) {
            return null;
        }

        if (!instance.getPlayerData().getData().containsKey(player.getUniqueId().toString())) {
            return null;
        }

        Map<String, PixelmonBoosterPlayerData.BoosterData> data = instance.getPlayerData()
                                                                          .getData()
                                                                          .get(player.getUniqueId().toString());

        if (!data.containsKey(boosterType.name().toLowerCase())) {
            return null;
        }

        return data.get(boosterType.name().toLowerCase());
    }

    public PixelmonBoosterPlayerData.BoosterData get(
            Player player, BoosterType boosterType, boolean createIfNotExists
    ) throws IOException {
        if (!boosters.containsKey(boosterType)) {
            return null;
        }

        if (!instance.getPlayerData().getData().containsKey(player.getUniqueId().toString())) {
            if (!createIfNotExists) {
                return null;
            }

            instance.getPlayerData().getData().put(player.getUniqueId().toString(), new HashMap<>());
        }

        Map<String, PixelmonBoosterPlayerData.BoosterData> data = instance.getPlayerData()
                                                                          .getData()
                                                                          .get(player.getUniqueId().toString());

        if (!data.containsKey(boosterType.name().toLowerCase())) {
            if (!createIfNotExists) {
                return null;
            }

            data.put(boosterType.name().toLowerCase(), new PixelmonBoosterPlayerData.BoosterData());
        }

        return data.get(boosterType.name().toLowerCase());
    }

    public void activateGlobal(BoosterType boosterType) {
        if (!boosters.containsKey(boosterType)) {
            return;
        }

        BoosterBase boosterBase = boosters.get(boosterType);
        boosterBase.setGlobalActivate(true);
    }


}