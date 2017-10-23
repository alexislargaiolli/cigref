package com.sherpa.mynelis.cigref.model.campaign;

import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.model.common.Link;
import com.sherpa.mynelis.cigref.service.AuthenticationService;

import java.io.Serializable;

/**
 * Created by Alexis Largaiolli on 20/10/17.
 */
public class CampaignModelLinks implements Serializable {

    private int id;

    @SerializedName("images")
    private Link images;

    public String getPosterUrl(){
        if(images == null){
            return new String();
        }
        return NelisInterface.API_ROOT + images.getHref() + AuthenticationService.getInstance().getAccessTokenAsSufix();
    }

    @Override
    public String toString() {
        return "CampaignModelLinks{" +
                "images=" + images +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Link getImages() {
        return images;
    }

    public void setImages(Link images) {
        this.images = images;
    }
}
