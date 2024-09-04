package ru.dashko.selsuptask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.AccessException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@NoArgsConstructor
public class SelsupTaskApplication {

    public static  TimeUnit timeUnit;
    public static int counter;
    public static int buf;
    public static Long start ;
    public static Long time ;


    public SelsupTaskApplication(TimeUnit timeUnit, int counter) {
        SelsupTaskApplication.timeUnit = timeUnit;
        SelsupTaskApplication.counter = counter;
        start = System.currentTimeMillis();
        time = Duration.ofHours(timeUnit.getHOUR()).toMillis()+Duration.ofMinutes(timeUnit.getMINUTE()).toMillis()+Duration.ofSeconds(timeUnit.getSECOND()).toMillis();
        buf = counter;
    }

    public static void main(String[] args) {
        SelsupTaskApplication apiApplication = new SelsupTaskApplication(new TimeUnit(30L,0L,0L),5);
        SpringApplication.run(SelsupTaskApplication.class, args);

    }
    public static Optional<Document>createDocument(Document document,String signature) {
        return Optional.of(document);
    }

}
@RestController
@RequestMapping("/api/v3/lk/documents")
@Scope("prototype")
class CrptController {
    @PostMapping("/create")
    public Optional<?> create(@RequestBody Document document) throws AccessException {
        if (System.currentTimeMillis() - SelsupTaskApplication.start >= SelsupTaskApplication.time) {
            SelsupTaskApplication.start = System.currentTimeMillis();
            SelsupTaskApplication.buf = SelsupTaskApplication.counter;
        }
        if (SelsupTaskApplication.buf <= 0 && System.currentTimeMillis() - SelsupTaskApplication.start < SelsupTaskApplication.time)
            throw new AccessException("number of requests exceeded");
        else {
            SelsupTaskApplication.buf--;
            //Не понятно как использовать подпись
            return SelsupTaskApplication.createDocument(document, "signature");
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
class Document {

    @JsonProperty("certificate_document")
    private Description description;

    @JsonProperty("doc_id")
    private String docId;

    @JsonProperty("doc_status")
    private String docStatus;

    @JsonProperty("doc_type")
    private String docType;

    @JsonProperty("import_request")
    private boolean importRequest;

    @JsonProperty("owner_inn")
    private String ownerInn;

    @JsonProperty("participant_inn")
    private String participantInn;

    @JsonProperty("producer_inn")
    private String producerInn;

    @JsonProperty("production_date")
    private String productionDate;

    @JsonProperty("production_type")
    private String productionType;

    @JsonProperty("products")
    private List<Product> products;

    @JsonProperty("reg_date")
    private String regDate;

    @JsonProperty("reg_number")
    private String regNumber;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Description {
    private String participantInn;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Product {
    @JsonProperty("certificate_document")
    private String certificateDocument;

    @JsonProperty("certificate_document_date")
    private LocalDate certificateDocumentDate;

    @JsonProperty("certificate_document_number")
    private String certificateDocumentNumber;

    @JsonProperty("owner_inn")
    private String ownerInn;

    @JsonProperty("producer_inn")
    private String producerInn;

    @JsonProperty("production_date")
    private LocalDate productionDate;

    @JsonProperty("tnved_code")
    private String tnvedCode;

    @JsonProperty("uit_code")
    private String uitCode;

    @JsonProperty("uitu_code")
    private String uituCode;
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class TimeUnit {
    private Long SECOND, MINUTE, HOUR;
}