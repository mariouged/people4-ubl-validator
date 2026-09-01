package eu.simplecompliance.ublvalidator.schematron;

import java.util.Collections;
import java.util.List;

/**
 * Result of a Schematron/EN16931 validation: whether the document is valid ({@code ok}) and,
 * when it is not, the list of rule violation messages (each prefixed with the failing rule id,
 * e.g. {@code [BR-13]-An Invoice shall have the Invoice total amount without VAT (BT-109).}).
 */
public record SchematronResult(boolean ok, List<String> messages) {

    public SchematronResult {
        messages = messages == null ? List.of() : List.copyOf(messages);
    }

    public static SchematronResult valid() {
        return new SchematronResult(true, Collections.emptyList());
    }

    public static SchematronResult invalid(List<String> messages) {
        return new SchematronResult(false, messages);
    }
}
