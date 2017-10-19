package com.sherpa.mynelis.cigref.database;


import android.content.Context;
import android.util.Log;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.api.RestClient;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.utils.PreferencesManager;

import java.util.ArrayList;

import retrofit2.Call;


public class SynchroManager {

    public final static String TAG = "SynchroManager";

    private Context mContext;
    private String mToken;


    /* Constructeur */
    public SynchroManager(Context context, String token) {
        this.mContext = context;
        this.mToken = token;
    }


    public ArrayList<CampaignModel> getMyInvitations() {
        ArrayList<CampaignModel> list = null;

        try {
            NelisInterface nelisInterface = RestClient
                    .getClient(PreferencesManager.getStringPreference(mContext, mContext.getString(R.string.sp_user_domain)), false)
                    .create(NelisInterface.class);

            Call<ArrayList<CampaignModel>> callCampaign = nelisInterface.getInvitations(mToken);
            list = callCampaign.execute().body();
        }
        catch (Exception ex) {
            Log.e(TAG, "Sync Exception - "+ex.getMessage());
            ex.printStackTrace();
        }

        return list;
    }
}
