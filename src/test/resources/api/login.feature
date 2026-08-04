Feature: User Login

    Background:
        * url 'https://parabank.parasoft.com/parabank/services/bank'
        * def username = 'logintester'
        * def password = 'logintesterpassword'
        
    Scenario: Login with valid username and password
        Given path '/login', username, password
        When method get    
        Then status 200

    Scenario: Login with valid username and invalid password
        Given path '/login', username, 'invalidpassword'
        When method get    
        Then status 400

    Scenario: Login with invalid username and valid password
        Given path '/login', 'invaliduser', password
        When method get    
        Then status 400

    Scenario: Login with invalid username and invalid password
        Given path '/login', 'invaliduser', 'invalidpassword'
        When method get    
        Then status 400

    Scenario: Login with valid username and blank password
        Given path '/login', username, ''
        When method get    
        Then status 404

    Scenario: Login with blank username and valid password
        Given path '/login', '', password
        When method get    
        Then status 404

    Scenario: Login with blank username and blank password
        Given path '/login','', ''
        When method get    
        Then status 404

    Scenario: Login with valid username and valid password but with wrong HTTP method (POST instead of GET)
        Given path '/login', username, password
        When method post    
        Then status 405 

    Scenario: Login with sql injection in username
        Given path '/login', 'logintester\' OR \'1\'=\'1', 'logintesterpassword'
        When method get    
        Then status 403