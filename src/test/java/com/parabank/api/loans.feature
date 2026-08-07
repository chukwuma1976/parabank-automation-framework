Feature: Common Bank Operations API

  Background:
    * url baseUrl
    * def customerId = 12212
    * def accountId = 13344
    * configure afterScenario = function(){ karate.call('classpath:api/cleanup.feature') }

  Scenario: Request a loan from the bank
    Given path 'requestLoan'
    * def loanAmount = 2000
    * def downPayment = 400
    And param customerId = customerId
    And param amount = loanAmount
    And param downPayment = downPayment
    And param fromAccountId = accountId
    And header Accept = 'application/json'
    When method post
    Then status 200
    And match response == 
    """
    {
    responseDate: '#number',
    loanProviderName: '#string',
    approved: '#boolean',
    accountId: '#number'
    }
    """
