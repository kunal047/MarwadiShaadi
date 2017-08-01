package com.example.sid.marwadishaadi.Dashboard_Favourites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by pranay on 02-06-2017.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {
    private static final String TAG = "FavouritesAdapter";
    Context context;
    private List<FavouriteModel> fav;
    private String customer_id;
    private LinearLayout empty_view_favourites;

    public FavouritesAdapter(Context context, List<FavouriteModel> fav,LinearLayout empty_view_favourites) {
        this.context = context;
        this.fav = fav;
        this.empty_view_favourites = empty_view_favourites;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_row, parent, false);


        SharedPreferences sharedpref = itemView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

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
        Picasso.with(context).load(favouriteModel.getImageurl()).fit().into(holder.fav_profile_image);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fav.remove(position);
                notifyDataSetChanged();
                if (fav.size() == 0){
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

            AndroidNetworking.post("http://208.91.199.50:5000/removeFromFavourite")
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

            AndroidNetworking.post("http://208.91.199.50:5000/sendInterest")
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
