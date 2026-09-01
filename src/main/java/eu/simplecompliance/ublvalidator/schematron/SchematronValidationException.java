package eu.simplecompliance.ublvalidator.schematron;

/**
 * Thrown when the supplied XML could not be validated against the Schematron rules, e.g. because
 * it is not well-formed XML.
 */
public class SchematronValidationException extends RuntimeException {

    public SchematronValidationException(String message) {
        super(message);
    }

    public SchematronValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
