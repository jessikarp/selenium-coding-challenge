package qa.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import qa.locators.CartPageLocators;
import qa.utils.HelperClass;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartPageActions {
    /**
     * Class dedicated to implement
     */
    CartPageLocators cartPageLocators = null;
    private Map<String, String> productSuggestedInfo = new HashMap<>();
    private Map<String, String> firstProductInfo = new HashMap<>();
    private Map<String, String> secondProductInfo = new HashMap<>();
    private Map<String, String> firstLikeableProductInfo = new HashMap<>();
    private String subtotalAmount;
    private static WebDriver driver;

    public CartPageActions() {
        this.cartPageLocators = new CartPageLocators();

        PageFactory.initElements(HelperClass.getDriver(), cartPageLocators);
    }

    Actions actions = new Actions(HelperClass.getDriver());

    public String cartIsEmptyMessage() {
        return cartPageLocators.cartEmptyMessage.getText();
    }

    public Boolean continueShoppingButtonIsDisplayed() {
        return cartPageLocators.continueShoppingButton.isDisplayed();
    }

    public Boolean cartTitleWithContentsIsDisplayed() {
        return cartPageLocators.shoppingBasketTitle.isDisplayed();
    }

    public Map<String, String> productToBeAddedInfo() {
        return productSuggestedInfo;
    }

    public Map<String, String> firstProductAddedInfo() {
        return firstProductInfo;
    }

    public Map<String, String> getProductAddedToCartInfo(WebElement productElement) {
        Map<String, String> productInfo = new HashMap<>();

        String productName = productElement.findElement(By.cssSelector(".z-anchor--bold")).getText();
        String productPrice = productElement.findElement(By.cssSelector(".z-price__amount--standard")).getText();

        productInfo.put("productName", productName);
        productInfo.put("productPrice", productPrice);

        return productInfo;
    }

    public void getFirstProductAddedToCartInfo() {
        firstProductInfo = getProductAddedToCartInfo(cartPageLocators.firstProductAddedToCart);
    }

    public void getSecondProductAddedToCartInfo() {
        secondProductInfo = getProductAddedToCartInfo(cartPageLocators.secondProductAddedToCart);
    }


//    public void getFirstProductAddedToCartInfo() {
//        String productName = cartPageLocators.firstProductAddedToCart.findElement(By.cssSelector(".z-anchor--bold")).getText();
//        String productPrice = cartPageLocators.firstProductAddedToCart.findElement(By.cssSelector(".z-price__amount--standard")).getText();
//        firstProductInfo.put("productName", productName);
//        firstProductInfo.put("productPrice", productPrice);
//    }
//
//    public void getSecondProductAddedToCartInfo() {
//        String productName = cartPageLocators.secondProductAddedToCart.findElement(By.cssSelector(".z-anchor--bold")).getText();
//        String productPrice = cartPageLocators.secondProductAddedToCart.findElement(By.cssSelector(".z-price__amount--standard")).getText();
//        secondProductInfo.put("productName", productName);
//        secondProductInfo.put("productPrice", productPrice);
//    }

    public boolean suggestedProductAddedMatchesTheOneAddedToCart() {
        getFirstProductAddedToCartInfo();

        String value1 = productSuggestedInfo.get("productName");
        String value2 = firstProductInfo.get("productName");

        return value1.equals(value2);
    }

    public boolean likeableProductAddedMatchesTheSecondAddedToCart() {
        getSecondProductAddedToCartInfo();

        String value1 = firstLikeableProductInfo.get("productName");
        String value2 = secondProductInfo.get("productName");

        System.out.println("suggested: " + value1 + ". added: " + value2);

        return value1.equals(value2);
    }

    public Boolean onlyOneSuggestedProductWasAdded(){
        String subtotal = cartPageLocators.subtotalAmount.getText();
        String productPrice = productSuggestedInfo.get("productPrice");

        return subtotal.equals(productPrice);
    }

    public Map<String, String> getProductInformationFromSlidesAndClick(WebElement productElement) {
        Map<String, String> productInfo = new HashMap<>();

        actions.moveToElement(productElement).perform();

        // Getting information of the product selected
        String productName = productElement.findElement(By.cssSelector(".recommendation-item-module_productName__-tQsH")).getText();
        String productPrice = productElement.findElement(By.cssSelector(".z-price__amount--standard")).getText();

        productInfo.put("productName", productName);
        productInfo.put("productPrice", productPrice);

        // Clicking on the product selected
        productElement.findElement(By.cssSelector(".recommendation-item-module_cartBtn__i-O64")).click();

        return productInfo;
    }

    public void getSuggestedProductInformationAndClick() {
        productSuggestedInfo = getProductInformationFromSlidesAndClick(cartPageLocators.suggestedProduct);
    }

    public void getLikeableProductInformationAndClick() {
        firstLikeableProductInfo = getProductInformationFromSlidesAndClick(cartPageLocators.firstLikeableProduct);
    }

    public String getAmountWithoutEuro(String amount){
        // Remove the Euro symbol and any leading/trailing whitespace
        amount = amount.replaceAll("[^0-9.,]", "").trim();

        // Replace any comma with a period to ensure it's a valid float representation
        amount = amount.replace(',', '.');

        return amount;
    }

    public String getSubtotalAmount() {
        subtotalAmount = getAmountWithoutEuro(cartPageLocators.subtotalAmount.getText());
        return subtotalAmount;
    }

    public String calculateNewSubtotalAmount(Map<String, String> newProductAdded) {
        String productPrice = getAmountWithoutEuro(newProductAdded.get("productPrice"));

        Float productPriceAmount = Float.parseFloat(productPrice);
        Float oldSubtotalAmount = Float.parseFloat(subtotalAmount);

        float subtotal = productPriceAmount + oldSubtotalAmount;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedSubtotal = Float.parseFloat(decimalFormat.format(subtotal));

        return Float.toString(roundedSubtotal);
    }

    public Boolean newSubtotalIsCorrect() {
        subtotalAmount = calculateNewSubtotalAmount(secondProductInfo);

        System.out.println(subtotalAmount);

        return subtotalAmount.equals(getSubtotalAmount());
    }

    public void clearShoppingCart() {
        while (cartIsNotEmpty()) {
            // Find and click the "Remove" button for each item in the cart
            List<WebElement> removeButtons = driver.findElements(By.cssSelector(".z-qty-stepper__btn"));
            if (!removeButtons.isEmpty()) {
                WebElement firstRemoveButton = removeButtons.get(0);
                firstRemoveButton.click();
            }
        }
    }

    private static Boolean cartIsNotEmpty() {
        List<WebElement> cartItems = driver.findElements(By.cssSelector("._y8rnTfjeHCx2lXE5RDE"));
        return !cartItems.isEmpty();
    }
}