package io.digitalstate.camunda.prometheus.parselisteners;

import io.digitalstate.camunda.prometheus.PrometheusProcessEnginePlugin;
import io.digitalstate.camunda.prometheus.config.yaml.DurationTrackingConfig;
import io.digitalstate.camunda.prometheus.executionlisteners.ProcessDurationExecutionListener;
import org.camunda.bpm.engine.BpmnParseException;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class ProcessDurationHelpers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDurationHelpers.class);
    private static Map<String, DurationTrackingConfig> yamlFile = PrometheusProcessEnginePlugin.getYamlConfig().getActivityDurationTrackingConfigs();

    /**
     * Determines if Process Duration Tracking is found in the Process Definition
     * @param processDefinition
     * @param containsValue
     * @return
     */
    static boolean hasProcessDurationTracking(ProcessDefinitionEntity processDefinition, String containsValue){
        Object documentationProperty = processDefinition.getProcessDefinition().getProperty("documentation");
        return documentationProperty != null && documentationProperty.toString()
                .contains(containsValue);
    }

    /**
     * Generates a Execution Listener on the Process Definition on the End of the Process.
     * @param processDefinitionEntity
     * @param element
     * @param trackArguments
     */
    static void generateProcessDurationListener(ProcessDefinitionEntity processDefinitionEntity, Element element, Map trackArguments){
        // Get the defined metric name from the metric key in the config
        String metricName = trackArguments.get("metric").toString();
        // Check if the yaml file has the metric key.  If not, throw a parse error:
        if (yamlFile.containsKey(metricName)){
            DurationTrackingConfig config = yamlFile.get(metricName);
            config.setMetricName(metricName);

            //@TODO add logic to use a default duration tracker for processes or fall back to customized tracker

            // If there is a append Process Definition Id boolean set to true then we add this config.
            if (trackArguments.containsKey("appendPdId")){
                config.setAppendProcessDefinitionIdToMetricName(Boolean.parseBoolean(trackArguments.get("appendPdId").toString()));
            }

            ProcessDurationExecutionListener listener = new ProcessDurationExecutionListener(config);
            processDefinitionEntity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);

            LOGGER.debug("Process Duration Tracker Listener has been added to {}", processDefinitionEntity.getId());

        } else {
            LOGGER.error("Could not parse Process: metric name ({}) in {} Process Element Document Field, could not be found in YAML configuration", metricName, processDefinitionEntity.getKey());
        }
    }

    /**
     * Primary method for processing a Process Definition through Process Duration Tracking.
     * @param element
     * @param processDefinition
     */
    static void processForProcessDurationTracking(Element element, ProcessDefinitionEntity processDefinition){
        String promTrack = "prometheus.track:";
        String trackInfo = "{type:'process-duration'}";
        String containsValue = String.join("",promTrack, trackInfo);
        boolean processDurationTracking = hasProcessDurationTracking(processDefinition, containsValue);

        if (processDurationTracking){
            String processDefinitionKeyClean = processDefinition.getKey().replace("-", "_");
            Map processDurationTrackingConfig = ParsingHelpers.evalProperty(String.join("",
                    "{type:'process-duration', metric:'process_instance_duration_", processDefinitionKeyClean, "'}"));

            generateProcessDurationListener(processDefinition, element, processDurationTrackingConfig);
            LOGGER.debug("Process Duration Tracking has been added to {}, from BPMN Configuration", processDefinition.getKey());
        } else{
            LOGGER.debug("Process Duration Tracking config is not found for {}", processDefinition.getKey());
        }
    }

}
