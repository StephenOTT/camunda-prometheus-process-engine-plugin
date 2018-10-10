import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

/**
 * User Task Collectors
 */

collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect Count of active user tasks.
     * @param processEngine
     * @param engineName
     */
    static void collectActiveUserTasksCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "active_user_tasks_count",
                "The number of active user tasks",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getTaskService().createTaskQuery().active().count();

        LOG.debug("Collecting Metric Number of Active User Tasks: " + count);

        counter.setValue(count, Arrays.asList(engineName));

    }

    /**
     * Collect Count of unassigned active user tasks.
     * @param processEngine
     * @param engineName
     */
    static void collectUnassignedActiveUserTasksCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "active_unassigned_user_tasks_count",
                "The number of unassigned active user tasks",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getTaskService().createTaskQuery().active().taskUnassigned().count();

        LOG.debug("Collecting Metric Number of Unassigned Active User Tasks: " + count);

        counter.setValue(count, Arrays.asList(engineName));

    }

    /**
     * Collect Count of Suspended User Tasks, where suspended occurs because the process instance is suspended.
     * @param processEngine
     * @param engineName
     */
    static void collectSuspendedUserTasksCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "suspended_user_tasks_count",
                "The number of suspended user tasks where the task is suspended because the process is suspended.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getTaskService().createTaskQuery().suspended().count();

        LOG.debug("Collecting Metric Number of Suspended User Tasks: " + count);

        counter.setValue(count, Arrays.asList(engineName));

    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
     static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectActiveUserTasksCount(processEngine, engineName, LOG);
        collectUnassignedActiveUserTasksCount(processEngine, engineName, LOG);
        collectSuspendedUserTasksCount(processEngine, engineName, LOG);

    }
