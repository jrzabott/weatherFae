package fae.weather.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.InMemoryListAppender;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeepAliveTest {

    private static final InMemoryListAppender IN_MEMORY_LIST_APPENDER = new InMemoryListAppender("InMemoryListAppender");
    private static LoggerContext logContext;
    private KeepAlive keepAlive;

    @BeforeEach
    void setUp() {
        keepAlive = new KeepAlive();
    }

    @Test
    void keepAliveThreadIsAliveAfterStart() {
        keepAlive.start();
        await().atLeast(2, TimeUnit.SECONDS);
        assertTrue(keepAlive.isAlive());
    }

    @Test
    void keepAliveThreadIsAliveAfterStartAndShutdown() {
        keepAlive.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
    }

    @Test
    void keepAliveIsStartedTwiceItIsStillAliveLogsWarning() {
        startLogAppender();

        // Starting keep alive thread for the first time
        keepAlive.start();
        await().atLeast(1, TimeUnit.SECONDS);

        // Starting keep alive thread for the second time
        keepAlive.start();
        await().atLeast(1, TimeUnit.SECONDS);

        assertTrue(keepAlive.isAlive());
        Assertions.assertThat(IN_MEMORY_LIST_APPENDER.getLogEventsMessagesAsText()).contains("Keep alive thread is already running.");

        stopLogAppender();
    }

    private void stopLogAppender() {
        logContext.getConfiguration().getRootLogger().removeAppender("InMemoryListAppender");
        logContext.getConfiguration().getRootLogger().setAdditive(false);
        Configurator.setRootLevel(Level.DEBUG);
    }

    private void startLogAppender() {
        // Configuring log4j2
        Configurator.setRootLevel(Level.ALL);

        // Adding appender to root logger programmatically
        logContext = (LoggerContext) LogManager.getContext(false);
        final Configuration logConfiguration = logContext.getConfiguration();

        // The importance of this is that we can add the appender to the root logger and it will be inherited by all loggers
        logConfiguration.getRootLogger().setAdditive(true);

        // now that we know all loggers will inherit the appender, add it to the root logger
        logConfiguration.getRootLogger().addAppender(IN_MEMORY_LIST_APPENDER, null, null);
        logConfiguration.addAppender(IN_MEMORY_LIST_APPENDER);
        IN_MEMORY_LIST_APPENDER.start();

        // without this, log events will not be written to the new added appender
        logContext.updateLoggers();
    }

    @Test
    void keepAliveStopsAfterStop() {
        keepAlive.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
    }

    @Test
    void keepAliveCanRunMultipleTimes() {
        keepAlive.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
        keepAlive.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
    }

    @Test
    void keepAliveCanRunMultipleTimesWithDifferentThreads() {
        KeepAlive keepAlive1 = new KeepAlive();
        KeepAlive keepAlive2 = new KeepAlive();
        keepAlive1.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive1.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive1.isAlive());
        keepAlive2.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive2.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive2.isAlive());
    }

    @Test
    void keepAliveStopBeforeStart() {
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
    }

    @Test
    void setKeepAliveRestartedWillRemainAliveAfter3Seconds() {
        keepAlive.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
        keepAlive.start();
        await().atLeast(3, TimeUnit.SECONDS);
        keepAlive.stop();
        await().atLeast(1, TimeUnit.SECONDS);
        assertFalse(keepAlive.isAlive());
    }
}
