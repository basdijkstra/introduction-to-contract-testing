package order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.assertj.core.groups.Tuple;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "zip_provider.base-url:http://localhost:${RANDOM_PORT}",
        classes = LocationServiceClient.class)
public class LocationServiceContractTest {

    private static final String ZIP_CODE = "90210";
    private static final String COUNTRY = "United States";
    private static final String COUNTRY_ABBREVIATION = "US";
    private static final String PLACE_NAME = "Beverly Hills";
    private static final String STATE = "California";
    private static final String STATE_ABBREVIATION = "CA";

    @ClassRule
    public static RandomPortRule randomPort = new RandomPortRule();

    @Rule
    public PactProviderRule provider = new PactProviderRule("zip_provider", null,
            randomPort.getPort(), this);

    @Autowired
    private LocationServiceClient locationServiceClient;


    @Pact(consumer = "zip_consumer")
    public RequestResponsePact pactForZipCodeExists(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonBody((o) -> o
                .stringType("zipCode", ZIP_CODE)
                .stringType("country", COUNTRY)
                .stringType("countryAbbreviation", COUNTRY_ABBREVIATION)
                .minArrayLike("places", 1, 1, place -> place
                        .stringType("placeName", PLACE_NAME)
                        .stringType("state", STATE)
                        .stringType("stateAbbreviation", STATE_ABBREVIATION)
                )).build();

        return builder.given(
                "the zip code exists")
                .uponReceiving("A request for location data")
                .path("/us/90210")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();
    }

    @Pact(consumer = "zip_consumer")
    public RequestResponsePact pactForZipCodeDoesNotExist(PactDslWithProvider builder) {

        return builder.given(
                "the zip code does not exist")
                .uponReceiving("A request for location data")
                .path("/us/99999")
                .method("GET")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @PactVerification(fragment = "pactForZipCodeExists")
    @Test
    public void testFor_existingZipCode_shouldYieldLocationData() {
        final Location location = locationServiceClient.getLocation("us", "90210");

        assertThat(location.getZipCode()).isEqualTo(ZIP_CODE);
        assertThat(location.getCountry()).isEqualTo(COUNTRY);
        assertThat(location.getCountryAbbreviation()).isEqualTo(COUNTRY_ABBREVIATION);
        assertThat(location.getPlaces()).hasSize(1)
                .extracting(Place::getPlaceName, Place::getState, Place::getStateAbbreviation)
                .containsExactly(Tuple.tuple(PLACE_NAME, STATE, STATE_ABBREVIATION));
    }

    @PactVerification(fragment = "pactForZipCodeDoesNotExist")
    @Test
    public void testFor_nonExistingZipCode_shouldYieldHttp404() {

        assertThatThrownBy(
                () -> locationServiceClient.getLocation("us", "99999")
        ).isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("404 Not Found");
    }
}