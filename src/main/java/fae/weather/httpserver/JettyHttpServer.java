package fae.weather.httpserver;

/**
 * A implementation of HttpServer using Jetty.
 */
public class JettyHttpServer implements HttpServer {

    private static final String NOT_IMPLEMENTED_YET = "Not implemented yet.";
    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void start() {
        // at the end isRunning should be true
        isRunning = true;
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
