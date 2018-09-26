package io.digitalstate.camunda.prometheus;

import io.prometheus.client.exporter.HTTPServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class MetricsExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsExporter.class);

    /**
     * Starts HTTPServer for Prometheus on the defined port.
     * @param port The Port to expose the HTTP Server that Prometheus will connect to.
     */
    MetricsExporter(int port) {
        LOGGER.info("Starting HTTP Server for Prometheus Metrics Exporting on Port: " + port);
        try{
            HTTPServer server = new HTTPServer(port);
        } catch(IOException e){
            LOGGER.error("Unable to load HTTP Server for Prometheus Plugin: " + e.getMessage());
        }

    }

    /**
     * Starts HTTPServer for Prometheus on port 9999.
     */
    MetricsExporter() {
        int port = 9999;
        LOGGER.info("Starting HTTP Server for Prometheus Metrics Exporting on port: " + port);
        try {
            HTTPServer server = new HTTPServer(port, true);
        } catch(IOException e){
            LOGGER.error("Unable to load HTTP Server for Prometheus Plugin: " + e.getMessage());
        }
    }
}
