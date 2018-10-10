import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;


collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect count of users.
     * @param processEngine
     * @param engineName
     */
    static void collectUserCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "identity_service_user_count",
                "The count of users in the Identity Service",
                Arrays.asList("engine_name"));

        long count = processEngine.getIdentityService().createUserQuery().count();

        LOG.debug("Collecting Metric Number of Users in Identity Service: " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect count of groups.
     * @param processEngine
     * @param engineName
     */
    static void collectGroupCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "identity_service_group_count",
                "The count of groups in the Identity Service",
                Arrays.asList("engine_name"));

        long count = processEngine.getIdentityService().createGroupQuery().count();

        LOG.debug("Collecting Metric Number of Groups in Identity Service: " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect count of tenants.
     * @param processEngine
     * @param engineName
     */
    static void collectTenantCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "identity_service_tenant_count",
                "The count of tenants in the Identity Service",
                Arrays.asList("engine_name"));

        long count = processEngine.getIdentityService().createUserQuery().count();

        LOG.debug("Collecting Metric Number of Tenants in Identity Service: " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectUserCount(processEngine, engineName, LOG);
        collectGroupCount(processEngine, engineName, LOG);
        collectTenantCount(processEngine, engineName, LOG);

        /*
         * @TODO: Other Queries to Create:
         * 1. Count of User Per Tenant
         * 2. Count of Groups per tenant
         */

    }
