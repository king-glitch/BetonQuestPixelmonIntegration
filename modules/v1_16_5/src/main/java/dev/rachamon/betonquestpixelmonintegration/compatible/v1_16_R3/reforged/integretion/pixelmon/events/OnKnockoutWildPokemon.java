package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PixelmonKnockoutEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.factory.IntegrationFactoryImpl;
import dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.utils.SpecUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

import java.util.Locale;
import java.util.function.Consumer;

import static dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events.OnKnockoutPlayerPokemon.getString;

public class OnKnockoutWildPokemon extends Objective {

    protected String[] specs;
    protected int amount = 1;
    protected Consumer<PixelmonKnockoutEvent> listener = this::onKnockout;


    public OnKnockoutWildPokemon(Instruction instruction) throws InstructionParseException {
        super(instruction);

        template = OnKnockout.Data.class;
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
    public void onKnockout(PixelmonKnockoutEvent event) {
        if (event.isCanceled()) {
            return;
        }

        ServerPlayerEntity player = event.source.getPlayerOwner();
        PixelmonEntity pixelmon = event.pokemon.entity;
        if (player == null || pixelmon == null) {
            return;
        }

        if (!containsPlayer(player.getStringUUID())) {
            return;
        }

        if (!checkConditions(player.getStringUUID())) {
            return;
        }


        IntegrationFactoryImpl.logger.debug("pixelmon.knockout.wild: " + event.source.getPlayerOwner() + " " + event.pokemon.getTrainerOwner());
        IntegrationFactoryImpl.logger.debug("pixelmon.knockout.wild: " + event.pokemon.getPokemonName());
        IntegrationFactoryImpl.logger.debug("pixelmon.knockout.wild: " + SpecUtil.match(pixelmon, SpecUtil.parseSpecs(specs)));


        if (event.pokemon.getPlayerOwner() != null || event.pokemon.getTrainerOwner() != null) {
            return;
        }

        OnKnockout.Data data = (OnKnockout.Data) dataMap.get(player.getStringUUID());
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
}
