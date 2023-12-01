package fae.weather.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KeepAlive {
    private final Logger logger = LogManager.getLogger();
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
        logger.debug("Starting keep alive thread.");

        if (executor.isShutdown()) {
            final IllegalStateException exception = new IllegalStateException("Keep alive thread executor is already shut down.");
            logger.error("Keep alive thread executor is already shut down.", exception);
            throw exception;
        }

        if (isAlive()) {
            logger.warn("Keep alive thread is already running.");
            return;
        }

        keepAliveFuture = executor.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.error("Keep alive thread interrupted while waiting for latch.", e);
                Thread.currentThread().interrupt();
            }
        });
        logger.debug("Keep alive thread started.");
    }

    public void stop() {
        logger.debug("Shutting down keep alive thread.");
        latch.countDown();
        logger.debug("Keep alive thread latch counted down.");

        logger.debug("Shutting down keep alive thread executor.");
        keepAliveFuture.cancel(true);
        if (keepAliveFuture.isCancelled())
            logger.debug("Keep alive thread future cancelled.");
        if (keepAliveFuture.isDone())
            logger.debug("Keep alive thread future done.");
        logger.debug("Keep alive thread shut down.");
    }
}