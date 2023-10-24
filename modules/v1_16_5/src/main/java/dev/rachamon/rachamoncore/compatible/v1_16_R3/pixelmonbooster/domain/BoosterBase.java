package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BoosterBase {
    private final BoosterType boosterType;
    private PixelmonBoosterConfig.Booster config;
    private final PixelmonBoosterFactoryImpl instance;
    private final int interval;

    private boolean isRunning = false;
    private BukkitTask task;
    private final Map<String, PixelmonBoosterPlayerData.BoosterData> players = new HashMap<>();
    private boolean isGlobalActivate = false;


    public BoosterBase(BoosterType boosterType) {
        this.boosterType = boosterType;
        this.instance = (PixelmonBoosterFactoryImpl) PixelmonBoosterFactoryImpl.getInstance();
        this.config = instance.getConfig().getBoosters().get(boosterType.name().toLowerCase());
        this.interval = instance.getConfig().getGeneralConfig().getIntervalSeconds();
    }

    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;

        this.instance.getModuleLogger().info("Starting " + boosterType.name() + " booster...");

        this.task = instance.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(
                instance.getPlugin(),
                this::process,
                0L,
                interval * 20L
        );
    }

    public void stop() {
        if (!isRunning) {
            return;
        }

        this.task.cancel();
        this.isRunning = false;
    }

    public void reload() {
        this.config = instance.getConfig().getBoosters().get(boosterType.name().toLowerCase());
    }

    public void process() {
        this.players.entrySet().removeIf(p -> {
            if (p.getValue().getTimeLeft() <= System.currentTimeMillis()) {
                this.instance.getModuleLogger()
                             .info("Removing " + p.getKey() + " from " + boosterType.name() + " booster...");
                return true;
            }

            Player player = instance.getPlugin().getServer().getPlayer(p.getKey());

            if (player == null) {
                this.instance.getModuleLogger()
                             .info("Removing " + p.getKey() + " from " + boosterType.name() + " booster...");
                return true;
            }

            player.sendMessage(instance.getLocale()
                                       .from(s -> s.getGeneralConfig().getBoosterExpired())
                                       .process("booster", boosterType.name())
                                       .get());

            return false;
        });

        if (!this.players.isEmpty()) {
            return;
        }

        this.stop();
        this.instance.getModuleLogger().info("Stopped " + boosterType.name() + " booster! (No players)");
    }

    public void add(String uuid, int seconds) {
        if (this.players.containsKey(uuid)) {
            this.players.get(uuid).setTimeLeft(this.players.get(uuid).getTimeLeft() + seconds);
            return;
        }


        if (!this.isRunning) {
            this.start();
        }
    }

    public void set(String uuid, int seconds) {
        if (!this.players.containsKey(uuid)) {
            this.add(uuid, seconds);
            return;
        }

        this.players.get(uuid).setTimeLeft(seconds);
    }

    public void remove(String uuid) {
        this.players.remove(uuid);
    }

    public void setGlobalActivate(boolean globalActivate) {
        this.isGlobalActivate = globalActivate;
        if (globalActivate) {
            this.start();
        }
    }

    public boolean isActivated(String uuid) {
        if (!this.players.containsKey(uuid)) {
            return false;
        }

        return this.players.get(uuid).isActivated();
    }
}
