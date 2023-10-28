package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerActionListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PixelmonBoosterFactoryImpl module = PixelmonBoosterFactoryImpl.getInstance();
		Map<String, PixelmonBoosterPlayerData.BoosterData> boosterData = module.getPlayerData()
				.getData()
				.get(player.getUniqueId().toString());

		if (boosterData == null) {
			return;
		}

		List<String> activatedBoosters = new ArrayList<>();
		List<String> activatedGlobalBoosters = new ArrayList<>();
		int boosterAmount = 0;
		for (Map.Entry<BoosterType, BoosterBase> booster : BoosterService.getBoosters().entrySet()) {
			PixelmonBoosterPlayerData.BoosterData data = boosterData.get(booster.getKey().name().toLowerCase());

			if (data == null && !booster.getValue().isGlobalActivate()) {
				continue;
			}

			if (data != null && data.getTimeLeft() > 0) {
				boosterAmount++;
			}

			if (booster.getValue().isGlobalActivate()) {
				booster.getValue().add(player.getUniqueId());
				activatedGlobalBoosters.add(booster.getKey().getName());
			} else if (data.isActivated()) {
				booster.getValue().add(player.getUniqueId());
				activatedBoosters.add(booster.getKey().name());
			}

		}

		int finalBoosterAmount = boosterAmount;
		module.getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(module.getPlugin(), () -> {
			if (!activatedBoosters.isEmpty()) {
				player.sendMessage(module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterJoinInfo())
						.process("boosters", String.join(", ", activatedBoosters))
						.process("amount", finalBoosterAmount)
						.process("activated-amount", activatedBoosters.size())
						.get());
			}

			if (!activatedGlobalBoosters.isEmpty()) {
				player.sendMessage(module.getLocale()
						.from(s -> s.getGeneralConfig().getGlobalActivatedJoinInfo())
						.process("boosters", String.join(", ", activatedGlobalBoosters))
						.get());
			}
		}, 60L);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		BoosterService.getBoosters().forEach((boosterType, boosterBase) -> boosterBase.remove(player.getUniqueId()));
	}
}