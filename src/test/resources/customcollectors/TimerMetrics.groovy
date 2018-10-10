package customcollectors

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;


collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect Count of active timer jobs.
     * @param processEngine
     * @param engineName
     */
    static void collectActiveTimerJobCount(ProcessEngine processEngine, String engineName, Logger LOG){
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

        LOG.debug("Collecting Metric Number of active timer jobs: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of suspended timer jobs.
     * @param processEngine
     * @param engineName
     */
    static void collectSuspendedTimerJobCount(ProcessEngine processEngine, String engineName, Logger LOG){
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

        LOG.debug("Collecting Metric Number of suspended timer jobs: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectActiveTimerJobCount(processEngine, engineName, LOG);
        collectSuspendedTimerJobCount(processEngine, engineName, LOG);
    }