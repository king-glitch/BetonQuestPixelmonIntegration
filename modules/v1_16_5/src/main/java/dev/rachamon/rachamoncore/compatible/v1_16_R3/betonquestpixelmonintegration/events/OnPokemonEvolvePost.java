package dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.events;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.factory.BetonQuestObjectiveFactoryImpl;
import dev.rachamon.rachamoncore.compatible.v1_16_R3.betonquestpixelmonintegration.utils.SpecUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

import java.util.function.Consumer;

public class OnPokemonEvolvePost extends Objective {
    protected String[] specs;
    protected int amount = 1;
    protected Consumer<EvolveEvent.Post> listener = this::onEvolve;

    public OnPokemonEvolvePost(Instruction instruction) throws InstructionParseException {
        super(instruction);

        template = OnPokemonEvolvePre.Data.class;
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
        return OnKnockoutPlayerPokemon.getString(name, (OnKnockout.Data) dataMap.get(playerID), amount, playerID);
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
    public void onEvolve(EvolveEvent.Post event) {
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


        BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.evolve.post: " + event.getPokemon().getDisplayName());
        BetonQuestObjectiveFactoryImpl.instance.getModuleLogger().debug("pixelmon.evolve.post: " + SpecUtil.match(pixelmon, SpecUtil.parseSpecs(specs)));


        OnPokemonEvolvePre.Data data = (OnPokemonEvolvePre.Data) dataMap.get(player.getStringUUID());
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
