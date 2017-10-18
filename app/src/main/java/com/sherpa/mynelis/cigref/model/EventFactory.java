package com.sherpa.mynelis.cigref.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pascal on 14/10/2017.
 */

public class EventFactory implements Serializable {

    public static Event createEvent() {

        CigrefUser cigrefUser = new CigrefUser("Desmart", "Patrick", "http://www.cigref.fr/wp/wp-content/uploads/2017/06/P-LAURENS-FRINGS.png");
        List<CigrefUser> participantList = new ArrayList<CigrefUser>();
        for (int i = 1; i < 25; i++) {
            participantList.add(cigrefUser);
        }

        String eventDescription = "Set android:orientation to specify whether child." +
                " To control how linear layout aligns all the views it contains." +
                " For example, the snippet above sets android:gravity to 'center'. The value you" +
                " set affects both horizontal and vertical alignment of all child views within the" +
                " single row or column.You can set on individual child views to specify how" +
                " linear layout divides remaining space amongst the views it contains." +
                " See the Linear Layout guide for an example.See LinearLayout.LayoutParams to" +
                " learn about other attributes you can set on a child view to affect its position" +
                " and size in the containing linear layout.";

        EventAddress eventAddress = new EventAddress(
                "Halle Tody Garnier",
                "20 Place docteurs Charles et Christophe Mérieux",
                "69007",
                "Lyon"
        );

        EventDate eventDate = new EventDate("Samedi 28 Octobre", 14, 18);

        return new Event(
                1,
                "http://www.cigref.fr/wp/wp-content/uploads/2017/01/delegation-cigref.jpg",
                "Michel Serres, Philosophie du numériques",
                "Conférence CIGREF",
                participantList,
                0,
                eventDescription,
                eventAddress,
                eventDate,
                cigrefUser,
                "Montpellier",
                new Date()
        );
    }

    public static Event[] createEvents(int count) {
        Event[] events = new Event[count];
        for (int i = 0; i < count - 1; i++) {
            events[i] = EventFactory.createEvent();
        }
        return events;
    }

}
