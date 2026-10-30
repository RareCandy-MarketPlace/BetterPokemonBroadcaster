package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.better.poke.broadcaster.api.util.BroadcasterUtil;

import net.neoforged.bus.api.SubscribeEvent;

import com.pixelmonmod.pixelmon.api.events.raids.RandomizeRaidEvent;

public class MaxRaidBroadcasterType extends BaseRaidBroadcasterType {

    public MaxRaidBroadcasterType() {
        super("max_raid");
    }

    @Override
    protected boolean isEvent(RandomizeRaidEvent.SelectSpawnsEvent event) {
        if (event.getDen() == null || isTeraRaid(event)) {
            return false;
        }
        return resolveRaidPokemon(event) != null;
    }

    @SubscribeEvent
    public void onDenRandomize(RandomizeRaidEvent.SelectSpawnsEvent event) {
        UtilConcurrency.runLater(() -> BroadcasterUtil.handleEvent(event, this), 20L);
    }
}
