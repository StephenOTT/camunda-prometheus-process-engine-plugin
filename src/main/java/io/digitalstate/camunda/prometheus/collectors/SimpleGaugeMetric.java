package io.digitalstate.camunda.prometheus.collectors;

import io.prometheus.client.Gauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class SimpleGaugeMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGaugeMetric.class);
    private static HashMap<String, Gauge> gauges = new HashMap<>();
    private final String gaugeName;

    SimpleGaugeMetric(String name, String help, List<String> labelNames) {
        if (!gauges.containsKey(name)) {
            Gauge.Builder gaugeBuilder = Gauge.build()
                    .namespace("camunda")
                    .name(name)
                    .help(help);
            if (labelNames != null){
                gaugeBuilder.labelNames(labelNames.toArray(new String[0]));
            }
            Gauge gauge = gaugeBuilder.register();
            LOGGER.info("Prometheus SimpleGaugeMetric has been created: " + name);
            gauges.put(name,gauge);
        }
        gaugeName = name;
    }

    SimpleGaugeMetric(String name){
        this(name, "A basic gauge", null);
    }

    String getGaugeName(){
        return this.gaugeName;
    }

    void increment(Number incrementValue) {
        gauges.get(this.gaugeName).inc(incrementValue.doubleValue());
    }

    void increment() {
        gauges.get(this.gaugeName).inc();
    }

    void increment(Number incrementValue, List<String> labels) {
        gauges.get(this.gaugeName).labels(labels.toArray(new String[0])).inc(incrementValue.doubleValue());
    }

    void increment(List<String> labels) {
        gauges.get(this.gaugeName).labels(labels.toArray(new String[0])).inc();
    }

    void decrement(Number decrementValue) {
        gauges.get(this.gaugeName).dec(decrementValue.doubleValue());
    }

    void decrement() {
        gauges.get(this.gaugeName).dec();
    }

    void decrement(Number decrementValue, List<String> labels) {
        gauges.get(this.gaugeName).labels(labels.toArray(new String[0])).dec(decrementValue.doubleValue());
    }

    void decrement(List<String>labels) {
        gauges.get(this.gaugeName).labels(labels.toArray(new String[0])).dec();
    }

    Double getValue(List<String> labels){
        return gauges.get(this.gaugeName).labels(labels.toArray(new String[0])).get();
    }

    Double getValue(){
        return gauges.get(this.gaugeName).get();
    }

    void  setValue(Number value){
        gauges.get(this.gaugeName).set(value.doubleValue());
    }
}