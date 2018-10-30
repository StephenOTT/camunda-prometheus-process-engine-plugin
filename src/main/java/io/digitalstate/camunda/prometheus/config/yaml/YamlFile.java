package io.digitalstate.camunda.prometheus.config.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlFile {
    private List<SystemMetricsConfig> system = new ArrayList<>();
    private List<CustomMetricsConfig> custom = new ArrayList<>();
    private Map<String, DurationTrackingConfig> durationTracking = new HashMap<>();

    public void setSystem(List<SystemMetricsConfig> system) {
        this.system = system;
    }
    public List<SystemMetricsConfig> getSystem() {
        return system;
    }

    public void setCustom(List<CustomMetricsConfig> custom) {
        this.custom = custom;
    }
    public List<CustomMetricsConfig> getCustom() {
        return custom;
    }

    public void setDurationTracking(Map<String, DurationTrackingConfig> durationTracking) {
        this.durationTracking = durationTracking;
    }
    public Map<String, DurationTrackingConfig> getDurationTracking() {
        return durationTracking;
    }
}
