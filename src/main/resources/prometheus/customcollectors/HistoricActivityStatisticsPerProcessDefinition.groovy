package prometheus.customcollectors;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric
import io.digitalstate.camunda.prometheus.config.yaml.CustomMetricsConfig;
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.history.HistoricActivityStatistics
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.slf4j.Logger;

/**
 * Gathers Activity instance counts (finished and active) for each activity for a process definition ID
 * Can be reused multiple times within YAML configuration to create different timed collections
 */

CustomMetricsConfig configs = config
collectAll((ProcessEngine)processEngine, (Logger)LOGGER, configs.getConfig())

/**
 * Collect count of Active and Finished Activity Instance Counts.
 * @param processEngine
 * @param engineName
 * @param LOG
 * @param processDefinitionId
 */
static void collectProcessDefinitionActivityInstanceCounts(ProcessEngine processEngine, String engineName, Logger LOG, ProcessDefinition processDefinition){
    SimpleGaugeMetric counterFinished = new SimpleGaugeMetric(
            "process_definition_activity_finished_instance_counts",
            "Finished Activity instance counts (labels) for a specific process definition id (label)",
            Arrays.asList("engine_name", "process_definition_id", "process_definition_key", "activity_id")
    );

    SimpleGaugeMetric counterActive = new SimpleGaugeMetric(
            "process_definition_activity_active_instance_counts",
            "Active Activity instance counts (labels) for a specific process definition id (label)",
            Arrays.asList("engine_name", "process_definition_id", "process_definition_key", "activity_id")
    );

    List<HistoricActivityStatistics> activityFinishedStats = processEngine.getHistoryService()
            .createHistoricActivityStatisticsQuery(processDefinition.getId())
            .includeFinished()
            .list();

    List<HistoricActivityStatistics> activityActiveStats = processEngine.getHistoryService()
            .createHistoricActivityStatisticsQuery(processDefinition.getId())
            .list();

    activityActiveStats.each { instance ->
        LOG.debug("Activity Instance Stats (Active Query): ACTIVE: instance: ${instance.getId()}  active: ${instance.getInstances()} finished: ${instance.getFinished()}")
        counterActive.setValue(instance.getInstances(), [engineName, processDefinition.getId(), processDefinition.getKey(), instance.getId()])
    }

    activityFinishedStats.each { instance ->
        LOG.debug("Activity Instance Stats (Finished Query): FINISHED: instance: ${instance.getId()}  active: ${instance.getInstances()} finished: ${instance.getFinished()}")
        counterFinished.setValue(instance.getFinished(), [engineName, processDefinition.getId(), processDefinition.getKey(), instance.getId()])
    }
}

/**
 * Collects all collectors defined in this class.
 * @param processEngine
 */
static void collectAll(ProcessEngine processEngine, Logger LOG, Map<String, Object> config){
    String engineName = processEngine.getName();

    // Collect Process Definitions based on the keys.  Uses the Latest
    config.get('processDefinitionKeys').each { key ->
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey((String)key)
                .latestVersion()
                .singleResult()

        if (processDefinition != null){
            collectProcessDefinitionActivityInstanceCounts(processEngine, engineName, LOG, processDefinition)
        }
    }

    // Collect Process Definitions based on the Ids.
    config.get('processDefinitionIds').each { id ->
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId((String)id)
                .singleResult()

        if (processDefinition != null){
            collectProcessDefinitionActivityInstanceCounts(processEngine, engineName, LOG, processDefinition)
        }
    }

}