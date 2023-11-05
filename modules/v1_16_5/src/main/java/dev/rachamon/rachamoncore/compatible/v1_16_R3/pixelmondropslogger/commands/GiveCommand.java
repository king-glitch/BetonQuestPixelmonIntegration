package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.commands;

import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.config.PixelmonDropsLoggerLog;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.factory.PixelmonDropsLoggerFactoryImpl;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GiveCommand extends AbstractCommand {
	private final PixelmonDropsLoggerFactoryImpl module;

	public GiveCommand(PixelmonDropsLoggerFactoryImpl module) {
		super(CommandType.BOTH, false, "give");
		this.module = module;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		if (args.length < 1) {
			return ReturnType.SYNTAX_ERROR;
		}


		String key = args[0];

		if (this.module.getLogData().getDataConfig().get(key) == null) {
			return ReturnType.SYNTAX_ERROR;
		}

		PixelmonDropsLoggerLog.Data drops = this.module.getLogData().getDataConfig().get(key);

		Player player = module.getPlugin().getServer().getPlayer(drops.getUsername());
		if (player == null) {
			sender.sendMessage(this.module.getLocale()
					.from(s -> s.getGeneralConfig().getPlayerNotOnline())
					.process("player", drops.getUsername())
					.get());
			return ReturnType.FAILURE;
		}

		// give drops to player

		ItemStack[] items = new ItemStack[drops.getDrops().size()];
		for (Map.Entry<String, Integer> entry : drops.getDrops().entrySet()) {
			Material material = Material.getMaterial(entry.getKey()
					.replaceAll("minecraft:", "")
					.replaceAll(":", "_")
					.toUpperCase());
			if (material == null) {
				this.module.getModuleLogger().error("Material " + entry.getKey() + " is not found");
				continue;
			}

			items[new ArrayList<>(drops.getDrops().entrySet()).indexOf(entry)] = new ItemStack(
					material,
					entry.getValue()
			);
		}

		player.getInventory().addItem(items);

		player.sendMessage(this.module.getLocale()
				.from(s -> s.getGeneralConfig().getPlayerReceivedDrops())
				.process("id", key)
				.get());

		sender.sendMessage(this.module.getLocale()
				.from(s -> s.getGeneralConfig().getGiveCommand())
				.process("player", player.getName())
				.process("id", key)
				.get());

		this.module.getLogData().removeDrops(key);
		this.module.getLogDataNode().save();

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		if (args.length == 1) {
			return new ArrayList<>(this.module.getLogData().getDataConfig().keySet());
		}

		return null;
	}

	@Override
	public String getPermissionNode() {
		return "rachamoncore.module.pixelmondropslogger.give";
	}

	@Override
	public String getSyntax() {
		return "give <key>";
	}

	@Override
	public String getDescription() {
		return "give drops to player by key";
	}
}
