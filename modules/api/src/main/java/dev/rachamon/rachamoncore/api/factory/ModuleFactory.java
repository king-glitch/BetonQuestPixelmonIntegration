package dev.rachamon.rachamoncore.api.factory;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.api.utils.ReflectionUtil;
import dev.rachamon.rachamoncore.api.version.MCVersion;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class ModuleFactory implements IModuleFactory<ModuleFactory> {

	private final JavaPlugin plugin;
	private final LoggerUtil moduleLogger;
	private Path directory;
	private final CommandManager commandManager;
	private final String name;

	public static ModuleFactory create(IModuleFactory<? extends JavaPlugin> plugin, String pluginPath) {
		return (ModuleFactory) ReflectionUtil.getConstructor(
				"dev.rachamon.rachamoncore.compatible." + MCVersion.getCurrent()
						.name() + "." + pluginPath,
				IModuleFactory.class
		).invoke(plugin);
	}

	public ModuleFactory(JavaPlugin plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		this.moduleLogger = new LoggerUtil(plugin.getServer(), name);
		this.directory = plugin.getDataFolder().toPath().resolve(name);
		this.commandManager = new CommandManager(plugin);

		this.load();
	}

	@Override
	public LoggerUtil getModuleLogger() {
		return this.moduleLogger;
	}

	@Override
	public Path getDirectory() {
		return this.directory;
	}

	@Override
	public JavaPlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public CommandManager getCommandManager() {
		return this.commandManager;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void unload() {
		this.moduleLogger.info("Unloading module " + this.name + "...");
	}

	@Override
	public void load() {
		this.moduleLogger.info("Loading module " + this.name + "...");
		Path directory = this.getDirectory().resolve("modules").resolve(this.getName());
		if (!directory.toFile().exists()) {
			boolean success = directory.toFile().mkdir();
			if (!success) {
				this.getModuleLogger().error("Error creating directory: " + directory);
				return;
			}

			this.getModuleLogger().info("Created directory: " + directory);
		}

		this.directory = directory;
	}
}