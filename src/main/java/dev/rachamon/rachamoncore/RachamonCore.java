package dev.rachamon.rachamoncore;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.modules.BetonQuestPixelmonIntegrationFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PaletteTokensFactory;
import dev.rachamon.rachamoncore.api.factory.modules.PixelmonBoosterFactory;
import dev.rachamon.rachamoncore.api.utils.LoggerUtil;
import dev.rachamon.rachamoncore.config.LanguageConfig;
import dev.rachamon.rachamoncore.config.MainConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Objects;

@Getter
public class RachamonCore extends JavaPlugin implements IModuleFactory<RachamonCore> {
    public static RachamonCore instance;
    public LoggerUtil moduleLogger;
    public MainConfig pluginConfig;
    public LanguageConfig pluginLanguage;

    public RachamonCore() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Server server = Bukkit.getServer();
        moduleLogger = new LoggerUtil(server, "RachamonCore");
        moduleLogger.setDebug(true);
        moduleLogger.info("Starting plugin...");

        if (!this.getDataFolder().exists()) {
            boolean success = this.getDataFolder().mkdir();
            if (!success) {
                moduleLogger.error("Failed to create plugin directory!");
                this.getPluginLoader().disablePlugin(this);
                return;
            }

            moduleLogger.info("Plugin directory created!");

            // create modules directory
            boolean successModules = new java.io.File(this.getDataFolder(), "modules").mkdir();
            if (!successModules) {
                moduleLogger.error("Failed to create plugin modules directory!");
                this.getPluginLoader().disablePlugin(this);
                return;
            }

            moduleLogger.info("Plugin modules directory created!");
        }

        moduleLogger.info("Loading configs...");

        this.pluginConfig = new ConfigFactory<RachamonCore, MainConfig>(this, "main.yaml").setHeader("RachamonCore Config").build(MainConfig.class);
        this.pluginLanguage = new ConfigFactory<RachamonCore, LanguageConfig>(this, "language.yaml").setHeader("RachamonCore Language Config").build(LanguageConfig.class);

        moduleLogger.info("Configs loaded!");

        moduleLogger.info("Registering modules...");
        if (this.getPluginConfig().getModulesConfig().isBetonquestPixelmonIntegrationEnable()) {
            if (Bukkit.getPluginManager().getPlugin("BetonQuest") != null && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BetonQuest")).isEnabled()) {
                BetonQuestPixelmonIntegrationFactory.create(this, moduleLogger);
            }
        }

        if (this.getPluginConfig().getModulesConfig().isPaletteTokensEnable()) {
            PaletteTokensFactory.create(this, moduleLogger);
        }

        if (this.getPluginConfig().getModulesConfig().isPixelmonBoosterEnable()) {
            PixelmonBoosterFactory.create(this, moduleLogger);
        }

        moduleLogger.info("Features registered!");
        moduleLogger.info("Plugin started!");

    }

    @Override
    public void onDisable() {
        moduleLogger.info("Stopping plugin...");
    }


    @Override
    public Path getDirectory() {
        return this.getDataFolder().toPath();
    }

    @Override
    public RachamonCore getInstance() {
        return instance;
    }
}