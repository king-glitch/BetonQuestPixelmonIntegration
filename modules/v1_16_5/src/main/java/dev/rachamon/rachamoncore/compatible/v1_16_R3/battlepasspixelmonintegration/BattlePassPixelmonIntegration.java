package dev.rachamon.rachamoncore.compatible.v1_16_R3.battlepasspixelmonintegration;

import dev.rachamon.rachamoncore.api.IModuleFactory;
import dev.rachamon.rachamoncore.api.factory.ModuleFactory;
import org.bukkit.plugin.java.JavaPlugin;

public class BattlePassPixelmonIntegration extends ModuleFactory {
	public BattlePassPixelmonIntegration(IModuleFactory<? extends JavaPlugin> module) {
		super(module.getPlugin(), "BattlePassPixelmonIntegration");
	}
}
