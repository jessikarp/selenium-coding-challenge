#Feature: Quantity of products is editable
#
#  Scenario: The user increases the quantity of the cheapest product by clicking on the + button
#    Given The user is in the cart page
#    And The cart is not empty
#    When The user clicks on the + button of the cheapest product in the cart
#    Then The quantity of the product increases by 1
#    And The total amount of the product is changed
#    And The subtotal amount changes accordingly
#    And The total amount changes accordingly
#
#  Scenario: The user increases the quantity of a product by editing the quantity manually
#    Given The user is in the cart page
#    And The cart is not empty
#    When The user edits the quantity of the cheapest product by adding 3 more items
#    Then The quantity of the product increases by 3
#    And The total amount of the product is changed
#    And The subtotal amount changes accordingly
#    And The total amount changes accordingly
#
#  Scenario: The user removes the most expensive product from the cart
#    Given The user is in the cart page
#    And The cart is not empty
#    When The user removes the most expensive product from the cart
#    Then The product is no longer present in the cart
#    And A message indicating an item was removed is displayed
#    And The subtotal amount changes accordingly
#    And The total amount changes accordingly
#
#  Scenario: The user decreases the quantity of a product by clicking the - button
#    Given The user is in the cart page
#    And The cart is not empty
#    When The user clicks on the - button of the cheapest product in the cart
#    Then The quantity of the product decreases by 1
#    And The total amount of the product is changed
#    And The subtotal amount changes accordingly
#    And The total amount changes accordingly
#
#  Scenario: The user removes all products from the cart
#    Given The user is in the cart page
#    And The cart is not empty
#    When The user removes all the products from the cart
#    ThenThe user is still in the cart page
#    And The cart is empty
#    And A message indicating an item was removed is displayed
