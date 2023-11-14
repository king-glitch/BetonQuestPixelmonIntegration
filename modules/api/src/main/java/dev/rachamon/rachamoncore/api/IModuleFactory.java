package dev.rachamon.rachamoncore.api;

import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public interface IModuleFactory<T> {
	LoggerUtil getModuleLogger();

	Path getDirectory();

	JavaPlugin getPlugin();

	CommandManager getCommandManager();

	String getName();

	void unload();

	void load();
}
