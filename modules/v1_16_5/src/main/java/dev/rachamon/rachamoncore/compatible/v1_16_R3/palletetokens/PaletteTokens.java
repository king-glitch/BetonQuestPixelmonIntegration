package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens;


import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ConfigFactory;
import dev.rachamon.rachamoncore.api.factory.ModuleFactory;
import dev.rachamon.rachamoncore.api.locale.Locale;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.commands.PaletteTokensGiveCommand;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config.LanguageConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.config.MainConfig;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.listeners.PokemonTokenInteract;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PaletteTokens extends ModuleFactory {
	private final MainConfig config;
	private final Locale<LanguageConfig> locale;
	private final PokemonTokenInteract pokemonTokenInteract;

	public PaletteTokens(IModuleFactory<? extends JavaPlugin> module) {
		super(module.getPlugin(), "PaletteTokens");
		this.pokemonTokenInteract = new PokemonTokenInteract(this);

		this.getModuleLogger().info("Initializing PaletteTokens module...");

		this.getModuleLogger().info("Loading configs...");
		ConfigFactory<PaletteTokens, MainConfig> config = new ConfigFactory<>(this, "main.yaml");
		ConfigFactory<PaletteTokens, LanguageConfig> language = new ConfigFactory<>(this, "language.yaml");
		this.config = config.setHeader("PaletteTokens Config").build(MainConfig.class);
		this.locale = new Locale<>(
				language.setHeader("PaletteTokens Language").build(LanguageConfig.class),
				s -> s.getGeneralConfig().getPrefix()
		);
		this.getModuleLogger().info("Loaded configs!");

		this.getModuleLogger().info("Registering commands...");
		this.getCommandManager().mainCommand("palettetokens").subCommands(new PaletteTokensGiveCommand(this));
		this.getModuleLogger().info("Registered commands!");

		this.getModuleLogger().info("registering listeners...");

		MinecraftForge.EVENT_BUS.addListener(pokemonTokenInteract::onPokemonTokenInteract);

		this.getModuleLogger().info("Initialized PaletteTokens module!");

	}

	@Override
	public void unload() {
		this.getModuleLogger().info("Unloading PaletteTokens module...");
		MinecraftForge.EVENT_BUS.unregister(pokemonTokenInteract);
		this.getModuleLogger().info("Unloaded PaletteTokens module!");
	}
}
