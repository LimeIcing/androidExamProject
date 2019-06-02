package com.example.examproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    private int balance;
    private AccountType type;
    private String accountNumber, registrationNumber, owner;

    public Account() {
    }

    // region Parcelable
    protected Account(Parcel in) {
        balance = in.readInt();
        accountNumber = in.readString();
        registrationNumber = in.readString();
        owner = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(balance);
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
