package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.listeners;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.api.events.DropEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmondropslogger.factory.PixelmonDropsLoggerFactoryImpl;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PixelmonDropListener {
	private final PixelmonDropsLoggerFactoryImpl module;

	public PixelmonDropListener(PixelmonDropsLoggerFactoryImpl instance) {
		this.module = instance;
	}


	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void onDrop(DropEvent event) {
		if (event.player == null) {
			return;
		}

		if (!(event.entity instanceof PixelmonEntity)) {
			return;
		}

		PixelmonEntity pixelmon = (PixelmonEntity) event.entity;

		if (this.module.getConfig().getGeneralConfig().isOnlyBoss() && !pixelmon.isBossPokemon()) {
			return;
		}

		ImmutableList<DroppedItem> drops = event.getDrops();

		Map<String, Integer> dropsMap = new HashMap<>();
		for (DroppedItem drop : drops) {

			ItemStack item;
			if (drop.item == null) {
				item = drop.drop.getDisplayItem();
			} else {
				item = drop.item;
			}

			if (item == null) {
				this.module.getModuleLogger().info(drop.drop + " Item is null");
				continue;
			}

			if (item.getItem().getRegistryName() == null) {
				this.module.getModuleLogger().info(item.getDisplayName().getString() + " Registry name is null");
				continue;
			}

			String id = Objects.requireNonNull(Material.getMaterial(item.getItem()
					.getRegistryName()
					.toString()
					.replaceAll("minecraft:", "")
					.replaceAll(":", "_")
					.toUpperCase())).toString();
			if (dropsMap.containsKey(id)) {
				dropsMap.put(id, dropsMap.get(id) + item.getCount());
				continue;
			}
			dropsMap.put(id, item.getCount());


		}

		// generate random id for the drop
		// 8 characters
		String dropId = this.generateRandomId();

		Player player = this.module.getPlugin().getServer().getPlayer(event.player.getUUID());
		if (player == null) {
			return;
		}

		// save to file
		this.module.getLogData().addDrops(dropId, player.getName(), dropsMap);
		this.module.getLogDataNode().save();


		player.sendMessage(this.module.getLocale()
				.from(s -> s.getGeneralConfig().getPlayerDropMessage())
				.process("id", dropId)
				.get());


	}

	private String generateRandomId() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(8);

		for (int i = 0; i < 8; i++) {

			int index = (int) (AlphaNumericString.length() * Math.random());

			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}
}
