package eu.simplecompliance.ublvalidator.schematron;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.resource.inmemory.ReadableResourceByteArray;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.sch.SchematronResourceSCH;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;

/**
 * Validates UBL 2.1 EN16931 (Euro Invoice) XML documents against the ISO Schematron rules
 * published in the
 * <a href="https://github.com/ConnectingEurope/eInvoicing-EN16931">eInvoicing-EN16931</a>
 * repository (UBL model + syntax + code list rules, pre-processed into a single {@code .sch}
 * file bundled on the classpath).
 */
@Service
public class SchematronService {

    private static final Logger LOG = LoggerFactory.getLogger(SchematronService.class);

    private static final String SCHEMATRON_CLASSPATH_LOCATION =
            "schematron/EN16931-UBL-validation-preprocessed.sch";

    /**
     * The compiled Schematron. Building the underlying XSLT from the (large) EN16931 rule set is
     * expensive, so it is done once and reused. {@link SchematronResourceSCH} is not thread-safe,
     * so access to it is synchronized in {@link #validate(byte[])}.
     */
    private final ISchematronResource schematron;

    public SchematronService() {
        this.schematron = SchematronResourceSCH.fromClassPath(SCHEMATRON_CLASSPATH_LOCATION);
        if (!schematron.isValidSchematron()) {
            throw new IllegalStateException(
                    "Invalid Schematron resource on classpath: " + SCHEMATRON_CLASSPATH_LOCATION);
        }
    }

    /**
     * Validates the given XML content against the EN16931 UBL Schematron rules.
     *
     * @param xml the raw bytes of the XML document to validate (e.g. an UBL Invoice or CreditNote)
     * @return the validation result: {@code ok=true} and no messages if the document complies with
     *         all rules, otherwise {@code ok=false} and the list of violated rule messages, each
     *         one starting with the failing rule id, e.g.
     *         {@code [BR-13]-An Invoice shall have the Invoice total amount without VAT (BT-109).}
     * @throws SchematronValidationException if the XML cannot be parsed/validated (e.g. it is not
     *         well-formed XML)
     */
    public synchronized SchematronResult validate(byte[] xml) {
        if (xml == null || xml.length == 0) {
            throw new SchematronValidationException("The XML content to validate must not be empty");
        }

        final IReadableResource xmlResource = new ReadableResourceByteArray("xml-to-validate", xml);

        final SchematronOutputType output;
        try {
            output = schematron.applySchematronValidationToSVRL(xmlResource);
        } catch (Exception e) {
            throw new SchematronValidationException(
                    "Unable to validate the supplied XML: " + e.getMessage(), e);
        }

        if (output == null) {
            throw new SchematronValidationException(
                    "Unable to validate the supplied XML: it could not be parsed as XML");
        }

        final List<String> messages = SVRLHelper.getAllFailedAssertions(output)
                .stream()
                .map(failedAssert -> failedAssert.getText())
                .collect(Collectors.toList());

        if (!messages.isEmpty()) {
            LOG.debug("Schematron validation produced {} violation(s)", messages.size());
        }

        return messages.isEmpty() ? SchematronResult.valid() : SchematronResult.invalid(messages);
    }
}
