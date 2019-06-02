package com.example.examproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    private double balance;
    private AccountType type;
    private String accountNumber, registrationNumber, owner;

    public Account() {
    }

    // region Parcelable
    protected Account(Parcel in) {
        balance = in.readDouble();
        accountNumber = in.readString();
        registrationNumber = in.readString();
        owner = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(balance);
        dest.writeString(accountNumber);
        dest.writeString(registrationNumber);
        dest.writeString(owner);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
    // endregion

    // region getters
    public double getBalance() {
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
