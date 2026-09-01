package eu.simplecompliance.ublvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class UblValidatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UblValidatorApplication.class, args);
    }

    @GetMapping("/")
    String home() {
        return "UBL validator";
    }

    @GetMapping("/health")
    String health() {
        return "OK";
    }
}
