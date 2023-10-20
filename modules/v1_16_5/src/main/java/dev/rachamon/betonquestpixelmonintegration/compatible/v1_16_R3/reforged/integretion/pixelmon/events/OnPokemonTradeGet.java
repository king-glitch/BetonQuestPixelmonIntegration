package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.utils.SpecUtil;
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

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
    public void onPokemonTrade(PixelmonTradeEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }

        if (!(event.getPlayer1() instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer1();
        Pokemon pixelmon = event.getPokemon1();

        if (!containsPlayer(player.getStringUUID())) {
            return;
        }

        if (!checkConditions(player.getStringUUID())) {
            return;
        }

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
