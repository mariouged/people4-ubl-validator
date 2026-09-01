package eu.simplecompliance.ublvalidator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UblValidator {

    @PostMapping("/ubl-validator")
    public ResponseEntity<String> health(
        @ModelAttribute InvoiceRequest invoiceReq
    ) {

        return ResponseEntity.ok(
        "WIP " + invoiceReq.getLegalName() + "." + "\n"
        );
    }
}
