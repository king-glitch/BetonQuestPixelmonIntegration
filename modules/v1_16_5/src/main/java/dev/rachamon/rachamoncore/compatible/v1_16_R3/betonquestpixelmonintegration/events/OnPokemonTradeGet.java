package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.factory.BetonQuestObjectiveFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.utils.SpecUtil;
import lombok.Getter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

import java.util.Locale;
import java.util.function.Consumer;

public class OnPokemonTradeGet extends Objective {
	protected String[] specs;
	protected int amount = 1;
	protected Consumer<PixelmonTradeEvent.Post> listener = this::onPokemonTrade;


	public OnPokemonTradeGet(Instruction instruction) throws InstructionParseException {
		super(instruction);

		template = Data.class;
		specs = instruction.getArray();
		amount = instruction.getPositive();
	}

	@Override
	public void start() {
		Pixelmon.EVENT_BUS.addListener(listener);
	}

	@Override
	public void stop() {
		Pixelmon.EVENT_BUS.unregister(listener);
	}

	@Override
	public String getDefaultDataInstruction() {
		return Integer.toString(amount);
	}

	@Override
	public String getProperty(String name, String playerID) {
		switch (name.toLowerCase(Locale.ROOT)) {
			case "left":
				return Integer.toString(((Data) dataMap.get(playerID)).getAmount());
			case "amount":
				return Integer.toString(amount);
			default:
				return "";
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPokemonTrade(PixelmonTradeEvent.Post event) {
		if (event.isCanceled()) {
			return;
		}

		if (!(event.getPlayer1() instanceof ServerPlayerEntity)) {
			return;
		}

		ServerPlayerEntity player1 = (ServerPlayerEntity) event.getPlayer1();
		ServerPlayerEntity player2 = (ServerPlayerEntity) event.getPlayer2();
		Pokemon pixelmon1 = event.getPokemon1();
		Pokemon pixelmon2 = event.getPokemon2();

		BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.trade.get: " + player1.getName());
		BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.trade.get: " + player2.getName());
		BetonQuestObjectiveFactoryImpl.instance.getModuleLogger()
				.debug("pixelmon.trade.get: " + SpecUtil.match(pixelmon1, SpecUtil.parseSpecs(specs)));
		BetonQuestObjectiveFactoryImpl.instance.getModuleLogger()
				.debug("pixelmon.trade.get: " + SpecUtil.match(pixelmon2, SpecUtil.parseSpecs(specs)));


		if (!containsPlayer(player1.getStringUUID()) && !checkConditions(player1.getStringUUID())) {
			Data data = (Data) dataMap.get(player1.getStringUUID());
			// check if match the Pokémon specs
			if (SpecUtil.match(pixelmon1, SpecUtil.parseSpecs(specs))) {
				data.subtract();

				if (data.isZero()) {
					completeObjective(player1.getStringUUID());
				}
			}
		}

		if (!containsPlayer(player2.getStringUUID()) && !checkConditions(player2.getStringUUID())) {
			Data data = (Data) dataMap.get(player2.getStringUUID());
			// check if match the Pokémon specs
			if (SpecUtil.match(pixelmon2, SpecUtil.parseSpecs(specs))) {
				data.subtract();

				if (data.isZero()) {
					completeObjective(player2.getStringUUID());
				}
			}
		}
	}

	@Getter
	public static class Data extends ObjectiveData {
		public int amount;

		public Data(final String instruction, final String playerID, final String objID) {
			super(instruction, playerID, objID);
			amount = Integer.parseInt(instruction);
		}

		public void subtract() {
			amount--;
			update();
		}

		public boolean isZero() {
			return amount <= 0;
		}

		@Override
		public String toString() {
			return Integer.toString(amount);
		}
	}
}
