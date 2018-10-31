package io.digitalstate.camunda.prometheus;

public class PrometheusHelpers {

    /**
     * Cleans a string to remove characters that can be "unapproved" by prometheus and replaced with underscore.
     * Metric names must follow: [a-zA-Z_:][a-zA-Z0-9_:]*
     * and Labels much follow: [a-zA-Z_][a-zA-Z0-9_]*
     * So we just use a replaceAll with [^a-zA-Z0-9_] to remove all "generally" bad characters
     * mainly colons and hyphens are most common issues because of Camunda UUIDs and the general pattern of "IDs" in Camunda..
     * @param textToClean
     * @return
     */
    public static String promClean(String textToClean){
        final String regex = "[^a-zA-Z0-9_]";
        return textToClean.replaceAll(regex, "_");
    }
}
