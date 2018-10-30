package io.digitalstate.camunda.prometheus.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.digitalstate.camunda.prometheus.config.yaml.DurationTrackingConfig;
import io.digitalstate.camunda.prometheus.config.yaml.CustomMetricsConfig;
import io.digitalstate.camunda.prometheus.config.yaml.SystemMetricsConfig;
import io.digitalstate.camunda.prometheus.config.yaml.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlConfig.class);

    private YamlFile yamlConfig = new YamlFile();
    private List<SystemMetricsConfig> systemMetricsConfigs = new ArrayList<>();
    private List<CustomMetricsConfig> customMetricsConfigs = new ArrayList<>();
    private Map<String, DurationTrackingConfig> activityDurationTrackingConfigs = new HashMap<>();

    public YamlConfig(String filePath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            // @TODO Add error handling for file not found
            Path file = Paths.get(filePath);

            // @TODO Add error handling for various read errors such as no system section, no custom section, etc.
            yamlConfig = mapper.readValue(file.toFile(), YamlFile.class);

            // System Metrics
            if (yamlConfig != null && !yamlConfig.getSystem().isEmpty()) {
                    systemMetricsConfigs = yamlConfig.getSystem();
            } else {
                LOGGER.warn("No Prometheus Camunda System Metrics were not found.");
            }

            // Custom metrics
            if (yamlConfig != null && !yamlConfig.getCustom().isEmpty()) {
                customMetricsConfigs = yamlConfig.getCustom();
            } else {
                LOGGER.info("No Custom Prometheus Metrics Collectors were found.");
            }

            // Activity Duration Tracking Configuration
            if (yamlConfig != null && !yamlConfig.getDurationTracking().isEmpty()) {
                activityDurationTrackingConfigs = yamlConfig.getDurationTracking();
            } else {
                LOGGER.info("No Activity Duration Tracking Configs for Prometheus Metrics were found.");
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            LOGGER.error("Unable to parse YAML, likely due to bad YAML format.");
            e.printStackTrace();
        }
    }


    //
    // SETTERS AND GETTER
    //

    public List<CustomMetricsConfig> getCustomMetricsConfigs() {
        return customMetricsConfigs;
    }

    public List<SystemMetricsConfig> getSystemMetricsConfigs() {
        return systemMetricsConfigs;
    }

    public Map<String, DurationTrackingConfig> getActivityDurationTrackingConfigs() {
        return activityDurationTrackingConfigs;
    }

    public YamlFile getYamlConfig() {
        return yamlConfig;
    }
}
