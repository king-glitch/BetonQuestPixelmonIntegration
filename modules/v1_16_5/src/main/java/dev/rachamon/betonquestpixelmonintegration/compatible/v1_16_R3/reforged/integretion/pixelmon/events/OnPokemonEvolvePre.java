package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory.IntegrationFactoryImpl;
import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.utils.SpecUtil;
import lombok.Getter;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

import java.util.function.Consumer;

import static dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events.OnKnockoutPlayerPokemon.getString;

public class OnPokemonEvolvePre extends Objective {
    protected String[] specs;
    protected int amount = 1;
    protected Consumer<EvolveEvent.Pre> listener = this::onEvolve;


    public OnPokemonEvolvePre(Instruction instruction) throws InstructionParseException {
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
        return getString(name, (OnKnockout.Data) dataMap.get(playerID), amount, playerID);
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
    public void onEvolve(EvolveEvent.Pre event) {
        if (event.isCanceled()) {
            return;
        }

        ServerPlayerEntity player = event.getPlayer();
        PixelmonEntity pixelmon = event.getEntity();
        if (player == null || pixelmon == null) {
            return;
        }

        if (!containsPlayer(player.getStringUUID())) {
            return;
        }

        if (!checkConditions(player.getStringUUID())) {
            return;
        }


        IntegrationFactoryImpl.logger.debug("pixelmon.evolve.pre: " + event.getPokemon().getDisplayName());
        IntegrationFactoryImpl.logger.debug("pixelmon.evolve.pre: " + SpecUtil.match(pixelmon, SpecUtil.parseSpecs(specs)));


        Data data = (Data) dataMap.get(player.getStringUUID());
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
