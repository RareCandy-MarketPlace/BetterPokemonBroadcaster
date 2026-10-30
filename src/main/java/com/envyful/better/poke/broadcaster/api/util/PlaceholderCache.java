package com.envyful.better.poke.broadcaster.api.util;

import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.BetterPokeBroadcaster;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class PlaceholderCache {

    private static final Cache<UUID, Placeholder> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    public static Placeholder get(Pokemon pokemon) {
        if (pokemon == null) {
            return Placeholder.simple(line -> line);
        }

        Placeholder placeholder = CACHE.getIfPresent(pokemon.getUUID());

        if (placeholder == null) {
            placeholder =
                    BetterPokeBroadcaster.getConfig().getPlaceholderFormat().getPokemonPlaceholders(pokemon);
            CACHE.put(pokemon.getUUID(), placeholder);
        }

        return placeholder;
    }

    public static void clear() {
        CACHE.invalidateAll();
    }
}
