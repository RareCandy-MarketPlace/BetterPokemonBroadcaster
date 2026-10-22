package com.envyful.better.poke.broadcaster.config;

import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.envyful.api.discord.yaml.DiscordWebHookConfig;
import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.api.util.WebhookQueue;
import com.pixelmonmod.api.pokemon.PokemonSpecification;
import com.pixelmonmod.api.pokemon.PokemonSpecificationProxy;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class BroadcastOption extends AbstractYamlConfig {

    private boolean enabled = true;
    private String type;
    private PokemonSpecification pokemonSpec;
    private double nearestPlayerRadius;
    private List<String> broadcasts;
    private DiscordWebHookConfig webhook;
    private boolean nearestPlayerOnly;

    private BroadcastOption(Builder builder) {
        super();
        this.type = builder.type;
        this.pokemonSpec = builder.pokemonSpec;
        this.nearestPlayerRadius = builder.nearestPlayerRadius;
        this.broadcasts = builder.broadcasts;
        this.webhook = builder.webhook;
        this.nearestPlayerOnly = builder.nearestPlayerOnly;
    }

    public BroadcastOption() {
        super();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public String getType() {
        return this.type;
    }

    public double getNearestPlayerRadius() {
        return this.nearestPlayerRadius;
    }

    public PokemonSpecification getSpec() {
        return this.pokemonSpec;
    }

    public List<String> getBroadcasts() {
        return this.broadcasts;
    }

    public DiscordWebHookConfig getWebhook() {
        return this.webhook;
    }

    public boolean isNearestPlayerOnly() {
        return this.nearestPlayerOnly;
    }

    public void executeWebhook(Placeholder... placeholders) {
        WebhookQueue.enqueue(this, placeholders);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String type;
        private PokemonSpecification pokemonSpec;
        private double nearestPlayerRadius;
        private List<String> broadcasts;
        private DiscordWebHookConfig webhook;
        private boolean nearestPlayerOnly;

        private Builder() {}

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder spec(String spec) {
            this.pokemonSpec = PokemonSpecificationProxy.create(spec).get();
            return this;
        }

        public Builder nearestPlayerRadius(double nearestPlayerRadius) {
            this.nearestPlayerRadius = nearestPlayerRadius;
            return this;
        }

        public Builder broadcasts(String... broadcasts) {
            this.broadcasts = List.of(broadcasts);
            return this;
        }

        public Builder webhook(DiscordWebHookConfig webhook) {
            this.webhook = webhook;
            return this;
        }

        public Builder nearestPlayerOnly(boolean nearestPlayerOnly) {
            this.nearestPlayerOnly = nearestPlayerOnly;
            return this;
        }

        public BroadcastOption build() {
            return new BroadcastOption(this);
        }
    }
}