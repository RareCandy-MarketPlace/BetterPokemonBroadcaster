package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.BroadcasterUtil;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.raids.RandomizeRaidEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;

import java.lang.reflect.Method;

public class DenSpawnBroadcasterType extends AbstractBroadcasterType<RandomizeRaidEvent> {

    public DenSpawnBroadcasterType() {
        super("den_spawn", RandomizeRaidEvent.class, Pixelmon.EVENT_BUS);
    }

    private Pokemon resolveRaidPokemon(RandomizeRaidEvent event) {
        try {
            Method getRaidMethod = event.getClass().getMethod("getRaid");
            Object raidData = getRaidMethod.invoke(event);
            if (raidData != null) {
                Method getPokemonMethod = raidData.getClass().getMethod("getPokemon");
                return (Pokemon) getPokemonMethod.invoke(raidData);
            }
        } catch (Exception ignored) {}

        try {
            Method getPokemonMethod = event.getDen().getClass().getMethod("getPokemon");
            return (Pokemon) getPokemonMethod.invoke(event.getDen());
        } catch (Exception ignored) {}

        try {
            Method getSpeciesMethod = event.getClass().getMethod("getSpecies");
            Object species = getSpeciesMethod.invoke(event);
            if (species != null) {
                Method getDefaultFormMethod = species.getClass().getMethod("getDefaultForm");
                Object form = getDefaultFormMethod.invoke(species);
                Method getPokemonMethod = form.getClass().getMethod("getPokemon");
                return (Pokemon) getPokemonMethod.invoke(form);
            }
        } catch (Exception ignored) {}

        return null;
    }

    @Override
    protected boolean isEvent(RandomizeRaidEvent event) {
        if (event.getDen() == null) {
            return false;
        }
        return resolveRaidPokemon(event) != null;
    }

    @Override
    protected PixelmonEntity getEntity(RandomizeRaidEvent event) {
        return null;
    }

    @Override
    public Pokemon getPokemon(Event e) {
        if (e instanceof RandomizeRaidEvent event) {
            return resolveRaidPokemon(event);
        }
        return null;
    }

    @Override
    protected Placeholder asEventPlaceholder(RandomizeRaidEvent event, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        var den = event.getDen();
        var pokemon = resolveRaidPokemon(event);
        String playerName = nearestPlayer == null ? "None" : nearestPlayer.getName().getString();

        return Placeholder.composition(
                PlaceholderCache.get(pokemon),
                Placeholder.simple(line -> line
                        .replace("%nearest_name%", playerName)
                        .replace("%player%", playerName)
                        .replace("%x%", String.valueOf(den.getBlockX()))
                        .replace("%y%", String.valueOf(den.getBlockY()))
                        .replace("%z%", String.valueOf(den.getBlockZ()))
                        .replace("%world%", UtilWorld.getName(den.level()))
                        .replace("%pokemon%", pokemon != null ? pokemon.getSpecies().getName() : "Unknown")
                        .replace("%pokemon_lower%", pokemon != null ? pokemon.getSpecies().getName().toLowerCase().replace(" ", "") : "unknown")
                        .replace("%biome%", BiomeHelper.getLocalizedBiomeName(den.level().getBiome(den.blockPosition())).getString())
                ));
    }

    @Override
    public ServerPlayer findNearestPlayer(RandomizeRaidEvent event, PixelmonEntity entity, double range) {
        var den = event.getDen();
        return (ServerPlayer) den.level().getNearestPlayer(den, range);
    }

    @SubscribeEvent
    public void onDenRandomize(RandomizeRaidEvent event) {
        UtilConcurrency.runLater(() -> BroadcasterUtil.handleEvent(event), 10L);
    }
}