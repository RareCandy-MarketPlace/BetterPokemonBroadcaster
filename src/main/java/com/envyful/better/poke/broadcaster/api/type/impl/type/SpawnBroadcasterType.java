package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.BroadcasterUtil;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;

public class SpawnBroadcasterType extends AbstractBroadcasterType<SpawnEvent> {

    public SpawnBroadcasterType() {
        super("spawn", SpawnEvent.class, Pixelmon.EVENT_BUS);
    }

    @Override
    protected boolean isEvent(SpawnEvent event) {
        return event.action.getOrCreateEntity() instanceof PixelmonEntity;
    }

    @Override
    protected PixelmonEntity getEntity(SpawnEvent event) {
        Entity entity = event.action.getOrCreateEntity();
        if (entity instanceof PixelmonEntity) {
            return (PixelmonEntity) entity;
        }
        return null;
    }

    @Override
    public Pokemon getPokemon(Event e) {
        if (e instanceof SpawnEvent event) {
            PixelmonEntity entity = getEntity(event);
            if (entity != null) return entity.getPokemon();
        }
        return null;
    }

    @Override
    protected Placeholder asEventPlaceholder(SpawnEvent event, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        Pokemon pokemon = pixelmon != null ? pixelmon.getPokemon() : null;

        String playerName = "Unknown";
        if (nearestPlayer != null && nearestPlayer.getName() != null) {
            playerName = nearestPlayer.getName().getString();
        }
        if (playerName == null) playerName = "Unknown";

        String worldName = "Unknown";
        String biomeName = "Unknown";
        double x = 0, y = 0, z = 0;

        if (pixelmon != null && pixelmon.level() != null) {
            worldName = UtilWorld.getName(pixelmon.level());
            if (worldName == null) worldName = "Unknown";
            try {
                biomeName = BiomeHelper.getLocalizedBiomeName(pixelmon.level().getBiome(pixelmon.blockPosition())).getString();
                if (biomeName == null) biomeName = "Unknown";
            } catch (Exception ignored) {}
            x = pixelmon.getX();
            y = pixelmon.getY();
            z = pixelmon.getZ();
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
    public ServerPlayer findNearestPlayer(SpawnEvent event, PixelmonEntity entity, double range) {
        if (entity == null || entity.level() == null) return null;
        return (ServerPlayer) entity.level().getNearestPlayer(entity, range);
    }

    @SubscribeEvent
    public void onSpawn(SpawnEvent event) {
        BroadcasterUtil.handleEvent(event, this);
    }
}