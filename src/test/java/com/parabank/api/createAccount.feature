Feature: Common Bank Operations API

  Background:
    * url baseUrl
    * def customerId = 12212
    * def accountId = 13344

  Scenario: Create new checking account and deposit $10,000 for other tests
    Given path 'createAccount'
    And param customerId = customerId
    And param newAccountType = 0
    And param fromAccountId = accountId
    And header Accept = 'application/json'
    When method post
    Then status 200
    * def newAccountId = response.id

    Given path 'deposit'
    * def amountToDeposit = 10000
    And param accountId = newAccountId
    And param amount = amountToDeposit
    When method post
    Then status 200