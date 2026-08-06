Feature: Common Bank Operations API

  Background:
    * url 'https://parabank.parasoft.com/parabank/services/bank'
    * def customerId = 12212
    * def accountId = 13344
    * configure afterScenario = function(){ karate.call('classpath:api/cleanup.feature') }

  Scenario Outline: Create different types of bank accounts
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

  Scenario: Creating an account with an invalid account type
    Given path 'createAccount'
    And param customerId = customerId
    And param newAccountType = 3 
    #This is an invalid account type, as only 0, 1, and 2 are valid
    And param fromAccountId = accountId
    When method post
    Then status 500

  Scenario: Pay bill from account
    Given path 'billpay'
    And param accountId = accountId
    And param amount = 1200
    And header Accept = 'application/json'
    * def payee =  
    """
    {
        name: 'My Rent',
        address: {
            street: 'Somewhere',
            city: 'Some City',
            state: 'Some State',
            zipCode: '66666'
        },
        phoneNumber: '1-800-999-1924',
        accountNumber: 0
        }
    """
    And request payee
    When method post
    Then status 200
    And match response ==
    """
    {
        payeeName: 'My Rent',
        amount: 1200,
        accountId: 13344
    }
    """

  Scenario: Deposit money into an account
    Given path 'deposit'
    * def amountToDeposit = 1200
    And param accountId = accountId
    And param amount = amountToDeposit
    When method post
    Then status 200
    And match response == `Successfully deposited $${amountToDeposit} to account #${accountId}`

  Scenario Outline: Deposit a very large and very small amount of money into an account and expect status code of 400
    Given path 'deposit'
    And param accountId = accountId
    And param amount = <amount>
    When method post
    Then status 400
    And match response == `Could not find account number ${accountId}`
    Examples:
      | amount                  |
      | 1_000_000_000_000_000   |
      | -1_000_000_000_000_000  |

  Scenario: Transfer money from one account to another
    #Create savings account for money transfer
    Given path 'createAccount'
    And param customerId = customerId
    And param newAccountType = 1
    And param fromAccountId = accountId
    And header Accept = 'application/json'
    When method post
    Then status 200
    * def checkingAccountId = response.id
    * def amountToTransfer = 2000

    #Now transfer money into savings account
    Given path 'transfer'
    And param fromAccountId = accountId
    And param toAccountId = checkingAccountId
    And param amount = amountToTransfer
    When method post
    Then status 200
    And match response == `Successfully transferred $${amountToTransfer} from account #${accountId} to account #${checkingAccountId}`

  Scenario: Withdraw money from an account
    Given path 'withdraw'
    * def amountToWithdraw = 2000
    And param accountId = accountId
    And param amount = amountToWithdraw
    When method post
    Then status 200
    And match response == `Successfully withdrew $${amountToWithdraw} from account #${accountId}`
