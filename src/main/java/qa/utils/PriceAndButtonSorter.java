package qa.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriceAndButtonSorter {
    /**
     * Class dedicated to list and sort the products present in the cart
     * in descending order according to their price
     */
    private final WebDriver driver = HelperClass.getDriver();
    RemoveEuroSymbol removeEuroSymbol;

    public PriceAndButtonSorter() {
        this.removeEuroSymbol = new RemoveEuroSymbol();
    }

    public List<ProductsElements> getSortedPricesAndButtons() {
        WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));
        List<WebElement> productElements = driver.findElements(By.cssSelector("._y8rnTfjeHCx2lXE5RDE"));
        List<ProductsElements> priceAndButtons = new ArrayList<>();

        for (WebElement productElement : productElements) {
            String priceText = wait.until(ExpectedConditions.elementToBeClickable(productElement.findElement(By.cssSelector("span[data-zta='productStandardPriceAmount']")))).getText();
            Float price = Float.parseFloat(removeEuroSymbol.getAmountWithoutEuro(priceText));

            WebElement decreaseButtonElement = wait.until(ExpectedConditions.elementToBeClickable(productElement.findElement(By.cssSelector("[data-zta='quantityStepperDecrementButton']"))));
            WebElement increaseButtonElement = wait.until(ExpectedConditions.elementToBeClickable(productElement.findElement(By.cssSelector("[data-zta='quantityStepperIncrementButton']"))));
            WebElement currentQuantity = wait.until(ExpectedConditions.elementToBeClickable(productElement.findElement(By.cssSelector("[data-zta='quantityStepperInput']"))));

//            WebElement decreaseButtonElement = productElement.findElement(By.cssSelector("[data-zta='quantityStepperDecrementButton']"));
//            WebElement increaseButtonElement = productElement.findElement(By.cssSelector("[data-zta='quantityStepperIncrementButton']"));
//            WebElement currentQuantity = productElement.findElement(By.cssSelector("[data-zta='quantityStepperInput']"));
            ProductsElements ProductsElements = new ProductsElements(price, decreaseButtonElement, increaseButtonElement, currentQuantity);
            priceAndButtons.add(ProductsElements);
        }

        // Sort in descending order of prices
        priceAndButtons.sort(Comparator.comparing(ProductsElements::getPrice).reversed());

        return priceAndButtons;
    }

    public static class ProductsElements implements Comparable<ProductsElements> {
        private final Float price;
        private final WebElement decreaseButtonElement;
        private final WebElement increaseButtonElement;
        private final WebElement currentQuantity;


        public ProductsElements(Float price, WebElement decreaseButtonElement, WebElement increaseButtonElement, WebElement currentQuantity) {
            this.price = price;
            this.decreaseButtonElement = decreaseButtonElement;
            this.increaseButtonElement = increaseButtonElement;
            this.currentQuantity = currentQuantity;
        }

        public Float getPrice() {
            return price;
        }

        public WebElement getDecreaseButtonElement() {
            return decreaseButtonElement;
        }

        public WebElement getIncreaseButtonElement() {
            return increaseButtonElement;
        }

        public WebElement getQuantityInputElement() {
            return currentQuantity;
        }

        @Override
        public int compareTo(ProductsElements other) {
            return Double.compare(this.price, other.price);
        }
    }
}
