package com.envyful.better.poke.broadcaster.api.util;

import com.envyful.api.text.Placeholder;
import com.envyful.better.poke.broadcaster.BetterPokeBroadcaster;
import com.envyful.better.poke.broadcaster.config.BroadcastOption;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class WebhookQueue {

    private static final LinkedBlockingQueue<WebhookTask> QUEUE = new LinkedBlockingQueue<>();

    static {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    WebhookTask task = QUEUE.take();
                    task.option().getWebhook().execute(task.placeholders());
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (IOException e) {
                    BetterPokeBroadcaster.getLogger().error("Failed to execute webhook", e);
                }
            }
        });
        worker.setDaemon(true);
        worker.setName("BetterPokeBroadcaster-Webhook-Queue");
        worker.start();
    }

    public static void enqueue(BroadcastOption option, Placeholder... placeholders) {
        if (option.getWebhook() != null) {
            QUEUE.offer(new WebhookTask(option, placeholders));
        }
    }

    private record WebhookTask(BroadcastOption option, Placeholder[] placeholders) {}
}
