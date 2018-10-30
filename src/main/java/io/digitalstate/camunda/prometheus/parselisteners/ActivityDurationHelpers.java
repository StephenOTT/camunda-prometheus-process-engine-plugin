package io.digitalstate.camunda.prometheus.parselisteners;

import io.digitalstate.camunda.prometheus.PrometheusProcessEnginePlugin;
import io.digitalstate.camunda.prometheus.config.yaml.DurationTrackingConfig;
import io.digitalstate.camunda.prometheus.executionlisteners.ActivityDurationExecutionListener;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

import java.util.Map;

class ActivityDurationHelpers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityDurationHelpers.class);

    private static Map<String, DurationTrackingConfig> yamlFile = PrometheusProcessEnginePlugin.getYamlConfig().getActivityDurationTrackingConfigs();

    /**
     * Checks if the process definition has a tracking config in the documentation property of the BPMN
     * @param processDefinition ProcessDefinitionEntity
     * @return boolean
     */
    static boolean hasProcessWideActivityDurationTracking(ProcessDefinitionEntity processDefinition){
        Object documentationProperty = processDefinition.getProcessDefinition().getProperty("documentation");
        return documentationProperty != null && documentationProperty.toString()
                .contains("prometheus.track:{type:'activity-duration', metric:'activity_instance_duration'}");
    }

    /**
     * Adds a activity listener to the BPMN Activity definition based on the metric configuration found in the Activity Element.
     * @param activity
     * @param element
     * @param trackArguments
     */
    static void generateActivityDurationListener(ActivityImpl activity, Element element, Map trackArguments){
        // Get the defined metric name from the metric key in the config
        String metricName = trackArguments.get("metric").toString();
        // Check if the yaml file has the metric key.  If not, throw a parse error:
        if (yamlFile.containsKey(metricName)){
            DurationTrackingConfig config = yamlFile.get(metricName);
            config.setMetricName(metricName);

            // If there is a append Process Definition Id boolean set to true then we add this config.
            if (trackArguments.containsKey("appendPdId")){
                config.setAppendProcessDefinitionIdToMetricName(Boolean.parseBoolean(trackArguments.get("appendPdId").toString()));
            }

            // Add the Execution Listener
            ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener(config);
            activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);

            LOGGER.debug("Activity Duration Tracker Listener has been added to {}", activity.getActivityId());

        } else {
            LOGGER.error("Could not parse Activity: metric name({}) in {} could not be found in YAML configuration", metricName, activity.getActivityId());
        }
    }

    /**
     * Primary method used for processing a Activity element, to determine if activity tracking should be added, and with what configuration.
     * @param element
     * @param activity
     */
    static void processElementForActivityDurationTracking(Element element, ActivityImpl activity){
        MultiValueMap<String,String> properties = ParsingHelpers.getCamundaExtensionPropertiesThatStartWith(element, "prometheus.");

        boolean processWideTracking = ActivityDurationHelpers.hasProcessWideActivityDurationTracking((ProcessDefinitionEntity)activity.getProcessDefinition());

        // If there is a Process Wide Activity Duration Tracking Configuration
        if (processWideTracking){
            Map processWideConfig = ParsingHelpers.evalProperty("{type:'activity-duration', metric:'activity_instance_duration'}");
            generateActivityDurationListener(activity, element, processWideConfig );

            LOGGER.debug("Activity Duration Tracking has been added to {}, from BPMN Wide Tracking Configuration", activity.getActivityId());
        }

        // If the specific activity definition has Activity Duration Tracking Configuration
        if (ParsingHelpers.hasTrack(properties)){
            properties.get("prometheus.track").forEach(item -> {
                // @TODO Refactor into property Object parsing
                // Evals the string into a Map
                Map property = ParsingHelpers.evalProperty(item);
                if (property.containsKey("type") &&
                        property.get("type").equals("activity-duration") &&
                        property.containsKey("metric")) {
                    // If the created map has the required fields, then:
                    generateActivityDurationListener(activity, element, property);
                }
            });
            LOGGER.debug("Activity Duration Tracking has been added to {}, from Activity Definition specific configuration", activity.getActivityId());
        }
    }

}
