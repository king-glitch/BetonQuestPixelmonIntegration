package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
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

@SuppressWarnings("PMD.CommentRequired")
public class OnPokemonCatchEvent extends Objective {
    protected String[] specs;
    protected int amount = 1;

    protected Consumer<CaptureEvent.SuccessfulCapture> listener = this::onCatchListener;

    public OnPokemonCatchEvent(final Instruction instruction) throws InstructionParseException {
        super(instruction);
        template = PokemonCatchData.class;
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

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
    public void onCatchListener(CaptureEvent.SuccessfulCapture event) {
        if (event.isCanceled()) {
            return;
        }

        PixelmonEntity pixelmon = event.getPokemon();
        ServerPlayerEntity player = event.getPlayer();

        if (!containsPlayer(player.getStringUUID())) {
            return;
        }

        if (!checkConditions(player.getStringUUID())) {
            return;
        }


        BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.catch: " + event.getPokemon().getPokemonName());
        BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.catch: " + SpecUtil.match(pixelmon, SpecUtil.parseSpecs(specs)));


        PokemonCatchData data = (PokemonCatchData) dataMap.get(player.getStringUUID());

        // check if match the Pokémon specs
        if (!SpecUtil.match(pixelmon, SpecUtil.parseSpecs(specs))) {
            return;
        }

        data.subtract();
        if (!data.isZero()) {
            return;
        }

        completeObjective(player.getStringUUID());
    }

    @Override
    public String getProperty(String name, String playerID) {
        switch (name.toLowerCase(Locale.ROOT)) {
            case "left":
                return Integer.toString(((PokemonCatchData) dataMap.get(playerID)).getAmount());
            case "amount":
                return Integer.toString(amount);
            default:
                return "";
        }
    }

    @Getter
    public static class PokemonCatchData extends ObjectiveData {
        public int amount;

        public PokemonCatchData(final String instruction, final String playerID, final String objID) {
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
