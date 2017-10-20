package com.sherpa.mynelis.cigref.model.invitations;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexis Largaiolli on 20/10/17.
 */
public class InvitationStatusPatch {

    @SerializedName("status")
    InvitationStatus status;

    public InvitationStatusPatch(InvitationStatus status) {
        this.status = status;
    }
}
