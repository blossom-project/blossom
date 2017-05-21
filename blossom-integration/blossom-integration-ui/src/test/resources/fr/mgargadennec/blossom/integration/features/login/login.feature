@login
Feature: Login

  @web
  Scenario: LOG-001 - Success on login with active user
    Given I am on "login" page
    When I type "jdoe" in "username" field
    And I type "demo" in "password" field
    And I click on "login" element
    Then I should be on "home" page
    
  @web
  Scenario: LOG-002 - Fail on login with inactive user
    Given I am on "login" page
    When I type "jdoe" in "username" field
    And I type "demo" in "password" field
    And I click on "login" element
    Then I should be on "login_error" page
    
  @web
  Scenario: LOG-003 - Success on login with system user
    Given I am on "login" page
    When I type "system" in "username" field
    And I type "system" in "password" field
    And I click on "login" element
    Then I should be on "home" page
    
  @web
  Scenario: LOG-004 - Fail on login with unknown user
    Given I am on "login" page
    When I type "tkhasan" in "username" field
    And I type "system" in "password" field
    And I click on "login" element
    Then I should be on "login_error" page
    
  @web
  Scenario: LOG-005 - Fail on login with wrong password provided
    Given I am on "login" page
    When I type "jdoe" in "username" field
    And I type "system" in "password" field
    And I click on "login" element
    Then I should be on "login_error" page