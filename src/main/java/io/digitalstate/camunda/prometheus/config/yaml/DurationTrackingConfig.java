package io.digitalstate.camunda.prometheus.config.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DurationTrackingConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DurationTrackingConfig.class);

    private String help;
    private List<Double> buckets;
    private String metricName;
    private boolean appendProcessDefinitionIdToMetricName = false;


    //
    // SETTERS AND GETTERS
    //

    public void setHelp(String help) {
        this.help = help;
    }

    public String getHelp() {
        return help;
    }

    public void setBuckets(List<Double> buckets) {
        this.buckets = buckets;
    }

    public List<Double> getBuckets() {
        return buckets;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setAppendProcessDefinitionIdToMetricName(boolean appendProcessDefinitionIdToMetricName) {
        this.appendProcessDefinitionIdToMetricName = appendProcessDefinitionIdToMetricName;
    }
    public boolean getAppendProcessDefinitionIdToMetricName(){
        return this.appendProcessDefinitionIdToMetricName;
    }
}
