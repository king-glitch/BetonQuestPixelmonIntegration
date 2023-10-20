package dev.rachamon.betonquestpixelmonintegration.compatible.v1_16_R3.reforged.events;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PokemonCatch {
    @SubscribeEvent
    public void onCatch(CaptureEvent.SuccessfulCapture event) {
        System.out.println("Pokemon caught!");
        PixelmonEntity caught = event.getPokemon();
        ServerPlayerEntity player = event.getPlayer();
        System.out.println("Pokemon: " + caught.getDisplayName().getString());
        System.out.println("Player: " + player.getDisplayName().getString());
    }
}
