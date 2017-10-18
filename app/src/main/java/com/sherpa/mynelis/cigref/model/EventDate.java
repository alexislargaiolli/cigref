package com.sherpa.mynelis.cigref.model;

import java.io.Serializable;

/**
 * Created by Pascal on 14/10/2017.
 */

public class EventDate implements Serializable {

    private String mDate;
    private int mBeginTime;
    private int mEndTime;

    public EventDate(String mDate, int mBeginTime, int mEndTime) {
        this.mDate = mDate;
        this.mBeginTime = mBeginTime;
        this.mEndTime = mEndTime;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mBeginTime + "h - " + mEndTime + "h";
    }
}
