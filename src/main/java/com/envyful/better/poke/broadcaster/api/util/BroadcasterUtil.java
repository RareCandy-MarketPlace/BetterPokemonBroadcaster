package com.envyful.better.poke.broadcaster.api.util;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.platform.PlatformProxy;
import com.envyful.better.poke.broadcaster.BetterPokeBroadcaster;
import com.envyful.better.poke.broadcaster.api.type.BroadcasterType;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public class BroadcasterUtil {

    public static final Set<UUID> TRACKED_UUIDS = ConcurrentHashMap.newKeySet();

    private static final Set<UUID> RECENTLY_CAPTURED = ConcurrentHashMap.newKeySet();

    private static final long DESPAWN_DELAY_MS = 10_000L;

    private static final long CAPTURE_MEMORY_SECONDS = 60L;

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "BetterPokeBroadcaster-Delay");
        t.setDaemon(true);
        return t;
    });

    public static void handleEvent(Event event, BroadcasterType broadcasterType) {
        if (event instanceof CaptureEvent.SuccessfulCapture) {
            Pokemon captured = ((CaptureEvent.SuccessfulCapture) event).getPokemon();
            if (captured != null) {
                UUID capturedId = captured.getUUID();
                RECENTLY_CAPTURED.add(capturedId);
                SCHEDULER.schedule(
                        () -> RECENTLY_CAPTURED.remove(capturedId), CAPTURE_MEMORY_SECONDS, TimeUnit.SECONDS);
            }
        }

        if (event instanceof EntityLeaveLevelEvent) {
            var entity = ((EntityLeaveLevelEvent) event).getEntity();
            if (!(entity instanceof PixelmonEntity)) {
                return;
            }

            Pokemon leaving = ((PixelmonEntity) entity).getPokemon();
            if (leaving == null || !TRACKED_UUIDS.contains(leaving.getUUID())) {
                return;
            }

            SCHEDULER.schedule(() -> process(event, broadcasterType), DESPAWN_DELAY_MS, TimeUnit.MILLISECONDS);
            return;
        }

        UtilConcurrency.runAsync(() -> process(event, broadcasterType));
    }

    private static void process(Event event, BroadcasterType broadcasterType) {
        boolean debug = BetterPokeBroadcaster.getConfig().isDebugMode();

        if (debug) {
            BetterPokeBroadcaster.getLogger()
                    .info("[BetterPokeBroadcaster Debug] Processing event: "
                            + event.getClass().getSimpleName() + " for broadcaster type: " + broadcasterType.id());
        }

        var pixelmon = broadcasterType.getPixelmon(event);
        Pokemon pokemon = pixelmon != null ? pixelmon.getPokemon() : broadcasterType.getPokemon(event);

        if (pokemon == null && event instanceof CaptureEvent.SuccessfulCapture) {
            pokemon = ((CaptureEvent.SuccessfulCapture) event).getPokemon();
        }

        if (pokemon == null || pokemon.getForm() == null || pokemon.getSpecies() == null) {
            if (debug)
                BetterPokeBroadcaster.getLogger().info("[BetterPokeBroadcaster Debug] Skipped: Pokemon Data is null.");
            return;
        }

        UUID trackingUUID = pokemon.getUUID();
        boolean isSpawn = event instanceof SpawnEvent;
        boolean isCapture = event instanceof CaptureEvent.SuccessfulCapture;
        boolean isDespawnOrDefeat = event instanceof BattleEndEvent || event instanceof EntityLeaveLevelEvent;

        if (isDespawnOrDefeat) {
            if (!TRACKED_UUIDS.contains(trackingUUID)) {
                if (debug)
                    BetterPokeBroadcaster.getLogger()
                            .info("[BetterPokeBroadcaster Debug] Skipped: " + trackingUUID
                                    + " was not tracked as a wild spawn.");
                return;
            }
            if (RECENTLY_CAPTURED.contains(trackingUUID)) {
                if (debug)
                    BetterPokeBroadcaster.getLogger()
                            .info("[BetterPokeBroadcaster Debug] Skipped: " + trackingUUID
                                    + " was captured, ignoring despawn/defeat.");
                return;
            }
        }

        boolean wasBroadcasted = false;

        for (var option : BetterPokeBroadcaster.getConfig().getOptions()) {
            if (!option.isEnabled() || option.getSpec() == null) continue;
            if (!option.getType().equalsIgnoreCase(broadcasterType.id())) continue;
            if (!broadcasterType.isCorrectEvent(event)) continue;

            boolean matches = false;
            try {
                matches = (pixelmon != null)
                        ? option.getSpec().matches(pixelmon)
                        : option.getSpec().matches(pokemon);
            } catch (Exception e) {
                if (debug)
                    BetterPokeBroadcaster.getLogger()
                            .error("[BetterPokeBroadcaster Debug] Match Error: " + e.getMessage());
                continue;
            }

            if (!matches) continue;

            if (debug) {
                BetterPokeBroadcaster.getLogger()
                        .info("[BetterPokeBroadcaster Debug] "
                                + pokemon.getSpecies().getName() + " matched option: " + option.getType());
            }

            var nearestPlayer = broadcasterType.getNearestPlayer(event, pixelmon, option.getNearestPlayerRadius());
            if (nearestPlayer == null && option.isNearestPlayerOnly()) {
                continue;
            }

            var placeholder = broadcasterType.asPlaceholder(event, pixelmon, nearestPlayer);

            if (option.isNearestPlayerOnly()) {
                PlatformProxy.sendMessage(nearestPlayer, option.getBroadcasts(), placeholder);
            } else {
                PlatformProxy.broadcastMessage(option.getBroadcasts(), placeholder);
            }

            WebhookQueue.enqueue(option, placeholder);
            wasBroadcasted = true;
        }

        if (isSpawn && wasBroadcasted) {
            TRACKED_UUIDS.add(trackingUUID);
            if (debug)
                BetterPokeBroadcaster.getLogger().info("[BetterPokeBroadcaster Debug] Tracker added: " + trackingUUID);
        } else if (isCapture) {
            TRACKED_UUIDS.remove(trackingUUID);
            if (debug)
                BetterPokeBroadcaster.getLogger()
                        .info("[BetterPokeBroadcaster Debug] Tracker removed: " + trackingUUID);
        } else if (isDespawnOrDefeat && wasBroadcasted) {
            String typeId = broadcasterType.id().toLowerCase();
            if (typeId.equals("defeat") || typeId.equals("despawn")) {
                TRACKED_UUIDS.remove(trackingUUID);
                if (debug)
                    BetterPokeBroadcaster.getLogger()
                            .info("[BetterPokeBroadcaster Debug] Tracker removed: " + trackingUUID);
            }
        }
    }
}
