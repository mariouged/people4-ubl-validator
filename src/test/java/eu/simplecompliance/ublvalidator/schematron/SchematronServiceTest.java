package eu.simplecompliance.ublvalidator.schematron;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class SchematronServiceTest {

    private SchematronService schematronService;

    @BeforeEach
    void setUp() {
        schematronService = new SchematronService();
    }

    @Test
    void validSampleInvoiceIsAccepted() throws IOException {
        SchematronResult result = schematronService.validate(loadSampleInvoice());

        assertThat(result.ok()).isTrue();
        assertThat(result.messages()).isEmpty();
    }

    @Test
    void malformedXmlFailsWithoutMessagesButRaisesException() {
        byte[] malformedXml = "<Invoice><unclosed></Invoice>".getBytes(StandardCharsets.UTF_8);

        assertThrows(SchematronValidationException.class, () -> schematronService.validate(malformedXml));
    }

    @Test
    void invoiceViolatingBusinessRulesIsRejectedWithRuleMessages() throws IOException {
        String withoutTotalAmount = new String(loadSampleInvoice(), StandardCharsets.UTF_8)
                .replaceAll("<cbc:TaxExclusiveAmount[^>]*>[^<]*</cbc:TaxExclusiveAmount>", "");

        SchematronResult result = schematronService.validate(withoutTotalAmount.getBytes(StandardCharsets.UTF_8));

        assertThat(result.ok()).isFalse();
        assertThat(result.messages()).isNotEmpty();
        assertThat(result.messages()).allSatisfy(message -> assertThat(message).matches("^\\[[A-Za-z0-9-]+]-.*"));
        assertThat(result.messages()).anySatisfy(message -> assertThat(message).contains("BR-13"));
    }

    @Test
    void emptyXmlIsRejected() {
        assertThrows(SchematronValidationException.class, () -> schematronService.validate(new byte[0]));
        assertThrows(SchematronValidationException.class, () -> schematronService.validate(null));
    }

    private byte[] loadSampleInvoice() throws IOException {
        try (InputStream in = new ClassPathResource("ubl-tc434-example3.xml").getInputStream()) {
            return in.readAllBytes();
        }
    }
}
