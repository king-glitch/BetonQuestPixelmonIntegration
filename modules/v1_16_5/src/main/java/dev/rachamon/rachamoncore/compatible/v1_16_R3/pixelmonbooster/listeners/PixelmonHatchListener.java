package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.pixelmonmod.pixelmon.api.daycare.event.DayCareEvent;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonEggHatchBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

public class PixelmonHatchListener {
	private final PixelmonBoosterFactoryImpl module;

	public PixelmonHatchListener(PixelmonBoosterFactoryImpl instance) {
		this.module = instance;
	}


	@SubscribeEvent
	public void onBreed(DayCareEvent.PreCollect event) {

		ServerPlayerEntity player = event.getPlayer();
		PixelmonEggHatchBooster booster = (PixelmonEggHatchBooster) BoosterService.getBoosters()
				.get(BoosterType.HATCH);

		if (booster == null) {
			return;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.HATCH);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return;
		}


		try {


			Player p = this.module.getPlugin().getServer().getPlayer(player.getUUID());
			if (p == null) {
				return;
			}

			int eggSteps = event.getChildGiven().getEggSteps();
			int after = booster.calculate(eggSteps);
			event.getChildGiven().setEggSteps(after);

			p.sendMessage(this.module.getLocale()
					.from(s -> s.getBoosterConfig().getEggSteps())
					.process("steps", eggSteps - after)
					.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
