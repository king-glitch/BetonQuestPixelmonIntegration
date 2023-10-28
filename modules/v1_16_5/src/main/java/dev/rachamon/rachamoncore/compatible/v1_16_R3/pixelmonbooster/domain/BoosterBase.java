package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class BoosterBase {
	private final BoosterType boosterType;
	private PixelmonBoosterConfig.Booster config;
	private final PixelmonBoosterFactoryImpl module;
	private final int interval;

	private boolean isRunning = false;
	private BukkitTask task;
	private final List<UUID> players = new ArrayList<>();
	private boolean isGlobalActivate = false;


	public BoosterBase(PixelmonBoosterFactoryImpl module, BoosterType boosterType) {
		this.boosterType = boosterType;
		this.module = module;
		if (boosterType.getType() == BoosterType.BoosterConfigureType.CHANCE) {
			this.config = this.module.getConfig().getBoosters().getChanceBoosters().get(boosterType.name());
			this.setGlobalActivate(((PixelmonBoosterConfig.ChanceBooster) config).isGlobalActivate());
		} else {
			this.config = this.module.getConfig().getBoosters().getModifierBoosters().get(boosterType.name());
			this.setGlobalActivate(((PixelmonBoosterConfig.ModifierBooster) config).isGlobalActivate());

		}


		this.interval = this.module.getConfig().getGeneralConfig().getIntervalSeconds();
		this.module.getModuleLogger()
				.info("Loaded " + boosterType.name() + " booster! (Interval: " + interval + ")" + " (GlobalActivate: " + isGlobalActivate + ")");
	}

	public void initialize() {
		if (isRunning) {
			return;
		}

		isRunning = true;

		this.module.getPlugin().getLogger().info("Starting " + boosterType.name() + " booster...");

		this.task = module.getPlugin()
				.getServer()
				.getScheduler()
				.runTaskTimerAsynchronously(module.getPlugin(), this::process, 40L, this.interval * 20L);
	}

	public void shutdown() {
		if (!isRunning) {
			return;
		}

		this.task.cancel();
		this.isRunning = false;
	}

	public void reload() {
		if (boosterType.getType() == BoosterType.BoosterConfigureType.CHANCE) {
			this.config = module.getConfig().getBoosters().getChanceBoosters().get(boosterType.name().toLowerCase());
		} else {
			this.config = module.getConfig().getBoosters().getModifierBoosters().get(boosterType.name().toLowerCase());
		}
	}

	public void process() {
		this.module.getPlugin().getLogger().info("Processing " + boosterType.name() + " booster...");

		this.players.removeIf(p -> {
			if (this.isGlobalActivate) {
				return false;
			}

			Player player = module.getPlugin().getServer().getPlayer(p);
			if (player == null) {
				return true;
			}

			PixelmonBoosterPlayerData.BoosterData data = module.getBoosterService().get(player, boosterType);
			if (data == null) {
				return true;
			}

			data.setTimeLeft(data.getTimeLeft() - interval);
			this.module.getPlayerData().update(p.toString(), boosterType.name().toLowerCase(), data);

			this.module.getPlayerData().getData().get(p.toString()).put(boosterType.name().toLowerCase(), data);


			if (data.getTimeLeft() <= 0) {

				player.sendMessage(module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterExpired())
						.process("booster", boosterType.name())
						.get());
				return true;
			}

			return false;
		});

		if (!this.players.isEmpty()) {
			return;
		}

		this.module.getModuleLogger().info("Stopping " + boosterType.name() + " booster! (No players)");
		this.shutdown();
	}

	public void add(UUID uuid) {
		if (this.players.contains(uuid)) {
			return;
		}
		this.players.add(uuid);
		this.initialize();
	}

	public void remove(UUID uuid) {
		this.players.remove(uuid);
		this.process();
	}

	public void setGlobalActivate(boolean globalActivate) {
		this.isGlobalActivate = globalActivate;
		if (globalActivate) {
			this.initialize();
		}
	}

	public void removeOnlyGlobalActivate() {
		this.players.removeIf(p -> {
			if (this.isGlobalActivate) {
				return false;
			}

			Player player = module.getPlugin().getServer().getPlayer(p);
			if (player == null) {
				return true;
			}

			PixelmonBoosterPlayerData.BoosterData data = module.getBoosterService().get(player, boosterType);
			if (data == null) {
				return true;
			}

			return data.getTimeLeft() <= 0;
		});
	}
}
