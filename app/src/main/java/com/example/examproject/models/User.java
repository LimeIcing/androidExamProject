package com.example.examproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String email, firstName, lastName, dateOfBirth;

    public User() {
    }

    protected User(Parcel in) {
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        dateOfBirth = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(dateOfBirth);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // region getters
    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    // endregion
}
