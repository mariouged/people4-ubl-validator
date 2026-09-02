package eu.simplecompliance.ublvalidator.web;

import java.util.ArrayList;
import java.util.List;

import eu.simplecompliance.ublvalidator.schematron.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the UBL 2.1 EN16931 (Euro Invoice) Schematron validation as an HTTP endpoint.
 */
@RestController
public class SchematronController {

    private final SchematronService schematronService;

    public SchematronController(SchematronService schematronService) {
        this.schematronService = schematronService;
    }

    /**
     * Validates the XML document sent in the request body against the EN16931 UBL Schematron
     * rules.
     *
     * @param xml the raw XML document (e.g. an UBL Invoice), sent as the request body
     * @return {@code {"ok": true, "messages": []}} when the document is valid, otherwise
     *         {@code {"ok": false, "messages": ["[BR-13]-..."]}}
     */
    @PostMapping(value = "/schematron", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchematronResult> schematron(@RequestBody byte[] xml) {
        /*XmlSchemaValidator xsdValidator = new XmlSchemaValidator();
        // TODO write bytes xml to file
        XmlSchemaResult xsdResult = xsdValidator.validateSchema(
                "data/ubl-file.xml", // Mock
                "schema/UBL-Invoice-2.1.xsd"
        );*/
        SchematronResult rulesResult = schematronService.validateSchematron(xml);
        // if (xsdResult.ok() && rulesResult.ok()) {
        if (rulesResult.ok()) {
            return ResponseEntity.ok(rulesResult);
        } else {
            List<String> errors = new ArrayList<String>();
            // errors.addAll(xsdResult.messages());
            errors.addAll(rulesResult.messages());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(SchematronResult.invalid(errors));
        }
    }

    @ExceptionHandler(SchematronValidationException.class)
    public ResponseEntity<SchematronResult> handleValidationError(SchematronValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SchematronResult.invalid(List.of(ex.getMessage())));
    }
}
