package customcollectors;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric
import io.digitalstate.camunda.prometheus.config.yaml.CustomMetricsConfig;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

/**
 * External Task Collector for specific array of Worker Id
 * Can be reused multiple times within YAML configuration to create different timed collections
 */

CustomMetricsConfig configs = config
collectAll((ProcessEngine)processEngine, (Logger)LOGGER, configs.getConfig())

/**
 * Collect count of Active External Tasks that have most recently been locked by a given worker.
 * @param processEngine
 * @param engineName
 */
static void collectActiveExternalTasksLockedByWorkerId(ProcessEngine processEngine, String engineName, Logger LOG, String workerId){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "active_external_tasks_locked_by_workerid",
            "The number of active external tasks that have been most recently locked by worker ID",
            Arrays.asList("engine_name", "worker_id")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .active()
            .workerId(workerId)
            .count()

    LOG.debug("Collecting Metric Number of Active External Tasks that have most recently been locked by worker ID: ${workerId}: ${count}");

    counter.setValue(count, Arrays.asList(engineName, workerId));
}

/**
 * Collects all collectors defined in this class.
 * @param processEngine
 */
static void collectAll(ProcessEngine processEngine, Logger LOG, Map<String, Object> config){
    String engineName = processEngine.getName();

    config['workers'].each { workerId ->
        collectActiveExternalTasksLockedByWorkerId(processEngine, engineName, LOG, (String)workerId)
    }

}
