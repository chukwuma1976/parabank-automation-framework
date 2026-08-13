package com.parabank.model;

public class AuthenticatedUser {
    private final Customer customer;
    private final String sessionId;

    public AuthenticatedUser(Customer customer, String sessionId) {
        this.customer = customer;
        this.sessionId = sessionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getSessionId() {
        return sessionId;
    }
}