package qa.definitions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import qa.utils.HelperClass;

public class Hooks {

    @Before
    public static void setUp() {
        HelperClass.setUpDriver();
    }

    @After
    public static void tearDown(Scenario scenario) {
        // Validates if scenario has failed
        if(scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) HelperClass.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        HelperClass.tearDown();
    }
}