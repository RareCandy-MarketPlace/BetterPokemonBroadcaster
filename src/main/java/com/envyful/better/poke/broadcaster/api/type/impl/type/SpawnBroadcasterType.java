package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.BroadcasterUtil;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

public class SpawnBroadcasterType extends AbstractBroadcasterType<SpawnEvent> {

    public SpawnBroadcasterType() {
        super("spawn", SpawnEvent.class, Pixelmon.EVENT_BUS);
    }

    @Override
    protected boolean isEvent(SpawnEvent spawnEvent) {
        var entity = spawnEvent.action.getOrCreateEntity();

        if (!(entity instanceof PixelmonEntity pixelmon)) {
            return false;
        }

        if (pixelmon.getOwner() != null) {
            return false;
        }

        if (pixelmon.isRaidPokemon()) {
            return false;
        }

        return true;
    }

    @Override
    protected PixelmonEntity getEntity(SpawnEvent spawnEvent) {
        return (PixelmonEntity) spawnEvent.action.getOrCreateEntity();
    }

    @Override
    protected Placeholder asEventPlaceholder(SpawnEvent spawnEvent, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        String playerName = nearestPlayer == null ? "None" : nearestPlayer.getName().getString();

        return Placeholder.composition(
                PlaceholderCache.get(pixelmon.getPokemon()),
                Placeholder.simple(line -> line
                        .replace("%nearest_name%", playerName)
                        .replace("%player%", playerName)
                        .replace("%x%", String.valueOf(pixelmon.getBlockX()))
                        .replace("%y%", String.valueOf(pixelmon.getBlockY()))
                        .replace("%z%", String.valueOf(pixelmon.getBlockZ()))
                        .replace("%world%", UtilWorld.getName(pixelmon.level()))
                        .replace("%pokemon%", pixelmon.getPokemonName())
                        .replace("%pokemon_lower%", pixelmon.getPokemonName().toLowerCase().replace(" ", ""))
                        .replace("%biome%", BiomeHelper.getLocalizedBiomeName(pixelmon.level().getBiome(pixelmon.blockPosition())).getString())
                ));
    }

    @Override
    public ServerPlayer findNearestPlayer(SpawnEvent event, PixelmonEntity entity, double range) {
        return (ServerPlayer) entity.level().getNearestPlayer(entity, range);
    }

    @SubscribeEvent
    public void onSpawn(SpawnEvent event) {
        BroadcasterUtil.handleEvent(event);
    }
}