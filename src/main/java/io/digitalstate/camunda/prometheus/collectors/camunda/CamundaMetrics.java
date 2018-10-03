package io.digitalstate.camunda.prometheus.collectors.camunda;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.calendar.DateTimeUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;


public class CamundaMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaMetrics.class);

    /**
     * Constructor for starting the Prometheus Camunda Metrics Collectors
     * @param processEngine
     * @param frequencyMills
     * @param startDelayMills
     * @param startDate
     */
    public CamundaMetrics(ProcessEngine processEngine, long frequencyMills, long startDelayMills, DateTime startDate){

        LOGGER.info("Starting instance of Prometheus Camunda Metrics");

        Timer timer = new Timer("Camunda Prometheus Metrics", true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                LOGGER.debug("Timer Execution: Executing Prometheus BPMN, DMN, and Job Executor Collections.");

                BpmnExecution.collect(processEngine, startDate, DateTimeUtil.now());
                DmnExecution.collect(processEngine, startDate, DateTimeUtil.now());
                JobExecutor.collect(processEngine, startDate, DateTimeUtil.now());
            }
        }, startDelayMills, frequencyMills);
    }

}
