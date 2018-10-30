package io.digitalstate.camunda.prometheus.parselisteners;

import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmnDurationTrackingParseListener extends AbstractBpmnParseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BpmnDurationTrackingParseListener.class);

    //
    // OVERRIDES for parsing:
    //
    @Override
    public void parseProcess(Element element, ProcessDefinitionEntity processDefinition) {
        // Process Wide Activity Duration Tracking
        if (ActivityDurationHelpers.hasProcessWideActivityDurationTracking(processDefinition)){
            LOGGER.info("Process Wide Activity Duration Tracking is active for {}", processDefinition.getKey());
        }

        ProcessDurationHelpers.processForProcessDurationTracking(element, processDefinition);
    }

    @Override
    public void parseStartEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseExclusiveGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseInclusiveGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseParallelGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseScriptTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseServiceTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBusinessRuleTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseManualTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseUserTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseEndEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryTimerEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryErrorEventDefinition(Element element, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseSubProcess(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseCallActivity(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
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
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }


    // @TODO REVIEW as optional?
//    @Override
//    public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
//    }

    @Override
    public void parseIntermediateTimerEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

//    @Override
//    public void parseRootElement(Element rootElement, List<ProcessDefinitionEntity> processDefinitions) {
//    }

    @Override
    public void parseReceiveTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundarySignalEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseEventBasedGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseTransaction(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseCompensateEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateThrowEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateCatchEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateMessageCatchEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryEscalationEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseBoundaryConditionalEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseIntermediateConditionalEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }

    @Override
    public void parseConditionalStartEventForEventSubprocess(Element element, ActivityImpl activity, boolean interrupting) {
        ActivityDurationHelpers.processElementForActivityDurationTracking(element, activity);
    }
}
