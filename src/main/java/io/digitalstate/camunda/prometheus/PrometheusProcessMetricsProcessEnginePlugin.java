package io.digitalstate.camunda.prometheus;

import io.digitalstate.camunda.prometheus.collectors.camunda.CamundaMetrics;
import io.digitalstate.camunda.prometheus.collectors.custom.CamundaCustomMetrics;
import io.prometheus.client.CollectorRegistry;
import org.camunda.bpm.engine.impl.calendar.DateTimeUtil;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.camunda.bpm.engine.ProcessEngine;

public class PrometheusProcessMetricsProcessEnginePlugin extends AbstractProcessEnginePlugin {

//    private String classList = "";
//    private Boolean startupMetricLoading = false;
    private String port = "9999";
    private long pollingFrequencyMills = 900000;
    private long pollingStartDelayMills = 0;
    private String queryStartDate = DateTimeUtil.now().toString();

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
        new CamundaMetrics(processEngine,
                getPollingFrequencyMills(),
                getPollingStartDelayMills(),
                DateTimeUtil.parseDateTime(getQueryStartDate()));

        new CamundaCustomMetrics(processEngine,
                getPollingFrequencyMills(),
                getPollingStartDelayMills());
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

    public void setPollingFrequencyMills(long frequencyMills){
        this.pollingFrequencyMills = frequencyMills;
    }
    public long getPollingFrequencyMills(){
        return this.pollingFrequencyMills;
    }

    public void setPollingStartDelayMills(long delayMills){
        this.pollingStartDelayMills = delayMills;
    }
    public long getPollingStartDelayMills(){
        return this.pollingStartDelayMills;
    }

    public void setQueryStartDate(String startDate){
        this.queryStartDate = DateTimeUtil.parseDateTime(startDate).toString();
    }

    public String getQueryStartDate(){
        return this.queryStartDate;
    }
}
