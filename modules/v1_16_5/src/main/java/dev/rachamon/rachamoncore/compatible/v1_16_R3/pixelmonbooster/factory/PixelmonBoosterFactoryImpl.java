package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PixelmonBoosterFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PixelmonBoosterFactoryImpl extends PixelmonBoosterFactory {
    private final LoggerUtil moduleLogger;
    private Path directory;
    public static PixelmonBoosterFactoryImpl instance;

    public PixelmonBoosterFactoryImpl(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        this.moduleLogger = logger;
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

        logger.info("Initializing PixelmonBooster module...");
    }


    @Override
    public PixelmonBoosterFactory getInstance() {
        return instance;
    }
}
