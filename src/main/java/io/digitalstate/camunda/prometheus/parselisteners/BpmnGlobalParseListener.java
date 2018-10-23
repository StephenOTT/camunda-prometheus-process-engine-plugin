package io.digitalstate.camunda.prometheus.parselisteners;

import io.digitalstate.camunda.prometheus.parselisteners.executionlisteners.ActivityDurationExecutionListener;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.impl.variable.VariableDeclaration;

import java.util.List;

//@TODO Add YAML Configuration for each listener's configuration

public class BpmnGlobalParseListener extends AbstractBpmnParseListener {

    @Override
    public void parseStartEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseExclusiveGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseInclusiveGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseParallelGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseScriptTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseServiceTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBusinessRuleTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseManualTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseUserTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseEndEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundaryTimerEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundaryErrorEventDefinition(Element element, boolean interrupting, ActivityImpl activity, ActivityImpl nestedErrorEventActivity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseSubProcess(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseCallActivity(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
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
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseIntermediateTimerEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

//    @Override
//    public void parseRootElement(Element rootElement, List<ProcessDefinitionEntity> processDefinitions) {
//    }

    @Override
    public void parseReceiveTask(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseIntermediateSignalCatchEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundarySignalEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseEventBasedGateway(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseTransaction(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseCompensateEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseIntermediateThrowEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseIntermediateCatchEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundaryEvent(Element element, ScopeImpl scope, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseIntermediateMessageCatchEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundaryMessageEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundaryEscalationEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseBoundaryConditionalEventDefinition(Element element, boolean interrupting, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseIntermediateConditionalEventDefinition(Element element, ActivityImpl activity) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

    @Override
    public void parseConditionalStartEventForEventSubprocess(Element element, ActivityImpl activity, boolean interrupting) {
        ActivityDurationExecutionListener listener = new ActivityDurationExecutionListener();
        activity.addListener(ExecutionListener.EVENTNAME_END, listener, 0);
    }

}
