package io.digitalstate.camunda.prometheus.collectors.camunda;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.management.Metrics;
import org.camunda.bpm.engine.management.MetricsQuery;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class JobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);

    /**
     * Collects the number of jobs successfully executed.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobSuccessful(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_successful",
                "The number of jobs successfully executed.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_SUCCESSFUL)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Successful: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects the number of jobs that failed to execute and that were submitted for retry. Every failed attempt to execute a job is counted.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobFailed(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_failed",
                "The number of jobs that failed to execute and that were submitted for retry. Every failed attempt to execute a job is counted.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_FAILED)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Failed: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects the number of job acquisition cycles performed.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobAcquisitionAttempt(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_acquisition_attempt",
                "The number of job acquisition cycles performed.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_ACQUISITION_ATTEMPT)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Acquisition Attempts: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects the number of jobs that were acquired and successfully locked for execution.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobAcquiredSuccess(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_acquired_success",
                "The number of jobs that were acquired and successfully locked for execution.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_ACQUIRED_SUCCESS)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Acquired Success: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects the number of jobs that were acquired but could not be locked for execution due to another job executor locking/executing the jobs in parallel.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobAcquiredFailure(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_acquired_failure",
                "The number of jobs that were acquired but could not be locked for execution due to another job executor locking/executing the jobs in parallel.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_ACQUIRED_FAILURE)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Acquired Failure: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects the number of successfully acquired jobs submitted for execution that were rejected due to saturated execution resources. This is an indicator that the execution thread pool's job queue is full.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobExecutionRejected(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_execution_rejected",
                "The number of successfully acquired jobs submitted for execution that were rejected due to saturated execution resources. This is an indicator that the execution thread pool's job queue is full.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_EXECUTION_REJECTED)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Execution Rejected: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects the number of exclusive jobs that are immediately locked and executed.
     * @param metricsQuery
     * @param engineName
     * @param startDate
     * @param endDate
     */
    static void collectJobLockedExclusive(MetricsQuery metricsQuery, String engineName, DateTime startDate, DateTime endDate){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "metric_job_locked_exclusive",
                "The number of exclusive jobs that are immediately locked and executed.",
                Arrays.asList("engine_name")
        );

        long sum = metricsQuery.name(Metrics.JOB_LOCKED_EXCLUSIVE)
                .startDate(startDate.toDate())
                .endDate(endDate.toDate())
                .sum();

        LOGGER.debug("Collecting Metric Count for Job Locked Exclusive: " + sum);

        counter.setValue(sum, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class
     * @param processEngine
     * @param startDate
     * @param endDate
     */
    static void collect(ProcessEngine processEngine, DateTime startDate, DateTime endDate){
        MetricsQuery metricsQuery =  processEngine.getManagementService().createMetricsQuery();
        String engineName = processEngine.getName();

        collectJobSuccessful(metricsQuery, engineName, startDate, endDate);
        collectJobFailed(metricsQuery, engineName, startDate, endDate);
        collectJobAcquisitionAttempt(metricsQuery, engineName, startDate, endDate);
        collectJobAcquiredSuccess(metricsQuery, engineName, startDate, endDate);
        collectJobAcquiredFailure(metricsQuery, engineName, startDate, endDate);
        collectJobExecutionRejected(metricsQuery, engineName, startDate, endDate);
        collectJobLockedExclusive(metricsQuery, engineName, startDate, endDate);

    }

}
