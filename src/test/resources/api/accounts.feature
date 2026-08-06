Feature: Bank Accounts API

  Background:
    * url 'https://parabank.parasoft.com/parabank/services/bank'
    * def accountId = 13344
    * def customerId = 12212
  Scenario: Get accounts by account ID
    Given path '/accounts', accountId
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response.id == accountId
    And match response.customerId == customerId
    And match response.type == '#string'
    And match response.balance == '#number'

  Scenario: Get accounts by customer ID
    Given path '/customers', customerId, 'accounts'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response[0].customerId == customerId
    And match response[0].id == '#number'
    And match response[0].type == '#string'
    And match response[0].balance == '#number'

  Scenario: Get transactions by account number
    Given path 'accounts', accountId, 'transactions'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
