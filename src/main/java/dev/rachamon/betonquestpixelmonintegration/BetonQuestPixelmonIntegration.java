package dev.rachamon.betonquestpixelmonintegration;

import dev.rachamon.core.commons.factory.IntegrationFactory;
import dev.rachamon.core.commons.utils.LoggerUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BetonQuestPixelmonIntegration extends JavaPlugin {
    @Getter
    public static BetonQuestPixelmonIntegration instance;
    @Getter
    private IntegrationFactory integrationFactory;
    public LoggerUtil logger;

    public BetonQuestPixelmonIntegration() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Server server = Bukkit.getServer();
        logger = new LoggerUtil(server, "BetonQuestPixelmonIntegration");
        logger.info("Starting plugin...");

        if (Bukkit.getPluginManager().getPlugin("BetonQuest") == null || !Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BetonQuest")).isEnabled()) {
            logger.error("BetonQuest not found or not enabled!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        logger.info("Registering integrations...");
        integrationFactory = IntegrationFactory.create(logger, false);
        logger.info("Integrations registered!");

        logger.info("Plugin started!");
    }

    @Override
    public void onDisable() {
        logger.info("Stopping plugin...");
    }
}