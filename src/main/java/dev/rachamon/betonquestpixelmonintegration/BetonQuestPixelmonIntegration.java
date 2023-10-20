package dev.rachamon.betonquestpixelmonintegration;

import dev.rachamon.core.commons.factory.IntegrationEventFactory;
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
    private IntegrationEventFactory integrationEventFactory;

    public BetonQuestPixelmonIntegration() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Server server = Bukkit.getServer();
        LoggerUtil logger = new LoggerUtil(server, "BetonQuestPixelmonIntegration");
        logger.info("Starting plugin...");

        if (Bukkit.getPluginManager().getPlugin("BetonQuest") == null || !Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BetonQuest")).isEnabled()) {
            System.out.println("BetonQuest not found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        logger.info("Registering integrations...");
        integrationEventFactory = IntegrationEventFactory.create(logger);
        logger.info("Integrations registered!");

        logger.info("Plugin started!");
    }

    @Override
    public void onDisable() {
        System.out.println("Stopping plugin...");
        integrationEventFactory.unregister();
    }
}