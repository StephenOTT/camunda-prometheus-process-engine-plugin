package io.digitalstate.camunda.prometheus.collectors;

import io.prometheus.client.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleCounterMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCounterMetric.class);
    private static ConcurrentMap<String, Counter> counters = new ConcurrentHashMap<>();
    private final String counterName;

    // Counters always start at zero when they are initialized, as per prometheus docs.
    public SimpleCounterMetric(String name, String help, List<String> labelNames) {
        counters.computeIfAbsent(name, key ->{
            Counter.Builder counterBuilder = Counter.build()
                    .namespace("camunda")
                    .name(name)
                    .help(help);
            if (labelNames != null){
                counterBuilder.labelNames(labelNames.toArray(new String[0]));
            }
            Counter counter = counterBuilder.register();
            LOGGER.info("Prometheus SimpleCounterMetric has been created: " + name);
            return counter;
        });
        counterName = name;
    }

    public SimpleCounterMetric(String name){
        this(name, "A basic counter", null);
    }

    public String getCounterName(){
        return this.counterName;
    }

    public void increment(Number incrementValue) {
        counters.get(this.counterName).inc(incrementValue.doubleValue());
    }
    public void increment() {
        counters.get(this.counterName).inc();
    }
    public void increment(Number incrementValue, List<String> labels) {
        counters.get(this.counterName).labels(labels.toArray(new String[0])).inc(incrementValue.doubleValue());
    }
    public void increment(List<String> labels) {
        counters.get(this.counterName).labels(labels.toArray(new String[0])).inc();
    }

    public Double getValue(){
        return counters.get(this.counterName).get();
    }

    public Double getValue(List<String> labels){
        return counters.get(this.counterName).labels(labels.toArray(new String[0])).get();
    }

    public void setValue(double value){
        counters.get(this.counterName);
    }

}