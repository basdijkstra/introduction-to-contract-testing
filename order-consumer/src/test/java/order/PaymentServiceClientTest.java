package order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import com.github.tomakehurst.wiremock.common.Metadata;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactApi;
import se.bjurr.wiremockpact.wiremockpactlib.api.WireMockPactConfig;
import se.bjurr.wiremockpact.wiremockpactlib.api.model.WireMockPactMetadata;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
@ContextConfiguration(initializers = { WireMockInitialiser.class })
@TestInstance(Lifecycle.PER_CLASS)
public class PaymentServiceClientTest {

    private static final UUID ID = UUID.fromString("8383a7c3-f831-4f4d-a0a9-015165148af5");
    private static final UUID ORDER_ID = UUID.fromString("228aa55c-393c-411b-9410-4a995480e78e");
    private static final String STATUS = "payment_complete";
    private static final int AMOUNT = 42;
    private static final String DESCRIPTION = String.format("Payment for order %s", ORDER_ID);

    private static WireMockServer wireMockServer;
    private static WireMockPactApi wireMockPactApi;

    @BeforeAll
    void configureWiremockPactGenerator() {

        wireMockServer = new WireMockServer();

        wireMockServer.start();

        wireMockPactApi =
                WireMockPactApi.create(
                        new WireMockPactConfig()
                                .setConsumerDefaultValue("order_consumer")
                                .setProviderDefaultValue("payment_provider")
                                .setPactJsonFolder("target/pacts"));

        wireMockPactApi.clearAllSaved();
    }

    @Test
    public void getPayment_validOrderId_shouldYieldExpectedPayment() {

        Payment payment = new Payment(ID, ORDER_ID, STATUS, AMOUNT, DESCRIPTION);

        String paymentAsJson = new Gson().toJson(payment);

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(String.format("/payment/%s", ORDER_ID)))
                .willReturn(aResponse().withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(paymentAsJson))
                .withMetadata(new Metadata(
                        Map.of(
                                WireMockPactMetadata.METADATA_ATTR,
                                new WireMockPactMetadata()
                                        .setProvider("payment_provider")))));

        Payment responsePayment = new PaymentServiceClient(wireMockServer.baseUrl()).getPaymentForOrder(ORDER_ID.toString());

        assertThat(responsePayment.getId()).isEqualTo(ID);
        assertThat(responsePayment.getOrderId()).isEqualTo(ORDER_ID);
        assertThat(responsePayment.getStatus()).isEqualTo(STATUS);
        assertThat(responsePayment.getAmount()).isEqualTo(AMOUNT);
        assertThat(responsePayment.getDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    public void getPayment_nonexistentOrderId_shouldThrowException() {

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/payment/00000000-0000-0000-0000-000000000000"))
                .willReturn(aResponse().withStatus(404))
                .withMetadata(new Metadata(
                        Map.of(
                                WireMockPactMetadata.METADATA_ATTR,
                                new WireMockPactMetadata()
                                        .setProvider("payment_provider")))));

        assertThatThrownBy(
                () -> new PaymentServiceClient(wireMockServer.baseUrl()).getPaymentForOrder("00000000-0000-0000-0000-000000000000")
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("404 Not Found");
    }

    @Test
    public void getPayment_invalidOrderId_shouldThrowException() {

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/payment/this_is_not_a_valid_payment_id"))
                .willReturn(aResponse().withStatus(400))
                .withMetadata(new Metadata(
                        Map.of(
                                WireMockPactMetadata.METADATA_ATTR,
                                new WireMockPactMetadata()
                                        .setProvider("payment_provider")))));

        assertThatThrownBy(
                () -> new PaymentServiceClient(wireMockServer.baseUrl()).getPaymentForOrder("this_is_not_a_valid_payment_id")
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("400 Bad Request");
    }

    @AfterAll
    public static void after() {

        for (final ServeEvent serveEvent : wireMockServer.getAllServeEvents()) {
            wireMockPactApi.addServeEvent(serveEvent);
        }

        wireMockPactApi.saveAll();
        wireMockServer.stop();
    }
}
