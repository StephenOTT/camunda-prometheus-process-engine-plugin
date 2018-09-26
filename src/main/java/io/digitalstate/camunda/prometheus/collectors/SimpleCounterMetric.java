package io.digitalstate.camunda.prometheus.collectors;

import io.prometheus.client.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class SimpleCounterMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCounterMetric.class);
    private static HashMap<String, Counter> counters = new HashMap<>();
    private final String counterName;

    // Counters always start at zero when they are initialized, as per prometheus docs.
    SimpleCounterMetric(String name, String help, List<String> labelNames) {
        if (!counters.containsKey(name)) {
            Counter.Builder counterBuilder = Counter.build()
                    .namespace("camunda")
                    .name(name)
                    .help(help);
            if (labelNames != null){
                counterBuilder.labelNames(labelNames.toArray(new String[0]));
            }
            Counter counter = counterBuilder.register();
            LOGGER.info("Prometheus SimpleCounterMetric has been created: " + name);
            counters.put(name,counter);
        }
        counterName = name;
    }

    SimpleCounterMetric(String name){
        this(name, "A basic counter", null);
    }

    String getCounterName(){
        return this.counterName;
    }

    void increment(Number incrementValue) {
        counters.get(this.counterName).inc(incrementValue.doubleValue());
    }
    void increment() {
        counters.get(this.counterName).inc();
    }
    void increment(Number incrementValue, List<String> labels) {
        counters.get(this.counterName).labels(labels.toArray(new String[0])).inc(incrementValue.doubleValue());
    }
    void increment(List<String> labels) {
        counters.get(this.counterName).labels(labels.toArray(new String[0])).inc();
    }

    Double getValue(){
        return counters.get(this.counterName).get();
    }

    Double getValue(List<String> labels){
        return counters.get(this.counterName).labels(labels.toArray(new String[0])).get();
    }
}