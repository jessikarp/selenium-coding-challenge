Feature: Quantity of products is editable

  Background:
    Given The user is in the cart page
    And The user is authenticated
    When The user adds a product from the "Need some inspiration?" section
    Then The cart is not empty

  Scenario: The user increases the quantity of the cheapest product by clicking on the + button
    When The user adds products from the recommendations until the subtotal is higher than €30
    And The user clicks on the + button of the cheapest product in the cart
    Then The quantity of the product increases by 1

  Scenario: The user increases the quantity of the cheapest product by editing the quantity manually
    When The user adds products from the recommendations until the subtotal is higher than €30
    And The user edits the quantity of the cheapest product by adding "3" more items
    Then The quantity of the product increases by 2

  Scenario: The user removes the most expensive product from the cart
    When The user adds products from the recommendations until the subtotal is higher than €30
    And The user removes the most expensive product from the cart
    Then A message indicating an item was removed is displayed
    # This case is missing the verification that the (most expensive) product was actually removed from the list
