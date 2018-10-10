package io.digitalstate.camunda.prometheus.collectors.custom;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User Task Collectors
 *  */

public class ProcessInstances {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessInstances.class);

    public ProcessInstances(ProcessEngine processEngine, long startDelayMills, long frequencyMills){
        String timerName = this.getClass().getName() + " timer";
        new Timer(timerName, true).schedule(new TimerTask() {
            @Override
            public void run() {
                collectAll(processEngine);
            }
        }, startDelayMills, frequencyMills);
    }

    /**
     * Collect Counts for Active Process instances for each Process Definition Key
     * @param processEngine
     * @param engineName
     */
    public static void collectActiveProcessInstanceCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "active_process_instance_count",
                "The number of active process instances.",
                Arrays.asList("process_definition_key", "engine_name")
        );

        List<String> definitions = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .list()
                .stream()
                .map(ProcessDefinition::getKey)
                .collect(Collectors.toList());

        Map<String,Long> counts = new HashMap<>();

        definitions.forEach(definitionKey ->
                counts.put(definitionKey,
                        processEngine.getRuntimeService()
                        .createProcessInstanceQuery()
                        .processDefinitionKey(definitionKey)
                        .count()));

        LOGGER.debug("Collecting Metric Count for Active Process Instances By Process Definition Key: " + counts.toString());

        counts.forEach( (k,v) ->
                gauge.setValue(v, Arrays.asList(k, engineName)));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    public static void collectAll(ProcessEngine processEngine){
        String engineName = processEngine.getName();

        collectActiveProcessInstanceCount(processEngine, engineName);
    }
}
