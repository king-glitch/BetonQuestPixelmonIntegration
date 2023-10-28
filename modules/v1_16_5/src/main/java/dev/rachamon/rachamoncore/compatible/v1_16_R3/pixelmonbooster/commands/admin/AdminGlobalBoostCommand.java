package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin;

import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminGlobalBoostCommand extends AbstractCommand {
	private final PixelmonBoosterFactoryImpl module;

	public AdminGlobalBoostCommand(PixelmonBoosterFactoryImpl module) {
		super(CommandType.BOTH, false, "global");
		this.module = module;
	}

	@Override
	protected AbstractCommand.ReturnType execute(CommandSender sender, String... args) {

		if (args.length < 2) {
			return ReturnType.SYNTAX_ERROR;
		}

		args[0] = args[0].toUpperCase();
		args[1] = args[1].toLowerCase();

		BoosterType boosterType = BoosterType.valueOf(args[0]);

		if (!BoosterService.getBoosters().containsKey(boosterType)) {
			return ReturnType.SYNTAX_ERROR;
		}

		if (!args[1].toLowerCase().matches("activate|deactivate")) {
			return ReturnType.SYNTAX_ERROR;
		}


		if (!sender.hasPermission("rachamoncore.module.pixelmonbooster.boost.global." + args[0].toLowerCase() + "." + args[1])) {
			return ReturnType.SYNTAX_ERROR;
		}

		if (args[1].equalsIgnoreCase("activate")) {
			if (this.module.getBoosterService().isGlobalActivated(boosterType)) {
				sender.sendMessage(this.module.getLocale()
						.from(s -> s.getGlobalBoosterActivation().getAlreadyActivated())
						.process("booster", boosterType.getName())
						.get());
				return ReturnType.FAILURE;
			}

			try {
				this.module.getBoosterService().activateGlobal(BoosterType.valueOf(args[0]));
			} catch (Exception e) {
				return ReturnType.FAILURE;
			}
		} else if (args[1].equalsIgnoreCase("deactivate")) {
			if (this.module.getBoosterService().isGlobalActivated(boosterType)) {
				sender.sendMessage(this.module.getLocale()
						.from(s -> s.getGlobalBoosterActivation().getAlreadyDeactivated())
						.process("booster", boosterType.getName())
						.get());
				return ReturnType.FAILURE;
			}
			try {
				this.module.getBoosterService().deactivateGlobal(BoosterType.valueOf(args[0]));
				sender.sendMessage(this.module.getLocale()
						.from(s -> s.getGlobalBoosterActivation().getDeactivated())
						.process("booster", boosterType.getName())
						.get());
			} catch (Exception e) {
				return ReturnType.FAILURE;
			}
		}

		this.module.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
			if (args[1].equalsIgnoreCase("activate")) {
				player.sendMessage(this.module.getLocale()
						.from(s -> s.getGlobalBoosterActivation().getActivated())
						.process("booster", boosterType.getName())
						.get());
			} else if (args[1].equalsIgnoreCase("deactivate")) {
				player.sendMessage(this.module.getLocale()
						.from(s -> s.getGlobalBoosterActivation().getDeactivated())
						.process("booster", boosterType.getName())
						.get());
			}
		});


		return AbstractCommand.ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			return BoosterService.getBoosters().keySet().stream().map(Enum::name).collect(Collectors.toList());
		} else if (args.length == 2) {
			return new ArrayList<String>() {{
				add("activate");
				add("deactivate");
			}};
		}

		return null;
	}

	@Override
	public String getPermissionNode() {
		return "rachamoncore.module.pixelmonbooster.boost.global";
	}

	@Override
	public String getSyntax() {
		return "global <type> <activate|deactivate>";
	}

	@Override
	public String getDescription() {
		return "Pause or Resume a booster";
	}
}
