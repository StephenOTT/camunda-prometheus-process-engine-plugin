package io.digitalstate.camunda.prometheus.collectors.custom;

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class EventsMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventsMetrics.class);

    /**
     * Collect Count of message event subscriptions.
     * @param processEngine
     * @param engineName
     */
    public static void collectMessageEventSubscriptionCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "message_event_subscription_count",
                "The number of message event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("message")
                .count();

        LOGGER.debug("Collecting Metric Number of message event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of signal event subscriptions.
     * @param processEngine
     * @param engineName
     */
    public static void collectSignalEventSubscriptionCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "signal_event_subscription_count",
                "The number of signal event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("signal")
                .count();

        LOGGER.debug("Collecting Metric Number of signal event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of compensation event subscriptions.
     * @param processEngine
     * @param engineName
     */
    public static void collectCompensationEventSubscriptionCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "compensation_event_subscription_count",
                "The number of compensation event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("compensation")
                .count();

        LOGGER.debug("Collecting Metric Number of compensation event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of conditional event subscriptions.
     * @param processEngine
     * @param engineName
     */
    public static void collectConditionalEventSubscriptionCount(ProcessEngine processEngine, String engineName){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "conditional_event_subscription_count",
                "The number of conditional event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("conditional")
                .count();

        LOGGER.debug("Collecting Metric Number of conditional event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    public static void collectAll(ProcessEngine processEngine){
        String engineName = processEngine.getName();

        collectMessageEventSubscriptionCount(processEngine, engineName);
        collectSignalEventSubscriptionCount(processEngine, engineName);
        collectCompensationEventSubscriptionCount(processEngine, engineName);
        collectConditionalEventSubscriptionCount(processEngine, engineName);
    }
}
