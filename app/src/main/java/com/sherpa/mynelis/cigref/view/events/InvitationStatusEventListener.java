package com.sherpa.mynelis.cigref.view.events;

import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;

/**
 * Created by Alexis Largaiolli on 20/10/17.
 */
public interface InvitationStatusEventListener {
    public void onUpdateInvitationStatus(Invitation invitation, InvitationStatus status);
}
