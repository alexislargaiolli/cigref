package com.sherpa.mynelis.cigref.model.user;

import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.model.common.Link;
import com.sherpa.mynelis.cigref.service.AuthenticationService;

import java.io.Serializable;

/**
 * Created by Alexis Largaiolli on 23/10/2017.
 */
public class UserLinks implements Serializable {

    @SerializedName("picture")
    private Link picture;

    public String getPosterUrl(){
        if(picture == null){
            return null;
        }
        return NelisInterface.API_ROOT + picture.getHref() + AuthenticationService.getInstance().getAccessTokenAsSufix();
    }

    @Override
    public String toString() {
        return "CampaignModelLinks{" +
                "picture=" + picture +
                '}';
    }

    public Link getPicture() {
        return picture;
    }

    public void setPicture(Link picture) {
        this.picture = picture;
    }
}
