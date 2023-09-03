package qa.utils;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HelperClass {
    private static WebDriver driver;
    public final static int TIMEOUT = 10;


    public static void openPage(String url) {
        // Navigate to the desired web page
        driver.get(url);

        // Accept cookies
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(By.id("onetrust-reject-all-handler"))).click();
    }

    public static void setUpAuthCookie() {
        // Retrieve the current cookies
        Set<Cookie> cookies = driver.manage().getCookies();

        // Iterate through the cookies to find the "sid" cookie
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("sid")) {
                // Create a new cookie with the updated value
                Cookie updatedCookie = new Cookie("sid", System.getenv("AUTH_COOKIE"));
                // Delete the old cookie
                driver.manage().deleteCookie(cookie);
                // Add the updated cookie
                driver.manage().addCookie(updatedCookie);
            }
        }

        // Refresh the page to apply the updated cookie
        refreshPage();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
    }

    public static void tearDown() {
        while (cartIsNotEmpty()) {
            try {
                // Find and click the "Remove" button for each item in the cart
                List<WebElement> removeButtons = driver.findElements(By.cssSelector(".z-qty-stepper__btn"));
                if (!removeButtons.isEmpty()) {
                    WebElement firstRemoveButton = removeButtons.get(0);
                    firstRemoveButton.click();
                } else {
                    break;
                }
            } catch (StaleElementReferenceException e) {
                // Handle the StaleElementReferenceException gracefully
                System.out.println("StaleElementReferenceException occurred, retrying...");
            }
        }
        if(driver!=null) {
            driver.manage().deleteAllCookies();
            driver.close();
            driver.quit();
        }
    }

    private static Boolean cartIsNotEmpty() {
        try {
            List<WebElement> cartItems = driver.findElements(By.cssSelector("._y8rnTfjeHCx2lXE5RDE"));
            // If cart items are found, check if at least one is displayed
            if (!cartItems.isEmpty()) {
                return true;
            }
        } catch (NoSuchElementException e) {
            // NoSuchElementException is thrown when cart items are not found
        }
        return false; // Cart is empty or items were not found
    }

    public static void refreshPage() {
        driver.navigate().refresh();
    }
}