package fae.weather.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.*;

public class KeepAlive {
    private static final Logger LOGGER = LogManager.getLogger(KeepAlive.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ExecutorService executor;
    private Future<?> keepAliveFuture;

    public KeepAlive() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    public boolean isAlive() {
        return Objects.nonNull(keepAliveFuture) && !keepAliveFuture.isDone();
    }

    public void start() {
        LOGGER.debug("Starting keep alive thread.");

        if (executor.isShutdown()) {
            final IllegalStateException exception = new IllegalStateException("Keep alive thread executor is already shut down.");
            LOGGER.error("Keep alive thread executor is already shut down.", exception);
            throw exception;
        }

        if (isAlive()) {
            LOGGER.warn("Keep alive thread is already running.");
            return;
        }

        keepAliveFuture = executor.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                LOGGER.error("Keep alive thread interrupted while waiting for latch.", e);
                Thread.currentThread().interrupt();
            }
        });
        LOGGER.debug("Keep alive thread started.");
    }

    public void stop() {
        LOGGER.debug("Shutting down keep alive thread.");
        latch.countDown();
        LOGGER.debug("Keep alive thread latch counted down.");

        LOGGER.debug("Shutting down keep alive thread executor.");
        keepAliveFuture.cancel(true);
        if (keepAliveFuture.isCancelled())
            LOGGER.debug("Keep alive thread future cancelled.");
        if (keepAliveFuture.isDone())
            LOGGER.debug("Keep alive thread future done.");
        LOGGER.debug("Keep alive thread shut down.");
    }
}