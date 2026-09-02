package eu.simplecompliance.ublvalidator.schematron;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class XmlSchemaValidator {

    private static final Logger LOG = LoggerFactory.getLogger(XmlSchemaValidator.class);

    public XmlSchemaValidator() {}

    /**
     * Validate the ubl xml with schema
     * @see https://docs.oasis-open.org/ubl/os-UBL-2.1/xsd/maindoc/UBL-Invoice-2.1.xsd
     */
    public XmlSchemaResult validateSchema(
            String xmlFilePath,
            String schemaFilePath
    ) {
        // inputs
        File xmlFile = new File(xmlFilePath);
        File xsdFile = new File(schemaFilePath);
        XmlSchemaResult result = new XmlSchemaResult(false, Collections.emptyList());
        List<String> messages = new ArrayList<String>();
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
        } catch (SAXException e) {
            messages.add("XML ubl is invalid. " + e.getMessage());
        } catch (IOException e) {
            LOG.error("Error fatal on validator XML: " + e.getMessage());
            messages.add("Error fatal");
        }
        if (messages.isEmpty()) {
            // OK
            result = new XmlSchemaResult(true, Collections.emptyList());
        } else {
            // FAIL
            result = new XmlSchemaResult(false, messages);
        }
        return result;
    }
}