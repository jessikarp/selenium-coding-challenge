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
import qa.utils.PriceAndButtonSorter;
import qa.utils.RemoveEuroSymbol;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CartPageActions {
    /**
     * Class dedicated to implement the interactions with
     * the Shopping Cart Page.
     */
    CartPageLocators cartPageLocators;
    private Map<String, String> productSuggestedInfo = new HashMap<>();
    private Map<String, String> firstProductInfo = new HashMap<>();
    private Map<String, String> secondProductInfo = new HashMap<>();
    private Map<String, String> firstLikeableProductInfo = new HashMap<>();
    private String subtotalAmount;
    private String shipmentFeeAmount;
    private String totalAmount;
    private String currentProductQuantity;
    private Float expensiveProductPrice;
    RemoveEuroSymbol removeEuroSymbol;
    PriceAndButtonSorter sorter;

    public CartPageActions() {
        this.cartPageLocators = new CartPageLocators();
        this.removeEuroSymbol = new RemoveEuroSymbol();
        this.sorter = new PriceAndButtonSorter();

        PageFactory.initElements(HelperClass.getDriver(), cartPageLocators);
    }

    Actions actions = new Actions(HelperClass.getDriver());
    WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(10));


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

        return value1.equals(value2);
    }

    public boolean onlyOneSuggestedProductWasAdded() {
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


    public String getSubtotalAmount() {
        WebElement subtotalAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.subtotalAmount));
        subtotalAmount = removeEuroSymbol.getAmountWithoutEuro(subtotalAmountLocator.getText());
        return subtotalAmount;
    }

    public String getShipmentAmount() {
        WebElement shipmentAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.shipmentFeeAmount));
        shipmentFeeAmount = removeEuroSymbol.getAmountWithoutEuro(shipmentAmountLocator.getText());
        return shipmentFeeAmount;
    }

    public String getTotalAmount() {
        WebElement totalAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.totalAmount));
        totalAmount = removeEuroSymbol.getAmountWithoutEuro(totalAmountLocator.getText());
        return totalAmount;
    }

    public String calculateNewAmountOfAddedProduct(String currentAmount, Map<String, String> newProductAdded) {
        String productPrice = removeEuroSymbol.getAmountWithoutEuro(newProductAdded.get("productPrice"));
        float productPriceAmount = Float.parseFloat(productPrice);
        float oldAmount = Float.parseFloat(currentAmount);

        float newAmount = productPriceAmount + oldAmount;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedAmount = Float.parseFloat(decimalFormat.format(newAmount));

        return Float.toString(roundedAmount);
    }

    public String calculateNewSubtotalAmount(Map<String, String> newProductAdded) {
        return calculateNewAmountOfAddedProduct(subtotalAmount, newProductAdded);
    }

    public String calculateNewTotalAmount(Map<String, String> newProductAdded) {
        return calculateNewAmountOfAddedProduct(totalAmount, newProductAdded);
    }

    public Boolean newSubtotalIsCorrectWithSecondProductAdded() {
        subtotalAmount = calculateNewSubtotalAmount(secondProductInfo);
        return subtotalAmount.equals(getSubtotalAmount());
    }

    public Boolean newTotalWithSecondProductChangedIsCorrect() {
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
                } catch (StaleElementReferenceException e) {
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
        WebElement inputField = wait.until(ExpectedConditions.visibilityOf(field));
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
        selectNewCountry(country);
        selectNewPostcode(postcode);
        cartPageLocators.updateCountryButton.click();
    }

    public Boolean shipmentFeeIsChanged() throws InterruptedException {
        // Using sleeps is not the best practice, this should definitely be improved with implicitly or explicitly waits
        TimeUnit.SECONDS.sleep(1);
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

    public boolean totalHasChanged() throws InterruptedException {
        // Using sleeps is not the best practice, this should definitely be improved with implicitly or explicitly waits
        TimeUnit.SECONDS.sleep(1);
        String previousTotalAmount = totalAmount;
        totalAmount = getTotalAmount();
        return !previousTotalAmount.equals(totalAmount);
    }

    public boolean verifyShippingFeeIsFree() throws InterruptedException {
        // Using sleeps is not the best practice, this should definitely be improved with implicitly or explicitly waits
        TimeUnit.SECONDS.sleep(1);
        WebElement shipmentAmountLocator = wait.until(ExpectedConditions.visibilityOf(cartPageLocators.shipmentFeeAmount));
        String shipmentText = shipmentAmountLocator.getText();
        return shipmentText.equals("Free");
    }

    public void insertCouponCode(String couponCode) {
        wait.until(ExpectedConditions.visibilityOf(cartPageLocators.enterCouponCodeButton)).click();
        enterCouponCode(couponCode);
        wait.until(ExpectedConditions.visibilityOf(cartPageLocators.redeemCouponCodeButton)).click();
    }

    public String getInformationMessage() {
        return wait.until(ExpectedConditions.visibilityOf(cartPageLocators.alertInfo)).getText();
    }


    public void removeCheapestProductsUntilSubtotalAmountIsMet(Float amount) {
        String currentSubtotalAmount = subtotalAmount;
        while (Float.parseFloat(currentSubtotalAmount) > amount) {
            List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
            int itemCount = sortedData.size();

            WebElement cheapestProductDecreasingButton = sortedData.get(itemCount - 1).getDecreaseButtonElement();
            cheapestProductDecreasingButton.click();

            String newSubtotalAmount = getSubtotalAmount();

            while (newSubtotalAmount.equals(currentSubtotalAmount)) {
                try {
                    newSubtotalAmount = getSubtotalAmount();
                } catch (StaleElementReferenceException e) {
                    System.out.println("StaleElementReferenceException occurred, retrying...");
                }
            }

            currentSubtotalAmount = getSubtotalAmount();
        }
    }

    public String missingAmountForFreeShipping() {
        subtotalAmount = getSubtotalAmount();
        Float missingAmount = 69.0f - Float.parseFloat(subtotalAmount);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedMissingAmount = Float.parseFloat(decimalFormat.format(missingAmount));
        return Float.toString(roundedMissingAmount);
    }

    public void increaseCheapestProduct() {
        List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
        int itemCount = sortedData.size();
        WebElement cheapestProductIncreasingButton = sortedData.get(itemCount - 1).getIncreaseButtonElement();
        currentProductQuantity = sortedData.get(itemCount - 1).getQuantityInputElement().getAttribute("value");
        cheapestProductIncreasingButton.click();
    }

    public Boolean quantityOfCheapestProductHasIncreasedBy(int increment) {
        List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
        int itemCount = sortedData.size();
        Integer previousQuantity = Integer.parseInt(currentProductQuantity);
        WebElement inputElement = wait.until(ExpectedConditions.visibilityOf(sortedData.get(itemCount - 1).getQuantityInputElement()));
        Integer newQuantity = Integer.parseInt(inputElement.getAttribute("value"));
        int difference = newQuantity - previousQuantity;
        return difference == increment;
    }

    public void editQuantityOfCheapestProductBy(String value) {
        List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
        int itemCount = sortedData.size();
        WebElement quantityInput = sortedData.get(itemCount - 1).getQuantityInputElement();
        currentProductQuantity = quantityInput.getAttribute("value");
        setInputField(quantityInput, value);
    }

    public void decreaseMostExpensiveProduct() {
        List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
        WebElement expensiveProductDecreasingButton = sortedData.get(0).getDecreaseButtonElement();
        expensiveProductPrice = sortedData.get(0).getPrice();
        expensiveProductDecreasingButton.click();
    }

    public Boolean verifyRemovedItemWasTheMostExpensive() throws InterruptedException {
        // Using sleeps is not the best practice, this should definitely be improved with implicitly or explicitly waits
        TimeUnit.SECONDS.sleep(2);
        List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
        Float newExpensivePrice = sortedData.get(0).getPrice();

        return !Objects.equals(expensiveProductPrice, newExpensivePrice);
    }

    public Boolean subtotalIsCorrect() {
        float oldSubtotalAmount = Float.parseFloat(subtotalAmount);
        Float differenceInSubtotal = oldSubtotalAmount - Float.parseFloat(getSubtotalAmount());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedDifferenceAmount = Float.parseFloat(decimalFormat.format(differenceInSubtotal));
        return roundedDifferenceAmount == expensiveProductPrice;
    }

    public Boolean subtotalChangesWhenAddingMoreOfTheCheaperProduct(int increase) throws InterruptedException {
        // Using sleeps is not the best practice, this should definitely be improved with implicitly or explicitly waits
        TimeUnit.SECONDS.sleep(2);
        List<PriceAndButtonSorter.ProductsElements> sortedData = sorter.getSortedPricesAndButtons();
        int itemCount = sortedData.size();
        Float itemPrice = sortedData.get(itemCount-1).getPrice();
        float totalItemPrice = itemPrice * increase;
        // With this, we could even verify the total price in the item itself
        Float expectedSubtotal = Float.parseFloat(subtotalAmount) + totalItemPrice;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float roundedExpectedSubtotal = Float.parseFloat(decimalFormat.format(expectedSubtotal));
        return Float.toString(roundedExpectedSubtotal).equals(getSubtotalAmount());
    }
}