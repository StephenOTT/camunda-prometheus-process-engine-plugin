package customcollectors

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

import java.util.stream.Collectors;

/**
 * User Task Collectors
 *  */

collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect Counts for Active Process instances for each Process Definition Key
     * @param processEngine
     * @param engineName
     */
    static void collectActiveProcessInstanceCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "active_process_instance_count",
                "The number of active process instances.",
                Arrays.asList("process_definition_key", "engine_name")
        );

        List<String> definitions = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .list()
                .stream()
                .map{ pi -> pi.getKey() }
                .collect(Collectors.toList());

        Map<String,Long> counts = new HashMap<>();

        definitions.forEach { definitionKey ->
            counts.put(definitionKey,
                    processEngine.getRuntimeService()
                            .createProcessInstanceQuery()
                            .processDefinitionKey(definitionKey)
                            .count())
        }

        LOG.debug("Collecting Metric Count for Active Process Instances By Process Definition Key: " + counts.toString());

        counts.forEach{ k,v ->
                gauge.setValue(v, Arrays.asList(k, engineName))
        }
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectActiveProcessInstanceCount(processEngine, engineName, LOG);
    }
