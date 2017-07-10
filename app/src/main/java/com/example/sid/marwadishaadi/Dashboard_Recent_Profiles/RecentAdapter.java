package com.example.sid.marwadishaadi.Dashboard_Recent_Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;

import java.util.List;

import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


/**
 * Created by USER on 02-06-2017.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

    private static final String TAG = "RecentAdapter";
    private Context context;
    private List<RecentModel> recentModelList;
    private String favouriteState, interestState;
    private String customer_id, customer_gender;
    View iView;


    public RecentAdapter(Context context, List<RecentModel> recentModelList) {
        this.context = context;
        this.recentModelList = recentModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         iView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent, parent, false);

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(context);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);
        return new MyViewHolder(iView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
                Log.d(TAG, "onClick: customer id is-------"+recentModelList.get(position).getRecentCustomerId());
                context.startActivity(i);
            }
        });


        Glide.with(context).load(recentModel.getRecentUserImage()).into(holder.recentUserImage);
        holder.recentName.setText(recentModel.getRecentName());
        holder.recentAge.setText(recentModel.getRecentAge());
        holder.recentOnline.setText(recentModel.getRecentOnline());
        holder.recentHighestDegree.setText(recentModel.getRecentHighestDegree());
        holder.recentLocation.setText(recentModel.getRecentLocation());
        Log.d(TAG, "onBindViewHolder: recentModel.getRecentFavouriteStatus().toCharArray()[0]" + recentModel.getRecentFavouriteStatus().toCharArray()[0]);
        if (recentModel.getRecentFavouriteStatus().contains("contain")) {
            holder.sparkButtonFavourite.setChecked(false);
            holder.sparkButtonFavourite.setInactiveImage(R.mipmap.heart_disable);
        }
        if (recentModel.getRecentInterestStatus().contains("contain")) {
            holder.sparkButtonInterest.setChecked(false);
            holder.sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);
        }
        holder.sparkButtonFavourite.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {


                // when its active
                if (buttonState) {

                    favouriteState = "added";
                    new RecentAdapter.AddFavouriteFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), favouriteState);
                    Snackbar snackbar = Snackbar.make(iView, "Added to Favourites", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {

                    favouriteState = "removed";
                    new RecentAdapter.AddFavouriteFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), favouriteState);
                    Snackbar snackbar = Snackbar.make(iView, "Removed from Favourites", Snackbar.LENGTH_SHORT);
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
                    Log.d(TAG, "onEvent: interest added ^^^^^^^^^^^^^^^^^^^^^^^^^^^ ");
                    interestState = "added";
                    new RecentAdapter.AddInterestFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), interestState);
                    Snackbar snackbar = Snackbar.make(iView, "Interest Sent", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } else {
                    Log.d(TAG, "onEvent: interest removed ^^^^^^^^^^^^^^^^^^^^^^^^^^^ ");
                    interestState = "removed";
                    new RecentAdapter.AddInterestFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), interestState);
                    Snackbar snackbar = Snackbar.make(iView, "Removed from interest", Snackbar.LENGTH_SHORT);
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

            Log.d(TAG, "doInBackground: in here ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ");

            String customerId = params[0];
            String interestId = params[1];
            String status = params[2];
            Log.d(TAG, "doInBackground: interest is ------------------ " + status);

            AndroidNetworking.post(URL + "addInterestFromSuggestion")
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

            AndroidNetworking.post(URL + "addFavFromSuggestion")
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
