package io.digitalstate.camunda.prometheus.parselisteners;

import io.digitalstate.camunda.prometheus.PrometheusProcessEnginePlugin;
import io.digitalstate.camunda.prometheus.config.yaml.ActivityDurationTrackingConfig;
import io.digitalstate.camunda.prometheus.executionlisteners.ActivityDurationExecutionListener;
import org.camunda.bpm.engine.BpmnParseException;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public class BpmnActivityDurationTrackingParseListener extends AbstractBpmnParseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BpmnActivityDurationTrackingParseListener.class);

    private ExpressionParser parser = new SpelExpressionParser();
    private SimpleEvaluationContext context = new SimpleEvaluationContext.Builder().build();

    // Yaml File configuration holding the specific config for Activity Duration Tracking
    private Map<String, ActivityDurationTrackingConfig> yamlFile = PrometheusProcessEnginePlugin.getYamlConfig().getActivityDurationTrackingConfigs();


    /**
     * Gets the Camunda Extension Properties from a BPMN element and converts it into a MultiValueMap.
     * The MultiValueMap allows for multiple duplicate keys in the Camunda Extension Properties.
     * @param element Element: BPMN Element
     * @param propertyNameStartsWith String of Property key to look for.  Default is 'prometheus.'
     * @return MultiValueMap<String,String>
     */
    private MultiValueMap<String,String> getCamundaExtensionPropertiesThatStartWith(Element element, String propertyNameStartsWith){
        MultiValueMap<String,String> extensionProperties = new LinkedMultiValueMap<>();
        try{
            // @TODO rebuild this try/cactch so the properties variable is not handled through error handling...
            List<Element> properties = element.element("extensionElements").element("properties").elements("property");

            properties.forEach(property -> {
                String name = property.attribute("name");
                String value = property.attribute("value");

                if (property.attribute("name").startsWith(propertyNameStartsWith)){
                    extensionProperties.add(name, value);
                }
            });
            return extensionProperties;

        } catch (Exception e){
            // Did not find any properties, so return empty map
            LOGGER.debug("Activity Duration Tracking: No Camunda Extension Element properties were found");

            return extensionProperties;
        }
    }

    /**
     * Checks if the MultiValueMap of camunda extension properties has a "prometheus.track" key
     * @param properties MultiValueMap<String,String>
     * @return boolean
     */
    private Boolean hasTrack(MultiValueMap<String,String> properties){
        return !properties.isEmpty() &&
                properties.containsKey("prometheus.track");
    }

    /**
     * Checks if the process definition has a tracking config in the documentation property of the BPMN
     * @param processDefinition ProcessDefinitionEntity
     * @return boolean
     */
    private boolean hasProcessWideTracking(ProcessDefinitionEntity processDefinition){
        Object documentationProperty = processDefinition.getProcessDefinition().getProperty("documentation");
        return documentationProperty != null && documentationProperty.toString()
                .contains("prometheus.track:{type:'activity-duration', metric:'activity_instance_duration'}");
    }

    private void generateActivityDurationListener(ActivityImpl activity, Element element, Map trackArguments){
        // Get the defined metric name from the metric key in the config
        String metricName = trackArguments.get("metric").toString();
        // Check if the yaml file has the metric key.  If not, throw a parse error:
        if (this.yamlFile.containsKey(metricName)){
            ActivityDurationTrackingConfig config = this.yamlFile.get(metricName);
            config.setMetricName(metricName);

            // If there is a append Process Definition Id boolean set to true then we add this config.
            if (trackArguments.containsKey("appendPdId")){
                config.setAppendProcessDefinitionIdToMetricName(Boolean.parseBoolean(trackArguments.get("appendPdId").toString()));
            }

            ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener(config);
            activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);

            LOGGER.debug("Activity Duration Tracker Listener has been added to {}", activity.getActivityId());

        } else {
            throw new BpmnParseException(String.format("Could not parse: metric name in %s could not be found in YAML configuration", activity.getActivityId()), element);
        }
    }

    private void processElementForActivityDurationTracking(Element element, ActivityImpl activity){
        MultiValueMap<String,String> properties = getCamundaExtensionPropertiesThatStartWith(element, "prometheus.");

        boolean processWideTracking = hasProcessWideTracking((ProcessDefinitionEntity)activity.getProcessDefinition());

        if (processWideTracking){
            Map processWideConfig = evalProperty("{type:'activity-duration', metric:'activity_instance_duration'}");
            generateActivityDurationListener(activity, element, processWideConfig );

            LOGGER.debug("Activity Duration Tracking has been added to {}, from BPMN Wide Tracking Configuration", activity.getActivityId());

        }
        if (hasTrack(properties)){
                properties.get("prometheus.track").forEach(item -> {
                    // @TODO Refactor into propert Object parsing
                    // Evals the string into a Map
                    Map property = evalProperty(item);
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

    private Map evalProperty(String propertyValue){
        Map parsedProperty = this.parser.parseExpression(propertyValue).getValue(this.context, Map.class);
        LOGGER.debug("Prometheus Property Value Evaluation result: {}", parsedProperty);
        return parsedProperty;
    }


    //
    // OVERRIDES for parsing:
    //
    @Override
    public void parseProcess(Element element, ProcessDefinitionEntity processDefinition) {
        //@TODO refactor the below boolean into a method for reuse
        boolean processWideTracking = hasProcessWideTracking(processDefinition);
        if (processWideTracking){
            LOGGER.info("Process Wide Activity Duration Tracking is active for {}", processDefinition.getKey());
        }
    }

    @Override
    public void parseStartEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseExclusiveGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseInclusiveGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseParallelGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseScriptTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseServiceTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBusinessRuleTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseManualTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseUserTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseEndEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryTimerEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryErrorEventDefinition(Element element, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseSubProcess(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseCallActivity(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    //@TODO REVIEW
//    @Override
//    public void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, ActivityImpl activity) {
//    }


    // @TODO REVIEW as optional
//    @Override
//    public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
//    }

    @Override
    public void parseSendTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }


    // @TODO REVIEW as optional?
//    @Override
//    public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
//    }

    @Override
    public void parseIntermediateTimerEventDefinition(Element element, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

//    @Override
//    public void parseRootElement(Element rootElement, List<ProcessDefinitionEntity> processDefinitions) {
//    }

    @Override
    public void parseReceiveTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(Element element, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundarySignalEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseEventBasedGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseTransaction(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseCompensateEventDefinition(Element element, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateThrowEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateCatchEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateMessageCatchEventDefinition(Element element, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryEscalationEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryConditionalEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateConditionalEventDefinition(Element element, ActivityImpl activity) {
        processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseConditionalStartEventForEventSubprocess(Element element, ActivityImpl activity, boolean interrupting) {
        processElementForActivityDurationTracking(element, activity);
    }
}
