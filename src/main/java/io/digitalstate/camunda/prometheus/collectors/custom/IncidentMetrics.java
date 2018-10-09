package io.digitalstate.camunda.prometheus.collectors.custom;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.Incident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncidentMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncidentMetrics.class);

    /**
     * Collect Count of incidents per process definition key.
     * Does not take into consideration process definition version or version tag.
     * @param processEngine
     * @param engineName
     */
    public static void collectIncidentCountPerProcessDefinitionKey(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "incidents_per_process_definition_key_count",
                "The number of incidents per process definition key.",
                Arrays.asList("process_definition_key", "engine_name")
        );

        List<Incident> incidents = processEngine.getRuntimeService()
                .createIncidentQuery()
                .list();

        Map<String, Long> counted = incidents.stream()
                .collect(Collectors.groupingByConcurrent(incident ->
                                processEngine.getRepositoryService()
                                        .getProcessDefinition(incident.getProcessDefinitionId())
                                        .getKey(),
                        Collectors.counting()));

        LOGGER.debug("Collecting Metric Number of incidents per process definition key: " + counted.toString());

        if (counted.isEmpty()){
            gauge.setValue(0, Arrays.asList(null, engineName));
        } else {
            counted.forEach( (k,v) ->
                    gauge.setValue(v, Arrays.asList(k, engineName)));
        }
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    public static void collectAll(ProcessEngine processEngine){
        String engineName = processEngine.getName();

        collectIncidentCountPerProcessDefinitionKey(processEngine, engineName);
    }
}
