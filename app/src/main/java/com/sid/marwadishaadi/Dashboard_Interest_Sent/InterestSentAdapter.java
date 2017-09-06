package com.sid.marwadishaadi.Dashboard_Interest_Sent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.firebase.analytics.FirebaseAnalytics;


import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Lawrence Dalmet on 07-06-2017.
 */


public class
InterestSentAdapter extends RecyclerView.Adapter<InterestSentAdapter.MyViewHolder> {

    private FirebaseAnalytics mFirebaseAnalytics;
    private Context context;
    private List<InterestSentModel> interestSentModelList;
    private boolean isPaidMember;



    public InterestSentAdapter(Context context, List<InterestSentModel> interestSentModelList) {
        this.context = context;
        this.interestSentModelList = interestSentModelList;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Sent Interest view","view");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interestsent, parent, false);

        String[] array = context.getResources().getStringArray(R.array.communities);
        SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
        for (int i = 0; i < 5; i++) {
            if (communityChecker.getString(array[i], "null").contains("Yes")) {
                isPaidMember = true;
            }
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final InterestSentModel interestSentModel = interestSentModelList.get(position);

        String ag = interestSentModel.getName() + ", " + interestSentModel.getAge() + " years";

        if (!isPaidMember) {
            Glide.with(context)
                    .load(interestSentModel.getImgAdd())
                    .centerCrop()
                    .placeholder(R.drawable.default_drawer)
                    .error(R.drawable.default_drawer)
                    .bitmapTransform(new BlurTransformation(context))
                    .into(holder.profilepic);

        } else {
            Glide.with(context)
                    .load(interestSentModel.getImgAdd())
                    .centerCrop()
                    .placeholder(R.drawable.default_drawer)
                    .error(R.drawable.default_drawer)
                    .into(holder.profilepic);

        }

        holder.name_age.setText(ag);
        holder.degree.setText(interestSentModel.getDegree());
        holder.location.setText(interestSentModel.getCity());
        holder.req_status.setText(interestSentModel.getReqStatus());
        holder.date.setText(interestSentModel.getDate());

        holder.profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from","interestSent");
                i.putExtra("customerNo",interestSentModel.getCustomerId());
                context.startActivity(i);
            }
        });


        holder.name_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserProfileActivity.class);
                i.putExtra("from","interestSent");
                i.putExtra("customerNo",interestSentModel.getCustomerId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return interestSentModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_age, location, degree, req_status, date;
        public ImageView profilepic;

        public MyViewHolder(View view) {
            super(view);
            name_age = (TextView) view.findViewById(R.id.name_age);
            location = (TextView) view.findViewById(R.id.location);
            degree = (TextView) view.findViewById(R.id.degree);
            req_status = (TextView) view.findViewById(R.id.req_status);
            profilepic = (ImageView) view.findViewById(R.id.profilepic);
            date = (TextView) view.findViewById(R.id.date);



        }
    }
}
