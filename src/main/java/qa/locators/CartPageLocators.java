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

    @FindBy(xpath = "//*[@id=\"checkout-frontend\"]/div/main/section/article/button")
    public WebElement continueShoppingButton;

    @FindBy(xpath = "//*[@id=\"checkout-frontend\"]/div/main/section/article/h1")
    public WebElement cartEmptyMessage;

    @FindBy(xpath = "//*[@id=\"checkout-frontend\"]/div/main/h1")
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
}