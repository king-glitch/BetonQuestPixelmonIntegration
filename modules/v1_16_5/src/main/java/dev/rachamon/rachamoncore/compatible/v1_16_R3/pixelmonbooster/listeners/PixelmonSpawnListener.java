package dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.listeners;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnLocation;
import com.pixelmonmod.pixelmon.api.world.BlockCollection;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.BoosterType;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonBossSpawnRateBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonHASpawnRateBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.domain.boosters.PixelmonShinySpawnRateBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.PixelmonBooster;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.pixelmonbooster.service.BoosterService;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PixelmonSpawnListener {

	private final PixelmonBooster module;

	public PixelmonSpawnListener(PixelmonBooster instance) {
		this.module = instance;
	}

	@SubscribeEvent
	public void onPixelmonSpawn(SpawnEvent event) {
		if (!((event.action.getOrCreateEntity()) instanceof PixelmonEntity)) {
			return;
		}

		if (!(event.spawner instanceof PlayerTrackingSpawner)) {
			return;
		}

		ServerPlayerEntity player = ((PlayerTrackingSpawner) event.spawner).getTrackedPlayer();
		if (player == null) {
			return;
		}

		PixelmonEntity pixelmon = (PixelmonEntity) event.action.getOrCreateEntity();

		if (this.onSpawnBossModifier(player, pixelmon)) {
			return;
		}

		this.onSpawnHAModifier(player, pixelmon);
		this.onSpawnShinyModifier(player, pixelmon);

	}

	public boolean onSpawnHAModifier(ServerPlayerEntity player, PixelmonEntity entity) {
		PixelmonHASpawnRateBooster booster = (PixelmonHASpawnRateBooster) BoosterService.getBoosters()
				.get(BoosterType.HIDDEN_ABILITY);

		if (booster == null) {
			return false;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.HIDDEN_ABILITY);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return false;
		}

		if (!booster.success()) {
			return false;
		}

		entity.getPokemon().setAbility(entity.getForm().getAbilities().getRandomHiddenAbility());
		entity.update(EnumUpdateType.Ability);

		Player bukkitPlayer = this.module.getPlugin().getServer().getPlayer(player.getUUID());
		if (bukkitPlayer == null) {
			return false;
		}

		bukkitPlayer.sendMessage(this.module.getLocale()
				.from(s -> s.getBoosterConfig().getHiddenAbilitySpawnSuccess())
				.process("pokemon", entity.getPokemon().getSpecies().getName())
				.get());

		return true;
	}

	public boolean onSpawnShinyModifier(ServerPlayerEntity player, PixelmonEntity entity) {
		if (!entity.getPokemon().isDefaultForm() || entity.getPokemon().isShiny()) {
			return false;
		}

		PixelmonShinySpawnRateBooster booster = (PixelmonShinySpawnRateBooster) BoosterService.getBoosters()
				.get(BoosterType.SHINY_RATE);

		if (booster == null) {
			return false;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.SHINY_RATE);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return false;
		}

		if (!booster.success()) {
			return false;
		}


		entity.getPokemon().setShiny(true);

		Player bukkitPlayer = this.module.getPlugin().getServer().getPlayer(player.getUUID());
		if (bukkitPlayer == null) {
			return false;
		}

		bukkitPlayer.sendMessage(this.module.getLocale()
				.from(s -> s.getBoosterConfig().getShinySpawnSuccess())
				.process("pokemon", entity.getPokemon().getSpecies().getName())
				.get());

		return true;
	}

	public boolean onSpawnBossModifier(ServerPlayerEntity player, PixelmonEntity entity) {
		PixelmonBossSpawnRateBooster booster = (PixelmonBossSpawnRateBooster) BoosterService.getBoosters()
				.get(BoosterType.BOSS);

		if (booster == null) {
			return false;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.BOSS);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return false;
		}

		if (!booster.success()) {
			return false;
		}

		entity.setBossTier(BossTierRegistry.getRandomBossTier());

		Player bukkitPlayer = this.module.getPlugin().getServer().getPlayer(player.getUUID());
		if (bukkitPlayer == null) {
			return false;
		}

		bukkitPlayer.sendMessage(this.module.getLocale()
				.from(s -> s.getBoosterConfig().getHiddenAbilitySpawnSuccess())
				.process("pokemon", entity.getPokemon().getSpecies().getName())
				.get());

		return true;
	}

	@Nullable
	public PixelmonEntity getRandomPokemon(ServerPlayerEntity player) {
		if (!PixelmonSpawning.coordinator.getActive()) {
			return null;
		}

		PixelmonShinySpawnRateBooster booster = (PixelmonShinySpawnRateBooster) BoosterService.getBoosters()
				.get(BoosterType.POKEMON_SPAWN);

		if (booster == null) {
			return null;
		}

		boolean isPlayerBoosterActivated = this.module.getBoosterService()
				.isPlayerBoosterActivated(player.getUUID(), BoosterType.POKEMON_SPAWN);

		if (!booster.isGlobalActivate() && !isPlayerBoosterActivated) {
			return null;
		}

		if (!booster.success()) {
			return null;
		}


		AbstractSpawner spawner = PixelmonSpawning.coordinator.getSpawner(player.getName().getString());
		PlayerTrackingSpawner playerTrackingSpawner = (PlayerTrackingSpawner) spawner;

		if (playerTrackingSpawner == null) {
			return null;
		}

		CompletableFuture<BlockCollection> trackedBlock = spawner.getTrackedBlockCollection(
				player,
				0.0F,
				0.0F,
				playerTrackingSpawner.horizontalSliceRadius,
				playerTrackingSpawner.verticalSliceRadius,
				0,
				0
		);
		ArrayList<SpawnLocation> spawnLocations = null;
		try {
			trackedBlock.wait();

			spawnLocations = playerTrackingSpawner.spawnLocationCalculator.calculateSpawnableLocations(trackedBlock.get());
		} catch (InterruptedException | ExecutionException e) {
			return null;
		}

		if (spawnLocations == null) {
			return null;
		}


		Map<SpawnLocation, List<SpawnInfo>> possibleSpawns = new HashMap<>();

		for (SpawnLocation spawnLocation : spawnLocations) {
			ArrayList<SpawnInfo> spawns = (ArrayList<SpawnInfo>) spawner.getSuitableSpawns(spawnLocation);
			if (!spawns.isEmpty()) {
				possibleSpawns.put(spawnLocation, spawns);
			}
		}

		if (possibleSpawns.isEmpty()) {
			return null;
		}

		Map<String, Double> pokemons = spawner.selectionAlgorithm.getPercentages(spawner, possibleSpawns);

		List<String> blacklist = this.module.getConfig().getGeneralConfig().getRandomPokemonBlacklist();


		if (pokemons.isEmpty()) {
			return null;
		}

		// remove blacklisted Pokémon from the list
		for (String pokemon : blacklist) {
			pokemons.remove(pokemon);
		}

		if (pokemons.isEmpty()) {
			return null;
		}

		// check if the Pokémon list is legendary
		if (!this.module.getConfig().getGeneralConfig().isAllowedRandomPokemonLegendary()) {
			pokemons.keySet().removeIf(pokemon -> {
				RegistryValue<Species> species = PixelmonSpecies.fromName(pokemon);
				if (species == null) {
					return true;
				}

				if (!species.getValue().isPresent()) {
					return true;
				}

				return species.getValue().get().isLegendary();
			});
		}

		String pokemon = this.getRandomPokemon(pokemons);

		RegistryValue<Species> species = PixelmonSpecies.fromName(pokemon);
		if (species == null) {
			return null;
		}

		return null;

	}

	private String getRandomPokemon(Map<String, Double> chances) {
		double chance = new Random().nextDouble() * 100.0;
		double cumulative = 0.0;
		for (String pokemon : chances.keySet()) {
			cumulative += chances.get(pokemon);
			if (chance < cumulative) {
				return pokemon;
			}
		}
		return null;
	}
}
