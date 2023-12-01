package fae.weather;

import fae.weather.beanmanager.BeanManager;
import fae.weather.beanmanager.BeanManagerImpl;
import fae.weather.utils.KeepAlive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private App app;

    @BeforeEach
    void setUp() {
        final BeanManagerImpl beanManager = new BeanManagerImpl();
        final KeepAlive keepAlive = new KeepAlive();
        this.app = new App(beanManager, keepAlive);
        app.start();
    }

    @Test
    void getBeanManagerBeforeStart() {
        App anotherApp = new App(Mockito.mock(BeanManager.class), Mockito.mock(KeepAlive.class));
        assertNotNull(anotherApp.getBeanManager());
        assertInstanceOf(BeanManager.class, anotherApp.getBeanManager());
    }

    @Test
    void appCreationHasBeanManager() {
        assertNotNull(app.getBeanManager());
        assertInstanceOf(BeanManager.class, app.getBeanManager());
    }

    @Test
    void appCreationHasEmptyBeanManager() {
        assertEquals(0, app.getBeanManager().getAllBeans().length);
    }

    @Test
    void appAfterStartedIsRunning() {
        assertTrue(app.isRunning());
    }

    @Test
    void appAfterStartedWillRunUntilStopped() {
        assertTrue(app.isRunning());
        app.stop();
        assertFalse(app.isRunning());
    }

    @Test
    void appIsStillRunningAfterThreeSeconds() {
        await().atLeast(3, TimeUnit.SECONDS);
        assertTrue(app.isRunning());
    }
}