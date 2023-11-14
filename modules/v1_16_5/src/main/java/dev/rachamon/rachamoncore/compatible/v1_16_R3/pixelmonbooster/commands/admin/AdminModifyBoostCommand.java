package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin;

import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.PixelmonBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.utils.BoosterUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminModifyBoostCommand extends AbstractCommand {
	private final PixelmonBooster module;

	public AdminModifyBoostCommand(PixelmonBooster plugin) {
		super(CommandType.BOTH, false, "modify");
		this.module = plugin;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 4) {
			return ReturnType.SYNTAX_ERROR;
		}

		args[0] = args[0].toUpperCase();
		args[1] = args[1].toLowerCase();

		BoosterType boosterType = BoosterType.valueOf(args[0]);

		if (!BoosterService.getBoosters().containsKey(boosterType)) {
			return ReturnType.SYNTAX_ERROR;
		}

		if (!args[1].toLowerCase().matches("add|remove|set")) {
			return ReturnType.SYNTAX_ERROR;
		}

		Player target = module.getPlugin().getServer().getPlayer(args[2]);
		if (target == null) {
			return ReturnType.SYNTAX_ERROR;
		}

		if (!args[3].matches("\\d+")) {
			return ReturnType.SYNTAX_ERROR;
		}


		int seconds = Integer.parseInt(args[3]);


		if (!sender.hasPermission("rachamoncore.module.pixelmonbooster.boost.modify." + args[0].toLowerCase() + "." + args[1])) {
			return ReturnType.SYNTAX_ERROR;
		}

		try {
			if (args[1].equalsIgnoreCase("add")) {
				this.module.getBoosterService().add(target, BoosterType.valueOf(args[0]), seconds);
				sender.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterModificationAddedSuccess())
						.process("player", target.getName())
						.process("booster", boosterType.getName())
						.process("time", BoosterUtil.secondsToTime(seconds))
						.get());
			} else if (args[1].equalsIgnoreCase("remove")) {
				this.module.getBoosterService().remove(target, BoosterType.valueOf(args[0]), seconds);
				sender.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterModificationRemovedSuccess())
						.process("player", target.getName())
						.process("booster", boosterType.getName())
						.process("time", BoosterUtil.secondsToTime(seconds))
						.get());
			} else if (args[1].equalsIgnoreCase("set")) {
				this.module.getBoosterService().set(target, BoosterType.valueOf(args[0]), seconds);
				sender.sendMessage(this.module.getLocale()
						.from(s -> s.getGeneralConfig().getBoosterModificationSetSuccess())
						.process("player", target.getName())
						.process("booster", boosterType.getName())
						.process("time", BoosterUtil.secondsToTime(seconds))
						.get());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ReturnType.SYNTAX_ERROR;
		}


		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			return BoosterService.getBoosters().keySet().stream().map(Enum::name).collect(Collectors.toList());
		} else if (args.length == 2) {
			return new ArrayList<String>() {{
				add("add");
				add("remove");
				add("set");
			}};
		} else if (args.length == 3) {
			return module.getPlugin()
					.getServer()
					.getOnlinePlayers()
					.stream()
					.map(Player::getName)
					.collect(Collectors.toList());
		} else if (args.length == 4) {
			return new ArrayList<String>() {{
				add("<seconds>");
			}};
		}

		return null;
	}

	@Override
	public String getPermissionNode() {
		return "rachamoncore.module.pixelmonbooster.boost.modify";
	}

	@Override
	public String getSyntax() {
		return "modify <boost> <add|remove|set> <player> <seconds>";
	}

	@Override
	public String getDescription() {
		return "modify a boost to a player";
	}
}
