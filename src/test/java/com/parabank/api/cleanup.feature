Feature: Common Bank Operations API

  Background:
    * url baseUrl

  Scenario:
    Given path 'cleanDB'
    When method post
    Then status 204