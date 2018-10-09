package io.digitalstate.camunda.prometheus.collectors.custom;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class IdentityServiceMetrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServiceMetrics.class);

    /**
     * Collect count of users.
     * @param processEngine
     * @param engineName
     */
    public static void collectUserCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "identity_service_user_count",
                "The count of users in the Identity Service",
                Arrays.asList("engine_name"));

        long count = processEngine.getIdentityService().createUserQuery().count();

        LOGGER.debug("Collecting Metric Number of Users in Identity Service: " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect count of groups.
     * @param processEngine
     * @param engineName
     */
    public static void collectGroupCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "identity_service_group_count",
                "The count of groups in the Identity Service",
                Arrays.asList("engine_name"));

        long count = processEngine.getIdentityService().createGroupQuery().count();

        LOGGER.debug("Collecting Metric Number of Groups in Identity Service: " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect count of tenants.
     * @param processEngine
     * @param engineName
     */
    public static void collectTenantCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric gauge = new SimpleGaugeMetric(
                "identity_service_tenant_count",
                "The count of tenants in the Identity Service",
                Arrays.asList("engine_name"));

        long count = processEngine.getIdentityService().createUserQuery().count();

        LOGGER.debug("Collecting Metric Number of Tenants in Identity Service: " + count);

        gauge.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    public static void collectAll(ProcessEngine processEngine){
        String engineName = processEngine.getName();

        collectUserCount(processEngine, engineName);
        collectGroupCount(processEngine, engineName );
        collectTenantCount(processEngine, engineName );

        /*
         * @TODO: Other Queries to Create:
         * 1. Count of User Per Tenant
         * 2. Count of Groups per tenant
         */

    }

}
