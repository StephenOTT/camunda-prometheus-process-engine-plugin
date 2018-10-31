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
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance));
        ProcessInstance processInstance2 = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance2));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProcessInstance processInstance3 = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance3));
        ProcessInstance processInstance4 = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance4));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProcessInstance processInstance5 = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance5));
        ProcessInstance processInstance6 = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance6));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ProcessInstance processInstance7 = runtimeService().startProcessInstanceByKey("test-Process");
        execute(job(processInstance7));

        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}