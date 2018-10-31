package io.digitalstate.camunda.prometheus.executionlisteners;

import io.digitalstate.camunda.prometheus.collectors.SimpleHistogramMetric;
import io.digitalstate.camunda.prometheus.config.yaml.DurationTrackingConfig;
import static io.digitalstate.camunda.prometheus.PrometheusHelpers.promClean;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.cfg.TransactionState;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ProcessDurationExecutionListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDurationExecutionListener.class);

    private List<String> histogramLabelNames = Arrays.asList("engine_name", "process_definition_id");
    private DurationTrackingConfig metricConfig;

    public ProcessDurationExecutionListener(DurationTrackingConfig metricConfig){
        this.metricConfig = metricConfig;
    }

    public void notify(DelegateExecution execution) throws Exception {
        final String processDefinitionId = execution.getProcessDefinitionId();
        final String executionId = execution.getId();
        ProcessEngine engine = (ProcessEngine) execution.getProcessEngineServices();
        final String engineName = engine.getName();

        String histogramNameAggregate;
        // If boolean is set to true for useProcessDefinitionIdWithName, then we modify the metric name
        if (this.metricConfig.getAppendProcessDefinitionIdToMetricName()){
            histogramNameAggregate = String.join("__", promClean(this.metricConfig.getMetricName()), promClean(processDefinitionId));
        } else {
            histogramNameAggregate = promClean(this.metricConfig.getMetricName());
        }

        SimpleHistogramMetric histogramMetric = new SimpleHistogramMetric(histogramNameAggregate,
                this.metricConfig.getHelp(),
                this.metricConfig.getBuckets(),
                this.histogramLabelNames);

        // Adds a transaction listener into the current transaction so we can grab the activity duration once it has been committed.
        Context.getCommandContext().getTransactionContext()
                .addTransactionListener(TransactionState.COMMITTED, commandContext -> {
                    // Get the cached entity which holds the duration of the activity instance
                    // Cache is used so another call to the DB is not required, as this chunk of
                    // code may be run for every single activity execution.
                    HistoricProcessInstanceEventEntity process = commandContext.getDbEntityManager()
                            .getCachedEntity(HistoricProcessInstanceEventEntity.class, executionId);

                    if (process != null){
                        double durationInSeconds = process.getDurationInMillis() / 1000.0;
                        LOGGER.debug("Process Duration in seconds<double> calculation: {} : {} seconds", executionId, String.valueOf(durationInSeconds));

                        // set the histogram with the duration from the activity instance
                        histogramMetric.observeValue(durationInSeconds, Arrays.asList(promClean(engineName), promClean(processDefinitionId)));
                        LOGGER.debug("Prometheus Process Duration collected: {} : {}", processDefinitionId, durationInSeconds);

                    } else {
                        LOGGER.error("Process Instance Entity query returned null: {}", processDefinitionId);
                    }
                });
    }
}