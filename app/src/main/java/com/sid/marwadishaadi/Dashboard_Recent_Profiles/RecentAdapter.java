package com.sid.marwadishaadi.Dashboard_Recent_Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Notifications.NotificationsModel;
import com.sid.marwadishaadi.Notifications_Util;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;

import java.util.Date;
import java.util.List;



/**
 * Created by USER on 02-06-2017.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

    private static final String TAG = "RecentAdapter";
    private Context context;
    private List<RecentModel> recentModelList;
    private String favouriteState, interestState;
    private String customer_id, customer_gender,customer_name;
    private RecyclerView recentRecyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private View iView;


    public RecentAdapter(Context context, List<RecentModel> recentModelList, RecyclerView recyclerView) {
        this.context = context;
        this.recentModelList = recentModelList;
        this.recentRecyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         iView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent, parent, false);

        SharedPreferences sharedpref = iView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);
        customer_name = sharedpref.getString("firstname",null);
        return new MyViewHolder(iView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        RecentModel recentModel = recentModelList.get(position);
        holder.recentUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from","recent");
                i.putExtra("customerNo",recentModelList.get(position).getRecentCustomerId());

                context.startActivity(i);
            }
        });

        holder.recentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from","recent");
                i.putExtra("customerNo",recentModelList.get(position).getRecentCustomerId());
                
                context.startActivity(i);
            }
        });
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_drawer)
                .error(R.drawable.default_drawer);

        Glide.with(context).load(recentModel.getRecentUserImage()).apply(options).into(holder.recentUserImage);
        holder.recentName.setText(recentModel.getRecentName()+ ", ");
        holder.recentAge.setText(recentModel.getRecentAge());
        holder.recentOnline.setText(recentModel.getRecentOnline());
        holder.recentHighestDegree.setText(recentModel.getRecentHighestDegree());
        holder.recentLocation.setText(recentModel.getRecentLocation());

        if (recentModel.getRecentFavouriteStatus().contains("contain")) {
            holder.sparkButtonFavourite.setChecked(false);
            holder.sparkButtonFavourite.setInactiveImage(R.mipmap.heart_disable);
        } else {
            holder.sparkButtonFavourite.setActiveImage(R.mipmap.ic_fav);
            holder.sparkButtonFavourite.setChecked(true);
        }
        if (recentModel.getRecentInterestStatus().contains("contain")) {
            holder.sparkButtonInterest.setChecked(false);
            holder.sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);
        } else {
            holder.sparkButtonInterest.setActiveImage(R.mipmap.ic_heart);
            holder.sparkButtonInterest.setChecked(true);
        }

        holder.sparkButtonFavourite.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {


                // when its active
                if (buttonState) {
                    holder.sparkButtonFavourite.setChecked(true);
                    holder.sparkButtonFavourite.setActiveImage(R.mipmap.ic_fav);

                    favouriteState = "removed";
                    new RecentAdapter.AddFavouriteFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), favouriteState);
                    Snackbar snackbar = Snackbar.make(recentRecyclerView, "Removed from Favourites", Snackbar.LENGTH_SHORT);
                    snackbar.show();




                } else {
                    holder.sparkButtonFavourite.setChecked(false);
                    holder.sparkButtonFavourite.setInactiveImage(R.mipmap.heart_disable);
                    favouriteState = "added";
                    new RecentAdapter.AddFavouriteFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), favouriteState);
                    Snackbar snackbar = Snackbar.make(recentRecyclerView, "Added to Favourites", Snackbar.LENGTH_LONG);
                    snackbar.show();


                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        holder.sparkButtonInterest.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                if (buttonState) {

                    // ========================= NOTIFICATION =======================================


                    String touserid = recentModelList.get(position).getRecentCustomerId();

                    // adding it to her notifications list
                    String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                    mDatabase = FirebaseDatabase.getInstance().getReference(touserid).child("Notifications");
                    final NotificationsModel notification= new NotificationsModel(customer_name,date,3,false,true,false,false,false,false,false,false,false);
                    String hash = String.valueOf(notification.hashCode());
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
                    interestState = "removed";
                    new RecentAdapter.AddInterestFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), interestState);
                    Snackbar snackbar = Snackbar.make(recentRecyclerView, "Removed from interest", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    holder.sparkButtonInterest.setChecked(true);
                    holder.sparkButtonInterest.setActiveImage(R.mipmap.ic_heart);


                } else {

                    interestState = "added";
                    new RecentAdapter.AddInterestFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), interestState);
                    Snackbar snackbar = Snackbar.make(recentRecyclerView, "Interest Sent", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    holder.sparkButtonInterest.setChecked(false);
                    holder.sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);
                    

                }

            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                // when its active

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


    }
    public void setData(DataSnapshot dataSnapshot){

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), customer_name + " sent you an Interest", "New Interest", "Interest Request");
    }

    @Override
    public int getItemCount() {
        return recentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView recentUserImage;
        TextView recentName, recentAge, recentHighestDegree, recentLocation, recentOnline, recentCustomerId;
        SparkButton sparkButtonInterest, sparkButtonFavourite;


        public MyViewHolder(final View itemView) {

            super(itemView);
            recentCustomerId = (TextView) itemView.findViewById(R.id.recentCustomerId);
            recentUserImage = (ImageView) itemView.findViewById(R.id.recentUserImage);
            recentName = (TextView) itemView.findViewById(R.id.recentTextViewName);
            recentAge = (TextView) itemView.findViewById(R.id.recentTextViewAge);
            recentHighestDegree = (TextView) itemView.findViewById(R.id.recentTextViewEducation);
            recentLocation = (TextView) itemView.findViewById(R.id.recentTextViewCity);
            recentOnline = (TextView) itemView.findViewById(R.id.recentTextViewLastOnline);
            sparkButtonFavourite = (SparkButton) itemView.findViewById(R.id.recentFav);
            sparkButtonInterest = (SparkButton) itemView.findViewById(R.id.recentInterest);





        }
    }

    private class AddInterestFromSuggestion extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            

            String customerId = params[0];
            String interestId = params[1];
            String status = params[2];
            

            AndroidNetworking.post("http://208.91.199.50:5000/addInterestFromSuggestion")
                    .addBodyParameter("customerNo", customerId)
                    .addBodyParameter("interestId", interestId)
                    .addBodyParameter("status", status)
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

    private class AddFavouriteFromSuggestion extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String customerId = params[0];
            String favId = params[1];

            AndroidNetworking.post("http://208.91.199.50:5000/addFavFromSuggestion")
                    .addBodyParameter("customerNo", customerId)
                    .addBodyParameter("favId", favId)
                    .addBodyParameter("status", favouriteState)
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
