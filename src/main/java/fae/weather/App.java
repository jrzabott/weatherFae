package fae.weather;

import fae.weather.beanmanager.BeanManager;
import fae.weather.beanmanager.BeanManagerImpl;
import fae.weather.utils.KeepAlive;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * The main class of the application.
 */
public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);
    private static final String HELLO_WORLD = "Hello World!";

    private final KeepAlive keepAlive;
    private volatile boolean running = false;
    private final BeanManager beanManager;

    public App(BeanManager beanManager, KeepAlive keepAlive) {
        this.beanManager = beanManager;
        this.keepAlive = keepAlive;
    }

    public static void main(String[] args) {
        Configurator.setRootLevel(Level.ALL);
        KeepAlive keepAlive = new KeepAlive();
        BeanManager beanManager = new BeanManagerImpl();

        App app = new App(beanManager, keepAlive);
        beanManager.addBean(app);
        app.start();
        LOGGER.debug(HELLO_WORLD);
    }

    public boolean isRunning() {
        return running;
    }

    public BeanManager getBeanManager() {
        return beanManager;
    }

    void start() {
        LOGGER.debug("Starting the application.");

        //starting keep alive thread
        keepAlive.start();

        //End of initialization
        running = true;
        LOGGER.debug("Application started.");
    }

    public void stop() {
        LOGGER.debug("Stopping the application.");
        beanManager.invalidateAllBeans();

        //stopping keep alive thread
        keepAlive.stop();

        //End of destruction
        running = false;
        LOGGER.debug("Application stopped.");
    }
}
