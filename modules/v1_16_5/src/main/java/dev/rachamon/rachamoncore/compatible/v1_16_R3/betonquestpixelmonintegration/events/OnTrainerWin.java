package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.factory.BetonQuestObjectiveFactoryImpl;
import lombok.Getter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

import java.util.Locale;
import java.util.function.Consumer;

public class OnTrainerWin extends Objective {
	protected int amount = 1;
	protected String trainer = "*";
	protected Consumer<BeatTrainerEvent> listener = this::onWin;


	public OnTrainerWin(Instruction instruction) throws InstructionParseException {
		super(instruction);

		template = Data.class;
		trainer = instruction.getOptional("trainer");
		amount = instruction.getPositive();

		if (trainer == null) {
			trainer = "*";
		}
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
	public void onWin(BeatTrainerEvent event) {
		if (event.isCanceled()) {
			return;
		}

		ServerPlayerEntity player = event.player;
		if (player == null) {
			return;
		}

		if (!containsPlayer(player.getStringUUID())) {
			return;
		}

		if (!checkConditions(player.getStringUUID())) {
			return;
		}


		BetonQuestObjectiveFactoryImpl.instance.getModuleLogger()
				.debug("pixelmon.trainer.win: " + event.trainer.getStringUUID());
		BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.trainer.win: " + trainer);


		if (!trainer.equals("*") && !trainer.equals(event.trainer.getStringUUID())) {
			return;
		}

		Data data = (Data) dataMap.get(player.getStringUUID());

		data.subtract();

		if (!data.isZero()) {
			return;
		}

		completeObjective(player.getStringUUID());

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
