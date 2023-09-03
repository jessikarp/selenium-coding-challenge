package qa.definitions;

import io.cucumber.java.en.And;
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
        Assert.assertTrue(cartPage.cartTitle().contains("Your shopping basket is empty"));
        Assert.assertTrue(cartPage.continueShoppingButtonIsDisplayed());
    }

    @Then("The cart is not empty")
    public void cartIsNotEmpty() {
        Assert.assertTrue(cartPage.cartTitle().contains("Your Shopping Basket"));
        cartPage.getSubtotalAmount();
        cartPage.getShipmentAmount();
        cartPage.getTotalAmount();
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
        Assert.assertTrue(cartPage.newTotalWithSubtotalChangedIsCorrect());
    }

    @Given("The subtotal is less than €{float}")
    public void theSubtotalIsLessThan(float arg0) {
        Assert.assertTrue(Float.parseFloat(cartPage.getSubtotalAmount())<arg0);
    }

    @And("The Proceed to checkout button is disabled")
    public void theProceedToCheckoutButtonIsDisabled() {
        Assert.assertFalse(cartPage.verifyProceedToCheckoutButtonEnabled());
    }

    @When("The user adds products from the recommendations until the subtotal is higher than €{float}")
    public void theUserAddsProductsFromTheRecommendationsUntilTheSubtotalIsHigherThan(float arg0) {
        cartPage.addRecommendedProductsUntilSubtotalAmountIsMet(arg0);
    }

    @Then("The Proceed to checkout button is enabled")
    public void theProceedToCheckoutButtonIsEnabled() {
        Assert.assertTrue(cartPage.verifyProceedToCheckoutButtonEnabled());
    }

    @Given("The country to deliver to is {string}")
    public void theCountryToDeliverToIs(String arg0) {
        if (!cartPage.verifySelectedCountry(arg0)) {
            cartPage.acceptDeliveryChanges(arg0, "");
        }
        try {
            Assert.assertTrue(cartPage.verifySelectedCountry(arg0));
        } catch (Exception ignored) { }
        cartPage.getTotalAmount();
        cartPage.getShipmentAmount();
    }

    @Then("The country selected is {string}")
    public void countrySelectedIs(String arg0) {
        Assert.assertTrue(cartPage.verifySelectedCountry(arg0));
    }

    @When("The user changes the country to deliver to {string} with postcode {string}")
    public void theUserChangesTheCountryToDeliverToWithPostcode(String arg0, String arg1) {
        cartPage.acceptDeliveryChanges(arg0, arg1);
    }

    @Then("The Shipping fee changes")
    public void theShippingFeeChanges() {
        Assert.assertTrue(cartPage.shipmentFeeIsChanged());
    }

    @And("The total amount changes accordingly")
    public void theTotalAmountChangesAccordingly() {
        Assert.assertTrue(cartPage.totalHasChanged());
        Assert.assertTrue(cartPage.newTotalMatchesWithNewShipmentFee());
    }

    @Then("The shipping costs are free")
    public void theShippingCostsAreFree() {
        Assert.assertTrue(cartPage.verifyShippingFeeIsFree());
    }

    @And("The total amount is the same as the subtotal amount")
    public void theTotalAmountIsTheSameAsTheSubtotalAmount() {
        Assert.assertEquals(cartPage.getTotalAmount(), cartPage.getSubtotalAmount());
    }

    @When("The user inserts the coupon code {string}")
    public void theUserInsertsTheCouponCode(String arg0) {
        cartPage.insertCouponCode(arg0);
    }

    @Then("An information message appears indicating the code {string} is not valid")
    public void anInformationMessageAppearsIndicatingTheCodeIsNotValid(String arg0) {
        Assert.assertTrue(cartPage.getIncorrectCouponCodeMessage().contains(arg0));
    }

    @And("The Total amount doesn't include any discount")
    public void theTotalAmountDoesnTIncludeAnyDiscount() {
        Assert.assertFalse(cartPage.totalHasChanged());
    }
}
