package qa.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartPageLocators {
    /**
     * Class dedicated to the storage and localization of the
     * elements in the DOM. Regardless of the best practices,
     * most of them are using the xpath of the element to
     * locate them
     */

    @FindBy(xpath = "//*[@data-zta=\"continueShoppingBtn\"]")
    public WebElement continueShoppingButton;

    @FindBy(xpath = "//*[@data-zta=\"H1UIC\"]")
    public WebElement shoppingBasketTitle;

    @FindBy(xpath = "//*[@id=\"splide01-slide02\"]")
    public WebElement suggestedProduct;

    @FindBy(xpath = "//*[@id=\"checkout-frontend\"]/div/main/section/article[1]/div/article[1]")
    public WebElement firstProductAddedToCart;

    @FindBy(xpath = "//*[@id=\"cartSummary\"]/div[1]/p")
    public WebElement subtotalAmount;

    @FindBy(xpath = "//*[@id=\"splide02-slide01\"]")
    public WebElement firstLikeableProduct;

    @FindBy(xpath = "//*[@id=\"checkout-frontend\"]/div/main/section/article[1]/div/article[2]")
    public WebElement secondProductAddedToCart;

    @FindBy(xpath = "//*[@data-zta=\"gotoPreviewBottom\"]")
    public WebElement proceedToCheckoutButton;

    @FindBy(xpath = "//*[@id=\"splide03-slide01\"]")
    public WebElement firstRecommendedProduct;

    @FindBy(xpath = "//*[@data-zta=\"shippingCountryName\"]")
    public WebElement countrySelected;

    @FindBy(xpath = "//*[@data-zta=\"dropdownMenuInnerWrap\"]")
    public WebElement countriesDropdown;

    @FindBy(xpath = "//*[@data-zta=\"dropdownMenuMenu\"]")
    public WebElement dropdownMenu;

    @FindBy(xpath = "//*[@data-zta=\"inputInput\"]")
    public WebElement postcodeInput;

    @FindBy(xpath = "//*[@data-zta=\"shippingCostPopoverAction\"]")
    public WebElement updateCountryButton;

    @FindBy(xpath = "//*[@data-zta=\"shippingCostValueOverview\"]")
    public WebElement shipmentFeeAmount;

    @FindBy(xpath = "//*[@data-zta=\"total__price__value\"]")
    public WebElement totalAmount;

    @FindBy(xpath = "//*[@data-zta=\"enterCouponBtn\"]")
    public WebElement enterCouponCodeButton;

    @FindBy(xpath = "//*[@data-zta=\"couponCode\"]")
    public WebElement enterCouponCodeInput;

    @FindBy(xpath = "//*[@data-zta=\"redeemCode\"]")
    public WebElement redeemCouponCodeButton;

    @FindBy(css = ".z-alert--info")
    public WebElement incorrectCouponCodeMessage;
}