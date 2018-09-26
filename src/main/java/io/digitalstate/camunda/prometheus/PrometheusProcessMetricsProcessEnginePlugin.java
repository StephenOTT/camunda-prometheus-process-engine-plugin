package io.digitalstate.camunda.prometheus;

import io.prometheus.client.CollectorRegistry;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.camunda.bpm.engine.ProcessEngine;

public class PrometheusProcessMetricsProcessEnginePlugin extends AbstractProcessEnginePlugin {

//    private String classList = "";
//    private Boolean startupMetricLoading = false;
    private String port = "9999";

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusProcessMetricsProcessEnginePlugin.class);
    final private CollectorRegistry registry = CollectorRegistry.defaultRegistry;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

        new MetricsExporter(new Integer(getPort()));

        // Future Usage for loading initial values into some metrics
        // Example: Loading a Gauge Count based on a History Service Process Instance Query
//        if (this.startupMetricLoading) {
//            LOGGER.info("startupMetricLoading == true: initializing startup Metric configs for Prometheus");
//
//            getClassListAsArray().forEach(item -> {
//                try {
//                    getClass().getClassLoader().loadClass(item);
//                } catch (ClassNotFoundException e) {
//                    LOGGER.error("Failed to initialize Collector Class: " + item + " due to: " + e.getLocalizedMessage());
//                    e.printStackTrace();
//                }
//            });
//        }
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
    }


//    public void setClassList(String classList) {
//        this.classList = classList;
//    }

//    public String getClassList() {
//        return this.classList;
//    }

//    public List<String> getClassListAsArray(){
//        return Arrays.asList(this.classList.split("\\s*,\\s*"));
//    }


//    public void setStartupMetricLoading(Boolean value) {
//        this.startupMetricLoading = value;
//    }

//    public Boolean getStartupMetricLoading() {
//        return this.startupMetricLoading;
//    }

    public void setPort(String port){
        this.port = port;
    }

    public String getPort(){
        return this.port;
    }
}
