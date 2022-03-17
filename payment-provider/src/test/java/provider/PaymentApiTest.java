package provider;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PaymentApiTest {
    @LocalServerPort
    int port;

    File spec = new File("oas/swagger.yml");

    private final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(spec.getAbsolutePath());

    @Test
    public void testGetPayment_forValidOrderId_shouldYieldHttp200() {
        given()
                .port(port)
                .filter(validationFilter)
                .when()
                .get("/payment/228aa55c-393c-411b-9410-4a995480e78e")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGetPayment_forNonexistentOrderId_shouldYieldHttp404() {
        given()
                .port(port)
                .filter(validationFilter)
                .when()
                .get("/payment/00000000-0000-0000-0000-000000000000")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetPayment_forIncorrectlyFormattedOrderId_shouldYieldHttp400() {
        given()
                .port(port)
                .filter(validationFilter)
                .when()
                .get("/payment/this_is_not_a_valid_payment_id")
                .then()
                .assertThat()
                .statusCode(400);
    }
}