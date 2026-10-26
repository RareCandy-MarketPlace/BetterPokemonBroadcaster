package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.BroadcasterUtil;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;

public class CaptureBroadcasterType extends AbstractBroadcasterType<CaptureEvent.SuccessfulCapture> {

    public CaptureBroadcasterType() {
        super("capture", CaptureEvent.SuccessfulCapture.class, Pixelmon.EVENT_BUS);
    }

    @Override
    protected boolean isEvent(CaptureEvent.SuccessfulCapture event) {
        return true;
    }

    @Override
    protected PixelmonEntity getEntity(CaptureEvent.SuccessfulCapture event) {
        try {
            return (PixelmonEntity) event.getClass().getMethod("getEntity").invoke(event);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Pokemon getPokemon(Event e) {
        if (e instanceof CaptureEvent.SuccessfulCapture event) {
            return event.getPokemon();
        }
        return null;
    }

    @Override
    protected Placeholder asEventPlaceholder(CaptureEvent.SuccessfulCapture event, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        Pokemon pokemon = event.getPokemon();
        ServerPlayer player = event.getPlayer();

        String playerName = "Unknown";
        if (player != null && player.getName() != null) {
            playerName = player.getName().getString();
        }
        if (playerName == null) playerName = "Unknown";

        String worldName = "Unknown";
        String biomeName = "Unknown";
        double x = 0, y = 0, z = 0;

        if (player != null && player.level() != null) {
            worldName = UtilWorld.getName(player.level());
            if (worldName == null) worldName = "Unknown";
            try {
                biomeName = BiomeHelper.getLocalizedBiomeName(player.level().getBiome(player.blockPosition())).getString();
                if (biomeName == null) biomeName = "Unknown";
            } catch (Exception ignored) {}
            x = player.getX();
            y = player.getY();
            z = player.getZ();
        }

        String pName = "Unknown";
        if (pokemon != null && pokemon.getSpecies() != null) {
            pName = pokemon.getSpecies().getName();
            if (pName == null) pName = "Unknown";
        }
        String pNameLower = pName.toLowerCase().replace(" ", "");

        final String fPlayerName = playerName;
        final String fWorldName = worldName;
        final String fBiomeName = biomeName;
        final String fPName = pName;
        final String fX = String.valueOf((int) x);
        final String fY = String.valueOf((int) y);
        final String fZ = String.valueOf((int) z);

        return Placeholder.composition(
                PlaceholderCache.get(pokemon),
                Placeholder.simple(line -> line
                        .replace("%nearest_name%", fPlayerName)
                        .replace("%player%", fPlayerName)
                        .replace("%x%", fX)
                        .replace("%y%", fY)
                        .replace("%z%", fZ)
                        .replace("%world%", fWorldName)
                        .replace("%pokemon%", fPName)
                        .replace("%pokemon_lower%", pNameLower)
                        .replace("%biome%", fBiomeName)
                ));
    }

    @Override
    public ServerPlayer findNearestPlayer(CaptureEvent.SuccessfulCapture event, PixelmonEntity entity, double range) {
        return event.getPlayer();
    }

    @SubscribeEvent
    public void onCapture(CaptureEvent.SuccessfulCapture event) {
        BroadcasterUtil.handleEvent(event, this);
    }
}