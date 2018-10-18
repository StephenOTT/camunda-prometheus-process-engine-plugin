import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

/**
 * External Task Collectors
 * See methods, as some have specific configuration options.
 */

collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect count Active External Tasks.
     * @param processEngine
     * @param engineName
     */
    static void collectActiveExternalTasks(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "active_external_task_count",
                "The number of external tasks that are not suspended (thus are active).",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getExternalTaskService()
                .createExternalTaskQuery()
                .active()
                .count()

        LOG.debug("Collecting Metric Number of Active External Tasks: ${count}");

        counter.setValue(count, Arrays.asList(engineName));
    }

/**
 * Collect count Suspended External Tasks.
 * @param processEngine
 * @param engineName
 */
static void collectSuspendedExternalTasks(ProcessEngine processEngine, String engineName, Logger LOG){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "suspended_external_task_count",
            "The number of external tasks that are suspended (thus are not active).",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .suspended()
            .count()

    LOG.debug("Collecting Metric Number of Suspended External Tasks: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of Locked External Tasks.
 * @param processEngine
 * @param engineName
 */
static void collectLockedExternalTasks(ProcessEngine processEngine, String engineName, Logger LOG){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "locked_external_task_count",
            "The number of external tasks that are locked.",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .locked()
            .count()

    LOG.debug("Collecting Metric Number of Locked External Tasks: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of External Tasks with no reties left.
 * @param processEngine
 * @param engineName
 */
static void collectExternalTasksWithNoRetriesLeft(ProcessEngine processEngine, String engineName, Logger LOG){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "external_tasks_with_no_retries_left_count",
            "The number of external tasks that have no retries left (retries = 0).",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .noRetriesLeft()
            .count()

    LOG.debug("Collecting Metric Number of External Tasks with no retries left: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of External Tasks with reties left.
 * @param processEngine
 * @param engineName
 */
static void collectExternalTasksWithRetriesLeft(ProcessEngine processEngine, String engineName, Logger LOG){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "external_tasks_with_retries_left_count",
            "The number of external tasks that have retries left (retries > 0).",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .withRetriesLeft()
            .count()

    LOG.debug("Collecting Metric Number of External Tasks with retries left: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of External Tasks that are not locked.
 * @param processEngine
 * @param engineName
 */
static void collectExternalTasksNotLocked(ProcessEngine processEngine, String engineName, Logger LOG){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "external_tasks_not_locked_count",
            "The number of external tasks that are not locked",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .notLocked()
            .count()

    LOG.debug("Collecting Metric Number of External Tasks that are not locked: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of External Tasks with a Priority Higher Than or Equals the priority parameter.
 * @param processEngine
 * @param engineName
 */
static void collectExternalTasksWithPriorityHigherThanOrEquals(ProcessEngine processEngine, String engineName, Logger LOG, long externalTaskPriority){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "external_tasks_with_priority_higher_than_or_equals_${externalTaskPriority}",
            "The number of external tasks with a Priority Higher Than or Equals ${externalTaskPriority}",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .priorityHigherThanOrEquals(externalTaskPriority)
            .count()

    LOG.debug("Collecting Metric Number of External Tasks with a priority higher than or equals ${externalTaskPriority}: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of External Tasks with a Lower Higher Than or Equals the priority parameter.
 * @param processEngine
 * @param engineName
 */
static void collectExternalTasksWithPriorityLowerThanOrEquals(ProcessEngine processEngine, String engineName, Logger LOG, long externalTaskPriority){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "external_tasks_with_priority_lower_than_or_equals_${externalTaskPriority}",
            "The number of external tasks with a Priority Lower Than or Equals ${externalTaskPriority}",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .priorityLowerThanOrEquals(externalTaskPriority)
            .count()

    LOG.debug("Collecting Metric Number of External Tasks with a priority lower than or equals ${externalTaskPriority}: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of External Tasks with a Priority between a Lower and Upper long value.
 * @param processEngine
 * @param engineName
 */
static void collectExternalTasksWithPriorityBetweenLowerAndUpperValue(ProcessEngine processEngine, String engineName, Logger LOG, long externalTaskPriorityLower, long externalTaskPriorityUpper){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "external_tasks_with_priority_between_${externalTaskPriorityLower}_and_${externalTaskPriorityUpper}",
            "The number of external tasks with a Priority between ${externalTaskPriorityLower} and ${externalTaskPriorityUpper}",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .priorityLowerThanOrEquals(externalTaskPriorityUpper)
            .priorityHigherThanOrEquals(externalTaskPriorityLower)
            .count()

    LOG.debug("Collecting Metric Number of External Tasks with a priority between ${externalTaskPriorityLower} and ${externalTaskPriorityUpper}: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of Active External Tasks with a specific Topic Name.
 * @param processEngine
 * @param engineName
 */
static void collectActiveExternalTasksWithTopicName(ProcessEngine processEngine, String engineName, Logger LOG, String topicName){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "active_external_tasks_topicname_${topicName}",
            "The number of active external tasks with a Topic Name ${topicName}",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .active()
            .topicName(topicName)
            .count()

    LOG.debug("Collecting Metric Number of Active External Tasks with a topic name: ${topicName}: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}

/**
 * Collect count of Active External Tasks that have most recently been locked by a given worker.
 * @param processEngine
 * @param engineName
 */
static void collectActiveExternalTasksLockedByWorkerId(ProcessEngine processEngine, String engineName, Logger LOG, String workerId){
    SimpleGaugeMetric counter = new SimpleGaugeMetric(
            "active_external_tasks_locked_by_workerid_${workerId}",
            "The number of active external tasks that have been most recently locked by worker ID ${workerId}",
            Arrays.asList("engine_name")
    );

    long count = processEngine.getExternalTaskService()
            .createExternalTaskQuery()
            .active()
            .workerId(workerId)
            .count()

    LOG.debug("Collecting Metric Number of Active External Tasks that have most recently been locked by worker ID: ${workerId}: ${count}");

    counter.setValue(count, Arrays.asList(engineName));
}






    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
     static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

         collectActiveExternalTasks(processEngine, engineName, LOG)
         collectSuspendedExternalTasks(processEngine, engineName, LOG)
         collectLockedExternalTasks(processEngine, engineName, LOG)
         collectExternalTasksWithNoRetriesLeft(processEngine, engineName, LOG)
         collectExternalTasksWithRetriesLeft(processEngine, engineName, LOG)
         collectExternalTasksNotLocked(processEngine, engineName, LOG)
         collectExternalTasksWithPriorityHigherThanOrEquals(processEngine, engineName, LOG, 51)
         collectExternalTasksWithPriorityLowerThanOrEquals(processEngine, engineName, LOG, 50)
         collectExternalTasksWithPriorityBetweenLowerAndUpperValue(processEngine, engineName, LOG, 0, 100)
//         collectActiveExternalTasksWithTopicName(processEngine, engineName, LOG, 'MyTopicName')
//         collectActiveExternalTasksLockedByWorkerId(processEngine, engineName, LOG, "MyWorkerId")
    }
