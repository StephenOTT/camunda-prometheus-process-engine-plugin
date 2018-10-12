package io.digitalstate.camunda.prometheus.config.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import java.util.Map;

public class CustomMetricsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMetricsConfig.class);
    private Resource collector;
    private Boolean enable;
    private long startDelay;
    private long frequency;
    private Map<String, Object> config;

    public void setCollector(String filePath) {
        if (filePath.startsWith("classpath:")){
            collector = new ClassPathResource(filePath.substring(10));
        } else {
            collector = new FileSystemResource(filePath);
        }
    }

    public Resource getCollector() {
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
