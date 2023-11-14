package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonExpBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.PixelmonBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

public class PixelmonExpListener {
	private final PixelmonBooster module;

	public PixelmonExpListener(PixelmonBooster instance) {
		this.module = instance;
	}


	@SubscribeEvent
	public void onExpGain(ExperienceGainEvent event) {

		ServerPlayerEntity player = event.pokemon.getPlayerOwner();
		PixelmonExpBooster booster = (PixelmonExpBooster) BoosterService.getBoosters().get(BoosterType.EXP);

		if (booster == null) {
			return;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.EXP);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return;
		}


		try {


			Player p = this.module.getPlugin().getServer().getPlayer(player.getUUID());
			if (p == null) {
				return;
			}

			int before = event.getExperience();
			int exp = booster.calculate(event.getExperience());
			event.setExperience(exp);
			p.sendMessage(this.module.getLocale()
					.from(s -> s.getBoosterConfig().getExpGain())
					.process("exp", exp - before)
					.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
