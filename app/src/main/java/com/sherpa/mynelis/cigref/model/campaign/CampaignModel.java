package com.sherpa.mynelis.cigref.model.campaign;


import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.model.common.Links;


public class CampaignModel {

    private static final String TAG = "CampaignModel";

    private int id;
    @SerializedName("id")
    private int idNelis;
    @SerializedName("type")
    private CampaignTypeModel type;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("event_date")
    private String eventDate;
    @SerializedName("event_place")
    private String eventPlace;
    @SerializedName("event_organizer")
    private String eventOrganizer;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("closed_date")
    private String closedDate;
    @SerializedName("status")
    private String status;
    @SerializedName("date_creation")
    private String dateCreation;
    @SerializedName("date_update")
    private String dateUpdate;
    @SerializedName("_links")
    private Links links;
    @SerializedName("entity_type")
    private String entityType;


    /**
     * Constructeurs
     */
    public CampaignModel() { this(0, 0, null, "", "", "", "", "", "", "", "", "", "", null, ""); }

    public CampaignModel(int id, int idNelis, CampaignTypeModel type, String title, String description,
                         String eventDate, String eventPlace, String eventOrganizer, String startDate,
                         String closedDate, String status, String dateCreation, String dateUpdate,
                         Links links, String entityType) {
        this.id = id;
        this.idNelis = idNelis;
        this.type = type;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.eventPlace = eventPlace;
        this.eventOrganizer = eventOrganizer;
        this.startDate = startDate;
        this.closedDate = closedDate;
        this.status = status;
        this.dateCreation = dateCreation;
        this.dateUpdate = dateUpdate;
        this.links = links;
        this.entityType = entityType;
    }



    /**
     * log de l'objet pour le debug
     */
    public void consolePrint() {
        Log.d(TAG, "---------------CAMPAIGN----------------");
        //Log.d(TAG, "ID : "+id);
        Log.d(TAG, "IDNELIS : "+idNelis);
        if(type != null)
            type.consolePrint();
        Log.d(TAG, "TITLE : "+title);
        Log.d(TAG, "DESCRIPTION : "+description);
        Log.d(TAG, "EVENTDATE : "+eventDate);
        Log.d(TAG, "EVENTPLACE : "+eventPlace);
        Log.d(TAG, "EVENTORGANIZER : "+eventOrganizer);
        Log.d(TAG, "STARTDATE : "+startDate);
        Log.d(TAG, "CLOSEDDATE : "+closedDate);
        Log.d(TAG, "STATUS : "+status);
        Log.d(TAG, "DATECREATION : "+dateCreation);
        Log.d(TAG, "DATEUPDATE : "+dateUpdate);
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

    public CampaignTypeModel getType() {
        return type;
    }

    public void setType(CampaignTypeModel type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
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
