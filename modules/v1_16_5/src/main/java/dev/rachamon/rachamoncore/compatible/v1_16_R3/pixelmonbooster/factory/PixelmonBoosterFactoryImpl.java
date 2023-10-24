package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.factory.modules.PixelmonBoosterFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PixelmonBoosterFactoryImpl extends PixelmonBoosterFactory {
    private final LoggerUtil moduleLogger;
    private Path directory;
    private final JavaPlugin plugin;

    public PixelmonBoosterFactoryImpl(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        this.moduleLogger = logger;
        this.plugin = (JavaPlugin) plugin;

        logger.info("Initializing PixelmonBooster module...");
        Path directory = plugin.getDirectory().resolve("modules").resolve("PixelmonBooster");
        if (!directory.toFile().exists()) {
            boolean success = directory.toFile().mkdir();
            if (!success) {
                logger.error("Error creating directory: " + directory);
                return;
            }

            logger.info("Created directory: " + directory);
        }

        this.directory = directory;

        logger.info("Initialized PixelmonBooster module!");

    }

    @Override
    public CommandManager getCommandManager() {
        return null;
    }

    @Override
    public void unload() {
        this.moduleLogger.info("Unloading PixelmonBooster module...");
        this.moduleLogger.info("Unloaded PixelmonBooster module!");
    }
}
