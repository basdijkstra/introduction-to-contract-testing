import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class UploadContractTest {

    private final String PROVIDER_BASE_URL = "https://ota.pactflow.io";
    private final String PROVIDER_NAME = "payment_provider";
    private final String PROVIDER_VERSION = "1.0.0";
    private final String PROVIDER_AUTH_TOKEN = "HbtH0tZq7CU4d18JlKR2kA";

    @Test
    public void uploadPaymentProviderOASToPactflow() throws Exception {

        given().log().all()
                .auth().oauth2(PROVIDER_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(createPayload())
                .when()
                .put(String.format("%s/contracts/provider/%s/version/%s", PROVIDER_BASE_URL, PROVIDER_NAME, PROVIDER_VERSION))
                .then().log().all()
                .statusCode(201);
    }

    private HashMap<String, Object> createPayload() throws Exception {

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("content", Base64.getEncoder().encodeToString(
                Files.readString(
                        Path.of(Objects.requireNonNull(
                                UploadContractTest.class.getResource("swagger.yml")).toURI()
                        )
                ).getBytes()
        ));
        payload.put("contractType", "oas");
        payload.put("contentType", "application/yaml");

        HashMap<String, Object> verificationResults = new HashMap<>();
        verificationResults.put("success", true);
        verificationResults.put("content", "SGVyZSBiZSBhZGRpdGlvbmFsIHRlc3QgcmVzdWx0cy4uLg==");
        verificationResults.put("contentType", "text/plain");
        verificationResults.put("verifier", "verifier");

        payload.put("verificationResults", verificationResults);

        return payload;
    }
}
