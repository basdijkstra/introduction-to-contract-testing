package provider;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRestPactRunner.class)
@Provider("zip_provider")
@PactFolder("src/test/pacts")
// @PactBroker(host = "ota.pact.dius.com.au", authentication = @PactBrokerAuth(token = "HbtH0tZq7CU4d18JlKR2kA"))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContractTest {

    @TestTarget
    public final Target target = new SpringBootHttpTarget();

    @State("the zip code exists")
    public void theZipCodeExists() {
        // nothing to do, real service is used
    }

    @State("the zip code does not exist")
    public void theZipCodeDoesNotExist() {
        // nothing to do, real service is used
    }
}