package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;

import net.neoforged.bus.api.Event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.raids.RandomizeRaidEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.entities.DenEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public abstract class BaseRaidBroadcasterType extends AbstractBroadcasterType<RandomizeRaidEvent.SelectSpawnsEvent> {

    protected BaseRaidBroadcasterType(String id) {
        super(id, RandomizeRaidEvent.SelectSpawnsEvent.class, Pixelmon.EVENT_BUS);
    }

    protected Pokemon resolveRaidPokemon(RandomizeRaidEvent.SelectSpawnsEvent event) {
        try {
            Entity entity = (Entity) event.getDen();
            if (entity instanceof DenEntity denEntity) {
                var serverData = denEntity.getServerData();
                if (serverData != null) {
                    return serverData.getPokemon();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    protected boolean isTeraRaid(RandomizeRaidEvent.SelectSpawnsEvent event) {
        try {
            Entity entity = (Entity) event.getDen();
            if (entity instanceof DenEntity denEntity) {
                var raidType = denEntity.getRaidType();
                if (raidType != null) {
                    String typeStr = raidType.unwrapKey()
                            .map(key -> key.location().toString())
                            .orElse(raidType.toString())
                            .toLowerCase();
                    return typeStr.contains("tera") || typeStr.contains("paldea");
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    protected PixelmonEntity getEntity(RandomizeRaidEvent.SelectSpawnsEvent event) {
        return null;
    }

    @Override
    public Pokemon getPokemon(Event e) {
        if (e instanceof RandomizeRaidEvent.SelectSpawnsEvent event) {
            return resolveRaidPokemon(event);
        }
        return null;
    }

    @Override
    protected Placeholder asEventPlaceholder(
            RandomizeRaidEvent.SelectSpawnsEvent event, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        var den = event.getDen();
        var pokemon = resolveRaidPokemon(event);

        String playerName = "None";
        if (nearestPlayer != null && nearestPlayer.getName() != null) {
            playerName = nearestPlayer.getName().getString();
            if (playerName == null) playerName = "None";
        }

        String worldName = "Unknown";
        if (den.level() != null) {
            worldName = UtilWorld.getName(den.level());
            if (worldName == null) worldName = "Unknown";
        }

        String biomeName = "Unknown";
        if (den.level() != null) {
            try {
                biomeName = BiomeHelper.getLocalizedBiomeName(den.level().getBiome(den.blockPosition()))
                        .getString();
                if (biomeName == null) biomeName = "Unknown";
            } catch (Exception ignored) {
            }
        }

        String pName = "Unknown";
        if (pokemon != null && pokemon.getSpecies() != null) {
            pName = pokemon.getSpecies().getName();
            if (pName == null) pName = "Unknown";
        }

        String pNameLower = pName.toLowerCase().replace(" ", "");

        final String finalPlayerName = playerName;
        final String finalWorldName = worldName;
        final String finalBiomeName = biomeName;
        final String finalPName = pName;

        return Placeholder.composition(
                PlaceholderCache.get(pokemon),
                Placeholder.simple(line -> line.replace("%nearest_name%", finalPlayerName)
                        .replace("%player%", finalPlayerName)
                        .replace("%x%", String.valueOf(den.getBlockX()))
                        .replace("%y%", String.valueOf(den.getBlockY()))
                        .replace("%z%", String.valueOf(den.getBlockZ()))
                        .replace("%world%", finalWorldName)
                        .replace("%pokemon%", finalPName)
                        .replace("%pokemon_lower%", pNameLower)
                        .replace("%biome%", finalBiomeName)));
    }

    @Override
    public ServerPlayer findNearestPlayer(
            RandomizeRaidEvent.SelectSpawnsEvent event, PixelmonEntity entity, double range) {
        var den = event.getDen();
        return (ServerPlayer) den.level().getNearestPlayer(den, range);
    }
}
