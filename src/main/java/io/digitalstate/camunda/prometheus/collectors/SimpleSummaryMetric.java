package io.digitalstate.camunda.prometheus.collectors;

import io.prometheus.client.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleSummaryMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSummaryMetric.class);

    private static ConcurrentMap<String, Summary> summaries = new ConcurrentHashMap<>();

    private final String summaryName;
    private Summary.Timer requestTimer;

    // Counters always start at zero when they are initialized, as per prometheus docs.
    public SimpleSummaryMetric(String name, String help, List<String> labelNames) {
        summaries.computeIfAbsent(name, k -> {
            Summary.Builder summaryBuilder = Summary.build()
                    .namespace("camunda")
                    .name(name)
                    .help(help);
            if (labelNames != null){
                summaryBuilder.labelNames(labelNames.toArray(new String[0]));
            }
            Summary summary = summaryBuilder.register();
            LOGGER.info("Prometheus SimpleSummaryMetric has been created: " + name);
            return summary;
        });

        summaryName = name;
    }

    public void startTimer(List<String> labels){
        Summary summary = summaries.get(this.summaryName);
        if (labels != null){
            summary.labels(labels.toArray(new String[0]));
        }
        this.requestTimer = summary.startTimer();
    }
    public void startTimer(){
        Summary summary = summaries.get(this.summaryName);
        this.requestTimer = summary.startTimer();
    }

    // Stops Timer
    public void observeDuration(){
        this.requestTimer.observeDuration();
    }

    public void observeValue(Double observedValue, List<String> labels){
        Summary summary = summaries.get(this.summaryName);
        summary.labels(labels.toArray(new String[0])).observe(observedValue);
    }
    public void observeValue(Double observedValue){
        Summary summary = summaries.get(this.summaryName);
        summary.observe(observedValue);
    }

    public SimpleSummaryMetric(String name){
        this(name, "A basic summary", null);
    }

    public String getSummaryName(){
        return this.summaryName;
    }

    public Summary.Child.Value getValue(){
        return summaries.get(this.summaryName).get();
    }

    public Summary.Child.Value getValue(List<String> labels){
        return summaries.get(this.summaryName).labels(labels.toArray(new String[0])).get();
    }
}