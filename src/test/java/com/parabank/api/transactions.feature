Feature: Common Bank Operations API

  Background:
    * url baseUrl
    * def customerId = 12212
    * def accountId = 13344
    * def months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
    * configure afterScenario = function(){ karate.call('cleanup.feature') }

  Scenario: Perform a transaction (money withdrawal) from account and retrieve in a multitude of ways as listed below
    # Reset the database to a clean state before performing the transaction
    Given path 'cleanDB'
    When method post
    Then status 204

    # Withdraw $2000 from account
    Given path 'withdraw'
    * def amountToWithdraw = 2000
    And param accountId = accountId
    And param amount = amountToWithdraw
    And header Accept = 'application/json'
    When method post
    Then status 200
    And match response == `Successfully withdrew $${amountToWithdraw} from account #${accountId}`

    # Retrieve transaction by accountId
    Given path 'accounts', accountId, 'transactions'
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0].accountId == accountId
    And match response[0].amount == amountToWithdraw
    And match response[0] == 
    """
      {
    id: '#number',
    accountId: '#number',
    type: '#string',
    date: '#number',
    amount: '#number',
    description: '#string'
    }
    """
    * def transaction = response[0]

    # Retrieve transaction by transactionId
    Given path 'transactions', transaction.id
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == transaction

    # Retrieve transaction by account ID and amount 
    Given path 'accounts', accountId, 'transactions/amount', transaction.amount
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0] == transaction

    #Retrieve transaction by account ID and date
    * def monthIndex = new Date().getMonth()
    * def month = months[monthIndex]
    Given path 'accounts', accountId, 'transactions/month', month, 'type', transaction.type
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0] == transaction

    # Get current date in MM-DD-YYYY using JS
    * def getFormattedDate = 
    """
    function() {
    var d = new Date();
    var mm = String(d.getMonth() + 1).padStart(2, '0');
    var dd = String(d.getDate()).padStart(2, '0');
    var yyyy = d.getFullYear();
    return mm + '-' + dd + '-' + yyyy;
    }
    """
    * def todaysDate = getFormattedDate()

    # Get transaction by account ID and by start and end date
    Given path 'accounts', accountId, 'transactions/fromDate', todaysDate, 'toDate', todaysDate
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0] == transaction

    #Get transaction by account ID and by date
    Given path 'accounts', accountId, 'transactions/onDate', todaysDate
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == '#array'
    And match response[0] == transaction