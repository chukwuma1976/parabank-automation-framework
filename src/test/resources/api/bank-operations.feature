Feature: Common Bank Operations API

  Background:
    * url 'https://parabank.parasoft.com/parabank/services/bank'
    * def customerId = 12212
    * def accountId = 13344
    * configure afterScenario = function(){ karate.call('classpath:api/cleanup.feature') }

  Scenario Outline: Create different types of bank account
    Given path 'createAccount'
    And param customerId = customerId
    And param newAccountType = '<accountType>'
    And param fromAccountId = accountId
    And header Accept = 'application/json'
    When method post
    Then status 200
    And match response.customerId == customerId
    And match response.type == <accountName>
    And match response ==
    """
    {
        id: '#number',
        customerId: '#number',
        type: '#string',
        balance: '#number'
    }
    """

    Examples:
      | accountType |accountName |
      | 0           |'CHECKING'  |
      | 1           |'SAVINGS'   |
      | 2           |'LOAN'      |

