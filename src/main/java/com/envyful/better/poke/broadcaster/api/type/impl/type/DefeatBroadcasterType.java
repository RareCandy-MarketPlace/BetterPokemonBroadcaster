package com.envyful.better.poke.broadcaster.api.type.impl.type;

import com.envyful.api.neoforge.world.UtilWorld;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.type.impl.AbstractBroadcasterType;
import com.envyful.better.poke.broadcaster.api.util.BroadcasterUtil;
import com.envyful.better.poke.broadcaster.api.util.PlaceholderCache;

import java.util.Map;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.server.level.ServerPlayer;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.util.helpers.BiomeHelper;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;

public class DefeatBroadcasterType extends AbstractBroadcasterType<BattleEndEvent> {

    public DefeatBroadcasterType() {
        super("defeat", BattleEndEvent.class, Pixelmon.EVENT_BUS);
    }

    private PixelmonEntity getDefeatedWild(BattleEndEvent event) {
        boolean playerWon = false;
        for (Map.Entry<BattleParticipant, BattleResults> entry :
                event.getResults().entrySet()) {
            if (entry.getKey() instanceof PlayerParticipant
                    && entry.getValue().name().contains("VICTORY")) {
                playerWon = true;
                break;
            }
        }
        if (!playerWon) return null;

        for (Map.Entry<BattleParticipant, BattleResults> entry :
                event.getResults().entrySet()) {
            BattleParticipant participant = entry.getKey();
            if (participant instanceof PlayerParticipant) continue;
            if (participant.getClass().getSimpleName().toLowerCase().contains("raid")) continue;

            String result = entry.getValue().name();
            if (result.contains("DEFEAT") || result.contains("FAINT")) {
                if (participant instanceof WildPixelmonParticipant wildParticipant) {
                    if (wildParticipant.getEntity() instanceof PixelmonEntity pixelmonEntity) {
                        return pixelmonEntity;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected boolean isEvent(BattleEndEvent event) {
        return getDefeatedWild(event) != null;
    }

    @Override
    protected PixelmonEntity getEntity(BattleEndEvent event) {
        return getDefeatedWild(event);
    }

    @Override
    public Pokemon getPokemon(Event e) {
        if (e instanceof BattleEndEvent event) {
            PixelmonEntity entity = getDefeatedWild(event);
            if (entity != null) return entity.getPokemon();
        }
        return null;
    }

    @Override
    protected Placeholder asEventPlaceholder(
            BattleEndEvent event, PixelmonEntity pixelmon, ServerPlayer nearestPlayer) {
        Pokemon pokemon = pixelmon != null ? pixelmon.getPokemon() : null;

        String playerName = "Unknown";
        if (nearestPlayer != null && nearestPlayer.getName() != null) {
            playerName = nearestPlayer.getName().getString();
        } else {
            for (BattleParticipant p : event.getResults().keySet()) {
                if (p instanceof PlayerParticipant playerParticipant) {
                    ServerPlayer sp = playerParticipant.getPlayer();
                    if (sp != null && sp.getName() != null) {
                        playerName = sp.getName().getString();
                        break;
                    }
                }
            }
        }
        if (playerName == null) playerName = "Unknown";

        String worldName = "Unknown";
        String biomeName = "Unknown";
        double x = 0, y = 0, z = 0;

        if (pixelmon != null && pixelmon.level() != null) {
            worldName = UtilWorld.getName(pixelmon.level());
            if (worldName == null) worldName = "Unknown";
            try {
                biomeName = BiomeHelper.getLocalizedBiomeName(pixelmon.level().getBiome(pixelmon.blockPosition()))
                        .getString();
                if (biomeName == null) biomeName = "Unknown";
            } catch (Exception ignored) {
            }
            x = pixelmon.getX();
            y = pixelmon.getY();
            z = pixelmon.getZ();
        }

        String pName = "Unknown";
        if (pokemon != null && pokemon.getSpecies() != null) {
            pName = pokemon.getSpecies().getName();
            if (pName == null) pName = "Unknown";
        }
        String pNameLower = pName.toLowerCase().replace(" ", "");

        final String fPlayerName = playerName;
        final String fWorldName = worldName;
        final String fBiomeName = biomeName;
        final String fPName = pName;
        final String fX = String.valueOf((int) x);
        final String fY = String.valueOf((int) y);
        final String fZ = String.valueOf((int) z);

        return Placeholder.composition(
                PlaceholderCache.get(pokemon),
                Placeholder.simple(line -> line.replace("%nearest_name%", fPlayerName)
                        .replace("%player%", fPlayerName)
                        .replace("%x%", fX)
                        .replace("%y%", fY)
                        .replace("%z%", fZ)
                        .replace("%world%", fWorldName)
                        .replace("%pokemon%", fPName)
                        .replace("%pokemon_lower%", pNameLower)
                        .replace("%biome%", fBiomeName)));
    }

    @Override
    public ServerPlayer findNearestPlayer(BattleEndEvent event, PixelmonEntity entity, double range) {
        for (BattleParticipant p : event.getResults().keySet()) {
            if (p instanceof PlayerParticipant playerParticipant) {
                return playerParticipant.getPlayer();
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {
        BroadcasterUtil.handleEvent(event, this);
    }
}
