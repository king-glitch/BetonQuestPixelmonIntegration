package dev.rachamon.rachamoncore.api.factory;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import io.leangen.geantyref.TypeToken;
import lombok.Getter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class ConfigFactory<P extends IModuleFactory<?>, T> {
    @Getter
    private T root;
    @Getter
    private final String name;
    private final P plugin;
    private ConfigurationNode configRoot;
    private YamlConfigurationLoader configLoader;
    private Class<T> classType;
    private String header;

    public ConfigFactory(P plugin, String fileName) {
        this.plugin = plugin;
        this.name = fileName;
        this.plugin.getModuleLogger().info("Loading configuration -> " + fileName + " config module");
    }

    public T build(Class<T> clazz) {
        this.classType = clazz;
        try {
            File mainConfig = new File(this.plugin.getDirectory().toFile(), this.getName());
            if (!mainConfig.exists()) {
                this.plugin.getModuleLogger().info("Creating " + this.getName() + " Configuration...");

                boolean success = mainConfig.createNewFile();
                if (!success) {
                    this.plugin.getModuleLogger().error("Failed to create " + this.getName() + " Configuration!");
                }
            }

            this.configLoader = YamlConfigurationLoader.builder().indent(4).nodeStyle(NodeStyle.BLOCK).file(mainConfig).build();
            this.configRoot = configLoader.load(ConfigurationOptions.defaults().header(this.header));
            this.root = configRoot.get(TypeToken.get(this.classType));

            this.save();
            this.plugin.getModuleLogger().success("loaded " + this.getName() + " configuration...");
            return root;

        } catch (IOException e) {
            this.plugin.getModuleLogger().error(Arrays.toString(e.getStackTrace()));
        }
        return root;
    }

    public void save() throws IOException {
        try {
            this.configLoader.save(configRoot);
        } catch (IOException e) {
            this.plugin.getModuleLogger().error(e.getMessage());
        }
    }

    public ConfigFactory<P, T> setHeader(String header) {
        this.header = header;
        return this;
    }
}