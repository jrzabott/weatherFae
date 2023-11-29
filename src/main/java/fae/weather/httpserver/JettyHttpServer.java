package fae.weather.httpserver;

/**
 * A implementation of HttpServer using Jetty.
 */
public class JettyHttpServer implements HttpServer {

    private static final String NOT_IMPLEMENTED_YET = "Not implemented yet.";

    @Override
    public void start() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED_YET);
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
