package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.pixelmonmod.pixelmon.api.economy.BankAccountProxy;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.TrainerMoneyBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

public class BattleEndListener {

	private final PixelmonBoosterFactoryImpl module;

	public BattleEndListener(PixelmonBoosterFactoryImpl instance) {
		this.module = instance;
	}


	@SubscribeEvent
	public void onPlayerBeatTrainer(BeatTrainerEvent event) {

		if (event.player == null) {
			return;
		}

		ServerPlayerEntity player = event.player;
		TrainerMoneyBooster booster = (TrainerMoneyBooster) BoosterService.getBoosters()
				.get(BoosterType.BATTLE_WINNING);

		if (booster == null) {
			return;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.BATTLE_WINNING);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return;
		}

		try {
			int money = booster.calculate(event.trainer.winMoney) - event.trainer.winMoney;
			BankAccountProxy.getBankAccount(player).ifPresent((account) -> {
				account.add(money);
			});

			Player p = this.module.getPlugin().getServer().getPlayer(player.getUUID());
			if (p == null) {
				return;
			}

			p.sendMessage(this.module.getLocale()
					.from(s -> s.getBoosterConfig().getBeatTrainerWinMoney()).process("money", money)
					.get());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
