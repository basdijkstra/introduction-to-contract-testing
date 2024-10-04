package provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Provider("address_provider")
@PactFolder("src/test/pacts")
//@PactBroker(url="https://ota.pactflow.io", authentication = @PactBrokerAuth(token = "HbtH0tZq7CU4d18JlKR2kA"))
public class ContractVerificationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    public void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("Address with ID 8aed8fad-d554-4af8-abf5-a65830b49a5f exists")
    public void addressWithIdExists() {
    }

    @State("Address with ID 00000000-0000-0000-0000-000000000000 does not exist")
    public void addressWithIdDoesNotExist() {
    }

    @State("No specific state required")
    public void noSpecificStateRequired() {
    }
}