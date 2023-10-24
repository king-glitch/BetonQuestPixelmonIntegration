package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.factory;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.commands.CommandManager;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PaletteTokensFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.commands.PaletteTokensGiveCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config.LanguageConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config.MainConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.listeners.PokemonTokenInteract;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

@Getter
public class PaletteTokensFactoryImpl extends PaletteTokensFactory {
    private final LoggerUtil moduleLogger;
    private Path directory;
    private MainConfig config;
    private Locale<LanguageConfig> locale;
    private final JavaPlugin plugin;
    private CommandManager commandManager;

    private final PokemonTokenInteract pokemonTokenInteract;

    public PaletteTokensFactoryImpl(IModuleFactory<? extends JavaPlugin> plugin, LoggerUtil logger) {
        this.plugin = (JavaPlugin) plugin;
        this.plugin.getLogger().info("Initializing PaletteTokens module...");
        this.moduleLogger = logger;
        this.pokemonTokenInteract = new PokemonTokenInteract(this);

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

        logger.info("Loading configs...");
        ConfigFactory<PaletteTokensFactoryImpl, MainConfig> config = new ConfigFactory<>(this, "main.yaml");
        ConfigFactory<PaletteTokensFactoryImpl, LanguageConfig> language = new ConfigFactory<>(this, "language.yaml");
        this.config = config.setHeader("PaletteTokens Config").build(MainConfig.class);
        this.locale = new Locale<>(language.setHeader("PaletteTokens Language").build(LanguageConfig.class), s -> s.getGeneralConfig().getPrefix());
        logger.info("Loaded configs!");

        logger.info("Registering commands...");
        this.commandManager = new CommandManager(this.getPlugin());
        this.commandManager.mainCommand("palettetokens").subCommands(new PaletteTokensGiveCommand(this));
        logger.info("Registered commands!");

        logger.info("registering listeners...");

        MinecraftForge.EVENT_BUS.addListener(pokemonTokenInteract::onPokemonTokenInteract);

        logger.info("Initialized PaletteTokens module!");

    }

    @Override
    public void unload() {
        this.getModuleLogger().info("Unloading PaletteTokens module...");
        MinecraftForge.EVENT_BUS.unregister(pokemonTokenInteract);
        this.getModuleLogger().info("Unloaded PaletteTokens module!");
    }
}
