package customcollectors

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.ProcessDefinitionStatistics;
import org.slf4j.Logger;


collectAll((ProcessEngine)processEngine, (Logger)LOGGER)


    /**
     * Collected Process Definition Instance counts using the Management Service's Process Definition Statistics Query
     * @param processEngine
     * @param engineName
     */
    static void collectProcessDefinitionInstanceCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "process_definition_stats_instance_count",
                "The number of activity instances started. This is also known as flow node instances (FNI).",
                Arrays.asList("process_definition_key","engine_name")
        );

        List<ProcessDefinitionStatistics> stats = processEngine.getManagementService().createProcessDefinitionStatisticsQuery().list();

        LOG.debug("Collecting Metric Count for Active Process Instances By Process Definition Key: " + stats.toString());

        stats.forEach { stat ->
                counter.setValue(stat.getInstances(),
                        Arrays.asList(stat.getKey(),
                                engineName))
        }

    }

    /**
     * Collects all collectors defined in this class
     * @param processEngine
     */
    static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectProcessDefinitionInstanceCount(processEngine, engineName, LOG);

    }
