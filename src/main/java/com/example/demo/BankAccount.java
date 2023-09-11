package com.example.demo;

public class BankAccount {
    private int accountNo;
    private int userId;
    private int bankId;
    private int routingNo;

    // Constructors, getters, and setters
    public BankAccount(int accountNo, int userId, int bankId, int routingNo) {
        this.accountNo = accountNo;
        this.userId = userId;
        this.bankId = bankId;
        this.routingNo = routingNo;
    }

    public BankAccount(int fetchedBankId, int userId, int routingNumber) {
        this.accountNo = fetchedBankId;
        this.userId = userId;
        this.routingNo = routingNumber;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getRoutingNo() {
        return routingNo;
    }

    public void setRoutingNo(int routingNo) {
        this.routingNo = routingNo;
    }

    public String getAccountNumber() {
        return null;
    }

    public String getRoutingNumber() {
        return null;
    }
}
