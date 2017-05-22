@users
Feature: Users

  @web
  Scenario: USS-001 - Accessing users list
    Given I am authenticated with user "jdoe"
    When I am on "users" page
    Then there should be an item with property "lastname" equals to "Chapman" in list
    And its property "firstname" is equals to "Laura"