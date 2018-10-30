package io.digitalstate.camunda.grafana.annotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Grafana Annotation Reporter API allowing the connection to Grafana Rest API and the generation of Grafana Annotations using the Grafana Annotation REST API.
 */
public class GrafanaAnnotationReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrafanaAnnotationReporter.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    private URI grafanaUri;
    private String authToken;

    public GrafanaAnnotationReporter(){
        setGrafanaUri(URI.create("http://localhost:3000"));
    }

    public GrafanaAnnotationReporter(URI grafanaUri){
        setGrafanaUri(grafanaUri);
    }

    public GrafanaAnnotationReporter(URI grafanaUri, String authToken){
       setAuthToken(authToken);
       setGrafanaUri(grafanaUri);
    }


    public HttpResponse createAnnotation(GrafanaAnnotation ga) throws IOException, URISyntaxException {
        URI createAnnotationUri = new URI(getGrafanaUri().toString() + "/api/annotations");

        Request request = Request.Post(createAnnotationUri)
                .addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString())
                .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        if (getAuthToken() != null) {
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAuthToken());
        }

        String requestBody = this.objectMapper.writeValueAsString(ga);
        request.bodyString(requestBody, ContentType.APPLICATION_JSON);

        LOGGER.debug("Grafana Annotation Request JSON Body: {}", requestBody);
        LOGGER.debug("GRAFANA HTTP REQUEST: {}", request.toString());

        return request.execute().returnResponse();
    }

    //
    // Setters and Getters
    ///

    private void setGrafanaUri(URI grafanaUri) {
        this.grafanaUri = grafanaUri;
    }
    private URI getGrafanaUri() {
        return grafanaUri;
    }

    private void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    private String getAuthToken() {
        return authToken;
    }
}
