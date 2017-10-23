package com.sherpa.mynelis.cigref.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alexis Largaiolli on 19/10/2017.
 */
public class User implements Serializable{

    @SerializedName("id")
    int id;

    @SerializedName("lastname")
    String lastname;

    @SerializedName("firstname")
    String firstname;

    @SerializedName("_links")
    UserLinks links;

    public String getPosterUrl(){
        if(links == null){
            return null;
        }
        return links.getPosterUrl();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", links=" + links +
                '}';
    }
}
