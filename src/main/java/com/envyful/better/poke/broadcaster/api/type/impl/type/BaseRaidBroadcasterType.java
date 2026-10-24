package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.raids.RandomizeRaidEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

import java.lang.reflect.Field;
import java.util.Optional;

public abstract class BaseRaidBroadcasterType extends AbstractBroadcasterType<RandomizeRaidEvent.SelectSpawnsEvent> {

    protected BaseRaidBroadcasterType(String id) {
        super(id, RandomizeRaidEvent.SelectSpawnsEvent.class, Pixelmon.EVENT_BUS);
    }

    protected Pokemon resolveRaidPokemon(RandomizeRaidEvent.SelectSpawnsEvent event) {
        try {
            net.minecraft.world.entity.Entity den = (net.minecraft.world.entity.Entity) event.getDen();
            if (den != null) {
                Class<?> clazz = den.getClass();
                while (clazz != Object.class && clazz != null) {
                    for (Field field : clazz.getDeclaredFields()) {
                        field.setAccessible(true);
                        Object val = field.get(den);
                        if (val instanceof Pokemon) {
                            return (Pokemon) val;
                        }
                        if (val instanceof Optional) {
                            Optional<?> opt = (Optional<?>) val;
                            if (opt.isPresent() && opt.get() instanceof Pokemon) {
                                return (Pokemon) opt.get();
                            }
                        }
                        if (val != null && (val.getClass().getName().toLowerCase().contains("raid") || val.getClass().getName().toLowerCase().contains("den"))) {
                            for (Field inner : val.getClass().getDeclaredFields()) {
                                inner.setAccessible(true);
                                Object innerVal = inner.get(val);
                                if (innerVal instanceof Pokemon) {
                                    return (Pokemon) innerVal;
                                }
                                if (innerVal instanceof Optional) {
                                    Optional<?> opt = (Optional<?>) innerVal;
                                    if (opt.isPresent() && opt.get() instanceof Pokemon) {
                                        return (Pokemon) opt.get();
                                    }
                                }
                            }
                        }
                    }
                    clazz = clazz.getSuperclass();
                }

                net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
                den.saveWithoutId(tag);

                net.minecraft.nbt.CompoundTag pokemonTag = null;
                if (tag.contains("pokemon")) {
                    pokemonTag = tag.getCompound("pokemon");
                } else if (tag.contains("RaidData") && tag.getCompound("RaidData").contains("pokemon")) {
                    pokemonTag = tag.getCompound("RaidData").getCompound("pokemon");
                } else if (tag.contains("raid_data") && tag.getCompound("raid_data").contains("pokemon")) {
                    pokemonTag = tag.getCompound("raid_data").getCompound("pokemon");
                }

                if (pokemonTag != null) {
                    String speciesName = "";
                    if (pokemonTag.contains("Species")) speciesName = pokemonTag.getString("Species");
                    else if (pokemonTag.contains("species")) speciesName = pokemonTag.getString("species");

                    if (!speciesName.isEmpty()) {
                        StringBuilder spec = new StringBuilder(speciesName);
                        if (pokemonTag.contains("Shiny") && pokemonTag.getBoolean("Shiny")) spec.append(" shiny");
                        else if (pokemonTag.contains("shiny") && pokemonTag.getBoolean("shiny")) spec.append(" shiny");
                        if (pokemonTag.contains("Form")) spec.append(" form:").append(pokemonTag.getInt("Form"));
                        else if (pokemonTag.contains("form")) spec.append(" form:").append(pokemonTag.getInt("form"));

                        var specOpt = com.pixelmonmod.api.pokemon.PokemonSpecificationProxy.create(spec.toString());
                        if (specOpt.wasSuccess()) {
                            return specOpt.get().create();
                        }
                    }
                }
            }
        } catch (Exception ignored) {}

        return null;
    }

    protected boolean isTeraRaid(RandomizeRaidEvent.SelectSpawnsEvent event) {
        try {
            net.minecraft.world.entity.Entity den = (net.minecraft.world.entity.Entity) event.getDen();
            if (den == null) return false;

            net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
            den.saveWithoutId(tag);

            if (tag.contains("RaidType")) {
                String raidType = tag.getString("RaidType").toLowerCase();
                return raidType.contains("tera");
            }

        } catch (Exception ignored) {}

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
    protected Placeholder asEventPlaceholder(RandomizeRaidEvent.SelectSpawnsEvent event, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
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
    public ServerPlayer findNearestPlayer(RandomizeRaidEvent.SelectSpawnsEvent event, PixelmonEntity entity, double range) {
        var den = event.getDen();
        return (ServerPlayer) den.level().getNearestPlayer(den, range);
    }
}