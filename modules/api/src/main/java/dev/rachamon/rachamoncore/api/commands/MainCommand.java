package dev.rachamon.rachamoncore.api.commands;

import dev.rachamon.rachamoncore.api.locale.Locale;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand extends AbstractCommand {
	String header = null;
	String description;
	boolean sortHelp = false;
	final String command;
	final Plugin plugin;
	protected final NestedCommand nestedCommands;

	public MainCommand(Plugin plugin, String command) {
		super(CommandType.BOTH, false, command);

		this.command = command;
		this.plugin = plugin;
		this.description = "Shows the command help page for /" + command;
		this.nestedCommands = new NestedCommand(this);
	}

	public MainCommand setHeader(String header) {
		this.header = header;
		return this;
	}

	public MainCommand setDescription(String description) {
		this.description = description;
		return this;
	}

	public MainCommand setSortHelp(boolean sortHelp) {
		this.sortHelp = sortHelp;
		return this;
	}

	public MainCommand subCommand(AbstractCommand command) {
		nestedCommands.addSubCommand(command);
		return this;
	}

	public MainCommand subCommands(AbstractCommand... commands) {
		nestedCommands.addSubCommands(commands);
		return this;
	}

	@Override
	protected ReturnType execute(CommandSender sender, String... args) {
		sender.sendMessage("");

		if (header != null) {
			sender.sendMessage(header);
		} else {
			Locale.fromText(String.format(
					"{#ff8080}&l%s &8» &7Version %s Created with <3",
					plugin.getDescription().getName(),
					plugin.getDescription().getVersion()
			)).send(sender);
		}

		sender.sendMessage("");

		if (nestedCommands != null) {
			List<String> commands = nestedCommands.children.values()
					.stream()
					.distinct()
					.map(c -> c.getCommands().get(0))
					.collect(Collectors.toList());

			if (sortHelp) {
				Collections.sort(commands);
			}

			boolean isPlayer = sender instanceof Player;
			sender.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + getSyntax() + ChatColor.GRAY + " - " + getDescription());

			for (String cmdStr : commands) {
				final AbstractCommand cmd = nestedCommands.children.get(cmdStr);
				if (cmd == null) {
					continue;
				}
				if (!isPlayer) {
					sender.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + cmd.getSyntax() + ChatColor.GRAY + " - " + cmd.getDescription());
				} else if (cmd.getPermissionNode() == null || sender.hasPermission(cmd.getPermissionNode())) {
					Locale.fromText("/" + command + " " + ChatColor.DARK_GRAY + "- " + ChatColor.YELLOW + cmd.getSyntax() + ChatColor.GRAY + " - " + cmd.getDescription())
							.send(sender);
				}
			}
		}

		sender.sendMessage("");

		return ReturnType.SUCCESS;
	}

	@Override
	protected List<String> onTab(CommandSender sender, String... args) {
		return null;
	}

	@Override
	public String getPermissionNode() {
		return null;
	}

	@Override
	public String getSyntax() {
		return "/" + command;
	}

	@Override
	public String getDescription() {
		return description;
	}
}