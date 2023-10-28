package dev.rachamon.rachamoncore.api.factory;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import io.leangen.geantyref.TypeToken;
import lombok.Getter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.HeaderMode;
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
	private ConfigurationNode node;
	private YamlConfigurationLoader configLoader;
	private String header;
	private Class<T> clazz;

	public ConfigFactory(P plugin, String fileName) {
		this.plugin = plugin;
		this.name = fileName;
		this.plugin.getModuleLogger().info("Loading configuration -> " + fileName + " config module");
	}

	public T build(Class<T> clazz) {
		try {
			this.clazz = clazz;
			File mainConfig = new File(this.plugin.getDirectory().toFile(), this.getName());
			if (!mainConfig.exists()) {
				this.plugin.getModuleLogger().info("Creating " + this.getName() + " Configuration...");

				boolean success = mainConfig.createNewFile();
				if (!success) {
					this.plugin.getModuleLogger().error("Failed to create " + this.getName() + " Configuration!");
				}
			}

			this.configLoader = YamlConfigurationLoader.builder()
					.indent(4)
					.nodeStyle(NodeStyle.BLOCK)
					.headerMode(HeaderMode.PRESET)
					.defaultOptions(ConfigurationOptions.defaults().header(this.header))
					.file(mainConfig)
					.build();

			this.node = configLoader.load();
			this.root = node.get(TypeToken.get(this.clazz));

			this.save();
			this.plugin.getModuleLogger().success("loaded " + this.getName() + " configuration...");
			return this.root;

		} catch (IOException e) {
			this.plugin.getModuleLogger().error(Arrays.toString(e.getStackTrace()));
		}
		return root;
	}

	public void save() {
		try {
			this.node.set(this.clazz, this.root);
			this.configLoader.save(this.node);
		} catch (IOException e) {
			this.plugin.getModuleLogger().error(e.getMessage());
		}
	}

	public ConfigFactory<P, T> setHeader(String header) {
		this.header = header;
		return this;
	}
}