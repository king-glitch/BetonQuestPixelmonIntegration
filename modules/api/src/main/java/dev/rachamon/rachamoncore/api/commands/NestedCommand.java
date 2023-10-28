package dev.rachamon.rachamoncore.api.commands;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

public class NestedCommand {
	final AbstractCommand parent;
	final LinkedHashMap<String, AbstractCommand> children = new LinkedHashMap<>();

	public NestedCommand(AbstractCommand parent) {
		this.parent = parent;
	}

	public void addSubCommand(AbstractCommand command) {
		command.getCommands().forEach(cmd -> children.put(cmd.toLowerCase(), command));
	}

	public void addSubCommands(AbstractCommand... commands) {
		Stream.of(commands)
				.forEach(command -> command.getCommands().forEach(cmd -> children.put(cmd.toLowerCase(), command)));
	}
}
