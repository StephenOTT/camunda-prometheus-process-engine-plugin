package io.digitalstate.camunda.prometheus.parselisteners.executionlisteners;

import io.digitalstate.camunda.prometheus.collectors.SimpleHistogramMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.impl.cfg.TransactionState;
import org.camunda.bpm.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityDurationExecutionListener implements ExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityDurationExecutionListener.class);

    public String histogramName = "activity_instance_duration";
    public String histogramHelp = "the duration of activity instances";
    public List<String> histogramLabelNames = Arrays.asList("engine_name", "element_type", "process_definition_id", "activity_id");
    public List<Double> histogramBuckets = null;
    public Boolean useProcessDefinitionIdWithName = false;

    public void notify(DelegateExecution execution) {
        final String processDefinitionId = execution.getProcessDefinitionId();
        final String processInstanceId = execution.getProcessInstanceId();
        final String activityInstanceId = execution.getActivityInstanceId();
        final String elementTypeName = execution.getBpmnModelElementInstance().getElementType().getTypeName();

        ProcessEngine engine = (ProcessEngine) execution.getProcessEngineServices();
        final String engineName = engine.getName();

        // If boolean is set to true for useProcessDefinitionkeyWithName, then we modify the metric name
        String histogramNameAggregate;
        if (useProcessDefinitionIdWithName){
            histogramNameAggregate = String.format("%s_%s", histogramName, processDefinitionId);
        } else {
            histogramNameAggregate = histogramName;
        }

        // Will create a new Metric or return the existing metric if they share a name
        SimpleHistogramMetric histogramMetric = new SimpleHistogramMetric(histogramNameAggregate,
                histogramHelp,
                histogramBuckets,
                histogramLabelNames);


        if (ExecutionListener.EVENTNAME_END.equals(execution.getEventName())){
            // Adds a transaction into the current transaction so we can grab the activity duration.
            Context.getCommandContext().getTransactionContext()
                    .addTransactionListener(TransactionState.COMMITTED, commandContext -> {
                        // @TODO Review the usage of the command context for possible better way to access the activity instance
                        HistoricActivityInstance activity = execution.getProcessEngineServices()
                                .getHistoryService().createHistoricActivityInstanceQuery()
                                .activityInstanceId(activityInstanceId)
                                .processInstanceId(processInstanceId)
                                .singleResult();

                        if (activity != null){
                            long duration = activity.getDurationInMillis();

                            histogramMetric.observeValue(duration, Arrays.asList(engineName, elementTypeName, processDefinitionId, activityInstanceId));
                            LOGGER.debug("Prometheus Activity Duration collected: {} : {} : {}", processDefinitionId,  activityInstanceId, duration);

                        } else {
                            LOGGER.debug("Activity Instance query returned null: {} : {}", processDefinitionId,  activityInstanceId);
                        }
                    });
        }
    }
}