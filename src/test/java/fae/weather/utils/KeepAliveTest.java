package fae.weather.utils;

import Util.InMemoryListAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeepAliveTest {

    private static final InMemoryListAppender LOG_APPENDER = new InMemoryListAppender("InMemoryListAppender");
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
        Assertions.assertThat(LOG_APPENDER.getLogEventsMessagesAsText()).contains("Keep alive thread is already running.");

        stopLogAppender();
    }

    private void stopLogAppender() {
        logContext.getConfiguration().getRootLogger().removeAppender("InMemoryListAppender");
        logContext.getConfiguration().getRootLogger().setAdditive(false);
    }

    private void startLogAppender() {
        // Configuring log4j2
        Configurator.setRootLevel(Level.ALL);
        LOG_APPENDER.start();

        // Adding appender to root logger programmatically
        logContext = (LoggerContext) LogManager.getContext( false);
        final Configuration logConfiguration = logContext.getConfiguration();

        // The importance of this is that we can add the appender to the root logger and it will be inherited by all loggers
        logConfiguration.getRootLogger().setAdditive(true);

        // now that we know all loggers will inherit the appender, add it to the root logger
        logConfiguration.getRootLogger().addAppender(LOG_APPENDER, null, null);

        // without this, log events will not be written to the new added appender
        logContext.updateLoggers();
    }
}
