package io.digitalstate.camunda.prometheus.parselisteners.executionlisteners;

import io.digitalstate.camunda.prometheus.collectors.SimpleHistogramMetric;
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

public class ActivityDurationExecutionListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityDurationExecutionListener.class);

    private String histogramName = "activity_instance_duration";
    private String histogramHelp = "the duration of activity instances";
    private List<String> histogramLabelNames = Arrays.asList("engine_name", "element_type", "process_definition_id", "activity_id");
    private List<Double> histogramBuckets = null;
    private Boolean useProcessDefinitionIdWithName = false;

    public void notify(DelegateExecution execution) throws Exception {
        final String processDefinitionId = execution.getProcessDefinitionId();
        final String activityInstanceId = execution.getActivityInstanceId();
        final String activityId = execution.getCurrentActivityId();
        final String elementTypeName = execution.getBpmnModelElementInstance().getElementType().getTypeName();

        ProcessEngine engine = (ProcessEngine) execution.getProcessEngineServices();
        final String engineName = engine.getName();

        // If boolean is set to true for useProcessDefinitionkeyWithName, then we modify the metric name
        String histogramNameAggregate;
        if (this.useProcessDefinitionIdWithName){
            histogramNameAggregate = String.format("%s_%s", histogramName, processDefinitionId);
        } else {
            histogramNameAggregate = this.histogramName;
        }

        // Will create a new Metric or return the existing metric if the name already exists in the histogram map.
        //@TODO consider rebuilding this metric into the core prometheus api rather than Simple Metric
        //@TODO simple metric has extra overhead for business friendly which is not required here.
        SimpleHistogramMetric histogramMetric = new SimpleHistogramMetric(histogramNameAggregate,
                this.histogramHelp,
                this.histogramBuckets,
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
                        long duration = activity.getDurationInMillis();

                        // set the histogram with the duration from the activity instance
                        histogramMetric.observeValue(duration, Arrays.asList(engineName, elementTypeName, processDefinitionId, activityId));
                        LOGGER.debug("Prometheus Activity Duration collected: {} : {} : {}", processDefinitionId,  activityInstanceId, duration);

                    } else {
                        LOGGER.debug("Activity Instance Entity query returned null: {} : {}", processDefinitionId,  activityInstanceId);
                    }
                });
    }

    //
    // GETTERS AND SETTERS
    //

    public void setHistogramName(String histogramName) {
        this.histogramName = histogramName;
    }

    public void setHistogramHelp(String histogramHelp) {
        this.histogramHelp = histogramHelp;
    }

    public void setHistogramLabelNames(List<String> histogramLabelNames) {
        this.histogramLabelNames = histogramLabelNames;
    }

    public void setHistogramBuckets(List<Double> histogramBuckets) {
        this.histogramBuckets = histogramBuckets;
    }

    public void setUseProcessDefinitionIdWithName(Boolean useProcessDefinitionIdWithName) {
        this.useProcessDefinitionIdWithName = useProcessDefinitionIdWithName;
    }
}