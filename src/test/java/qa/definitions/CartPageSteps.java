package qa.definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import qa.actions.CartPageActions;
import qa.utils.HelperClass;

public class CartPageSteps {
    CartPageActions cartPage = new CartPageActions();

    @Given("The user is in the cart page")
    public void navigateToCartPage(){
        HelperClass.openPage("https://www.zooplus.com/checkout/cart");
    }

    @Given("The cart is empty")
    public void cartEmptyVerification() {
        Assert.assertTrue(cartPage.cartIsEmptyMessage().contains("Your shopping basket is empty"));
        Assert.assertTrue(cartPage.continueShoppingButtonIsDisplayed());
    }

    @Then("The cart is not empty")
    public void cartIsNotEmpty() {
        Assert.assertTrue(cartPage.cartTitleWithContentsIsDisplayed());
        cartPage.getSubtotalAmount();
    }

    @Then("It only contains the product added")
    public void cartContainsSuggestedProductAdded() {
        Assert.assertTrue(cartPage.suggestedProductAddedMatchesTheOneAddedToCart());
        Assert.assertTrue(cartPage.onlyOneSuggestedProductWasAdded());
    }

    @Given("The user is authenticated")
    public void theUserIsAuthenticated() {
        HelperClass.setUpAuthCookie();
    }

    @When("The user adds a product from the {string} section")
    public void theUserAddsAProductFromTheSection(String arg0) {
        switch (arg0){
            case "You might also like":
                cartPage.getLikeableProductInformationAndClick();
                break;
            case "Need some inspiration?":
                cartPage.getSuggestedProductInformationAndClick();
                break;
            default:
                throw new RuntimeException(arg0 + " is not a valid section in this webpage");
        }
    }

    @Then("The product has been added to the cart")
    public void theProductHasBeenAddedToTheCart() {
        Assert.assertTrue(cartPage.likeableProductAddedMatchesTheSecondAddedToCart());
        Assert.assertTrue(cartPage.newSubtotalIsCorrect());
    }
}
