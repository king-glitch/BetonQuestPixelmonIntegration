package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.integretion.pixelmon.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;

public class OnDefeated extends Objective {

    public OnDefeated(Instruction instruction) throws InstructionParseException {
        super(instruction);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getDefaultDataInstruction() {
        return null;
    }

    @Override
    public String getProperty(String name, String playerID) {
        return null;
    }

    @SubscribeEvent
    public void onDefeated() {

    }
}
