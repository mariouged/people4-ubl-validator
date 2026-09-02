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
        byte[] xmlBytes,
        String schemaFilePath
    ) {
        List<String> messages = new ArrayList<String>();
        try {
            // inputs
            String xmlFilePath = this.fileWrite(xmlBytes);
            File xmlFile = new File(xmlFilePath);
            File xsdFile = new File(schemaFilePath);
            // validate xml with his xsd
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
        // result
        boolean ok = messages.isEmpty();
        List<String> errors = ok ? Collections.emptyList() : messages;
        return new XmlSchemaResult(ok, messages);
    }

    private String fileWrite(byte[] xmlBytes) throws IOException {
        // TODO move resources/xml-inputs to a data file system
        // TODO generate a tmp file name
        String tmpFileName = "tmpSha256.xml";
        String filePath = "xml-inputs/" + tmpFileName;
        FileHelper helper = new FileHelper(filePath);
        helper.write(xmlBytes);
        return filePath;
    }
}