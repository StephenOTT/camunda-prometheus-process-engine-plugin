package io.digitalstate.camunda.prometheus.collectors.camunda;

import io.digitalstate.camunda.prometheus.config.SystemMetricsConfig;
import org.camunda.bpm.engine.ProcessEngine;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CamundaMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamundaMetrics.class);

    /**
     * Constructor for starting the Prometheus Camunda Metrics Collectors
     * @param processEngine
     */
    public CamundaMetrics(List<SystemMetricsConfig> systemMetricsConfigs, ProcessEngine processEngine){

        LOGGER.info("Starting instance of Prometheus Camunda Metrics");

        systemMetricsConfigs.forEach(config ->
                processSystemMetricConfig(config, processEngine)
        );
//        new BpmnExecution(processEngine, startDate, DateTimeUtil.now(), startDelayMills, frequencyMills);
//        new DmnExecution(processEngine, startDate, DateTimeUtil.now(), startDelayMills, frequencyMills);
//        new JobExecutor(processEngine, startDate, DateTimeUtil.now(), startDelayMills, frequencyMills);
    }

    /**
     *
     * @param config
     * @param processEngine
     */
    private void processSystemMetricConfig(SystemMetricsConfig config, ProcessEngine processEngine){
        if (config.getEnable()){
            try {
                Constructor<?> collectorConstructor = config.getCollector()
                        .getConstructor(ProcessEngine.class, DateTime.class, DateTime.class, long.class, long.class);

                collectorConstructor.newInstance(processEngine,
                        config.getStartDate(),
                        config.getEndDate(),
                        config.getStartDelay(),
                        config.getFrequency());

            } catch (NoSuchMethodException e) {
                LOGGER.error("Cant Find Method", e);
            } catch (IllegalAccessException e) {
                LOGGER.error("Illegal Access", e);
            } catch (InstantiationException e) {
                LOGGER.error("Could not Instantiate", e);
            } catch (InvocationTargetException e) {
                LOGGER.error("Invocation Target Exception", e);
            }
        }
    }

}
