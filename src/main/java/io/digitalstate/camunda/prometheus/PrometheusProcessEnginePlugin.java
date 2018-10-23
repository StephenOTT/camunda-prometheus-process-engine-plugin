package io.digitalstate.camunda.prometheus;

import io.digitalstate.camunda.prometheus.collectors.camunda.CamundaMetrics;
import io.digitalstate.camunda.prometheus.collectors.custom.CamundaCustomMetrics;
import io.digitalstate.camunda.prometheus.config.YamlConfig;
import io.digitalstate.camunda.prometheus.parselisteners.BpmnGlobalParseListener;
import io.prometheus.client.CollectorRegistry;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.camunda.bpm.engine.ProcessEngine;

import java.util.ArrayList;
import java.util.List;

public class PrometheusProcessEnginePlugin extends AbstractProcessEnginePlugin {

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
     * The Interval for Camunda Reporting Metrics to execute on the DB.
     * This is overriding the default 15min interval in the Camunda Engine.
     * This plugin defaults to a 900 second interval, which is 15 minutes.
     */
    private String camundaReportingIntervalInSeconds = "900";

    /**
     * YAML file path for Collector description/configuration.
     */
    private String collectorYmlFilePath;

    /**
     * Boolean as string (true/false) to indicate whether to activate the BPMN Activity Duration Parse Listener.
     */
    private String bpmnActivityDurationParseListener;


    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusProcessEnginePlugin.class);
    final private CollectorRegistry registry = CollectorRegistry.defaultRegistry;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        // Starts up the Prometheus Client HTTP Server
        new MetricsExporter(new Integer(getPort()));

        if (Boolean.parseBoolean(bpmnActivityDurationParseListener)){
            List<BpmnParseListener> parseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
            if (parseListeners == null) {
                parseListeners = new ArrayList<BpmnParseListener>();
                processEngineConfiguration.setCustomPreBPMNParseListeners(parseListeners);
            }
            //@TODO Add YAML Configuration for each listener's configuration
            parseListeners.add(new BpmnGlobalParseListener());
            LOGGER.info("Prometheus Bpmn Activity Duration Parse Listener is ACTIVE");

        } else {
            LOGGER.info("Prometheus Bpmn Activity Duration Parse Listener is DISABLED");
        }
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

    public String getBpmnActivityDurationParseListener(){
        return this.bpmnActivityDurationParseListener;
    }
    public void setBpmnActivityDurationParseListener(String bpmnActivityDurationParseListener){
        this.bpmnActivityDurationParseListener = bpmnActivityDurationParseListener;
    }
}