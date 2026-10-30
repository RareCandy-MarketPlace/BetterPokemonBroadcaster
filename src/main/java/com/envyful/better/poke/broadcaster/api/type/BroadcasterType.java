package com.envyful.better.poke.broadcaster.api.type;

import com.envyful.api.text.Placeholder;

import net.neoforged.bus.api.Event;

import net.minecraft.server.level.ServerPlayer;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public interface BroadcasterType {

    String id();

    boolean isCorrectEvent(Event e);

    PixelmonEntity getPixelmon(Event e);

    Pokemon getPokemon(Event e);

    ServerPlayer getNearestPlayer(Event e, PixelmonEntity entity, double range);

    Placeholder asPlaceholder(Event e, PixelmonEntity pixelmon, ServerPlayer nearestPlayer);
}
