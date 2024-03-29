package com.sherpa.mynelis.cigref.view.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * {@link RecyclerView.Adapter} that can display a user avatar
 */
public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.ViewHolder> {

    private List<Invitation> mContacts;
    private Context mContext;

    public ContactListRecyclerViewAdapter(List<Invitation> items, Context context) {
        mContacts = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event_details_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mContacts.get(position);
        String imageUrl = holder.mItem.getPosterUrl();
        if(imageUrl != null) {
            Picasso.with(mContext).load(imageUrl).into(holder.mProfileImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    holder.mProfileImage.setImageResource(R.mipmap.ic_avatar_default);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public List<Invitation> getmContacts() {
        return mContacts;
    }

    public void setmContacts(List<Invitation> mContacts) {
        this.mContacts = mContacts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView mProfileImage;
        public Invitation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProfileImage = (CircleImageView) view.findViewById(R.id.eventDetailsContactRegisteredProfileImage);
        }
    }
}
