package com.sherpa.mynelis.cigref.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexis Largaiolli on 19/10/2017.
 */
public class User {

    @SerializedName("id")
    int id;

    @SerializedName("lastname")
    String lastname;

    @SerializedName("firstname")
    String firstname;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                '}';
    }
}
