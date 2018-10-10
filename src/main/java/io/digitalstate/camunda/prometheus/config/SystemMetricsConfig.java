package io.digitalstate.camunda.prometheus.config;

import org.camunda.bpm.engine.impl.calendar.DateTimeUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemMetricsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemMetricsConfig.class);

    private Class<?> collector;
    private Boolean enable;
    private DateTime startDate;
    private DateTime endDate;
    private long startDelay;
    private long frequency;

    public void setCollector(String className) {
        try {
            this.collector = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Cannot find class: " + className, e);
        }
    }

    public Class<?> getCollector() {
        return collector;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setStartDate(String startDate) {
        if (startDate.equals("now")){
            this.startDate = DateTimeUtil.now();
        } else {
            this.startDate = DateTimeUtil.parseDateTime(startDate);
        }
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        if (endDate.equals("now")){
            // `now` will be constructed as being 100 years in the future to ensure all current dates are selected.
            // This is only being set on the End Date to deal with how End Dates are usually being used.
            this.endDate = DateTimeUtil.now().plusYears(100);
        } else {
            this.endDate = DateTimeUtil.parseDateTime(endDate);
        }
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setStartDelay(long startDelay) {
        this.startDelay = startDelay;
    }

    public long getStartDelay() {
        return startDelay;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public long getFrequency() {
        return frequency;
    }
}
