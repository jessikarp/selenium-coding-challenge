package qa.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "", features = "src/test/resources/features/AddToCart.feature", glue = "qa.definitions",
        plugin = {})

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
