package dev.rachamon.rachamoncore.api.utils;

import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.logging.Logger;

public class LoggerUtil {
	@Getter
	private String name = "RachamonAPI";
	private final Logger console;
	private boolean debug = false;

	/**
	 * Instantiates a new Logger util.
	 *
	 * @param server the server
	 */
	public LoggerUtil(Server server, String name) {
		this.console = server.getLogger();
		this.name = name;
	}

	/**
	 * Info.
	 *
	 * @param message the message
	 */
	public void info(String message) {
		console.info(("&8[&4&l" + this.getName() + "&8][&bINFO&8]&7: &a" + message).replaceAll(
				"&([0-9a-fA-Fk-oK-OrR])",
				"§$1"
		));
	}

	/**
	 * Success.
	 *
	 * @param message the message
	 */
	public void success(String message) {
		console.info(("&8[&4&l" + this.getName() + "&8][&aSUCCESS&8]&7: &a" + message).replaceAll(
				"&([0-9a-fA-Fk-oK-OrR])",
				"§$1"
		));
	}

	/**
	 * Error.
	 *
	 * @param message the message
	 */
	public void error(String message) {
		console.warning(("&8[&4&l" + this.getName() + "&8][&cERROR&8]&7: &a" + message).replaceAll(
				"&([0-9a-fA-Fk-oK-OrR])",
				"§$1"
		));

	}

	/**
	 * Warning.
	 *
	 * @param message the message
	 */
	public void warning(String message) {
		console.warning(("&8[&4&l" + this.getName() + "&8][&eWARNING&8]&7: &a" + message).replaceAll(
				"&([0-9a-fA-Fk-oK-OrR])",
				"§$1"
		));
	}

	/**
	 * Warning.
	 *
	 * @param message the message
	 */
	public void warning(java.lang.StackTraceElement[] message) {
		console.warning(("&8[&4&l" + this.getName() + "&8][&eWARNING&8]&7: &a" + Arrays.toString(message)).replaceAll(
				"&([0-9a-fA-Fk-oK-OrR])",
				"§$1"
		));
	}

	/**
	 * Raw.
	 *
	 * @param message the message
	 */
	public void raw(String message) {
		console.info((message).replaceAll("&([0-9a-fA-Fk-oK-OrR])", "§$1"));
	}

	/**
	 * Debug.
	 *
	 * @param message the message
	 */
	public void debug(String message) {
		if (!this.getDebug()) {
			return;
		}
		console.info(("&8[&4&l" + this.getName() + "&8][&dDEBUG&8]&7: &a" + message).replaceAll(
				"&([0-9a-fA-Fk-oK-OrR])",
				"§$1"
		));
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean getDebug() {
		return this.debug;
	}

}
