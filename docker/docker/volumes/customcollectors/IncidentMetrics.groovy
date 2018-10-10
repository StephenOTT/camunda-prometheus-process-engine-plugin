import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;


collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect Count of open incidents.
     * @param processEngine
     * @param engineName
     */
    static void collectOpenIncidentCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "open_incidents_count",
                "The number of incidents currently open.",
                Arrays.asList("engine_name"));

        long count = processEngine.getHistoryService()
                .createHistoricIncidentQuery()
                .open()
                .count();

        LOG.debug("Collecting Metric Number of open incidents " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of resolved incidents.
     * @param processEngine
     * @param engineName
     */
    static void collectResolvedIncidentCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "resolved_incidents_count",
                "The number of incidents resolved.",
                Arrays.asList("engine_name"));

        long count = processEngine.getHistoryService()
                .createHistoricIncidentQuery()
                .resolved()
                .count();

        LOG.debug("Collecting Metric Number of resolved incidents " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of deleted incidents.
     * @param processEngine
     * @param engineName
     */
    static void collectDeletedIncidentCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "deleted_incidents_count",
                "The number of incidents deleted.",
                Arrays.asList("engine_name"));

        long count = processEngine.getHistoryService()
                .createHistoricIncidentQuery()
                .deleted()
                .count();

        LOG.debug("Collecting Metric Number of deleted incidents " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }


    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectOpenIncidentCount(processEngine, engineName, LOG);
        collectDeletedIncidentCount(processEngine, engineName, LOG);
        collectResolvedIncidentCount(processEngine, engineName, LOG);
    }
