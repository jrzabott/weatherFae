package fae.weather.httpserver;

/**
 * A contract for a HTTP server.
 * We will start using Jetty for this project, but we should be able to switch to others as required.
 * The basic actions we will need, it to gracefully start, stop and add/remove endpoints without the need to restart the
 * server.
 */
public interface HttpServer {
        /**
        * Start the server.
         */
        void start() throws HttpServerException;
    
        /**
        * Stop the server.
        */
        void stop() throws HttpServerException;
    
        /**
        * Add an endpoint to the server.
        * 
        * @param endpoint - the endpoint to add
        */
        void addEndpoint(Endpoint endpoint) throws HttpServerException;
    
        /**
        * Remove an endpoint from the server.
        * 
        * @param endpoint - the endpoint to remove
        */
        void removeEndpoint(Endpoint endpoint) throws HttpServerException;


        class HttpServerException extends Exception {
        }
}
