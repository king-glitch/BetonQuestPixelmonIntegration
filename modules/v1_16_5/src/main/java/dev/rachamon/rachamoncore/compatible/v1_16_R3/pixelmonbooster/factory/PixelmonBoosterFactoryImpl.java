package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PixelmonBoosterFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterLanguageConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.config.PixelmonBoosterPlayerData;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PixelmonBoosterFactoryImpl extends PixelmonBoosterFactory {
    private final LoggerUtil moduleLogger;
    private Path directory;
    private final JavaPlugin plugin;
    private PixelmonBoosterConfig config;
    private ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterPlayerData> playerDataConfig;
    private PixelmonBoosterPlayerData playerData;
    private Locale<PixelmonBoosterLanguageConfig> locale;

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

        logger.info("Loading configs...");
        ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterConfig> config = new ConfigFactory<>(
                this,
                "main.yaml"
        );
        ConfigFactory<PixelmonBoosterFactoryImpl, PixelmonBoosterLanguageConfig> locale = new ConfigFactory<>(
                this,
                "language.yaml"
        );
        this.playerDataConfig = new ConfigFactory<>(
                this,
                "player-data.yaml"
        );

        this.config = config.setHeader("PixelmonBooster Config").build(PixelmonBoosterConfig.class);
        this.playerData = this.playerDataConfig.build(PixelmonBoosterPlayerData.class);
        this.locale = new Locale<>(
                locale.setHeader("PixelmonBooster Language Config")
                      .build(PixelmonBoosterLanguageConfig.class),
                s -> s.getGeneralConfig().getPrefix()
        );
        logger.info("Loaded configs!");

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
