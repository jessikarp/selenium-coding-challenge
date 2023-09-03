package qa.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.locators.CartPageLocators;
import qa.utils.HelperClass;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CartPageActions {
    /**
     * Class dedicated to implement the interactions with
     * the Shopping Cart Page.
     */
    CartPageLocators cartPageLocators = null;
    private Map<String, String> productSuggestedInfo = new HashMap<>();
    private Map<String, String> firstProductInfo = new HashMap<>();
    private Map<String, String> secondProductInfo = new HashMap<>();
    private Map<String, String> firstLikeableProductInfo = new HashMap<>();
    private String subtotalAmount;
    private String shipmentFeeAmount;
    private String totalAmount;

    public CartPageActions() {
        this.cartPageLocators = new CartPageLocators();

        PageFactory.initElements(HelperClass.getDriver(), cartPageLocators);
    }

    Actions actions = new Actions(HelperClass.getDriver());
    WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));;


    public String cartTitle() {
        WebElement shoppingBasketTitle = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.shoppingBasketTitle));
        return shoppingBasketTitle.getText();
    }

    public Boolean continueShoppingButtonIsDisplayed() {
        WebElement continueShoppingButton = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.continueShoppingButton));
        return continueShoppingButton.isDisplayed();
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

    public boolean onlyOneSuggestedProductWasAdded(){
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
        WebElement suggestedProduct = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.suggestedProduct));
        productSuggestedInfo = getProductInformationFromSlidesAndClick(suggestedProduct);
    }

    public void getLikeableProductInformationAndClick() {
        WebElement firstLikeableProduct = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.firstLikeableProduct));
        firstLikeableProductInfo = getProductInformationFromSlidesAndClick(firstLikeableProduct);
    }

    public String getAmountWithoutEuro(String amount){
        // Remove the Euro symbol and any leading/trailing whitespace
        amount = amount.replaceAll("[^0-9.,]", "").trim();

        // Replace any comma with a period to ensure it's a valid float representation
        amount = amount.replace(',', '.');

        return amount;
    }

    public String getSubtotalAmount() {
        WebElement subtotalAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.subtotalAmount));
        subtotalAmount = getAmountWithoutEuro(subtotalAmountLocator.getText());
        return subtotalAmount;
    }

    public String getShipmentAmount() {
        WebElement shipmentAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.shipmentFeeAmount));
        shipmentFeeAmount = getAmountWithoutEuro(shipmentAmountLocator.getText());
        return shipmentFeeAmount;
    }

    public String getTotalAmount() {
        WebElement totalAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.totalAmount));
        totalAmount = getAmountWithoutEuro(totalAmountLocator.getText());
        return totalAmount;
    }

    public String calculateNewAmount(String currentAmount, Map<String, String> newProductAdded) {
        String productPrice = getAmountWithoutEuro(newProductAdded.get("productPrice"));
        float productPriceAmount = Float.parseFloat(productPrice);
        float oldAmount = Float.parseFloat(currentAmount);

        float newAmount = productPriceAmount + oldAmount;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedAmount = Float.parseFloat(decimalFormat.format(newAmount));

        return Float.toString(roundedAmount);
    }

    public String calculateNewSubtotalAmount(Map<String, String> newProductAdded) {
        return calculateNewAmount(subtotalAmount, newProductAdded);
    }

    public String calculateNewTotalAmount(Map<String, String> newProductAdded) {
        return calculateNewAmount(totalAmount, newProductAdded);
    }

    public Boolean newSubtotalIsCorrect() {
        subtotalAmount = calculateNewSubtotalAmount(secondProductInfo);
        return subtotalAmount.equals(getSubtotalAmount());
    }

    public Boolean newTotalWithSubtotalChangedIsCorrect() {
        totalAmount = calculateNewTotalAmount(secondProductInfo);
        return totalAmount.equals(getTotalAmount());
    }

    public Boolean verifyProceedToCheckoutButtonEnabled() {
        WebElement proceedToCheckoutButton = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.proceedToCheckoutButton));
        return proceedToCheckoutButton.isEnabled();
    }

    public void addRecommendedProductsUntilSubtotalAmountIsMet(Float amount) {
        String currentSubtotalAmount = subtotalAmount;
        while (Float.parseFloat(currentSubtotalAmount) < amount) {
            WebElement firstRecommendedProductButton = cartPageLocators.firstRecommendedProduct.findElement(By.cssSelector(".recommendation-item-module_cartBtn__i-O64"));

            while (!firstRecommendedProductButton.isDisplayed()) {
                actions.moveToElement(firstRecommendedProductButton).perform();
            }

            firstRecommendedProductButton.click();

            String newSubtotalAmount = getSubtotalAmount();

            while (newSubtotalAmount.equals(currentSubtotalAmount)) {
                try {
                    newSubtotalAmount = getSubtotalAmount();
                }
                catch (StaleElementReferenceException e) {
                    System.out.println("StaleElementReferenceException occurred, retrying...");
                }
            }

            currentSubtotalAmount = getSubtotalAmount();
        }
    }

    public Boolean verifySelectedCountry(String selectedCountry) {
        String currentSelectedCountry = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.countrySelected)).getText();
        return currentSelectedCountry.contains(selectedCountry);
    }

    private void selectNewCountry(String country) {
        cartPageLocators.countrySelected.click();
        cartPageLocators.countriesDropdown.click();
        WebElement dropdownMenu = cartPageLocators.dropdownMenu;

        // Locate and click the specific option within the dropdown based on country
        String optionXPath = String.format("//*[@data-label='%s']", country);
        WebElement countryOption = dropdownMenu.findElement(By.xpath(optionXPath));
        countryOption.click();
    }

    private void setInputField(WebElement field, String value) {
        WebElement inputField =  wait.until(ExpectedConditions.visibilityOf(field));
        inputField.click();
        inputField.clear();
        inputField.sendKeys(value);
    }

    private void selectNewPostcode(String postcode) {
        setInputField(cartPageLocators.postcodeInput, postcode);
    }

    private void enterCouponCode(String couponCode) {
        setInputField(cartPageLocators.enterCouponCodeInput, couponCode);
    }

    public void acceptDeliveryChanges(String country, String postcode) {
        String currentCountry = cartPageLocators.countrySelected.getText();
        selectNewCountry(country);
        selectNewPostcode(postcode);
        cartPageLocators.updateCountryButton.click();
    }

    public Boolean shipmentFeeIsChanged() {
        String previousShipmentFeeAmount = shipmentFeeAmount;
        shipmentFeeAmount = getShipmentAmount();
        return !shipmentFeeAmount.equals(previousShipmentFeeAmount);
    }

    public boolean newTotalMatchesWithNewShipmentFee() {
        String newSubtotalAmount = getSubtotalAmount();

        Float newTotalExpected = Float.parseFloat(newSubtotalAmount) + Float.parseFloat(shipmentFeeAmount);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedTotal = Float.parseFloat(decimalFormat.format(newTotalExpected));

        String currentTotal = getTotalAmount();

        return currentTotal.equals(Float.toString(roundedTotal));
    }

    public boolean totalHasChanged() {
        String previousTotalAmount = totalAmount;
        totalAmount = getTotalAmount();
        return !previousTotalAmount.equals(totalAmount);
    }

    public boolean verifyShippingFeeIsFree() {
        WebElement shipmentAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.shipmentFeeAmount));
        String shipmentText = shipmentAmountLocator.getText();
        return shipmentText.equals("Free");
    }

    public void insertCouponCode(String couponCode) {
        wait.until(ExpectedConditions.visibilityOf(cartPageLocators.enterCouponCodeButton)).click();
        enterCouponCode(couponCode);
        wait.until(ExpectedConditions.visibilityOf(cartPageLocators.redeemCouponCodeButton)).click();
    }

    public String getIncorrectCouponCodeMessage() {
        String message = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.incorrectCouponCodeMessage)).getText();
        System.out.println(message);
        return message;
    }
}