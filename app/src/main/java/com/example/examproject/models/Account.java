package com.example.examproject.models;

public class Account {
    private int balance;
    private AccountType type;
    private String accountNumber, registrationNumber, owner;

    // region getters
    public int getBalance() {
        return balance;
    }

    public AccountType getType() {
        return type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getOwner() {
        return owner;
    }
    // endregion
}
