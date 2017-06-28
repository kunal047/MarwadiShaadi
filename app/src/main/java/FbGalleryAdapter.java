import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.R;

import java.util.List;

/**
 * Created by Sid on 28-Jun-17.
 */

public class FbGalleryAdapter extends RecyclerView.Adapter<FbGalleryAdapter.MyViewHolder> {
    private List<FbGalleryModel> fbGalleryModelList;
    private Context context;


    public FbGalleryAdapter(List<FbGalleryModel> fbGalleryModelList, Context context) {
        this.fbGalleryModelList = fbGalleryModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fb_gallery_row, parent, false);
        return new FbGalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        FbGalleryModel fbGalleryModel=fbGalleryModelList.get(position);
        Glide.with(context).load(fbGalleryModel.getUrl()).into(holder.fb_pic);
    }

    @Override
    public int getItemCount() {
        return fbGalleryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView fb_pic;

        public MyViewHolder(View itemView) {
            super(itemView);

            fb_pic=(ImageView)itemView.findViewById(R.id.fb_gallery_pic);
        }
    }
}
