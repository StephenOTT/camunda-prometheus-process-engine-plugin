package io.digitalstate.camunda.prometheus;

import io.digitalstate.camunda.prometheus.collectors.camunda.CamundaMetrics;
import io.digitalstate.camunda.prometheus.collectors.custom.CamundaCustomMetrics;
import io.digitalstate.camunda.prometheus.config.YamlConfig;
import io.prometheus.client.CollectorRegistry;
import org.camunda.bpm.engine.impl.calendar.DateTimeUtil;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.camunda.bpm.engine.ProcessEngine;

import java.net.URL;

public class PrometheusProcessMetricsProcessEnginePlugin extends AbstractProcessEnginePlugin {

    /**
     * The Port that the Prometheus Client HTTP Server will be exposed on.
     * Promethus.yml configuration on the Prometheus server can then be set up with a job such as
     * ```yml
     *   - job_name: 'camunda'
     *     scrape_interval: 10s
     *     honor_labels: true
     *     static_configs:
     *     - targets: ['camundaUrl:9999']
     * ```
     * Where the above configuration is scraping the data from port 9999 every 10 seconds.
     * Take note to have a sensible scape interval in your prometheus configuration that aligns with your
     * Polling Frequency and the Camunda Reporting reporting interval
     * Default Configuration is port 9999.
     */
    private String port = "9999";

    /**
     * Polling Frequency interval in Milliseconds: How often should Prometheus Metrics poll the Camunda Metrics System.
     * This number should ideally be a number slightly greater than the Camunda Reporting Interval.
     * Default pilling frequency is 900000 which is 15 minutes.
     */
    private String pollingFrequencyMills = "900000";

    /**
     * Start Delay period for Prometheus Metrics Reporting Timer in Milliseconds
     */
    private String pollingStartDelayMills = "0";

    /**
     * The Start Date that Metric data should be queried from.
     * The default is set to the DateTimeUtil.now() time which would be when the engine started.
     * This would change for each time the server starts up.
     * Recommend to manually set a sensible start date if you have longer Reporting Intervals.
     */
    private String queryStartDate = DateTimeUtil.now().toString();

    /**
     * The Interval for Camunda Reporting Metrics to execute on the DB.
     * This is overriding the default 15min interval in the Camunda Engine.
     * This plugin defaults to a 900 second interval, which is 15 minutes.
     */
    private String camundaReportingIntervalInSeconds = "900";

    /**
     * YAML file path for Collector description/configuration.
     */
    private String collectorYmlFilePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusProcessMetricsProcessEnginePlugin.class);
    final private CollectorRegistry registry = CollectorRegistry.defaultRegistry;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

        // @TODO add better file checking
        URL ymlConfigFile = getClass().getResource(getCollectorYmlFilePath());
        if (ymlConfigFile == null){
            LOGGER.error("Cannot find YAML configuration file", new RuntimeException());
        }

        // Starts up the Prometheus Client HTTP Server
        new MetricsExporter(new Integer(getPort()));
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        // Overrides the default Metrics reporter with a new instance that has a customized Reporting interval.
        // This allows the override of the built in 15min interval

        LOGGER.info("DbMetricsReporter is being started with Interval of: " + getCamundaReportingIntervalInSeconds() + " seconds.");

        DbMetricsReporter metricsReporter = new DbMetricsReporter(processEngineConfiguration.getMetricsRegistry(),
                processEngineConfiguration.getCommandExecutorTxRequired());

        metricsReporter.setReportingIntervalInSeconds(Long.parseLong(getCamundaReportingIntervalInSeconds()));
        processEngineConfiguration.setDbMetricsReporter(metricsReporter);
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {

        // Get YAML Config File
        YamlConfig yamlConfig = new YamlConfig(getCollectorYmlFilePath());

        // Starts Prometheus reporting for the built in Camunda Metrics system.
        new CamundaMetrics(yamlConfig.getSystemMetricsConfigs(), processEngine);

        // Starts Prometheus reporting for Custom defined Metrics.
        new CamundaCustomMetrics(yamlConfig.getCustomMetricsConfigs(), processEngine);
    }


    //
    // SETTERS AND GETTERS
    //

    public void setPort(String port){
        this.port = port;
    }
    public String getPort(){
        return this.port;
    }
    public void setPollingFrequencyMills(String frequencyMills){
        this.pollingFrequencyMills = frequencyMills;
    }
    public String getPollingFrequencyMills(){
        return this.pollingFrequencyMills;
    }
    public void setPollingStartDelayMills(String delayMills){
        this.pollingStartDelayMills = delayMills;
    }
    public String getPollingStartDelayMills(){
        return this.pollingStartDelayMills;
    }
    public void setQueryStartDate(String startDate){
        this.queryStartDate = DateTimeUtil.parseDateTime(startDate).toString();
    }
    public String getQueryStartDate(){
        return this.queryStartDate;
    }
    public String getCamundaReportingIntervalInSeconds(){
        return this.camundaReportingIntervalInSeconds;
    }
    public void setCamundaReportingIntervalInSeconds(String interval){
        this.camundaReportingIntervalInSeconds = interval;
    }
    public void setCollectorYmlFilePath(String collectorYmlFilePath) {
        this.collectorYmlFilePath = collectorYmlFilePath;
    }
    public String getCollectorYmlFilePath() {
        return collectorYmlFilePath;
    }
}