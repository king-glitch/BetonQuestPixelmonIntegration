package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands;

import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterBase;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.PixelmonBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.utils.BoosterUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class InfoCommand extends AbstractCommand {
	private final PixelmonBooster module;

	public InfoCommand(PixelmonBooster module) {
		super(CommandType.PLAYER_ONLY, false, "info");
		this.module = module;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		Player player = (Player) sender;
		StringBuilder text = new StringBuilder();
		StringBuilder dataText = new StringBuilder();
		for (Map.Entry<BoosterType, BoosterBase> booster : BoosterService.getBoosters().entrySet()) {
			PixelmonBoosterPlayerData.BoosterData boosterData = this.module.getBoosterService()
					.get(player, booster.getKey());
			if (boosterData == null) {
				continue;
			}

			dataText.append(module.getLocale()
					.raw(s -> s.getGeneralConfig().getBoosterInfo())
					.process("booster", booster.getKey().getName())
					.process("time", BoosterUtil.secondsToTime(boosterData.getTimeLeft()))
					.process(
							"activated",
							boosterData.isActivated() && boosterData.getTimeLeft() > 0 ? module.getLocale()
									.raw(s -> s.getGeneralConfig().getActivated())
									.get() : module.getLocale().raw(s -> s.getGeneralConfig().getDeactivated()).get()
					)
					.get()).append("\n");
		}

		for (int i = 0; i < module.getLocale().get().getGeneralConfig().getBoosterInfoTemplate().size(); i++) {
			int finalI = i;
			text.append(module.getLocale()
					.raw(s -> s.getGeneralConfig().getBoosterInfoTemplate().get(finalI))
					.process("data", dataText.toString())
					.get()).append("\n");
		}


		player.sendMessage(text.toString());

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "rachamoncore.module.pixelmonbooster.info";
	}

	@Override
	public String getSyntax() {
		return "info";
	}

	@Override
	public String getDescription() {
		return "get info";
	}
}
