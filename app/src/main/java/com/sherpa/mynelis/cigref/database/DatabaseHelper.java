package com.sherpa.mynelis.cigref.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constantes
     */
    /* BDD */
    private static final String DATABASE_NAME = "CigrefDB";
    private static final int DATABASE_VERSION = 1;

    /* Tables */
    private static final String TABLE_PEOPLE = "people";
    private static final String TABLE_CAMPAIGN = "campaign";
    private static final String TABLE_CAMPAIGN_TYPE = "campaign_type";
    private static final String TABLE_LINKS = "links";
    private static final String TABLE_INVITATION = "invitation";

    /* Champs communs */
    private static final String KEY_ID = "id";
    private static final String NELIS_ID = "nelis_id";

    /* Table People */
    private static final String PEOPLE_LASTNAME = "lastname";
    private static final String PEOPLE_FIRSTNAME = "firstname";
    private static final String PEOPLE_FULLNAME = "fullname";
    private static final String PEOPLE_ID_COURTESY = "id_courtesy";
    private static final String PEOPLE_ID_COMPANY = "id_company";
    private static final String PEOPLE_COMPANY_NAME = "company_name";
    private static final String PEOPLE_TITLE = "title";
    private static final String PEOPLE_MAIL = "mail";
    private static final String PEOPLE_PHONE = "phone";
    private static final String PEOPLE_ADDRESS = "address";
    private static final String PEOPLE_POSTAL_CODE = "postal_code";
    private static final String PEOPLE_CITY = "city";
    private static final String PEOPLE_COUNTRY = "country";
    private static final String PEOPLE_SEX = "sex";
    private static final String PEOPLE_AGE = "age";

    /* Table Campaign */
    private static final String CAMPAIGN_TYPE_ID = "type_id";
    private static final String CAMPAIGN_TITLE = "title";
    private static final String CAMPAIGN_DESCRIPTION = "description";
    private static final String CAMPAIGN_EVENT_DATE = "event_date";
    private static final String CAMPAIGN_EVENT_PLACE = "event_place";
    private static final String CAMPAIGN_EVENT_ORGANIZER = "event_organizer";
    private static final String CAMPAIGN_START_DATE = "start_date";
    private static final String CAMPAIGN_CLOSED_DATE = "closed_date";
    private static final String CAMPAIGN_STATUS = "status";
    private static final String CAMPAIGN_DATE_CREATION = "date_creation";
    private static final String CAMPAIGN_DATE_UPDATE = "date_update";
    private static final String CAMPAIGN_LINKS_ID = "links_id";
    private static final String CAMPAIGN_ENTITY_TYPE = "entity_type";

    /* Table Campaign Type */
    private static final String CAMPAIGN_TYPE_SHORTNAME = "shortname";
    private static final String CAMPAIGN_TYPE_ISDEFAULT = "is_default";
    private static final String CAMPAIGN_TYPE_LABEL_FR = "label_fr";
    private static final String CAMPAIGN_TYPE_LABEL_EN = "label_en";
    private static final String CAMPAIGN_TYPE_HIDDEN = "hidden";
    private static final String CAMPAIGN_TYPE_LINKS_ID = "links_id";
    private static final String CAMPAIGN_TYPE_ENTITY_TYPE = "entity_type";

    /* Table Links */
    private static final String LINKS_SELF = "self";
    private static final String LINKS_ACCOUNT = "account";
    private static final String LINKS_COMPANY = "company";
    private static final String LINKS_CREATOR = "creator";
    private static final String LINKS_TYPE = "type";
    private static final String LINKS_COURTESY = "courtesy";

    /* Scripts de creation des tables */
    /* Creation TABLE_CAMPAIGN */
    private static final String CREATE_TABLE_CAMPAIGN = "CREATE TABLE "
            + TABLE_CAMPAIGN + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
            + NELIS_ID + " INTEGER, " + CAMPAIGN_TYPE_ID + " INTEGER, "
            + CAMPAIGN_TITLE + " TEXT, " + CAMPAIGN_DESCRIPTION + " TEXT, "
            + CAMPAIGN_EVENT_DATE + " TEXT, " + CAMPAIGN_EVENT_PLACE + " TEXT, "
            + CAMPAIGN_EVENT_ORGANIZER + " TEXT, " + CAMPAIGN_START_DATE + " TEXT, "
            + CAMPAIGN_CLOSED_DATE + " TEXT, " + CAMPAIGN_STATUS + " TEXT, "
            + CAMPAIGN_DATE_CREATION + " TEXT, " + CAMPAIGN_DATE_UPDATE + " TEXT, "
            + CAMPAIGN_LINKS_ID + " INTEGER, " + CAMPAIGN_ENTITY_TYPE + " TEXT"
            + ")";

    /* Creation TABLE_CAMPAIGN_TYPE */
    private static final String CREATE_TABLE_CAMPAIGN_TYPE = "CREATE TABLE "
            + TABLE_CAMPAIGN + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
            + NELIS_ID + " INTEGER, " + CAMPAIGN_TYPE_SHORTNAME + " TEXT, "
            + CAMPAIGN_TYPE_ISDEFAULT + " INTEGER, " + CAMPAIGN_TYPE_LABEL_FR + " TEXT, "
            + CAMPAIGN_TYPE_LABEL_EN + " TEXT, " + CAMPAIGN_TYPE_HIDDEN + " INTEGER, "
            + CAMPAIGN_TYPE_LINKS_ID + " INTEGER, " + CAMPAIGN_TYPE_ENTITY_TYPE + " TEXT"
            + ")";

    /* Creation TABLE_LINKS */
    private static final String CREATE_TABLE_LINKS = "CREATE TABLE "
            + TABLE_LINKS + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
            + NELIS_ID + " INTEGER, " + LINKS_SELF + " TEXT, "
            + LINKS_ACCOUNT + " TEXT, " + LINKS_COMPANY + " TEXT, "
            + LINKS_CREATOR + " TEXT, " + LINKS_TYPE + " TEXT, "
            + LINKS_COURTESY + " TEXT"
            + ")";


    /**
     * Constructeur
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /**
     * Overrides
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CAMPAIGN);
        db.execSQL(CREATE_TABLE_CAMPAIGN_TYPE);
        db.execSQL(CREATE_TABLE_LINKS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CAMPAIGN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CAMPAIGN_TYPE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LINKS);
        onCreate(db);
    }



    /************************* CAMPAIGN CRUD *************************/

    /**
     * Creation
     */
    public long createCampaign(CampaignModel campaign, boolean temp) {

        //check si existe deja
        if(!temp) {
            if (campaignExists(campaign.getIdNelis()))
                return 0;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NELIS_ID, campaign.getIdNelis());
        values.put(CAMPAIGN_TYPE_ID, campaign.getType().getId());
        values.put(CAMPAIGN_TITLE, campaign.getTitle());
        values.put(CAMPAIGN_DESCRIPTION, campaign.getDescription());
        values.put(CAMPAIGN_EVENT_DATE, campaign.getEventDate());
        values.put(CAMPAIGN_EVENT_PLACE, campaign.getEventPlace());
        values.put(CAMPAIGN_EVENT_ORGANIZER, campaign.getEventOrganizer());
        values.put(CAMPAIGN_START_DATE, campaign.getStartDate());
        values.put(CAMPAIGN_CLOSED_DATE, campaign.getClosedDate());
        values.put(CAMPAIGN_STATUS, campaign.getStatus());
        values.put(CAMPAIGN_DATE_CREATION, campaign.getDateCreation());
        values.put(CAMPAIGN_DATE_UPDATE, campaign.getDateUpdate());
        values.put(CAMPAIGN_LINKS_ID, campaign.getLinks().getId());
        values.put(CAMPAIGN_ENTITY_TYPE, campaign.getEntityType());


        long id = db.insert(TABLE_CAMPAIGN, null, values);

        return id;
    }


    /**
     * Lecture
     */
    public CampaignModel readCampaign(long id, boolean localId) {
        SQLiteDatabase db = this.getReadableDatabase();
        CampaignModel c = new CampaignModel();

        String query = "";
        if(localId)
            query = "SELECT * FROM " + TABLE_CAMPAIGN + " WHERE " + KEY_ID + " = " + id;
        else query = "SELECT * FROM " + TABLE_CAMPAIGN + " WHERE " + NELIS_ID + " = " + id;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null && cursor.moveToFirst()) {
            c.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            c.setIdNelis(cursor.getInt(cursor.getColumnIndex(NELIS_ID)));
            c.setTitle(cursor.getString(cursor.getColumnIndex(CAMPAIGN_TITLE)));
            c.setDescription(cursor.getString(cursor.getColumnIndex(CAMPAIGN_DESCRIPTION)));
            c.setEventDate(cursor.getString(cursor.getColumnIndex(CAMPAIGN_EVENT_DATE)));
            c.setEventPlace(cursor.getString(cursor.getColumnIndex(CAMPAIGN_EVENT_PLACE)));
            c.setEventOrganizer(cursor.getString(cursor.getColumnIndex(CAMPAIGN_EVENT_ORGANIZER)));
            c.setStartDate(cursor.getString(cursor.getColumnIndex(CAMPAIGN_START_DATE)));
            c.setClosedDate(cursor.getString(cursor.getColumnIndex(CAMPAIGN_CLOSED_DATE)));
            c.setStatus(cursor.getString(cursor.getColumnIndex(CAMPAIGN_STATUS)));
            c.setDateCreation(cursor.getString(cursor.getColumnIndex(CAMPAIGN_DATE_CREATION)));
            c.setDateUpdate(cursor.getString(cursor.getColumnIndex(CAMPAIGN_DATE_UPDATE)));
            //c.setlinks
            c.setEntityType(cursor.getString(cursor.getColumnIndex(CAMPAIGN_ENTITY_TYPE)));
        }

        cursor.close();
        return c;
    }


    /**
     * Exists
     */
    public boolean campaignExists(long idNelis) {
        CampaignModel c = readCampaign(idNelis, false);
        if(c.getId() == 0)
            return false;
        else return true;
    }
}