package com.parabank.model;

public class Transaction {
    private int id;
    private int accountId;
    private String type;
    private long date; // epoch millis — long, not int, it'll overflow otherwise
    private double amount;
    private String description;

    // No-args constructor required by Jackson
    public Transaction() {
    }

    // Getters and setters (Jackson uses these for deserialization)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", accountId=" + accountId + ", type=" + type + ", date=" + date + ", amount="
                + amount + ", description=" + description + "]";
    }

}
