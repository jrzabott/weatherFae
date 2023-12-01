package fae.weather.httpserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JettyHttpServerTest {

    @Test
    void startJettyHttpServer() {
        JettyHttpServer jettyHttpServer = new JettyHttpServer();
        jettyHttpServer.start();
        assertTrue(jettyHttpServer.isRunning());
    }
}