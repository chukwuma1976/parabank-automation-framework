Feature: User Login

    Background:
        * url 'https://parabank.parasoft.com/parabank/services/bank'
        * def username = 'logintester'
        * def password = 'logintesterpassword'
        
    Scenario: Login with valid username and password
        Given path '/login', username, password
        When method get    
        Then status 200

    Scenario Outline: Login with combinations of valid and invalid usernames and passwords
        Given path '/login', <username>, <password>
        When method get    
        Then status <status>

        Examples:
            | username       | password              | status |
            | 'logintester'  | 'invalidpassword'     | 400    |
            | 'invaliduser'  | 'logintesterpassword' | 400    |
            | 'invaliduser'  | 'invalidpassword'     | 400    |
            | 'logintester'  | ''                    | 404    |
            | ''             | 'logintesterpassword' | 404    |
            | ''             | ''                    | 404    |

    Scenario: Login with valid username and valid password but with wrong HTTP method (POST instead of GET)
        Given path '/login', username, password
        When method post    
        Then status 405 

    Scenario: Login with sql injection in username
        Given path '/login', 'logintester\' OR \'1\'=\'1', 'logintesterpassword'
        When method get    
        Then status 403