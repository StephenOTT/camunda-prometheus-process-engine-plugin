package io.digitalstate.camunda.grafana.annotations.reporters;

import io.digitalstate.camunda.grafana.annotations.GrafanaAnnotation;
import io.digitalstate.camunda.grafana.annotations.GrafanaAnnotationReporter;
import org.apache.http.HttpResponse;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.TransactionState;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class DeploymentReporterParseListener extends AbstractBpmnParseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentReporterParseListener.class);
    private GrafanaAnnotationReporter grafanaAnnotationReporter;

    public DeploymentReporterParseListener(URI grafanaUri, String authToken){
        setGrafanaAnnotationReporter(new GrafanaAnnotationReporter(grafanaUri, authToken));
    }
    public DeploymentReporterParseListener(URI grafanaUri){
        setGrafanaAnnotationReporter(new GrafanaAnnotationReporter(grafanaUri));
    }
    public DeploymentReporterParseListener(){
        setGrafanaAnnotationReporter(new GrafanaAnnotationReporter());
    }


    @Override
    public void parseProcess(Element element, ProcessDefinitionEntity processDefinition) {
        Context.getCommandContext().getTransactionContext()
                .addTransactionListener(TransactionState.COMMITTED, commandContext -> {
                    GrafanaAnnotation annotation = new GrafanaAnnotation();
                    String annotationText = String.format("Camunda Deployment(BPMN Process): <br>Key: <strong>%s</strong> <br>Id: <strong>%s</strong> <br>Version: <strong>%s</strong>",
                            processDefinition.getKey(), processDefinition.getId(), processDefinition.getVersion());

                    String engineNameTag = "engine:" + commandContext.getProcessEngineConfiguration().getProcessEngineName();
                    String processKeyTag = "processDefKey:" + processDefinition.getKey();

                    annotation.setText(annotationText);
                    annotation.setTags(Arrays.asList("camunda", "bpmn", "deployment", engineNameTag, processKeyTag));

                    try {
                        HttpResponse response = getGrafanaAnnotationReporter().createAnnotation(annotation);
                        if (response.getStatusLine().getStatusCode() == 200){
                            LOGGER.info("Grafana Deployment Annotation has been created for: {}", processDefinition.getKey());
                        } else {
                            LOGGER.error("Did not receive status code 200 from Grafana Annotation: {}",
                                    FileCopyUtils.copyToString(new InputStreamReader(response.getEntity().getContent())));
                        }
                    } catch (IOException e) {
                        LOGGER.error("Unable to Create Camunda Deployment Grafana Annotation due to IO:", e);
                    } catch (URISyntaxException e) {
                        LOGGER.error("Unable to Create Camunda Deployment Grafana Annotation due to URI:", e);
                    }
                });
    }

    //
    // Setters and Getters
    //

    private void setGrafanaAnnotationReporter(GrafanaAnnotationReporter grafanaAnnotationReporter) {
        this.grafanaAnnotationReporter = grafanaAnnotationReporter;
    }
    private GrafanaAnnotationReporter getGrafanaAnnotationReporter() {
        return grafanaAnnotationReporter;
    }
}
