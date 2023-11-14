package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands;

import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.PixelmonBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsBoostCommand extends AbstractCommand {
	private final PixelmonBooster module;

	public SettingsBoostCommand(PixelmonBooster module) {
		super(AbstractCommand.CommandType.PLAYER_ONLY, false, "settings");
		this.module = module;
	}

	@Override
	protected AbstractCommand.ReturnType execute(CommandSender sender, String... args) {
		Player player = (Player) sender;

		if (args.length < 2) {
			return ReturnType.SYNTAX_ERROR;
		}

		args[0] = args[0].toUpperCase();
		args[1] = args[1].toLowerCase();

		BoosterType boosterType = BoosterType.valueOf(args[0]);


		if (!BoosterService.getBoosters().containsKey(boosterType)) {
			return ReturnType.SYNTAX_ERROR;
		}

		if (!args[1].toLowerCase().matches("pause|resume")) {
			return ReturnType.SYNTAX_ERROR;
		}


		if (!sender.hasPermission("rachamoncore.module.pixelmonbooster.boost.settings." + args[0].toLowerCase() + "." + args[1])) {
			return ReturnType.SYNTAX_ERROR;
		}

		if (args[1].equalsIgnoreCase("pause")) {
			try {

				PixelmonBoosterPlayerData.BoosterData boosterData = this.module.getBoosterService()
						.get(player, boosterType);

				if (boosterData == null || boosterData.getTimeLeft() <= 0) {
					player.sendMessage(this.module.getLocale()
							.from(s -> s.getGeneralConfig().getBoosterNoTime())
							.process("booster", boosterType.getName())
							.get());

					return ReturnType.FAILURE;
				}

				if (!this.module.getBoosterService()
						.isPlayerBoosterActivated(player.getUniqueId(), boosterType)) {
					player.sendMessage(this.module.getLocale()
							.from(s -> s.getGeneralConfig().getBoosterAlreadyPause())
							.process("booster", boosterType.getName())
							.get());

					return ReturnType.FAILURE;
				}

				this.module.getBoosterService().pause(player, boosterType);
				player.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterPause())
						.process("booster", boosterType.getName())
						.get());
			} catch (Exception e) {
				return ReturnType.FAILURE;
			}
		} else if (args[1].equalsIgnoreCase("resume")) {
			PixelmonBoosterPlayerData.BoosterData boosterData = this.module.getBoosterService()
					.get(player, boosterType);

			if (boosterData == null || boosterData.getTimeLeft() <= 0) {
				player.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterNoTime())
						.process("booster", boosterType.getName())
						.get());

				return ReturnType.FAILURE;
			}

			if (this.module.getBoosterService()
					.isPlayerBoosterActivated(player.getUniqueId(), boosterType)) {
				player.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterAlreadyResume())
						.process("booster", boosterType.getName())
						.get());

				return ReturnType.FAILURE;
			}
			try {
				this.module.getBoosterService().resume(player, boosterType);
				player.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterResume())
						.process("booster", boosterType.getName())
						.get());
			} catch (Exception e) {
				return ReturnType.FAILURE;
			}
		}


		return AbstractCommand.ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			return BoosterService.getBoosters().keySet().stream().map(Enum::name).collect(Collectors.toList());
		} else if (args.length == 2) {
			return new ArrayList<String>() {{
				add("pause");
				add("resume");
			}};
		}

		return null;
	}

	@Override
	public String getPermissionNode() {
		return "rachamoncore.module.pixelmonbooster.boost.settings";
	}

	@Override
	public String getSyntax() {
		return "settings <type> <resume|pause>";
	}

	@Override
	public String getDescription() {
		return "Pause or Resume a booster";
	}
}
