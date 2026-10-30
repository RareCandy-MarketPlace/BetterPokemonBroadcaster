package com.envyful.better.poke.broadcaster.api.type.impl;

import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.BroadcasterType;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.server.level.ServerPlayer;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public abstract class AbstractBroadcasterType<A extends Event> implements BroadcasterType {

    protected final String id;
    protected final Class<A> clazz;

    protected AbstractBroadcasterType(String id, Class<A> clazz, IEventBus... eventBus) {
        this.id = id;
        this.clazz = clazz;

        for (var bus : eventBus) {
            bus.register(this);
        }
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isCorrectEvent(Event e) {
        if (!this.clazz.isAssignableFrom(e.getClass())) {
            return false;
        }

        return this.isEvent((A) e);
    }

    protected abstract boolean isEvent(A a);

    @Override
    public PixelmonEntity getPixelmon(Event e) {
        if (!this.clazz.isInstance(e)) {
            return null;
        }

        @SuppressWarnings("unchecked")
        A event = (A) e;

        if (!this.isEvent(event)) {
            return null;
        }

        return this.getEntity(event);
    }

    @Override
    public Pokemon getPokemon(Event e) {
        PixelmonEntity entity = this.getPixelmon(e);
        return entity != null ? entity.getPokemon() : null;
    }

    protected abstract PixelmonEntity getEntity(A a);

    @SuppressWarnings("unchecked")
    public Placeholder asPlaceholder(Event e, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        return this.asEventPlaceholder((A) e, pixelmon, nearestPlayer);
    }

    protected abstract Placeholder asEventPlaceholder(A a, PixelmonEntity pixelmon, ServerPlayer nearestPlayer);

    @Override
    @SuppressWarnings("unchecked")
    public ServerPlayer getNearestPlayer(Event event, PixelmonEntity entity, double range) {
        return this.findNearestPlayer((A) event, entity, range);
    }

    protected abstract ServerPlayer findNearestPlayer(A a, PixelmonEntity entity, double range);
}
