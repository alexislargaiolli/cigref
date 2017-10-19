package com.sherpa.mynelis.cigref.model.common;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links implements Serializable {

    private static final String TAG = "Links";

    private int id;
    @SerializedName("self")
    private Link self;
    @SerializedName("account")
    private Link account;
    @SerializedName("company")
    private Link company;
    @SerializedName("creator")
    private Link creator;
    @SerializedName("type")
    private Link type;
    @SerializedName("courtesy")
    private Link courtesy;


    /**
     * Constructeurs
     */
    public Links() { this(0, null, null, null, null, null, null); }

    public Links(int id, Link self, Link account, Link company, Link creator, Link type, Link courtesy) {
        this.id = id;
        this.self = self;
        this.account = account;
        this.company = company;
        this.creator = creator;
        this.type = type;
        this.courtesy = courtesy;
    }


    /**
     * log de l'objet pour le debug
     */
    public void consolePrint() {
        Log.d(TAG, "---------------LINKS----------------");
        if(self != null)
            Log.d(TAG, "Self : "+self.getHref());
        if(account != null)
            Log.d(TAG, "Account : "+account.getHref());
        if(company != null)
            Log.d(TAG, "Company : "+company.getHref());
        if(creator != null)
            Log.d(TAG, "Creator : "+creator.getHref());
        if(type != null)
            Log.d(TAG, "Type : "+type.getHref());
        if(type != null)
            Log.d(TAG, "Courtesy : "+type.getHref());
    }



    /**
     * Getters & setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Link getSelf() {
        return self;
    }

    public void setSelf(Link self) {
        this.self = self;
    }

    public Link getAccount() {
        return account;
    }

    public void setAccount(Link account) {
        this.account = account;
    }

    public Link getCompany() {
        return company;
    }

    public void setCompany(Link company) {
        this.company = company;
    }

    public Link getCreator() {
        return creator;
    }

    public void setCreator(Link creator) {
        this.creator = creator;
    }

    public Link getType() {
        return type;
    }

    public void setType(Link type) {
        this.type = type;
    }

    public Link getCourtesy() {
        return courtesy;
    }

    public void setCourtesy(Link courtesy) {
        this.courtesy = courtesy;
    }
}