package com.example.sid.marwadishaadi.Dashboard_Favourites;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;

import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;

/**
 * Created by pranay on 02-06-2017.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {
    private static final String TAG = "FavouritesAdapter";
    Context context;
    private List<FavouriteModel> fav;

    public FavouritesAdapter(Context context, List<FavouriteModel> fav) {
        this.context = context;
        this.fav = fav;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_row, parent, false);

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
        Glide.with(context).load(favouriteModel.getImageurl()).into(holder.fav_profile_image);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.remove(position);
                notifyDataSetChanged();
                new RemoveFromFavourite().execute(favouriteModel.getCustomerId());
            }
        });

        holder.sendInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.sendInterest.setText("Interest Sent");
                holder.sendInterest.setEnabled(false);
                new SendInterestFromFavourite().execute(favouriteModel.getCustomerId());

            }
        });

        holder.fav_name_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from","favourites");
                i.putExtra("customerNo",favouriteModel.getCustomerId());
                context.startActivity(i);
            }
        });

        holder.fav_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from","favourites");
                i.putExtra("customerNo",favouriteModel.getCustomerId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fav.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fav_name_age, fav_city, fav_education, favCustomerId;
        ImageView fav_profile_image;
        Button remove, sendInterest;

        public MyViewHolder(View view) {

            super(view);
            favCustomerId = (TextView) view.findViewById(R.id.favCustomerId);
            fav_name_age = (TextView) view.findViewById(R.id.fav_name_age);
            fav_city = (TextView) view.findViewById(R.id.fav_city);
            fav_education = (TextView) view.findViewById(R.id.fav_education);
            fav_profile_image = (ImageView) view.findViewById(R.id.fav_profile_image);
            remove = (Button) view.findViewById(R.id.remove);
            sendInterest = (Button) view.findViewById(R.id.send_interest);


        }
    }

    private class RemoveFromFavourite extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String customerIdToRemove = params[0];

            AndroidNetworking.post(URL + "removeFromFavourite")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("customerNoToRemove", customerIdToRemove)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "onResponse: json response array is " + response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "onResponse: json response array is " + error.toString());
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

            AndroidNetworking.post(URL + "sendInterest")
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
