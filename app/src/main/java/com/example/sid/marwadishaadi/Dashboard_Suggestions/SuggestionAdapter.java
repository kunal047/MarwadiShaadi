package com.example.sid.marwadishaadi.Dashboard_Suggestions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sid.marwadishaadi.Chat.DefaultMessagesActivity;
import com.example.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Lawrence Dalmet on 31-05-2017.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private final Context context;
    View iView;
    private DatabaseReference mDatabase;
    private List<SuggestionModel> suggestionModelList;
    private RecyclerView rv;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String favouriteState, interestState;
    private String customer_id;

    public SuggestionAdapter(Context context, List<SuggestionModel> suggestionModelList, RecyclerView recyclerView) {

        this.suggestionModelList = suggestionModelList;
        this.context = context;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.rv = recyclerView;

    }

    @Override
    public SuggestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        iView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.suggestions_row, parent, false);


        SharedPreferences sharedpref = iView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        
        return new SuggestionAdapter.MyViewHolder(iView);
    }

    @Override
    public void onBindViewHolder(SuggestionAdapter.MyViewHolder holder, final int position) {
        SuggestionModel suggest = suggestionModelList.get(position);


        String ag = suggest.getName() + ", " + suggest.getAge();
        String cd = "None";
        if (suggest.getDesignation().length() > 0 && suggest.getComapany().length() == 0) {
            cd = suggest.getDesignation();
        } else if (suggest.getDesignation().length() == 0 && suggest.getComapany().length() > 0) {
            cd = suggest.getComapany();
        } else if (suggest.getDesignation().length() > 0 && suggest.getComapany().length() > 0) {
            cd = suggest.getComapany() + ", " + suggest.getDesignation();
        } else if (suggest.getDesignation().length() == 0 && suggest.getComapany().length() == 0) {
            cd = "Not Mentioned";
        }
        holder.name.setText(ag);
        holder.cusId.setText(suggest.getCusId());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_drawer)
                .error(R.drawable.default_drawer);
        Glide.with(context)
                .load(suggest.getImgAdd())
                .apply(options)
                .into(holder.imgAdd);

        holder.height.setText(suggest.getHeight());
        holder.workLoc.setText(suggest.getWorkLoc());

        holder.annInc.setText(suggest.getAnnInc().replace("000000", "0L").replace("00000", "L"));

        holder.hometown.setText(suggest.getHometown());
        holder.mariSta.setText(suggest.getMariSta());
        holder.company.setText(cd);
        holder.highDeg.setText(suggest.getHighDeg());

        if (suggest.getFavouriteStatus().toCharArray()[0] == '1') {
            
            holder.sparkButtonFav.setChecked(false);
            holder.sparkButtonFav.setInactiveImage(R.mipmap.heart_disable);
        }
        if (!suggest.getInterestStatus().contains("Not")) {
            holder.sparkButtonInterest.setChecked(false);
            holder.sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);
        }

        holder.sparkButtonFav.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {


                // when its active
                if (buttonState) {

                    favouriteState = "added";
                    new AddFavouriteFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), favouriteState);

                    Snackbar snackbar = Snackbar.make(rv, "Added to Favourites", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {

                    favouriteState = "removed";
                    new AddFavouriteFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), favouriteState);
                    Snackbar snackbar = Snackbar.make(rv, "Removed from Favourites", Snackbar.LENGTH_SHORT);
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

                    mDatabase = FirebaseDatabase.getInstance().getReference("users");

                    mDatabase.child(suggestionModelList.get(position).getCusId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                     /*       DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
                            String registration_id = device.getDevice_id();
                            SharedPreferences sharedpref = context.getSharedPreferences("userinfo", MODE_PRIVATE);
                            String customer_name = sharedpref.getString("customer_name", null);
                            String body = customer_name + " has sent you an Interest";
                            // sending notification
                            Notifications_Util.SendNotification(registration_id,body,"New Interest","Interest Request");*/

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    
                    interestState = "added";
                    new AddInterestFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), interestState);
                    Snackbar snackbar = Snackbar.make(rv, "Interest Sent", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    
                    interestState = "removed";
                    new AddInterestFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), interestState);
                    Snackbar snackbar = Snackbar.make(rv, "Removed from interest", Snackbar.LENGTH_SHORT);
                    snackbar.show();
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

    @Override
    public int getItemCount() {
        

        return suggestionModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, cusId, highDeg, workLoc, height, company, annInc, mariSta, hometown;
        ImageView imgAdd;
        SparkButton sparkButtonChat, sparkButtonInterest, sparkButtonFav;


        public MyViewHolder(View view) {

            super(view);
            name = (TextView) view.findViewById(R.id.name);
            cusId = (TextView) view.findViewById(R.id.cusId);
            imgAdd = (ImageView) view.findViewById(R.id.imgAdd);
            highDeg = (TextView) view.findViewById(R.id.highDeg);
            workLoc = (TextView) view.findViewById(R.id.workLoc);
            height = (TextView) view.findViewById(R.id.height);
            company = (TextView) view.findViewById(R.id.company);
            annInc = (TextView) view.findViewById(R.id.annInc);
            mariSta = (TextView) view.findViewById(R.id.mariSta);
            hometown = (TextView) view.findViewById(R.id.hometown);
            sparkButtonChat = (SparkButton) view.findViewById(R.id.chat);
            sparkButtonFav = (SparkButton) view.findViewById(R.id.fav);
            sparkButtonInterest = (SparkButton) view.findViewById(R.id.interest);

            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("customerNo", cusId.getText());
                    i.putExtra("from", "suggestion");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("customerNo", cusId.getText());
                    i.putExtra("from", "suggestion");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            sparkButtonChat.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {


                    int counter = 0;
                    String[] array = context.getResources().getStringArray(R.array.communities);
                    SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
                    for (int i = 0; i < 5; i++) {
                        if (communityChecker.getString(array[i], "null").contains("Yes")) {
                            counter++;
                        }
                    }
                    if (counter > 0) {
                        Intent intent = new Intent(context, DefaultMessagesActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("customerName", name.getText().toString());
                        extras.putString("customerId", cusId.getText().toString());
                        intent.putExtras(extras);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "This feature is only for premium members", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, UpgradeMembershipActivity.class);
                        context.startActivity(intent);
                    }
                }


                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });

        }
    }

    public class AddInterestFromSuggestion extends AsyncTask<String, Void, Void> {


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

    public class AddFavouriteFromSuggestion extends AsyncTask<String, Void, Void> {

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

}
