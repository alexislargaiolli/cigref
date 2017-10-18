package com.sherpa.mynelis.cigref.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Pascal on 14/10/2017.
 */
public class Event implements Serializable{

    private int mId;
    private String mImageUrl;
    private String mTitle;
    private String mType; //Todo: change with real model
    private List<CigrefUser> mParticpantsList; //Todo: change with real model
    private int mRegisterState;
    private String mDescription;

    private String eventPlace;
    private EventAddress mEventAdress;
    private EventDate mEventDate;
    private CigrefUser mAnimator;

    public Event(
            int mId,
            String mImageUrl,
            String mTitle,
            String mType,
            List<CigrefUser> mParticpantsList,
            int mRegisterState,
            String mDescription,
            EventAddress mEventAdress,
            EventDate mEventDate,
            CigrefUser mAnimator,
            String eventPlace
    ) {
        this.mId = mId;
        this.mImageUrl = mImageUrl;
        this.mTitle = mTitle;
        this.mType = mType;
        this.mParticpantsList = mParticpantsList;
        this.mRegisterState = mRegisterState;
        this.mDescription = mDescription;
        this.mEventAdress = mEventAdress;
        this.mEventDate = mEventDate;
        this.mAnimator = mAnimator;
        this.eventPlace = eventPlace;
    }

    public String getFormattedAddress(){
        if(mEventAdress  == null){
            return "";
        }
        return mEventAdress.getFormatted();
    }

    public int getId() {
        return mId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getType() {
        return mType;
    }

    public List<CigrefUser> getParticpantsList() {
        return mParticpantsList;
    }

    public int getRegisterState() {
        return mRegisterState;
    }

    public boolean isRegistered() {
        return mRegisterState == 1;
    }

    public boolean isNotGo() {
        return mRegisterState == 2;
    }

    public String getDescription() {
        return mDescription;
    }

    public EventAddress getEventAdress() {
        return mEventAdress;
    }

    public EventDate getEventDate() {
        return mEventDate;
    }

    public CigrefUser getAnimator() {
        return mAnimator;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }
}
