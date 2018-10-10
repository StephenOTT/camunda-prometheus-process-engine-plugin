package io.digitalstate.camunda.prometheus.config.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CustomMetricsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMetricsConfig.class);

    private Path collector;
    private Boolean enable;
    private long startDelay;
    private long frequency;
    private Map<String, Object> config;

    public void setCollector(String filePath) {
        try {
            this.collector = Paths.get(getClass().getResource(filePath).toURI());
        } catch (URISyntaxException e) {
            LOGGER.error("Cannot find groovy script: " + filePath, e);
        }
    }

    public Path getCollector() {
        return collector;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setStartDelay(long startDelay) {
        this.startDelay = startDelay;
    }

    public long getStartDelay() {
        return startDelay;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getConfig() {
        return config;
    }
}
