package com.sherpa.mynelis.cigref.model.campaign;


import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.model.common.Links;

import java.io.Serializable;
import java.util.Arrays;

public class CampaignTypeListModel implements Serializable {

    @SerializedName("id")
    private int idNelis;
    @SerializedName("shortname")
    private String shortname;
    @SerializedName("is_default")
    private boolean isDefault;
    @SerializedName("label_fr")
    private String labelFr;
    @SerializedName("label_en")
    private String labelEn;
    @SerializedName("items")
    private CampaignTypeModel[] types;

    @Override
    public String toString() {
        return "CampaignTypeListModel{" +
                "idNelis=" + idNelis +
                ", shortname='" + shortname + '\'' +
                ", isDefault=" + isDefault +
                ", labelFr='" + labelFr + '\'' +
                ", labelEn='" + labelEn + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }

    public int getIdNelis() {
        return idNelis;
    }

    public void setIdNelis(int idNelis) {
        this.idNelis = idNelis;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getLabelFr() {
        return labelFr;
    }

    public void setLabelFr(String labelFr) {
        this.labelFr = labelFr;
    }

    public String getLabelEn() {
        return labelEn;
    }

    public void setLabelEn(String labelEn) {
        this.labelEn = labelEn;
    }

    public CampaignTypeModel[] getTypes() {
        return types;
    }

    public void setTypes(CampaignTypeModel[] types) {
        this.types = types;
    }
}
