package fae.weather;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);
    private static final String HELLO_WORLD = "Hello World!";

    public static void main(String[] args) {
        LOGGER.debug(HELLO_WORLD);
    }
}
