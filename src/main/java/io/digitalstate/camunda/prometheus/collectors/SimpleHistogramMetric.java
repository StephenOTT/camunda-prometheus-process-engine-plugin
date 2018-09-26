package io.digitalstate.camunda.prometheus.collectors;

import io.prometheus.client.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleHistogramMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHistogramMetric.class);

    private static HashMap<String, Histogram> histograms = new HashMap<>();

    private final String histogramName;
    private Histogram.Timer requestTimer;

    SimpleHistogramMetric(String name, String help, List<Double> buckets, List<String> labelNames) {
        if (!histograms.containsKey(name)) {
            Histogram.Builder summaryBuilder = Histogram.build()
                    .namespace("camunda")
                    .name(name)
                    .help(help);
            if (buckets != null){
                summaryBuilder.buckets(buckets.stream().mapToDouble(Double::doubleValue).toArray());
            }
            if (labelNames != null){
                summaryBuilder.labelNames(labelNames.toArray(new String[0]));
            }
            Histogram summary = summaryBuilder.register();
            LOGGER.info("Prometheus SimpleHistogramMetric has been created: " + name);
            histograms.put(name,summary);
        }
        histogramName = name;
    }

    SimpleHistogramMetric(String name){
        this(name, "A basic histogram", null, null);
    }

    void startTimer(List<String> labels){
        this.requestTimer = histograms.get(this.histogramName)
                .labels(labels.toArray(new String[0]))
                .startTimer();
    }
    void startTimer(){
        Histogram histogram = histograms.get(this.histogramName);
        this.requestTimer = histogram.startTimer();
    }

    // Stops Timer
    void observeDuration(){
        this.requestTimer.observeDuration();
    }

    void observeValue(Number observedValue, List<String> labels){
        Histogram histogram = histograms.get(this.histogramName);
        if (labels != null){
            histogram.labels(labels.toArray(new String[0]));
        }
        histogram.observe(observedValue.doubleValue());
    }
    void observeValue(Number observedValue){
        Histogram histogram = histograms.get(this.histogramName);
        histogram.observe(observedValue.doubleValue());
    }

    String getHistogramName(){
        return this.histogramName;
    }

}