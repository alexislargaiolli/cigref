package com.sherpa.mynelis.cigref.model.invitations;

import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.model.user.User;

import java.io.Serializable;

/**
 * Created by Alexis Largaiolli on 19/10/2017.
 */
public class Invitation implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("guests_count")
    int guestCount;

    @SerializedName("status")
    InvitationStatus status;

    @SerializedName("account")
    User user;

    public String getPosterUrl(){
        if(user == null){
            return null;
        }
        return user.getPosterUrl();
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "id=" + id +
                ", guestCount=" + guestCount +
                ", status=" + status +
                ", user=" + user +
                '}';
    }

    public boolean isAccepted(){
        return InvitationStatus.ACCEPTED.equals(this.status);
    }

    public boolean isRefused(){
        return InvitationStatus.REFUSED.equals(this.status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
