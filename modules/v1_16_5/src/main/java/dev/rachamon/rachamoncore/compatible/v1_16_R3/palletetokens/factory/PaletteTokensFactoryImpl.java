package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.factory;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PaletteTokensFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config.LanguageConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config.MainConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PaletteTokensFactoryImpl extends PaletteTokensFactory {
    private final LoggerUtil moduleLogger;
    private Path directory;
    public MainConfig config;
    public LanguageConfig language;
    public static PaletteTokensFactoryImpl instance;

    public PaletteTokensFactoryImpl(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        PaletteTokensFactoryImpl.instance = this;

        this.moduleLogger = logger;

        Path directory = plugin.getDirectory().resolve("modules").resolve("PaletteTokens");
        if (!directory.toFile().exists()) {
            boolean success = directory.toFile().mkdir();
            if (!success) {
                logger.error("Error creating directory: " + directory);
                return;
            }

            logger.info("Created directory: " + directory);

        }

        this.directory = directory;

        logger.info("Initializing PaletteTokens module...");
        ConfigFactory<PaletteTokensFactoryImpl, MainConfig> config = new ConfigFactory<>(this, "main.yaml");
        ConfigFactory<PaletteTokensFactoryImpl, LanguageConfig> language = new ConfigFactory<>(this, "language.yaml");
        this.config = config.setHeader("PaletteTokens Config").build(MainConfig.class);
        this.language = language.setHeader("PaletteTokens Language Config").build(LanguageConfig.class);

    }

    @Override
    public PaletteTokensFactory getInstance() {
        return instance;
    }
}
