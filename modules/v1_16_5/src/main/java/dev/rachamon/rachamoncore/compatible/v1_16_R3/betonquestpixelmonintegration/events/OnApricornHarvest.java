package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.ApricornEvent;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.BetonQuestPixelmonIntegration;
import lombok.Getter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

import java.util.Locale;
import java.util.function.Consumer;

public class OnApricornHarvest extends Objective {
	protected int amount = 1;
	protected String apricorn = "*";
	protected Consumer<ApricornEvent.Pick> listener = this::onApricornHarvestListener;


	public OnApricornHarvest(Instruction instruction) throws InstructionParseException {
		super(instruction);

		template = ApricornHarvestData.class;
		amount = instruction.getPositive();
		apricorn = instruction.getOptional("apricorn");

		if (apricorn == null) {
			apricorn = "*";
		}
	}

	@Override
	public void start() {
		Pixelmon.EVENT_BUS.addListener(listener);
	}

	@Override
	public void stop() {
		Pixelmon.EVENT_BUS.unregister(this);
	}

	@Override
	public String getDefaultDataInstruction() {
		return Integer.toString(amount);
	}

	@Override
	public String getProperty(String name, String playerID) {
		switch (name.toLowerCase(Locale.ROOT)) {
			case "left":
				return Integer.toString(((OnPokemonCatchEvent.PokemonCatchData) dataMap.get(playerID)).getAmount());
			case "amount":
				return Integer.toString(amount);
			default:
				return "";
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onApricornHarvestListener(ApricornEvent.Pick event) {
		if (event.isCanceled()) {
			return;
		}

		ServerPlayerEntity player = event.getPlayer();
		ApricornHarvestData data = (ApricornHarvestData) dataMap.get(player.getStringUUID());

		if (data == null) {
			return;
		}

		if (!containsPlayer(player.getStringUUID())) {
			return;
		}

		if (!checkConditions(player.getStringUUID())) {
			return;
		}


		BetonQuestPixelmonIntegration.instance.getModuleLogger()
				.debug("pixelmon.apricorn.harvest: " + event.getApricorn().apricorn().apricorn.name());
		BetonQuestPixelmonIntegration.instance.getModuleLogger().debug("pixelmon.apricorn.harvest: " + apricorn);
		BetonQuestPixelmonIntegration.instance.getModuleLogger()
				.debug("pixelmon.apricorn.harvest: " + (apricorn.equals("*") || event.getApricorn()
						.apricorn().apricorn.name()
						.toLowerCase()
						.contains(apricorn.toLowerCase())));


		if (apricorn.equals("*") || event.getApricorn()
				.apricorn().apricorn.name()
				.toLowerCase()
				.contains(apricorn.toLowerCase())) {
			data.subtract();
		}

		if (!data.isZero()) {
			return;
		}

		completeObjective(player.getStringUUID());
	}


	@Getter
	public static class ApricornHarvestData extends ObjectiveData {
		public int amount;

		public ApricornHarvestData(final String instruction, final String playerID, final String objID) {
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
