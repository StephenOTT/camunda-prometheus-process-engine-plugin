package io.digitalstate.camunda.prometheus.collectors.custom;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class TimerMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerMetrics.class);

    /**
     * Collect Count of active timer jobs.
     * @param processEngine
     * @param engineName
     */
    public static void collectActiveTimerJobCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "active_timer_job_count",
                "The number of active timer jobs.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getManagementService()
                .createJobQuery()
                .active()
                .timers()
                .count();

        LOGGER.debug("Collecting Metric Number of active timer jobs: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of suspended timer jobs.
     * @param processEngine
     * @param engineName
     */
    public static void collectSuspendedTimerJobCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "suspended_timer_job_count",
                "The number of suspended timer jobs.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getManagementService()
                .createJobQuery()
                .suspended()
                .timers()
                .count();

        LOGGER.debug("Collecting Metric Number of suspended timer jobs: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    public static void collectAll(ProcessEngine processEngine){
        String engineName = processEngine.getName();

        collectActiveTimerJobCount(processEngine, engineName);
        collectSuspendedTimerJobCount(processEngine, engineName);
    }
}
