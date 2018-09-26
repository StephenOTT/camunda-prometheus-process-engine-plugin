package prometheus;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import org.junit.Rule;
import org.junit.Test;

public class PromethusTest1 {

    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule("camunda_config/camunda.cfg.xml");

    @Test
    @Deployment(resources = {"testProcess.bpmn"})
    public void shouldExecuteProcess() {
        // Given we create a new process instance
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess");
        ProcessInstance processInstance2 = runtimeService().startProcessInstanceByKey("testProcess");

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProcessInstance processInstance3 = runtimeService().startProcessInstanceByKey("testProcess");
        ProcessInstance processInstance4 = runtimeService().startProcessInstanceByKey("testProcess");

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProcessInstance processInstance5 = runtimeService().startProcessInstanceByKey("testProcess");
        ProcessInstance processInstance6 = runtimeService().startProcessInstanceByKey("testProcess");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProcessInstance processInstance7 = runtimeService().startProcessInstanceByKey("testProcess");

        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}