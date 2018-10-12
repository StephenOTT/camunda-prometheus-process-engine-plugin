package prometheus.customcollectors

import io.digitalstate.camunda.prometheus.collectors.SimpleGaugeMetric;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

collectAll((ProcessEngine)processEngine, (Logger)LOGGER)

    /**
     * Collect Count of message event subscriptions.
     * @param processEngine
     * @param engineName
     */
    static void collectMessageEventSubscriptionCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "message_event_subscription_count",
                "The number of message event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("message")
                .count();

        LOG.debug("Collecting Metric Number of message event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of signal event subscriptions.
     * @param processEngine
     * @param engineName
     */
    static void collectSignalEventSubscriptionCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "signal_event_subscription_count",
                "The number of signal event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("signal")
                .count();

        LOG.debug("Collecting Metric Number of signal event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of compensation event subscriptions.
     * @param processEngine
     * @param engineName
     */
    static void collectCompensationEventSubscriptionCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "compensation_event_subscription_count",
                "The number of compensation event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("compensation")
                .count();

        LOG.debug("Collecting Metric Number of compensation event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collect Count of conditional event subscriptions.
     * @param processEngine
     * @param engineName
     */
    static void collectConditionalEventSubscriptionCount(ProcessEngine processEngine, String engineName, Logger LOG){
        SimpleGaugeMetric counter = new SimpleGaugeMetric(
                "conditional_event_subscription_count",
                "The number of conditional event subscriptions.",
                Arrays.asList("engine_name")
        );

        long count = processEngine.getRuntimeService()
                .createEventSubscriptionQuery()
                .eventType("conditional")
                .count();

        LOG.debug("Collecting Metric Number of conditional event subscriptions: " + count);

        counter.setValue(count, Arrays.asList(engineName));
    }

    /**
     * Collects all collectors defined in this class.
     * @param processEngine
     */
    static void collectAll(ProcessEngine processEngine, Logger LOG){
        String engineName = processEngine.getName();

        collectMessageEventSubscriptionCount(processEngine, engineName, LOG);
        collectSignalEventSubscriptionCount(processEngine, engineName, LOG);
        collectCompensationEventSubscriptionCount(processEngine, engineName, LOG);
        collectConditionalEventSubscriptionCount(processEngine, engineName, LOG);
    }
