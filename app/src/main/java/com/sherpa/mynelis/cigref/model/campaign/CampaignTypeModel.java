package com.sherpa.mynelis.cigref.model.campaign;


import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.model.common.Links;

public class CampaignTypeModel {

    private static final String TAG = "CampaignTypeModel";

    private int id;
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
    @SerializedName("hidden")
    private boolean hidden;
    @SerializedName("_links")
    private Links links;
    @SerializedName("entity_type")
    private String entityType;


    /**
     * Constructeurs
     */
    public CampaignTypeModel() { this(0, 0, "", false, "", "", false, null, ""); }

    public CampaignTypeModel(int id, int idNelis, String shortname, boolean isDefault, String labelFr,
                             String labelEn, boolean hidden, Links links, String entityType) {
        this.id = id;
        this.idNelis = idNelis;
        this.shortname = shortname;
        this.isDefault = isDefault;
        this.labelFr = labelFr;
        this.labelEn = labelEn;
        this.hidden = hidden;
        this.links = links;
        this.entityType = entityType;
    }


    /**
     * log de l'objet pour le debug
     */
    public void consolePrint() {
        Log.d(TAG, "---------------CAMPAIGN TYPE----------------");
        Log.d(TAG, "ID : "+id);
        Log.d(TAG, "IDNELIS : "+idNelis);
        Log.d(TAG, "SHORTNAME : "+shortname);
        Log.d(TAG, "ISDEFAULT : "+isDefault);
        Log.d(TAG, "LABELFR : "+labelFr);
        Log.d(TAG, "LABELEN : "+labelEn);
        Log.d(TAG, "HIDDEN : "+hidden);
        if(links != null)
            links.consolePrint();
        Log.d(TAG, "ENTITYTYPE : "+entityType);
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
