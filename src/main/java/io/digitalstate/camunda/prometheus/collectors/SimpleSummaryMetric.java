package io.digitalstate.camunda.prometheus.collectors;

import io.prometheus.client.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class SimpleSummaryMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSummaryMetric.class);

    private static HashMap<String, Summary> summaries = new HashMap<>();

    private final String summaryName;
    private Summary.Timer requestTimer;

    // Counters always start at zero when they are initialized, as per prometheus docs.
    SimpleSummaryMetric(String name, String help, List<String> labelNames) {
        if (!summaries.containsKey(name)) {
            Summary.Builder summaryBuilder = Summary.build()
                    .namespace("camunda")
                    .name(name)
                    .help(help);
            if (labelNames != null){
                summaryBuilder.labelNames(labelNames.toArray(new String[0]));
            }
            Summary summary = summaryBuilder.register();
            LOGGER.info("Prometheus SimpleSummaryMetric has been created: " + name);
            summaries.put(name,summary);
        }
        summaryName = name;
    }

    void startTimer(List<String> labels){
        Summary summary = summaries.get(this.summaryName);
        if (labels != null){
            summary.labels(labels.toArray(new String[0]));
        }
        this.requestTimer = summary.startTimer();
    }
    void startTimer(){
        Summary summary = summaries.get(this.summaryName);
        this.requestTimer = summary.startTimer();
    }

    // Stops Timer
    void observeDuration(){
        this.requestTimer.observeDuration();
    }

    void observeValue(Double observedValue, List<String> labels){
        Summary summary = summaries.get(this.summaryName);
        if (labels != null){
            summary.labels(labels.toArray(new String[0]));
        }
        summary.observe(observedValue);
    }
    void observeValue(Double observedValue){
        Summary summary = summaries.get(this.summaryName);
        summary.observe(observedValue);
    }

    SimpleSummaryMetric(String name){
        this(name, "A basic summary", null);
    }

    String getSummaryName(){
        return this.summaryName;
    }

    Summary.Child.Value getValue(){
        return summaries.get(this.summaryName).get();
    }

    Summary.Child.Value getValue(List<String> labels){
        return summaries.get(this.summaryName).labels(labels.toArray(new String[0])).get();
    }
}