#Feature: Checkout amounts and options
#
#  Scenario: User cannot checkout if minimum subtotal isn't met
#    Given The user is in the cart page
#    And The cart is not empty
#    And The subtotal is less than €19
#    And The Proceed to checkout button is disabled
#    When The user adds products from the recommendations until the subtotal is higher than €19
#    Then The Proceed to checkout button is enabled
#
#  Scenario: Shipping fees changes when changing the country
#    Given The user is in the cart page
#    And The cart is not empty
#    And The country to deliver to is Estonia
#    When The user changes the country to deliver to Portugal
#    And The user adds the postcode 5000
#    Then The Shipping fee changes
#    And The total amount changes accordingly
#
#  Scenario: Shipping fees are free when meeting the minimum value
#    Given The user is in the cart page
#    And The cart is not empty
#    And The country to deliver to is Estonia
#    When The user adds products from the recommendations until the subtotal is higher than €69
#    Then The shipping costs are free
#    And The total amount is the same as the subtotal amount
#
#  Scenario: Total amount doesn't change if an invalid coupon code is added
#    Given The user is in the cart page
#    And The cart is not empty
#    When The user inserts the coupon code this-is-a-test
#    Then An information message appears indicating the code this-is-a-test is not valid
#    And The Total amount doesn't include any discount
#
#  # flaky, let's wait to consider it
#  Scenario: Free shipping costs are revoked when the subtotal doesn't met the minimum value
#    Given The user is in the cart page
#    And The cart is not empty
#    And The country to deliver to is Estonia
#    And The subtotal is more than €69
#    When The user removes the cheapest products until the subtotal is less than €69
#    Then The shipping fee changes
#    And The total amount changes accordingly
#    And The information message about Free shipping costs appears