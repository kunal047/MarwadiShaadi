package com.sid.marwadishaadi.Notifications;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sid.marwadishaadi.R;

import java.util.List;

/**
 * Created by Sid on 02-Jun-17.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private final Context context;
    private List<NotificationsModel> notificationsModelList;

    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_row, parent, false);

        return new NotificationsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        NotificationsModel notificationsModel = notificationsModelList.get(position);

        holder.timestamp.setText(notificationsModel.getTimeStamp());

        if(notificationsModel.isBday())
        {
            String notification = "Marwadishaadi.com wishes you a very happy birthday.";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_birthday)
                    .into(holder.notifimage);

        }

        else if(notificationsModel.isInterestAcc())
        {
            String notification = notificationsModel.getName()+" has accepted your interest request.";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_interest)
                    .into(holder.notifimage);

        }

        else if(notificationsModel.isInterestRec())
        {
            String notification = notificationsModel.getName()+" has sent you an interest request.";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_interest)
                    .into(holder.notifimage);

        }
        else if(notificationsModel.isSuggested())
        {   String notification=null;
            if(notificationsModel.getNumber()>1)
                notification = "you have "+ notificationsModel.getNumber()+" new suggestions.";
            else if(notificationsModel.getNumber()==1)
                notification = "you have "+ notificationsModel.getNumber()+" new suggestion.";


            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_suggestion)
                    .into(holder.notifimage);

        }



        else if(notificationsModel.isMsgRec())
        {
            String notification = notificationsModel.getName()+" has sent you an message.";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_msg)
                    .into(holder.notifimage);

        }

        else if(notificationsModel.isOffers())
        {
            String notification = "Offers!!!";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_offer)
                    .into(holder.notifimage);

        }

        else if(notificationsModel.isPremMem())
        {
            String notification = "Become a member to experience premium features of our service ";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_membership)
                    .into(holder.notifimage);

        }

        else if(notificationsModel.isReminders())
        {
            String notification = "Reminders!!!";
            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_reminder)
                    .into(holder.notifimage);

        }

        else if(!notificationsModel.isMemExp())
        {
            String notification=null ;
            if(notificationsModel.getNumber()>1)
                notification= "Your membership is about to expire in "+ notificationsModel.getNumber()+"days.";
            if(notificationsModel.getNumber()==1)
                notification= "Your membership is going to expire tomorrow";

            holder.notiftext.setText(notification);
            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }
            Glide.with(context)
                    .load(R.drawable.notif_reminder)
                    .into(holder.notifimage);

        }

        else if(notificationsModel.isMemExp())
        {
            String notification = "Your membrship has expired.";
            holder.notiftext.setText(notification);

            if (notificationsModel.isRead()) {
                holder.notiftext.setTypeface(null, Typeface.NORMAL);
            } else {
                holder.notiftext.setTypeface(null, Typeface.BOLD);
            }

            Glide.with(context)
                    .load(R.drawable.notif_membership)
                    .into(holder.notifimage);

        }



    }

    @Override
    public int getItemCount() {
        return notificationsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView notiftext,timestamp;
        ImageView notifimage;

        public MyViewHolder(View view){
            super(view);
            timestamp=(TextView)view.findViewById(R.id.timestamp);
            notiftext=(TextView)view.findViewById(R.id.notiftext);
            notifimage=(ImageView)view.findViewById(R.id.notifimage);
        }

    }

    public NotificationsAdapter(Context context, List<NotificationsModel> notificationsModelList){

        this.context=context;
        this.notificationsModelList = notificationsModelList;

    }



}
