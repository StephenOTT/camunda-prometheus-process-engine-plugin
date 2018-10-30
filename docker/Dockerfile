FROM camunda/camunda-bpm-platform:tomcat-7.9.0
RUN rm -r webapps/camunda-invoice
# add custom configurations
COPY docker/camunda/conf/ /camunda/conf

# Copy third-party Java libraries
COPY docker/camunda/lib/camunda-prometheus-process-engine-plugin-v1.6.0-jar-with-dependencies.jar /camunda/lib/camunda-prometheus-process-engine-plugin-v1.6.0-jar-with-dependencies.jar