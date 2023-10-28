package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.TrainerMoneyBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BoosterService {
	@Getter
	private static Map<BoosterType, BoosterBase> boosters = new HashMap<>();
	private final PixelmonBoosterFactoryImpl module;

	public BoosterService(PixelmonBoosterFactoryImpl plugin) {
		this.module = plugin;

		boosters.put(BoosterType.BATTLE_WINNING, new TrainerMoneyBooster(plugin));
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

		boosters.get(boosterType).add(player.getUniqueId());

		boosterData.setActivated(true);

		module.getPlayerData().update(player.getUniqueId().toString(), boosterType.name().toLowerCase(), boosterData);
		module.getPlayerDataConfigNode().save();
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

		module.getPlayerData().update(player.getUniqueId().toString(), boosterType.name().toLowerCase(), boosterData);
		module.getPlayerDataConfigNode().save();

		boosters.get(boosterType).remove(player.getUniqueId());

	}

	public void add(Player player, BoosterType boosterType, int seconds) throws IOException {

		if (!boosters.containsKey(boosterType)) {
			return;
		}

		PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType, true);

		boosterData.setTimeLeft(boosterData.getTimeLeft() + seconds);

		this.module.getPlayerData()
				.update(player.getUniqueId().toString(), boosterType.name().toLowerCase(), boosterData);
		this.module.getPlayerDataConfigNode().save();

		if (boosterData.isActivated() || boosters.get(boosterType).isGlobalActivate()) {
			boosters.get(boosterType).add(player.getUniqueId());
		}
	}

	public void remove(Player player, BoosterType boosterType, int seconds) throws IOException {
		if (!boosters.containsKey(boosterType)) {
			return;
		}

		PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType, true);

		boosterData.setTimeLeft(boosterData.getTimeLeft() - seconds);

		if (boosterData.getTimeLeft() < 0) {
			boosterData.setTimeLeft(0);
		}

		module.getPlayerData().update(player.getUniqueId().toString(), boosterType.name().toLowerCase(), boosterData);
		module.getPlayerDataConfigNode().save();

		if (boosterData.isActivated() && boosterData.getTimeLeft() > 0) {
			boosters.get(boosterType).add(player.getUniqueId());
		}
	}

	public void set(Player player, BoosterType boosterType, int seconds) throws IOException {
		if (!boosters.containsKey(boosterType)) {
			return;
		}

		PixelmonBoosterPlayerData.BoosterData boosterData = this.get(player, boosterType, true);

		boosterData.setTimeLeft(seconds);

		module.getPlayerData().update(player.getUniqueId().toString(), boosterType.name().toLowerCase(), boosterData);
		module.getPlayerDataConfigNode().save();

		if (boosterData.isActivated()) {
			boosters.get(boosterType).add(player.getUniqueId());
		}
	}

	@Nullable
	public PixelmonBoosterPlayerData.BoosterData get(Player player, BoosterType boosterType) {
		return this.get(player.getUniqueId(), boosterType);
	}

	public PixelmonBoosterPlayerData.BoosterData get(UUID uuid, BoosterType boosterType) {
		if (!boosters.containsKey(boosterType)) {
			return null;
		}

		if (!module.getPlayerData().getData().containsKey(uuid.toString())) {
			return null;
		}

		Map<String, PixelmonBoosterPlayerData.BoosterData> data = module.getPlayerData()
				.getData()
				.get(uuid.toString());

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

		if (!module.getPlayerData().getData().containsKey(player.getUniqueId().toString())) {
			if (!createIfNotExists) {
				return null;
			}

			module.getPlayerData().getData().put(player.getUniqueId().toString(), new HashMap<>());
		}

		Map<String, PixelmonBoosterPlayerData.BoosterData> data = module.getPlayerData()
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

		this.saveGlobalActivate(boosterType);

		for (Player player : module.getPlugin().getServer().getOnlinePlayers()) {
			try {
				this.add(player, boosterType, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveGlobalActivate(BoosterType boosterType) {
		if (boosterType.getType() == BoosterType.BoosterConfigureType.CHANCE) {
			this.module.getConfig()
					.getBoosters()
					.getChanceBoosters()
					.get(boosterType.name().toLowerCase())
					.setGlobalActivate(false);
		} else {
			this.module.getConfig()
					.getBoosters()
					.getModifierBoosters()
					.get(boosterType.name().toLowerCase())
					.setGlobalActivate(false);
		}

		this.module.getConfigNode().save();
	}

	public boolean isGlobalActivated(BoosterType boosterType) {
		if (!boosters.containsKey(boosterType)) {
			return false;
		}

		BoosterBase boosterBase = boosters.get(boosterType);
		return boosterBase.isGlobalActivate();
	}

	public void deactivateGlobal(BoosterType boosterType) {
		if (!boosters.containsKey(boosterType)) {
			return;
		}

		BoosterBase boosterBase = boosters.get(boosterType);
		boosterBase.setGlobalActivate(false);
		boosterBase.removeOnlyGlobalActivate();

		this.saveGlobalActivate(boosterType);
	}

	public void reload() {
		boosters.values().forEach(BoosterBase::reload);
	}

	public boolean isPlayerBoosterActivated(UUID uuid, BoosterType boosterType) {
		if (!boosters.containsKey(boosterType)) {
			return false;
		}

		if (!module.getPlayerData().getData().containsKey(uuid.toString())) {
			return false;
		}


		PixelmonBoosterPlayerData.BoosterData boosterData = this.get(uuid, boosterType);
		if (boosterData == null) {
			return false;
		}

		return boosterData.isActivated();
	}

}