package fae.weather.httpserver;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JettyHttpServerTest {

    private static final String LOCALHOST = "http://localhost";
    private static final String PORT_DELIM = ":";
    private final static String HTTP_PORT = "10001";

    @Test
    void startJettyHttpServer() {
        JettyHttpServer jettyHttpServer = new JettyHttpServer();
        jettyHttpServer.start();
        assertTrue(jettyHttpServer.isRunning());
    }

    @Test
    void stopJettyHttpServer() {
        JettyHttpServer jettyHttpServer = new JettyHttpServer();
        jettyHttpServer.start();
        jettyHttpServer.stop();
        assertFalse(jettyHttpServer.isRunning());
    }

    @Test
    void startAndStopJettyHttpServer() {
        JettyHttpServer jettyHttpServer = new JettyHttpServer();
        jettyHttpServer.start();
        assertTrue(jettyHttpServer.isRunning());
        jettyHttpServer.stop();
        assertFalse(jettyHttpServer.isRunning());
    }

    @Test
    void startAndStopJettyHttpServerTwice() {
        JettyHttpServer jettyHttpServer = new JettyHttpServer();
        jettyHttpServer.start();
        assertTrue(jettyHttpServer.isRunning());
        jettyHttpServer.stop();
        assertFalse(jettyHttpServer.isRunning());
        jettyHttpServer.start();
        assertTrue(jettyHttpServer.isRunning());
        jettyHttpServer.stop();
        assertFalse(jettyHttpServer.isRunning());
    }

    @Test
    void startHttpServerWillStartListeningInPort() throws IOException {
        JettyHttpServer jettyHttpServer = new JettyHttpServer();
        jettyHttpServer.start();
        assertTrue(jettyHttpServer.isRunning());

        //check and assert that port 8080 is listening using java network methods
        final URI uri = URI.create(LOCALHOST + PORT_DELIM + HTTP_PORT);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");

        // Check that the response code indicates success (e.g., 200 OK; 204 No Content)
        int responseCode = connection.getResponseCode();
        assertTrue(responseCode == 200 || responseCode == 204);

        // Stop the server after the test
        jettyHttpServer.stop();

    }
}