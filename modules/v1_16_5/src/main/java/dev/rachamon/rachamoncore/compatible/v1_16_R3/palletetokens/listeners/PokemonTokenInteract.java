package dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.listeners;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.palletetokens.PaletteTokens;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PokemonTokenInteract {
	private final PaletteTokens plugin;

	public PokemonTokenInteract(PaletteTokens plugin) {
		this.plugin = plugin;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPokemonTokenInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.isCanceled()) {
			return;
		}

		if (event.getHand().equals(Hand.OFF_HAND)) {
			return;
		}

		if (event.getPlayer().getItemInHand(Hand.MAIN_HAND).isEmpty()) {
			return;
		}

		if (!(event.getTarget() instanceof PixelmonEntity)) {
			return;
		}


		PixelmonEntity pixelmon = (PixelmonEntity) event.getTarget();
		ItemStack item = event.getPlayer().getItemInHand(Hand.MAIN_HAND);

		Player player = this.plugin.getPlugin().getServer().getPlayer(event.getPlayer().getUUID());
		if (player == null) {
			return;
		}

		if (!item.serializeNBT().contains("tag") || !item.serializeNBT()
				.getCompound("tag")
				.contains("rachamon-palette-token")) {
			return;
		}

		if (!Objects.equals(pixelmon.getOwnerUUID(), event.getPlayer().getUUID())) {
			player.sendMessage(this.plugin.getLocale().from(s -> s.getGeneralConfig().getNotPokemonOwner()).get());
			return;
		}

		CompoundNBT data = item.serializeNBT().getCompound("tag").getCompound("rachamon-palette-token");
		String pokemon = data.getString("pokemon");
		String palette = data.getString("palette");

		if (!pokemon.equals(pixelmon.getPokemon().getSpecies().getName())) {
			player.sendMessage(this.plugin.getLocale()
					.from(s -> s.getGeneralConfig().getCantUseOnThisPokemon())
					.process("pokemon", pokemon)
					.get());
			return;
		}

		event.setCanceled(true);

		if (pixelmon.getPalette().is(palette)) {
			player.sendMessage(this.plugin.getLocale()
					.from(s -> s.getGeneralConfig().getPokemonAlreadyHasTexture())
					.get());
			return;
		}

		if (pixelmon.getPokemon().isShiny() && !this.plugin.getConfig()
				.getGeneralConfig()
				.isApplyTextureOnShinyPokemon()) {
			player.sendMessage(this.plugin.getLocale()
					.from(s -> s.getGeneralConfig().getCantUseOnShinyPokemon())
					.get());
			return;
		}

		boolean success = pixelmon.getPokemon().setPalette(palette);
		if (!success) {
			player.sendMessage(this.plugin.getLocale()
					.from(s -> s.getGeneralConfig().getThisPokemonDoesntHaveThisPalette())
					.process("palette", palette)
					.process("pokemon", pokemon)
					.get());
			return;
		}


		pixelmon.resetDataWatchers();

		item.setCount(item.getCount() - 1);

		player.sendMessage(this.plugin.getLocale()
				.from(s -> s.getGeneralConfig().getSuccessfullyApplyTextureOnPokemon())
				.process("pokemon", pokemon)
				.process("palette", palette)
				.get());
	}
}
