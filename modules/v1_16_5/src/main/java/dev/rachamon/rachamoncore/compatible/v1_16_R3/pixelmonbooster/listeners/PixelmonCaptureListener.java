package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.catching.CaptureValues;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonCaptureBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

public class PixelmonCaptureListener {
	private final PixelmonBoosterFactoryImpl module;

	public PixelmonCaptureListener(PixelmonBoosterFactoryImpl instance) {
		this.module = instance;
	}


	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGeneralCapture(CaptureEvent.StartCapture event) {
		onCapture(event.getPlayer(), event.getCaptureValues());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onRaidCapture(CaptureEvent.StartRaidCapture event) {
		onCapture(event.getPlayer(), event.getCaptureValues());
	}

	private void onCapture(
			ServerPlayerEntity player,
			CaptureValues captureValues
	) {
		PixelmonCaptureBooster booster = (PixelmonCaptureBooster) BoosterService.getBoosters().get(BoosterType.CAPTURE);

		if (booster == null) {
			return;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.CAPTURE);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return;
		}


		try {


			Player p = this.module.getPlugin().getServer().getPlayer(player.getUUID());
			if (p == null) {
				return;
			}

			int before = captureValues.getCatchRate();
			int rate = booster.calculate(before);
			captureValues.setCatchRate(rate);
			p.sendMessage(this.module.getLocale()
					.from(s -> s.getBoosterConfig().getCaptureRate())
					.process("rate", rate - before)
					.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
