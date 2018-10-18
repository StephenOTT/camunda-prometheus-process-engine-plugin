import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.history.UserOperationLogEntry;
import org.slf4j.Logger;

/**
 * User Operation Log Collectors
 */

collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

/**
 * Generic collector User Operation Log: Operation Type filter
 * @param processEngine
 * @param engineName
 */
static void collectOperationType(ProcessEngine processEngine, String engineName, Logger LOG, String operationType){
    String name = "user_operation_log_type_" + operationType;
    String help = "User Operation Log, count of instances, Type:" + operationType;

    SimpleGaugeMetric counter = new SimpleGaugeMetric(name, help,
            Arrays.asList("engine_name")
    );

    long count = processEngine.getHistoryService()
            .createUserOperationLogQuery()
            .operationType(operationType)
            .count()

    String debugStatement = "Collecting Metric Oepration User Log, Number of " + operationType + ": " + count
    LOG.debug(debugStatement);

    counter.setValue(count, Arrays.asList(engineName));

}


/**
 * Collects all collectors defined in this class.
 * @param processEngine
 */
static void collectAll(ProcessEngine processEngine, Logger LOG){
    String engineName = processEngine.getName();

    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ACTIVATE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ACTIVATE_BATCH);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ACTIVATE_JOB);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ACTIVATE_JOB_DEFINITION);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ACTIVATE_PROCESS_DEFINITION);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ADD_ATTACHMENT);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ADD_GROUP_LINK);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ADD_USER_LINK);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_ASSIGN);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_CLAIM);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_COMPLETE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_CREATE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_DELEGATE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_DELETE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_DELETE_ATTACHMENT);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_DELETE_GROUP_LINK);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_DELETE_USER_LINK);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_MIGRATE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_MODIFY_PROCESS_INSTANCE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_MODIFY_VARIABLE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_RESOLVE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_RESTART_PROCESS_INSTANCE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SET_EXTERNAL_TASK_RETRIES);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SET_JOB_RETRIES);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SET_OWNER);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SET_PRIORITY);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SET_VARIABLE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SUSPEND);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SUSPEND_BATCH);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SUSPEND_JOB);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SUSPEND_JOB_DEFINITION);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_SUSPEND_PROCESS_DEFINITION);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_UPDATE);
    collectOperationType(processEngine, engineName, LOG, UserOperationLogEntry.OPERATION_TYPE_UPDATE_HISTORY_TIME_TO_LIVE);
}