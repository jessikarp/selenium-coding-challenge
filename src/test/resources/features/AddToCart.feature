Feature: Adding products to the cart

  Background:
    Given The user is in the cart page

  Scenario: The user can add products to the cart from the inspirational suggestions
    Given The cart is empty
    When The user adds a product from the "Need some inspiration?" section
    Then The cart is not empty
    And It only contains the product added

  Scenario: The user can add products to the cart from the "You might also like" products
    Given The user is authenticated
    When The user adds a product from the "Need some inspiration?" section
    Then The cart is not empty
    When The user adds a product from the "You might also like" section
    Then The product has been added to the cart
