package fae.weather.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A class to keep the application alive.
 * <p>
 *     This class is used to keep the application alive by starting a thread that will wait for a latch to be counted
 *     down. The latch is counted down when the application is stopped.
 *     <br>
 *     The keep alive thread is started when the application is started and stopped when the application is stopped.
 *     <br>
 *     The keep alive thread is started only once and stopped only once. However, the keep alive thread can be started
 *     after it has been stopped.
 *     <br>
 *     The keep alive thread is started by calling {@link #start()} and stopped by calling {@link #stop()}.
 *     <br>
 *     Pay attention to the fact that KeepAlive provides one ExecutorService per instance. This means that if you create
 *     multiple instances of KeepAlive, you will have multiple ExecutorServices. This is not a problem if you only have
 *     one instance of KeepAlive.
 *     <br>
 *     The keep alive thread is started in a new thread. This means that the thread that calls {@link #start()} will
 *     not be blocked.
 *     <br>
 *     The keep alive thread is stopped in the thread that calls {@link #stop()}. This means that the thread that calls
 *     {@link #stop()} will be blocked until the keep alive thread is stopped.
 *     <br>
 *     The ExecutorService is never shut down. This means that the keep alive thread can be started after it has been
 *     stopped. But it also means that the keep alive thread will never be stopped unless the application is stopped.
 *     We also have to be careful with resources that are used by the keep alive thread.
 */
public class KeepAlive {
    /**
     * The logger for this class is not static because due to the nature of this class, it is not possible to have a
     * total control over the order of initialization of the static members of this class and the static members of
     * other classes. This means that it is possible that the logger is initialized before the KeepAlive instance is
     * created. This means that the LoggerContext will be different from the one we would get when we request the logger
     * from LogManager. This means that the logger will not be properly configured. The logger will not work properly
     * and we will not log anything, neither be able to capture logs in our test appenders.
     */
    private final Logger logger = LogManager.getLogger();
    private final CountDownLatch latch = new CountDownLatch(1);

    //TODO - jundan
    // the executor created here is never shut down. We need to implement some kind of Lyfecycle interface
    // for our BeanManager to handle the executor shutdown for us.
    private final ExecutorService executor;
    private Future<?> keepAliveFuture;

    /**
     * Initializes a new KeepAlive instance with its own ExecutorService and CountdownLatch.
     */
    public KeepAlive() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Checks if the KeepAlive thread is currently running.
     *
     * @return {@code true} if the KeepAlive thread is running, {@code false} otherwise.
     */
    public boolean isAlive() {
        return Objects.nonNull(keepAliveFuture) && !keepAliveFuture.isDone();
    }

    /**
     * Starts the keep alive thread.
     */
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

    /**
     * Stops the KeepAlive thread, allowing the application to gracefully shut down.
     */
    public void stop() {
        logger.debug("Shutting down keep alive thread.");

        latch.countDown();
        logger.trace("Keep alive thread latch counted down.");

        logger.trace("Shutting down keep alive thread.");

        // for keeoAliveFuture to be null it must have been cancelled, done or the keepAlive was never started.
        if (keepAliveFuture == null) {
            logger.trace("Keep alive thread future is null.");
            logger.debug("Keep alive thread shut down.");
            return;
        }

        keepAliveFuture.cancel(true);
        if (keepAliveFuture.isCancelled())
            logger.trace("Keep alive thread future cancelled.");
        if (keepAliveFuture.isDone())
            logger.trace("Keep alive thread future done.");

        logger.trace("setting keepAliveFuture to null");
        keepAliveFuture = null;
        logger.debug("Keep alive thread shut down.");
    }
}