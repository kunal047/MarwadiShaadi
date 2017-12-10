package com.sid.marwadishaadi.Dashboard_Interest_Received;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Notifications.NotificationsModel;
import com.sid.marwadishaadi.Notifications_Util;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by USER on 01-06-2017.
 */

public class InterestReceivedAdapter extends RecyclerView.Adapter<InterestReceivedAdapter.MyViewHolder> {

    private RecyclerView rv;
    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<InterestReceivedModel> mInterestReceivedModelList;
    private String customer_id, customer_name;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private boolean isPaidMember;
    private boolean hasDP;


    public InterestReceivedAdapter(Context context, List<InterestReceivedModel> interestReceivedModelList, RecyclerView rv) {
        this.context = context;
        this.mInterestReceivedModelList = interestReceivedModelList;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.rv = rv;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);


        SharedPreferences sharedpref = itemView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", "null");
        customer_name = sharedpref.getString("firstname", "null");
        try {
            String[] array = context.getResources().getStringArray(R.array.communities);
            SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
            for (int i = 0; i < 5; i++) {
                if (communityChecker.getString(array[i], "null").contains("Yes")) {
                    isPaidMember = true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }


        SharedPreferences sharedPref = itemView.getContext().getSharedPreferences("userDp", MODE_PRIVATE);

        hasDP = sharedPref.getBoolean("hasDP", false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final InterestReceivedModel interestReceivedModel = mInterestReceivedModelList.get(position);

        String ag = interestReceivedModel.getName() + ", " + interestReceivedModel.getAge();

        holder.customerNo.setText(interestReceivedModel.getCustomerId());


        if (isPaidMember || hasDP) {

            Glide.with(context)
                    .load(interestReceivedModel.getUserImage())
                    .centerCrop()
                    .placeholder(R.drawable.default_drawer)
                    .error(R.drawable.default_drawer)
                    .into(holder.userImage);

            holder.showTextOnPhoto.setVisibility(View.GONE);

        } else {

            Glide.with(context)
                    .load(interestReceivedModel.getUserImage())
                    .centerCrop()
                    .placeholder(R.drawable.default_drawer)
                    .bitmapTransform(new BlurTransformation(context))
                    .error(R.drawable.default_drawer)
                    .into(holder.userImage);

            holder.showTextOnPhoto.setVisibility(View.VISIBLE);

        }


        holder.name.setText(ag);
        holder.highestDegree.setText(interestReceivedModel.getHighestDegree());
        holder.location.setText(interestReceivedModel.getLocation());

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from", "interestReceived");
                i.putExtra("customerNo", interestReceivedModel.getCustomerId());
                context.startActivity(i);

            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from", "interestReceived");
                i.putExtra("customerNo", interestReceivedModel.getCustomerId());
                context.startActivity(i);

            }
        });

        if (interestReceivedModel.getStatus() == 0) {
            holder.status.setText("Accepted");
            holder.status.setBackgroundColor(Color.parseColor("#00c864"));
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
        } else if (interestReceivedModel.getStatus() == 1) {
            holder.status.setText("Rejected");
            holder.status.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
        } else if (interestReceivedModel.getStatus() == 2) {
            holder.status.setText("Awaiting");
            holder.status.setBackgroundColor(Color.parseColor("#7faeff"));
        }

    }

    @Override
    public int getItemCount() {
        return mInterestReceivedModelList.size();
    }

    public void setData(DataSnapshot dataSnapshot) {

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), customer_name + " accepted your Interest", "Marwadi Shaadi: Interest Accepted", "Interest Accept");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImage;
        public TextView name, age, highestDegree, location, status, customerNo, showTextOnPhoto;
        public ImageView accept, reject;

        public MyViewHolder(final View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            name = itemView.findViewById(R.id.textviewName);
            highestDegree = itemView.findViewById(R.id.textviewHighestDegree);
            location = itemView.findViewById(R.id.textviewLocation);
            status = itemView.findViewById(R.id.interest_status);
            accept = itemView.findViewById(R.id.interest_accept);
            reject = itemView.findViewById(R.id.interest_reject);
            customerNo = itemView.findViewById(R.id.textViewCustomerNo);

            showTextOnPhoto = itemView.findViewById(R.id.showTextOnPictureOfInterestReceived);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics, "Interest Accepted", "button");

                    final int position = getAdapterPosition();
                    final InterestReceivedModel interestmodel = mInterestReceivedModelList.get(position);
                    interestmodel.setStatus(0);
                    notifyItemChanged(position);
                    new PrepareInterest().execute(customerNo.getText().toString(), "Accepted");


                    // ===================== NOTIFICATION ===================================

                    String touserid = interestmodel.getCustomerId();

                    // adding it to her notifications list
                    String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                    mDatabase = FirebaseDatabase.getInstance().getReference(touserid).child("Notifications");
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    Date currentDate = calendar.getTime();
                    String hash = String.valueOf(currentDate.hashCode());
                    final NotificationsModel notification = new NotificationsModel(hash, customer_name, date, 3, false, false, true, false, false, false, false, false, false, false);
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

                    Snackbar snackbar = Snackbar
                            .make(rv, "Interest Accepted !", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    interestmodel.setStatus(2);
                                    notifyItemChanged(position);
                                    new PrepareInterest().execute(customerNo.getText().toString(), "Pending");

                                    Snackbar snackbar = Snackbar.make(rv, "Interest restored!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            });

                    snackbar.show();
                }
            });


            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics, "Interest Rejected", "button");

                    final int position = getAdapterPosition();
                    final InterestReceivedModel interestmodel = mInterestReceivedModelList.get(position);
                    interestmodel.setStatus(1);
                    notifyItemChanged(position);
                    new PrepareInterest().execute(customerNo.getText().toString(), "Rejected");
                    Snackbar snackbar = Snackbar
                            .make(rv, "Interest Rejected !", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    interestmodel.setStatus(2);
                                    notifyItemChanged(position);
                                    new PrepareInterest().execute(customerNo.getText().toString(), "Pending");
                                    Snackbar snackbar = Snackbar.make(rv, "Interest restored!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            });

                    snackbar.show();
                }
            });
        }
    }

    private class PrepareInterest extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String clickedId = params[0];
            String status = params[1];
            AndroidNetworking.post(Constants.AWS_SERVER + "/prepareInterest")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("clickedId", clickedId)
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
}
