package com.envyful.better.poke.broadcaster.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.config.yaml.DefaultConfig;
import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.discord.yaml.DiscordEmbedConfig;
import com.envyful.api.discord.yaml.DiscordWebHookConfig;
import com.envyful.api.reforged.pixelmon.config.SpriteConfig;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.util.List;

@ConfigSerializable
@ConfigPath("config/BetterPokeBroadcaster/config.yml")
public class BetterPokeBroadcasterConfig extends AbstractYamlConfig {

        private boolean debugMode = false;
        private transient List<BroadcastOption> broadcastOptions;
        private SpriteConfig placeholderFormat = new SpriteConfig();

        public BetterPokeBroadcasterConfig() throws IOException {
                super();

                this.broadcastOptions = YamlConfigFactory.getInstances(BroadcastOption.class, "config/BetterPokeBroadcaster/broadcasts",
                        DefaultConfig.onlyNew("examples/shiny_spawns.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("shiny !boss !legendary !ub")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &6&lSHINY SPAWN",
                                        "   &e%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &e%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 255, 0, 255))
                                                .description("A shiny %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/shiny_capture.yml", BroadcastOption.builder()
                                .type("capture")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("shiny !boss !legendary !ub")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &6&lSHINY CAPTURED",
                                        "   &e%pokemon% &7has been captured at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The player is &e%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 255, 0, 255))
                                                .description("A shiny %pokemon% has been captured at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/ub_spawns.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("ub")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &d&lULTRA BEAST SPAWN",
                                        "   &5%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(128, 0, 128, 255))
                                                .description("An Ultra Beast %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/ub_capture.yml", BroadcastOption.builder()
                                .type("capture")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("ub")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &d&lULTRA BEAST CAPTURED",
                                        "   &5%pokemon% &7has been captured at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(128, 0, 128, 255))
                                                .description("An Ultra Beast %pokemon% has been captured at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/legendary_spawns.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &5&lLEGENDARY SPAWN",
                                        "   &d%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 255, 0, 255))
                                                .description("A legendary %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/legendary_capture.yml", BroadcastOption.builder()
                                .type("capture")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &4&lLEGENDARY CAPTURED",
                                        "   &c%pokemon% &7has been captured at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 0, 0, 255))
                                                .description("A legendary %pokemon% has been captured at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/legendary_defeat.yml", BroadcastOption.builder()
                                .type("defeat")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &4&lLEGENDARY DEFEATED",
                                        "   &c%pokemon% &7has been defeated at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 0, 0, 255))
                                                .description("A legendary %pokemon% has been defeated at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/legendary_despawn.yml", BroadcastOption.builder()
                                .type("despawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &4&lLEGENDARY DESPAWNED",
                                        "   &c%pokemon% &7has despawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(139, 0, 0, 255))
                                                .description("A legendary %pokemon% has despawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/boss_common.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("boss:common")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &f&lCOMMON BOSS SPAWN",
                                        "   &f%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &f%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(200, 200, 200, 255))
                                                .description("A Common Boss %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/boss_uncommon.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("boss:uncommon")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &a&lUNCOMMON BOSS SPAWN",
                                        "   &a%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &a%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(0, 255, 0, 255))
                                                .description("An Uncommon Boss %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/boss_rare.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("boss:rare")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &b&lRARE BOSS SPAWN",
                                        "   &b%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &b%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(0, 0, 255, 255))
                                                .description("A Rare Boss %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/boss_epic.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("boss:epic")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &5&lEPIC BOSS SPAWN",
                                        "   &5%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &5%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(128, 0, 128, 255))
                                                .description("An Epic Boss %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/boss_legendary.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("boss:legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &6&lLEGENDARY BOSS SPAWN",
                                        "   &e%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &e%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 215, 0, 255))
                                                .description("A Legendary Boss %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/boss_ultimate.yml", BroadcastOption.builder()
                                .type("spawn")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("boss:ultimate")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &c&lULTIMATE BOSS SPAWN",
                                        "   &c%pokemon% &7has spawned at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &c%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 0, 0, 255))
                                                .description("An Ultimate Boss %pokemon% has spawned at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/max_raid_legendary.yml", BroadcastOption.builder()
                                .type("max_raid")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &5&lMAX RAID DEN",
                                        "   &d%pokemon% &7has appeared in a Max Raid at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 0, 255, 255))
                                                .description("A legendary %pokemon% has appeared in a Max Raid at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/max_raid_ultrabeast.yml", BroadcastOption.builder()
                                .type("max_raid")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("ub")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &d&lULTRA BEAST RAID",
                                        "   &d%pokemon% &7has appeared in a Max Raid at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &d%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(128, 0, 128, 255))
                                                .description("An Ultra Beast %pokemon% has appeared in a Max Raid at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/max_raid_mega.yml", BroadcastOption.builder()
                                .type("max_raid")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("form:mega")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &c&lMEGA EVOLUTION RAID",
                                        "   &c%pokemon% &7has appeared in a Max Raid at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &c%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 100, 100, 255))
                                                .description("A Mega %pokemon% has appeared in a Max Raid at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/tera_raid_legendary.yml", BroadcastOption.builder()
                                .type("tera_raid")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("legendary")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &b&lTERA RAID DEN",
                                        "   &b%pokemon% &7has appeared in a Tera Raid at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &b%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(0, 255, 255, 255))
                                                .description("A legendary %pokemon% has appeared in a Tera Raid at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/tera_raid_ultrabeast.yml", BroadcastOption.builder()
                                .type("tera_raid")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("ub")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &b&lTERA ULTRA BEAST RAID",
                                        "   &b%pokemon% &7has appeared in a Tera Raid at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &b%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(0, 255, 255, 255))
                                                .description("An Ultra Beast %pokemon% has appeared in a Tera Raid at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        ),
                        DefaultConfig.onlyNew("examples/tera_raid_mega.yml", BroadcastOption.builder()
                                .type("tera_raid")
                                .nearestPlayerOnly(false)
                                .nearestPlayerRadius(100)
                                .spec("form:mega")
                                .broadcasts(
                                        "&8&m---------------------------------",
                                        " ",
                                        "       &c&lMEGA TERA RAID",
                                        "   &c%pokemon% &7has appeared in a Tera Raid at &a%x%&7, &a%y%&7, &a%z%&7 in &f%biome%&7!",
                                        "   &7The nearest player is &c%nearest_name%&7",
                                        " ",
                                        "&8&m---------------------------------"
                                )
                                .webhook(DiscordWebHookConfig.builder()
                                        .disabled()
                                        .url("YOUR WEBHOOK URL HERE")
                                        .tts(false)
                                        .username("RareCandy BetterPokeBroadcaster")
                                        .embeds(DiscordEmbedConfig.builder()
                                                .color(new DiscordEmbedConfig.DiscordColor(255, 100, 100, 255))
                                                .description("A Mega %pokemon% has appeared in a Tera Raid at %x%, %y%, %z% in %biome%!")
                                                .fields(new DiscordEmbedConfig.Field("Nearest Player", "%nearest_name%", false),
                                                        new DiscordEmbedConfig.Field("World", "%world%", false),
                                                        new DiscordEmbedConfig.Field("Biome", "%biome%", false),
                                                        new DiscordEmbedConfig.Field("X", "%x%", false),
                                                        new DiscordEmbedConfig.Field("Y", "%y%", false),
                                                        new DiscordEmbedConfig.Field("Z", "%z%", false))
                                                .build())
                                        .build())
                                .build()
                        )
                );
        }

        public List<BroadcastOption> getOptions() {
                return this.broadcastOptions;
        }

        public SpriteConfig getPlaceholderFormat() {
                return this.placeholderFormat;
        }

        public boolean isDebugMode() {
                return this.debugMode;
        }
}