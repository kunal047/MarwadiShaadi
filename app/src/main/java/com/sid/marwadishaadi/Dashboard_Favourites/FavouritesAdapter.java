package com.sid.marwadishaadi.Dashboard_Favourites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Notifications.NotificationsModel;
import com.sid.marwadishaadi.Notifications_Util;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pranay on 02-06-2017.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {

    private static final String TAG = "FavouritesAdapter";
    Context context;
    private List<FavouriteModel> fav;
    private String customer_id, customer_name;
    private LinearLayout empty_view_favourites;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private boolean isPaidMember;
    private boolean hasDP;

    public FavouritesAdapter(Context context, List<FavouriteModel> fav, LinearLayout empty_view_favourites) {
        this.context = context;
        this.fav = fav;
        this.empty_view_favourites = empty_view_favourites;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_row, parent, false);


        SharedPreferences sharedpref = itemView.getContext().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_name = sharedpref.getString("firstname", null);

        String[] array = context.getResources().getStringArray(R.array.communities);
        SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
        for (int i = 0; i < 5; i++) {
            if (communityChecker.getString(array[i], "null").contains("Yes")) {
                isPaidMember = true;
            }
        }
        SharedPreferences sharedPref = itemView.getContext().getSharedPreferences("userDp", MODE_PRIVATE);
        hasDP = sharedPref.getBoolean("hasDP", false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FavouriteModel favouriteModel = fav.get(position);

        String name_age = favouriteModel.getName() + ", " + favouriteModel.getAge();

        holder.favCustomerId.setText(favouriteModel.getCustomerId());
        holder.fav_name_age.setText(name_age);
        holder.fav_education.setText(favouriteModel.getHighest_degree());
        holder.fav_city.setText(favouriteModel.getLocation());
        if (!favouriteModel.getInterestStatus().contains("Not")) {
            holder.sendInterest.setText(favouriteModel.getInterestStatus());
            holder.sendInterest.setEnabled(false);
        }

//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.default_drawer)
//                .error(R.drawable.default_drawer);


        if (isPaidMember || hasDP) {
            Glide.with(context)
                    .load(favouriteModel.getImageurl())
                    .placeholder(R.drawable.default_drawer)
                    .error(R.drawable.default_drawer)
                    .fitCenter()
                    .into(holder.fav_profile_image);

            holder.showTextOnPicture.setVisibility(View.GONE);

        } else {
            Glide.with(context)
                    .load(favouriteModel.getImageurl())
                    .placeholder(R.drawable.default_drawer)
                    .error(R.drawable.default_drawer)
                    .fitCenter()
                    .bitmapTransform(new BlurTransformation(context))
                    .into(holder.fav_profile_image);

            holder.showTextOnPicture.setVisibility(View.VISIBLE);

        }


//        Picasso.with(context).load(favouriteModel.getImageurl()).fit().into(holder.fav_profile_image);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.remove(position);
                notifyDataSetChanged();
                if (fav.size() == 0) {
                    if (empty_view_favourites.getVisibility() != View.VISIBLE) {
                        empty_view_favourites.setVisibility(View.VISIBLE);
                    }
                }
                new RemoveFromFavourite().execute(favouriteModel.getCustomerId());
            }
        });

        holder.sendInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.sendInterest.setText("Awaiting");
                holder.sendInterest.setEnabled(false);


                // ========================= NOTIFICATION =======================================


                String touserid = fav.get(position).getCustomerId();

                // adding it to her notifications list
                String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                mDatabase = FirebaseDatabase.getInstance().getReference(touserid).child("Notifications");
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                Date currentDate = calendar.getTime();
                String hash = String.valueOf(currentDate.hashCode());
                final NotificationsModel notification = new NotificationsModel(hash, customer_name, date, 3, false, true, false, false, false, false, false, false, false, false);
                mDatabase.child(hash).setValue(notification);

                // sending push notification to her
                // get all devices

                mDatabases = FirebaseDatabase.getInstance().getReference(touserid).child("Devices");
                mDatabases.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        setData(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //======================================================================

                new SendInterestFromFavourite().execute(favouriteModel.getCustomerId());

            }
        });

        holder.fav_name_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from", "favourites");
                i.putExtra("customerNo", favouriteModel.getCustomerId());
                context.startActivity(i);
            }
        });

        holder.fav_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from", "favourites");
                i.putExtra("customerNo", favouriteModel.getCustomerId());
                context.startActivity(i);
            }
        });
    }

    public void setData(DataSnapshot dataSnapshot) {

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), customer_name + " sent you an Interest", "Marwadi Shaadi: New Interest", "Interest Request");
    }

    @Override
    public int getItemCount() {
        return fav.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fav_name_age, fav_city, fav_education, favCustomerId, showTextOnPicture;
        ImageView fav_profile_image;
        Button remove, sendInterest;


        public MyViewHolder(View view) {

            super(view);
            favCustomerId = view.findViewById(R.id.favCustomerId);
            fav_name_age = view.findViewById(R.id.fav_name_age);
            fav_city = view.findViewById(R.id.fav_city);
            fav_education = view.findViewById(R.id.fav_education);
            fav_profile_image = view.findViewById(R.id.fav_profile_image);
            remove = view.findViewById(R.id.remove);
            sendInterest = view.findViewById(R.id.send_interest);
            showTextOnPicture = view.findViewById(R.id.showTextOnPicture);


        }
    }

    private class RemoveFromFavourite extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String customerIdToRemove = params[0];

            AndroidNetworking.post(Constants.AWS_SERVER + "/removeFromFavourite")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("customerNoToRemove", customerIdToRemove)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        public void onResponse(JSONArray response) {


                        }

                        @Override
                        public void onError(ANError error) {

                            // handle error
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private class SendInterestFromFavourite extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String customerIdToSendInterest = params[0];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = format.format(Calendar.getInstance().getTime());

            AndroidNetworking.post(Constants.AWS_SERVER + "/sendInterest")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("interestSentTo", customerIdToSendInterest)
                    .addBodyParameter("interestSentTime", formatted)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }
    }
}
