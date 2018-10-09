package io.digitalstate.camunda.prometheus.collectors.custom;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.ProcessDefinitionStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * User Task Collectors
 *  */

class UserTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTasks.class);

    /**
     * Collect Count of active user tasks.
     * @param processEngine
     * @param engineName
     */
    public static void collectActiveUserTasksCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "active_user_tasks_count",
                "The number of active user tasks",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getTaskService().createTaskQuery().active().count();

        LOGGER.debug("Collecting Metric Number of Active User Tasks: " + count);

        counter.setValue(count, Arrays.asList(engineName));

    }

    /**
     * Collect Count of unassigned active user tasks.
     * @param processEngine
     * @param engineName
     */
    public static void collectUnassignedActiveUserTasksCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "active_unassigned_user_tasks_count",
                "The number of unassigned active user tasks",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getTaskService().createTaskQuery().active().taskUnassigned().count();

        LOGGER.debug("Collecting Metric Number of Unassigned Active User Tasks: " + count);

        counter.setValue(count, Arrays.asList(engineName));

    }

    /**
     * Collect Count of Suspended User Tasks, where suspended occurs because the process instance is suspended.
     * @param processEngine
     * @param engineName
     */
    public static void collectSuspendedUserTasksCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "suspended_user_tasks_count",
                "The number of suspended user tasks where the task is suspended because the process is suspended.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getTaskService().createTaskQuery().suspended().count();

        LOGGER.debug("Collecting Metric Number of Suspended User Tasks: " + count);

        counter.setValue(count, Arrays.asList(engineName));

    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    public static void collectAll(ProcessEngine processEngine){
        String engineName = processEngine.getName();

        collectActiveUserTasksCount(processEngine, engineName);
        collectUnassignedActiveUserTasksCount(processEngine, engineName);
        collectSuspendedUserTasksCount(processEngine, engineName);

    }
}
