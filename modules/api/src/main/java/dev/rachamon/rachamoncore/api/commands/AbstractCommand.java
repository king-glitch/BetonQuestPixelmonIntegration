package dev.rachamon.rachamoncore.api.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand {
	private final CommandType commandType;
	private final boolean hasArgs;
	private final List<String> handledCommands = new ArrayList<>();

	protected AbstractCommand(CommandType type, boolean hasArgs, String... command) {
		this.handledCommands.addAll(Arrays.asList(command));
		this.hasArgs = hasArgs;
		this.commandType = type;
	}

	public final List<String> getCommands() {
		return Collections.unmodifiableList(handledCommands);
	}

	public final void addSubCommand(String command) {
		handledCommands.add(command);
	}

	protected abstract ReturnType execute(CommandSender sender, String... args);

	protected abstract List<String> onTab(CommandSender sender, String... args);

	public abstract String getPermissionNode();

	public abstract String getSyntax();

	public abstract String getDescription();

	public boolean hasArgs() {
		return hasArgs;
	}

	public boolean isNoConsole() {
		return commandType == CommandType.PLAYER_ONLY;
	}

	public enum ReturnType {SUCCESS, NEEDS_PLAYER, FAILURE, SYNTAX_ERROR, EMPTY}

	public enum CommandType {PLAYER_ONLY, CONSOLE_ONLY, BOTH}
}
