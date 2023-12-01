package fae.weather.httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * A implementation of HttpServer using Jetty.
 */
public class JettyHttpServer implements HttpServer {

    private static final String NOT_IMPLEMENTED_YET = "Not implemented yet.";
    private static final Logger LOGGER = LogManager.getLogger(JettyHttpServer.class);
    private boolean isRunning;
    private Server server;

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void start() {

        // Create and configure a ThreadPool.
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("server");

        // Create a Server instance.
        server = new Server(threadPool);

        // Create a ServerConnector to accept connections from clients.
        Connector connector = new ServerConnector(server);
        ((ServerConnector) connector).setReusePort(true);
        ((ServerConnector) connector).setPort(10001);

        // Add the Connector to the Server
        server.addConnector(connector);

        // Set a simple Handler to handle requests/responses.
        server.setHandler(new Handler.Abstract() {
            @Override
            public boolean handle(Request request, Response response, Callback callback) {
                // Succeed the callback to signal that the
                // request/response processing is complete.
                callback.succeeded();
                return true;
            }
        });

        // Start the Server to start accepting connections from clients.
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // at the end isRunning should be true
        isRunning = server.isStarted();
    }

    @Override
    public void stop() {
        // at the end isRunning should be false
        isRunning = false;
    }

    @Override
    public void addEndpoint(Endpoint endpoint) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public void removeEndpoint(Endpoint endpoint) {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }
}
