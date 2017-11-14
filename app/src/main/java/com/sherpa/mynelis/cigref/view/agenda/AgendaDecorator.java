package com.sherpa.mynelis.cigref.view.agenda;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexis Largaiolli on 17/10/2017.
 */

public class AgendaDecorator implements DayViewDecorator {

    private HashMap<CalendarDay, ArrayList<CampaignModel>> eventsByDate;

    public AgendaDecorator(){
        eventsByDate = new HashMap<>();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return eventsByDate.containsKey(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.RED));
    }

    public void addEvent(Date date, CampaignModel event){
        CalendarDay day = CalendarDay.from(date);
        ArrayList events = eventsByDate.get(day);
        if(events == null){
            events = new ArrayList();
        }
        if(!events.contains(events)){
            events.add(event);
        }
        eventsByDate.put(day, events);
    }

    public void remove(Date date, CampaignModel event){
        CalendarDay day = CalendarDay.from(date);
        ArrayList events = eventsByDate.get(day);
        if(events == null){
            return;
        }
        events.remove(event);
        if(events.size() == 0){
            eventsByDate.remove(day);
        }
        else{
            eventsByDate.put(day, events);
        }
    }

    public ArrayList<CampaignModel> getEvents(CalendarDay day){
        return eventsByDate.get(day);
    }
}
