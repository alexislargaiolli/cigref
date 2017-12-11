package com.sherpa.mynelis.cigref.model.campaign;


import android.util.Log;

import com.annimon.stream.Stream;
import com.google.gson.annotations.SerializedName;
import com.sherpa.mynelis.cigref.model.common.Links;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CampaignModel implements Serializable {

    private static final String TAG = "CampaignModel";

    private transient int idLocal;

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

    @SerializedName("start_date")
    private Date startDate;

    @SerializedName("closed_date")
    private Date closedDate;

    @SerializedName("event_place")
    private String eventPlace;

    @SerializedName("event_organizer")
    private String eventOrganizer;

    @SerializedName("status")
    private String status;

    @SerializedName("date_creation")
    private String dateCreation;

    @SerializedName("date_update")
    private String dateUpdate;

    @SerializedName("_links")
    private CampaignModelLinks links;

    @SerializedName("entity_type")
    private String entityType;

    @SerializedName("guests_count")
    private int guestsCount;

    @SerializedName("confirmed_count")
    private int confirmedCount;

    @SerializedName("my_status")
    private InvitationStatus myStatus;

    private Invitation myInvitation;

    private boolean invitationLoaded;

    private List<Invitation> invitations = new ArrayList<>();

    /**
     * Constructeurs
     */
    public CampaignModel() {
        this(0, 0, null, "", "", "", new Date(), new Date(), "", "", "", "", "", null, "");
    }

    public CampaignModel(int idLocal, int idNelis, CampaignTypeModel type, String title, String description, String eventDate, Date startDate, Date closedDate, String eventPlace, String eventOrganizer, String status, String dateCreation, String dateUpdate, CampaignModelLinks links, String entityType) {
        this.idLocal = idLocal;
        this.idNelis = idNelis;
        this.type = type;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.startDate = startDate;
        this.closedDate = closedDate;
        this.eventPlace = eventPlace;
        this.eventOrganizer = eventOrganizer;
        this.status = status;
        this.dateCreation = dateCreation;
        this.dateUpdate = dateUpdate;
        this.links = links;
        this.entityType = entityType;
        this.invitations =  new ArrayList<>();
    }

    public void addInvitation(Invitation invit){
        if(invitations == null){
            invitations = new ArrayList<Invitation>();
        }
        int invitPosition = -1;
        for (int i = 0; i < this.invitations.size(); i++) {
            if(invitations.get(i).getId() == invit.getId()){
                invitPosition = i;
            }
        }
        if(invitPosition != -1){
            invitations.set(invitPosition, invit);
        }
        else{
            invitations.add(invit);
        }
    }

    public void removeInvitation(Invitation invit){
        int invitPosition = -1;
        for (int i = 0; i < this.invitations.size(); i++) {
            if(invitations.get(i).getId() == invit.getId()){
                invitPosition = i;
            }
        }
        if(invitPosition != -1){
            invitations.remove(invitPosition);
        }
    }

    public String getPosterUrl(){
        if(links == null){
            return null;
        }
        return links.getPosterUrl();
    }

    public InvitationStatus getInvitationStatus(){
        return myInvitation != null ? myInvitation.getStatus() : InvitationStatus.NO_RESPONSE;
    }

    public long getGuestCount(){
        return this.guestsCount;
    }

    public List<Invitation> getAcceptedInvitations(){
        return Stream.of(invitations).filter(invit -> InvitationStatus.ACCEPTED.equals(invit.getStatus())).toList();
    }

    /**
     * Change the current user invitation status of the campaign
     * @param status
     */
    public void changeInvitationStatus(InvitationStatus status){
        if(isRefused()){
            if(InvitationStatus.ACCEPTED.equals(status)){
                this.confirmedCount++;
            }
        } else if (isAccepted()){
            if(InvitationStatus.REFUSED.equals(status) || InvitationStatus.NO_RESPONSE.equals(status)){
                this.confirmedCount--;
            }
        } else {
            if(InvitationStatus.ACCEPTED.equals(status)){
                this.confirmedCount++;
            }
        }
        myStatus = status;
//        Invitation invit = Stream.of(invitations).filter(i -> i.getId() == invitationId).findFirst().get();
//        if(invit != null) {
//            invit.setStatus(status);
//        }
//        if(myInvitation != null && myInvitation.getId() == invitationId){
//            myInvitation.setStatus(status);
//        }
    }

    public boolean isAccepted(){
        return InvitationStatus.ACCEPTED.equals(this.myStatus);
    }

    public boolean isRefused(){
        return InvitationStatus.REFUSED.equals(this.myStatus);
    }

    public long getNegativeGuestCount(){
        return getConfirmedCount() * -1;
    }

    /**
     * log de l'objet pour le debug
     */
    public void consolePrint() {
        Log.d(TAG, "---------------CAMPAIGN----------------");
        Log.d(TAG, "ID : " + idLocal);
        Log.d(TAG, "IDNELIS : " + idNelis);
        if (type != null)
            type.consolePrint();
        Log.d(TAG, "TITLE : " + title);
        Log.d(TAG, "DESCRIPTION : " + description);
        Log.d(TAG, "EVENTDATE : " + eventDate);
        Log.d(TAG, "EVENTPLACE : " + eventPlace);
        Log.d(TAG, "EVENTORGANIZER : " + eventOrganizer);
        Log.d(TAG, "STARTDATE : " + startDate);
        Log.d(TAG, "CLOSEDDATE : " + closedDate);
        Log.d(TAG, "STATUS : " + status);
        Log.d(TAG, "DATECREATION : " + dateCreation);
        Log.d(TAG, "DATEUPDATE : " + dateUpdate);
        if (links != null)
            Log.d(TAG, links.toString());
        Log.d(TAG, "ENTITYTYPE : " + entityType);
    }

    @Override
    public String toString() {
        return "CampaignModel{" +
                "idLocal=" + idLocal +
                ", idNelis=" + idNelis +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", startDate=" + startDate +
                ", closedDate=" + closedDate +
                ", eventPlace='" + eventPlace + '\'' +
                ", eventOrganizer='" + eventOrganizer + '\'' +
                ", status='" + status + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", dateUpdate='" + dateUpdate + '\'' +
                ", links=" + links +
                ", entityType='" + entityType + '\'' +
                ", myInvitation=" + myInvitation +
                ", invitations=" + invitations +
                '}';
    }

    /**
     * Getters & setters
     */

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
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

    public CampaignModelLinks getLinks() {
        return links;
    }

    public void setLinks(CampaignModelLinks links) {
        this.links = links;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Invitation getMyInvitation() {
        return myInvitation;
    }

    public void setMyInvitation(Invitation myInvitation) {
        this.myInvitation = myInvitation;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    public InvitationStatus getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(InvitationStatus myStatus) {
        this.myStatus = myStatus;
    }

    public boolean isInvitationLoaded() {
        return invitationLoaded;
    }

    public void setInvitationLoaded(boolean invitationLoaded) {
        this.invitationLoaded = invitationLoaded;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(int confirmedCount) {
        this.confirmedCount = confirmedCount;
    }
}
