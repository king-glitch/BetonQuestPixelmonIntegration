package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.TrainerMoneyBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

public class BattleEndListener {


    @SubscribeEvent
    public void onPlayerBeatTrainer(BeatTrainerEvent event) {

        if (event.player == null) {
            return;
        }

        if (!(event.player instanceof Player)) {
            return;
        }

        Player player = (Player) event.player;
        TrainerMoneyBooster booster = (TrainerMoneyBooster) BoosterService.getBoosters().get(BoosterType.BATTLE_WINNING);

        if (booster == null) {
            return;
        }

        boolean isPlayerBoosterActivated = booster.isActivated(player.getUniqueId().toString());

        if (!booster.isGlobalActivate() && isPlayerBoosterActivated) {
            return;
        }

        try {
            event.trainer.winMoney = booster.calculate(event.trainer.winMoney);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
