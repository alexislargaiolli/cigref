package com.sherpa.mynelis.cigref.model;

import java.io.Serializable;

/**
 * Created by Pascal on 14/10/2017.
 */

public class CigrefUser implements Serializable{

    private String mLastName;
    private String mFirstName;
    private String mProfileImageUrl;

    public CigrefUser(String mLastName, String mFirstName, String mProfileImageUrl) {
        this.mLastName = mLastName;
        this.mFirstName = mFirstName;
        this.mProfileImageUrl = mProfileImageUrl;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public String getPreparedName(boolean lastNameFirst) {
        String firstName = mFirstName.substring(0, 1).toUpperCase() + mFirstName.substring(1).toLowerCase();
        String lastName = mLastName.toUpperCase();

        if (lastNameFirst) {
            return lastName + " " + firstName;
        }
        return firstName + " " + lastName;
    }
}
