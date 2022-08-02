package com.dhy.moleconfig;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountP implements Parcelable {
    String name;

    @Override
    public int describeContents() {
        return 0;
    }

    public AccountP() {
    }

    protected AccountP(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static final Creator<AccountP> CREATOR = new Creator<AccountP>() {
        @Override
        public AccountP createFromParcel(Parcel in) {
            return new AccountP(in);
        }

        @Override
        public AccountP[] newArray(int size) {
            return new AccountP[size];
        }
    };
}
