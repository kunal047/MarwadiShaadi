package com.sid.marwadishaadi.FB_Gallery_Photo_Upload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sid.marwadishaadi.R;

import java.util.List;

/**
 * Created by Sid on 28-Jun-17.
 */

public class FbGalleryAdapter extends RecyclerView.Adapter<FbGalleryAdapter.MyViewHolder> {

    private List<FbGalleryModel> fbGalleryModelList;
    private Context context;
    private int count;
    private OnPicSelectedListener onPicSelectedListener;

    public FbGalleryAdapter(Context context, List<FbGalleryModel> fbGalleryModelList) {
        this.fbGalleryModelList = fbGalleryModelList;
        this.context = context;
    }

    public void setListener(OnPicSelectedListener listener) {
        this.onPicSelectedListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fb_gallery_row, parent, false);
        return new FbGalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FbGalleryModel fbGalleryModel = fbGalleryModelList.get(position);

        Glide.with(context)
                .load(fbGalleryModel.getUrl())
                .into(holder.fb_pic);
    }

    @Override
    public int getItemCount() {
        return fbGalleryModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView fb_pic;
        public ImageView selected;

        public MyViewHolder(View itemView) {
            super(itemView);
            fb_pic = itemView.findViewById(R.id.fb_gallery_pic);
            selected = itemView.findViewById(R.id.is_selected);
            fb_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    FbGalleryModel fbmodel = fbGalleryModelList.get(position);
                    if (selected.getVisibility() == View.GONE) {
                        fbmodel.setSelected(true);
                        selected.setVisibility(View.VISIBLE);
                        count++;
                        if (onPicSelectedListener != null) {
                            onPicSelectedListener.updateToolbar(count);
                        }
                    } else {
                        fbmodel.setSelected(false);
                        selected.setVisibility(View.GONE);
                        count--;
                        if (onPicSelectedListener != null) {
                            onPicSelectedListener.updateToolbar(count);
                        }
                    }
                }
            });
        }
    }
}
