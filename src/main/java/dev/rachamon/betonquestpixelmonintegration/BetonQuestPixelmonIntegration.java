package dev.rachamon.betonquestpixelmonintegration;

import dev.rachamon.betonquestpixelmonintegration.commons.factory.IntegrationEventFactory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        System.out.println("Starting plugin...");

        if (Bukkit.getPluginManager().getPlugin("BetonQuest") == null) {
            System.out.println("BetonQuest not found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        System.out.println("injecting events...");
        integrationEventFactory = IntegrationEventFactory.create();
        System.out.println("events injected!");

        System.out.println("Plugin Loaded!");
    }

    @Override
    public void onDisable() {
        System.out.println("Stopping plugin...");
        integrationEventFactory.unregister();
    }
}