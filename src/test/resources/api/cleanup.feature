Feature: Common Bank Operations API

  Background:
    * url 'https://parabank.parasoft.com/parabank/services/bank'

  Scenario:
    Given path 'cleanDB'
    When method post
    Then status 204