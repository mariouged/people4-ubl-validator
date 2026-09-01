package eu.simplecompliance.ublvalidator.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.simplecompliance.ublvalidator.schematron.SchematronResult;
import eu.simplecompliance.ublvalidator.schematron.SchematronService;
import eu.simplecompliance.ublvalidator.schematron.SchematronValidationException;

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
        return ResponseEntity.ok(schematronService.validate(xml));
    }

    @ExceptionHandler(SchematronValidationException.class)
    public ResponseEntity<SchematronResult> handleValidationError(SchematronValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SchematronResult.invalid(List.of(ex.getMessage())));
    }
}
