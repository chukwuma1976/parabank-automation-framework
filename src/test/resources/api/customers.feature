Feature: Test Customers API

  Background:
    * url 'https://parabank.parasoft.com/parabank/services/bank'
    * def customerId = 21758
    * def customerId2 = 12212
  Scenario: Get customer caccounts by customer id
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
    Given path 'customers', customerId2, 'positions'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0].customerId == customerId2
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
    