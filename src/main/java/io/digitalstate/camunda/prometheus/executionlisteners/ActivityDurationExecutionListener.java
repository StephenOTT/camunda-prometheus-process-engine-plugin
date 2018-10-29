package io.digitalstate.camunda.prometheus.executionlisteners;

import io.digitalstate.camunda.prometheus.collectors.SimpleHistogramMetric;
import io.digitalstate.camunda.prometheus.config.yaml.ActivityDurationTrackingConfig;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.cfg.TransactionState;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActivityDurationExecutionListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityDurationExecutionListener.class);

    private List<String> histogramLabelNames = Arrays.asList("engine_name", "element_type", "process_definition_id", "activity_id");
    private ActivityDurationTrackingConfig metricConfig;

    public ActivityDurationExecutionListener(ActivityDurationTrackingConfig metricConfig){
        this.metricConfig = metricConfig;
    }

    public void notify(DelegateExecution execution) throws Exception {
        final String processDefinitionId = execution.getProcessDefinitionId();
        final String activityInstanceId = execution.getActivityInstanceId();
        final String activityId = execution.getCurrentActivityId();
        final String elementTypeName = execution.getBpmnModelElementInstance().getElementType().getTypeName();

        ProcessEngine engine = (ProcessEngine) execution.getProcessEngineServices();
        final String engineName = engine.getName();

        // If boolean is set to true for useProcessDefinitionIdWithName, then we modify the metric name
        String histogramNameAggregate;
        if (this.metricConfig.getAppendProcessDefinitionIdToMetricName()){
            histogramNameAggregate = String.format("%s_%s", this.metricConfig.getMetricName(), processDefinitionId);
        } else {
            histogramNameAggregate = this.metricConfig.getMetricName();
        }

        // Will create a new Metric or return the existing metric if the name already exists in the histogram map.
        //@TODO consider rebuilding this metric into the core prometheus api rather than Simple Metric
        //@TODO simple metric has extra overhead for business friendly which is not required here.
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
                    HistoricActivityInstanceEventEntity activity = commandContext.getDbEntityManager()
                            .getCachedEntity(HistoricActivityInstanceEventEntity.class, activityInstanceId);

                    if (activity != null){
                        double durationInSeconds = activity.getDurationInMillis() / 1000.0;
                        LOGGER.debug("Duration in seconds<double> calculation: {} : {} seconds", activityId, String.valueOf(durationInSeconds));

                        // set the histogram with the duration from the activity instance
                        histogramMetric.observeValue(durationInSeconds, Arrays.asList(engineName, elementTypeName, processDefinitionId, activityId));
                        LOGGER.debug("Prometheus Activity Duration collected: {} : {} : {}", processDefinitionId,  activityInstanceId, durationInSeconds);

                    } else {
                        LOGGER.debug("Activity Instance Entity query returned null: {} : {}", processDefinitionId,  activityInstanceId);
                    }
                });
    }
}