package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.google.common.collect.ImmutableList;
import com.pixelmonmod.pixelmon.api.events.DropEvent;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DroppedItem;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonDropBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.factory.PixelmonBoosterFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

import java.util.*;

public class PixelmonDropListener {
	private final PixelmonBoosterFactoryImpl module;

	public PixelmonDropListener(PixelmonBoosterFactoryImpl instance) {
		this.module = instance;
	}


	@SubscribeEvent
	public void onDrop(DropEvent event) {

		if (event.player == null) {
			return;
		}

		ServerPlayerEntity player = event.player;
		PixelmonDropBooster booster = (PixelmonDropBooster) BoosterService.getBoosters().get(BoosterType.DROP);

		if (booster == null) {
			return;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.DROP);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return;
		}

		PixelmonEntity dropper = (PixelmonEntity) event.entity;
		List<ItemStack> items = DropItemRegistry.getDropsForPokemon(dropper);

		try {
			Map<String, Integer> extraDrops = new HashMap<>();
			ImmutableList<DroppedItem> drops = event.getDrops();
			int difference = (booster.calculate(drops.size())) - drops.size();
			for (int i = 0; i < difference; i++) {
				ItemStack stack = items.get(new Random().nextInt(items.size()));
				Item dropItem = stack.getItem();
				try {
					extraDrops.put(
							dropItem.getName(stack).getString(),
							extraDrops.getOrDefault(Objects.requireNonNull(dropItem.getRegistryName()).toString(), 0) + 1
					);
				} catch (Exception ignored) {

				}

				event.addDrop(new ItemStack(dropItem));
			}

			Player p = this.module.getPlugin().getServer().getPlayer(player.getUUID());
			if (p == null) {
				return;
			}

			StringBuilder data = new StringBuilder();
			for (Map.Entry<String, Integer> entry : extraDrops.entrySet()) {
				data.append(this.module.getLocale()
						.raw(s -> s.getBoosterConfig().getPixelmonExtraDropsItemTemplate())
						.process("item", entry.getKey())
						.process("amount", entry.getValue())
						.get()).append(", ");
			}
			p.sendMessage(this.module.getLocale()
					.from(s -> s.getBoosterConfig().getPixelmonExtraDrops())
					.process("data", data.toString())
					.get());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
