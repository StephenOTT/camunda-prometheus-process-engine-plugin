package io.digitalstate.camunda.prometheus.collectors.camunda;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.Metrics;
import org.camunda.bpm.engine.management.MetricsQuery;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

class BpmnExecution {

    private static final Logger LOGGER = LoggerFactory.getLogger(BpmnExecution.class);

    public BpmnExecution(ProcessEngine processEngine, DateTime startDate, DateTime endDate, long startDelayMills, long frequencyMills){
        String timerName = this.getClass().getName() + " timer";
        new Timer(timerName, true).schedule(new TimerTask() {
            @Override
            public void run() {
                collectAll(processEngine, startDate, endDate);
            }
        }, startDelayMills, frequencyMills);

        LOGGER.info("Created Prometheus Metrics Collection Timer for: " + getClass().getName());
    }

    /**
     * Collects the number of activity instances started. This is also known as flow node instances (FNI).
     * @param metricsQuery Metric Query used for accessing the metric
     * @param engineName name of engine
     * @param startDate start date for query
     * @param endDate end date for query
     */
    public static void collectActivityInstancesStarted(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_activity_instance_start",
                "The number of activity instances started. This is also known as flow node instances (FNI).",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.ACTIVTY_INSTANCE_START)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Instances Started: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));

    }

    /**
     * Collects the number of activity instances ended.
     * @param metricsQuery Metric Query used for accessing the metric
     * @param engineName name of engine
     * @param startDate start date for query
     * @param endDate end date for query
     */
    public static void collectActivityInstancesEnded(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_activity_instance_end",
                "The number of activity instances ended.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.ACTIVTY_INSTANCE_END)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();
        LOGGER.debug("Collecting Metric Count for Instances Ended: " + sum);
        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class
     * @param processEngine The Process engine to collectAll from
     * @param startDate The DataTime to use as a Start Date filter
     * @param endDate The DateTime to use as a End Date filter
     */
    public static void collectAll(ProcessEngine processEngine, DateTime startDate, DateTime endDate){
        MetricsQuery metricsQuery =  processEngine.getManagementService().createMetricsQuery();
        String engineName = processEngine.getName();

        collectActivityInstancesStarted(metricsQuery, engineName, startDate, endDate);
        collectActivityInstancesEnded(metricsQuery, engineName, startDate, endDate);
    }
}
