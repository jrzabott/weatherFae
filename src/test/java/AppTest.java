import Util.InMemoryListAppender;
import fae.weather.App;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTest {
    private static final String LOG_MESSAGE = "Hello World!";
    private InMemoryListAppender inMemoryListAppender;
    private LoggerContext logContext;

    @BeforeEach
    void setUp() {
        Configurator.setRootLevel(Level.DEBUG);

        inMemoryListAppender = new InMemoryListAppender("InMemoryListAppender");
        inMemoryListAppender.start();

        logContext = (LoggerContext) LogManager.getContext(false);
        final Configuration logConfig = logContext.getConfiguration();
        final LoggerConfig rootLogger = logConfig.getRootLogger();
        rootLogger.addAppender(inMemoryListAppender, null, null);

        logContext.updateLoggers();
    }

    @AfterEach
    void tearDown() {
        logContext.stop();
    }

    @Test
    void shouldStartAndLogHelloWorld() {
        App.main(null);
        Assertions.assertThat(inMemoryListAppender.getLogEventsMessagesAsText()).contains(LOG_MESSAGE);
    }
}
