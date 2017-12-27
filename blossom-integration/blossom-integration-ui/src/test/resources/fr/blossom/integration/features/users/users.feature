@users
Feature: Users

  @web
  Scenario: USS-001 - Accessing users list
    Given I am authenticated with user "jdoe"
    When I am on "users" page
    Then there should be an item with property "lastname" equals to "Chapman" in list
    And its property "firstname" is equals to "Laura"

  @web
  Scenario: USS-002 - Creating a new user
    Given I am authenticated with user "jdoe"
    When I am on "users" page
    And I click on "create" element
    And I type "Clark" in "firstname" field
    And I type "superman" in "identifier" field
    And I type "clark.kent@smallville.com" in "email" field
    And I click on "save" element
    Then I should be on the "users" detail page of the selected item
    And the page displays "Clark" for property "firstname"
    And the page displays "Kent" for property "lastname"
    And the page displays "superman" for property "identifier"
    And the page displays "clark.kent@smallville.com" for property "email"