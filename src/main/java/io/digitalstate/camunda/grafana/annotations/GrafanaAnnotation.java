package io.digitalstate.camunda.grafana.annotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.camunda.bpm.engine.impl.calendar.DateTimeUtil;

import java.util.List;

/**
 * Defines object structure of a Grafana Annotation that is sent to the Grafana Rest API as a JSON Object using Jackson ObjectMapper
 */
public class GrafanaAnnotation {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer dashboardId = null;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer panelId = null;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private long time = DateTimeUtil.now().getMillis();

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private boolean isRegion = false;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private long timeEnd = DateTimeUtil.now().getMillis();

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private List<String> tags;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String text;

    //
    // Setters and Getters
    //

    public void setDashboardId(Integer dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Integer getDashboardId() {
        return dashboardId;
    }

    public void setPanelId(Integer panelId) {
        this.panelId = panelId;
    }

    public Integer getPanelId() {
        return panelId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setRegion(boolean region) {
        isRegion = region;
    }

    public boolean isRegion() {
        return isRegion;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
