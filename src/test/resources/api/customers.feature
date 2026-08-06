Feature: Test Customers API

  Background:
    * url 'https://parabank.parasoft.com/parabank/services/bank'
    * def customerId = 12212
    * configure afterScenario = function(){ karate.call('classpath:api/cleanup.feature') }
  Scenario: Get customer accounts by customer id
    Given path 'customers', customerId, 'accounts'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0].customerId == customerId
    And match response[0] ==
    """
    {
        id: '#number',
        customerId: '#number',
        type: '#string',
        balance: '#number'
    }
    """

  Scenario: Get customer information
    Given path 'customers', customerId
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response.id == customerId
    And match response ==
    """
    {
        id: '#number',
        firstName: '#string',
        lastName: '#string',
        address: {
            street: '#string',
            city: '#string',
            state: '#string',
            zipCode: '#string'
        },
        phoneNumber: '#string',
        ssn: '#string'
    }
    """

  Scenario: Get customer positions
    Given path 'customers', customerId, 'positions'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0].customerId == customerId
    And match response[0] ==
    """
    {
        positionId: '#number',
        customerId: '#number',
        name: '#string',
        symbol: '#string',
        shares: '#number',
        purchasePrice: '#number'
    }
    """

  Scenario: Update customer information
    Given path 'customers/update', customerId
    And header Accept = 'application/json'
    And header Content-Type = 'application/json'
    And params { firstName: 'Calvin', lastName: 'Ellis', street: '1300 Pennsylvania Avenue', city: 'Washington', state: 'DC', zipCode: '12345', phoneNumber: '1-800-555-1234', ssn: '123-45-6789', username: 'logintester', password: 'logintesterpassword'}
    When method post
    Then status 200
    And match response == 'Successfully updated customer profile'
    