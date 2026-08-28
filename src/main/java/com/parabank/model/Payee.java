package com.parabank.model;

public class Payee {

    private String name;
    private Address address;
    private String phoneNumber;
    private int accountNumber;

    public Payee(String name, Address address, String phoneNumber, int accountNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

}

/*
 * {
 * name: 'My Rent',
 * address: {
 * street: 'Somewhere',
 * city: 'Some City',
 * state: 'Some State',
 * zipCode: '66666'
 * },
 * phoneNumber: '1-800-999-1924',
 * accountNumber: 0
 * }
 */