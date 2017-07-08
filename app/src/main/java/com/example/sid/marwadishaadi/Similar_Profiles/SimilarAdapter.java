package com.example.sid.marwadishaadi.Similar_Profiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.R;

import java.util.List;

/**
 * Created by Lawrence Dalmet on 13-06-2017.
 */

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.MyViewHolder> {

    List<SimilarModel> similarModelList;
    Context context;

    public SimilarAdapter(List<SimilarModel> similarModelList, Context context) {
        this.similarModelList = similarModelList;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_item, parent, false);

        return new SimilarAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        SimilarModel similarModel = similarModelList.get(position);

        String name_age = similarModel.getName()+", "+similarModel.getAge();


        holder.name_age.setText(name_age);
        holder.city.setText(similarModel.getCity());
        holder.education.setText(similarModel.getEducation());
        Glide.with(context).load(similarModel.getImgAdd()).into(holder.imgAdd);

    }

    @Override
    public int getItemCount() {
        return similarModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAdd;
        TextView name_age,city,education;
        public MyViewHolder(View itemView) {
            super(itemView);

            imgAdd = (ImageView) itemView.findViewById(R.id.imgAdd);
            name_age = (TextView) itemView.findViewById(R.id.name_age);
            city = (TextView) itemView.findViewById(R.id.city);
            education = (TextView) itemView.findViewById(R.id.education);
        }
    }
}
