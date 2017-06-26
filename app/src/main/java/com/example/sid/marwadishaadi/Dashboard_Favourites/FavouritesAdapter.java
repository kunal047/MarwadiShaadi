package com.example.sid.marwadishaadi.Dashboard_Favourites;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;

import java.util.List;

/**
 * Created by pranay on 02-06-2017.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.MyViewHolder> {
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        FavouriteModel favouriteModel = fav.get(position);

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
            }
        });
    }

    @Override
    public int getItemCount() {
        return fav.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fav_name_age, fav_city, fav_education, favCustomerId ;
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

            fav_name_age.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    context.startActivity(i);
                }
            });

            fav_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    context.startActivity(i);
                }
            });
        }


    }
}
