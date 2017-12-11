package com.sherpa.mynelis.cigref.model.invitations;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Alexis Largaiolli on 19/10/2017.
 */
public enum InvitationStatus implements Serializable {

    @SerializedName("accepted")
    ACCEPTED("accepted"),

    @SerializedName("refused")
    REFUSED("refused"),

    @SerializedName("not_answered")
    NO_RESPONSE("not_answered");

    private final String value;

    InvitationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isAccepted(){
        return InvitationStatus.ACCEPTED.equals(this);
    }

    public boolean isRefused(){
        return InvitationStatus.REFUSED.equals(this);
    }

    public boolean isNoResponse(){
        return InvitationStatus.NO_RESPONSE.equals(this);
    }
}
