package com.sherpa.mynelis.cigref.model;

import java.io.Serializable;

/**
 * Created by Pascal on 14/10/2017.
 */

public class EventAddress implements Serializable {

    private String mLocationName;
    private String mStreet;
    private String mPostalCode;
    private String mCity;

    public EventAddress(String mLocationName, String mStreet, String mPostalCode, String mCity) {
        this.mLocationName = mLocationName;
        this.mStreet = mStreet;
        this.mPostalCode = mPostalCode;
        this.mCity = mCity;
    }

    public String getFormatted(){
        return getLocationName() + getStreet() + getPostalCode() + getCity();
    }

    public String getLocationName() {
        return mLocationName;
    }

    public String getStreet() {
        return mStreet;
    }

    public String getPostalCode() {
        return mPostalCode;
    }

    public String getCity() {
        return mCity;
    }

    public String getPostalCodeWithCity() {
        return mPostalCode + ' ' + mCity;
    }
}
