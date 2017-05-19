@login
Feature: Login

  @web
  Scenario: LOG-001 - Success on login with system
    Given I am on "login" page
    When I type "system" in "username" field
    And I type "system" in "password" field
    And I click on "login" element
    Then I should be on "home" page