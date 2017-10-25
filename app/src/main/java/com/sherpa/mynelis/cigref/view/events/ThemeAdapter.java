package com.sherpa.mynelis.cigref.view.events;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;

/**
 * Created by Alexis Largaiolli on 24/10/17.
 */
public class ThemeAdapter extends ArrayAdapter<CampaignTypeModel> {

    public ThemeAdapter(@NonNull Context context, CampaignTypeModel[] themes) {
        super(context, 0, themes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView text = (TextView)convertView.findViewById(android.R.id.text1);
        text.setText(getItem(position).getLabelFr());
        text.setTextColor(Color.GRAY);
        return convertView;
    }
}
