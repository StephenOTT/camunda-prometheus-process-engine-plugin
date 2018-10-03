package io.digitalstate.camunda.prometheus.collectors.custom;

import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class CamundaCustomMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaCustomMetrics.class);

    /**
     * Constructor for starting the Prometheus Camunda Custom Metrics Collectors
     * @param processEngine
     * @param frequencyMills
     * @param startDelayMills
     */
    public CamundaCustomMetrics(ProcessEngine processEngine, long frequencyMills, long startDelayMills){

        LOGGER.info("Starting instance of Prometheus Camunda Custom Metrics");

        Timer timer = new Timer("Camunda Prometheus Custom Metrics", true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                LOGGER.debug("Timer Execution: Camunda Custom Metrics");

                BpmnProcessDefinition.collect(processEngine);

            }
        }, startDelayMills, frequencyMills);
    }

}
