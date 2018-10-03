package io.digitalstate.camunda.prometheus.collectors.camunda;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.Metrics;
import org.camunda.bpm.engine.management.MetricsQuery;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class DmnExecution {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmnExecution.class);

    /**
     * Collects the number of decision elements executed during evaluation of DMN decision tables. For one table, this is calculated as the number of clauses multiplied by the number of rules.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectExecutedDecisionElements(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_executed_decision_elements",
                "The number of decision elements executed during evaluation of DMN decision tables. For one table, this is calculated as the number of clauses multiplied by the number of rules.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.EXECUTED_DECISION_ELEMENTS)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Executed Decision Elements: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     * @param startDate
     * @param endDate
     */
    static void collect(ProcessEngine processEngine, DateTime startDate, DateTime endDate){
        MetricsQuery metricsQuery =  processEngine.getManagementService().createMetricsQuery();
        String engineName = processEngine.getName();

        collectExecutedDecisionElements(metricsQuery, engineName, startDate, endDate);
    }

}
