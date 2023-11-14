package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.commands.admin;

import dev.rachamon.rachamoncore.api.commands.AbstractCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.PixelmonBooster;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminReloadCommand extends AbstractCommand {
	private final PixelmonBooster module;

	public AdminReloadCommand(PixelmonBooster module) {
		super(CommandType.BOTH, false, "reload");
		this.module = module;
	}

	@Override
	protected AbstractCommand.ReturnType execute(CommandSender sender, String... args) {

		this.module.reload();

		sender.sendMessage(this.module.getLocale()
				.from(s -> s.getGeneralConfig().getReload()).get());

		return AbstractCommand.ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return "rachamoncore.module.pixelmonbooster.reload";
	}

	@Override
	public String getSyntax() {
		return "reload";
	}

	@Override
	public String getDescription() {
		return "reload pixelmonbooster config";
	}
}
