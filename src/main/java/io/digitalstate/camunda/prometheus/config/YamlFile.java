package io.digitalstate.camunda.prometheus.config;

import java.util.ArrayList;
import java.util.List;

public class YamlFile {
    private List<SystemMetricsConfig> system = new ArrayList<>();
    private List<CustomMetricsConfig> custom = new ArrayList<>();

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
}
