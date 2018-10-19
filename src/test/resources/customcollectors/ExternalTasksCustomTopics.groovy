package customcollectors;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric
import io.digitalstate.camunda.prometheus.config.yaml.CustomMetricsConfig;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

/**
 * External Task Collector for specific array of Topic Names
 * Can be reused multiple times within YAML configuration to create different timed collections
 */

CustomMetricsConfig configs = config
collectAll((ProcessEngine)processEngine, (Logger)LOGGER, configs.getConfig())

/**
 * Collect count of Active External Tasks with a specific Topic Name.
 * @param processEngine
 * @param engineName
 */
static void collectActiveExternalTasksWithTopicName(ProcessEngine processEngine, String engineName, Logger LOG, String topicName){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "active_external_tasks_with_topicname",
            "The number of active external tasks with a Topic Name label",
            Arrays.asList("engine_name", "topic_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .active()
            .topicName(topicName)
            .count()

    LOG.debug("Collecting Metric Number of Active External Tasks with a topic name: ${topicName}: ${count}");

    counter.setValue(count, Arrays.asList(engineName, topicName));
}

/**
 * Collects all collectors defined in this class.
 * @param processEngine
 */
static void collectAll(ProcessEngine processEngine, Logger LOG, Map<String, Object> config){
    String engineName = processEngine.getName();

    config['topics'].each { topicName ->
        collectActiveExternalTasksWithTopicName(processEngine, engineName, LOG, (String)topicName )
    }

}
