package io.digitalstate.camunda.prometheus.parselisteners;

import org.camunda.bpm.engine.impl.util.xml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

class ParsingHelpers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParsingHelpers.class);

    private static ExpressionParser parser = new SpelExpressionParser();
    private static SimpleEvaluationContext context = new SimpleEvaluationContext.Builder().build();

    /**
     * Gets the Camunda Extension Properties from a BPMN element and converts it into a MultiValueMap.
     * The MultiValueMap allows for multiple duplicate keys in the Camunda Extension Properties.
     * @param element Element: BPMN Element
     * @param propertyNameStartsWith String of Property key to look for.  Default is 'prometheus.'
     * @return MultiValueMap<String,String>
     */
    static MultiValueMap<String,String> getCamundaExtensionPropertiesThatStartWith(Element element, String propertyNameStartsWith){
        MultiValueMap<String,String> extensionProperties = new LinkedMultiValueMap<>();
        try{
            // @TODO rebuild this try/cactch so the properties variable is not handled through error handling...
            List<Element> properties = element.element("extensionElements").element("properties").elements("property");

            properties.forEach(property -> {
                String name = property.attribute("name");
                String value = property.attribute("value");

                if (property.attribute("name").startsWith(propertyNameStartsWith)){
                    extensionProperties.add(name, value);
                }
            });
            return extensionProperties;

        } catch (Exception e){
            // Did not find any properties, so return empty map
            LOGGER.debug("Activity Duration Tracking: No Camunda Extension Element properties were found");

            return extensionProperties;
        }
    }

    /**
     * Checks if the MultiValueMap of camunda extension properties has a "prometheus.track" key
     * @param properties MultiValueMap<String,String>
     * @return boolean
     */
    static Boolean hasTrack(MultiValueMap<String,String> properties){
        return !properties.isEmpty() &&
                properties.containsKey("prometheus.track");
    }

    /**
     * Eval a string into a Map.
     * Used for Evaluating JSON-like strings that turn into Maps.
     * Used for evaluating property values in Camunda Extension Properties.
     * @param propertyValue
     * @return
     */
    static Map evalProperty(String propertyValue){
        Map parsedProperty = parser.parseExpression(propertyValue).getValue(context, Map.class);
        LOGGER.debug("Prometheus Property Value Evaluation result: {}", parsedProperty);
        return parsedProperty;
    }
}
