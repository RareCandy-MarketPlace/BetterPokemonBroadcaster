package com.envyful.better.poke.broadcaster.api.util;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.poke.broadcaster.BetterPokeBroadcaster;
import com.envyful.better.poke.broadcaster.api.type.BroadcasterType;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.neoforged.bus.api.Event;

public class BroadcasterUtil {

    public static void handleEvent(Event event, BroadcasterType broadcasterType) {
        UtilConcurrency.runAsync(() -> {
            for (var option : BetterPokeBroadcaster.getConfig().getOptions()) {
                if (!option.isEnabled()) continue;
                if (!option.getType().equalsIgnoreCase(broadcasterType.id())) continue;

                if (!broadcasterType.isCorrectEvent(event)) continue;

                var pixelmon = broadcasterType.getPixelmon(event);
                Pokemon pokemon = broadcasterType.getPokemon(event);

                if (pokemon == null && event instanceof CaptureEvent.SuccessfulCapture) {
                    pokemon = ((CaptureEvent.SuccessfulCapture) event).getPokemon();
                }

                if (pokemon == null) continue;

                boolean matches = (pixelmon != null) ? option.getSpec().matches(pixelmon) : option.getSpec().matches(pokemon);
                if (!matches) continue;

                if (pixelmon != null && pixelmon.isBossPokemon()) {
                    boolean isBossBroadcast = false;
                    for (String line : option.getBroadcasts()) {
                        if (line.toLowerCase().contains("boss")) {
                            isBossBroadcast = true;
                            break;
                        }
                    }
                    if (!isBossBroadcast) continue;
                }

                var nearestPlayer = broadcasterType.getNearestPlayer(event, pixelmon, option.getNearestPlayerRadius());
                if (nearestPlayer == null && option.isNearestPlayerOnly()) continue;

                var placeholder = broadcasterType.asPlaceholder(event, pixelmon, nearestPlayer);

                if (option.isNearestPlayerOnly()) {
                    PlatformProxy.sendMessage(nearestPlayer, option.getBroadcasts(), placeholder);
                } else {
                    PlatformProxy.broadcastMessage(option.getBroadcasts(), placeholder);
                }

                WebhookQueue.enqueue(option, placeholder);
            }
        });
    }
}