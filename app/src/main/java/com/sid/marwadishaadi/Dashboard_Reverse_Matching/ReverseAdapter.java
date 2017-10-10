package com.sid.marwadishaadi.Dashboard_Reverse_Matching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sid.marwadishaadi.OnLoadMoreListener;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sid on 31-May-17.
 */

public class ReverseAdapter extends RecyclerView.Adapter {

    private final int VIEW_PROG = 0;
    private final int VIEW_ITEM = 1;
    private boolean reverseIsLoading = false;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount, previousTotal = 0;
    private ProgressBar progressBar;
    private RecyclerView reverseRecyclerView;
    private boolean isPaidMember;
    private boolean hasDP;


    private List<ReverseModel> reverseModelList;
    private Context context;

    public ReverseAdapter(List<ReverseModel> reverseModelList, Context context, RecyclerView recyclerView) {
        this.reverseModelList = reverseModelList;
        this.context = context;
        this.reverseRecyclerView = recyclerView;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findFirstVisibleItemPositions(null)[0];


                if (reverseIsLoading) {
                    if (totalItemCount > previousTotal + 1) {

                        reverseIsLoading = false;
                        previousTotal = totalItemCount;

                    }
                }

                if (!reverseIsLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something

                    reverseIsLoading = true;
                    try {
                        mOnLoadMoreListener.onLoadMore();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

    }

    public void setLoaded() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        reverseIsLoading = false;

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {

        return reverseModelList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reverse_row, parent, false);
            String[] array = context.getResources().getStringArray(R.array.communities);

            SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
            for (int i = 0; i < 5; i++) {
                if (communityChecker.getString(array[i], "null").contains("Yes")) {
                    isPaidMember = true;
                }
            }

            SharedPreferences sharedPref = itemView.getContext().getSharedPreferences("userDp", MODE_PRIVATE);
            hasDP = sharedPref.getBoolean("hasDP", false);

            return new ReverseViewHolder(itemView);

        } else {
            View iView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);

            SharedPreferences sharedPref = iView.getContext().getSharedPreferences("userDp", MODE_PRIVATE);
            hasDP = sharedPref.getBoolean("hasDP", false);

            return new ProgressViewHolder(iView);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ReverseViewHolder) {


            final ReverseModel rev = reverseModelList.get(position);


            if (isPaidMember || hasDP) {
                Glide.with(context)
                        .load(rev.getImg_url())
                        .placeholder(R.drawable.default_drawer)
                        .error(R.drawable.default_drawer)
                        .fitCenter()
                        .into(((ReverseViewHolder) holder).dp);
                ((ReverseViewHolder) holder).showTextOnPicture.setVisibility(View.GONE);
            } else {

                Glide.with(context)
                        .load(R.drawable.default_drawer)
                        .bitmapTransform(new BlurTransformation(context))
                        .placeholder(R.drawable.default_drawer)
                        .error(R.drawable.default_drawer)
                        .fitCenter()
                        .into(((ReverseViewHolder) holder).dp);

                ((ReverseViewHolder) holder).showTextOnPicture.setVisibility(View.VISIBLE);
            }


            ((ReverseViewHolder) holder).name.setText(rev.getName());
            ((ReverseViewHolder) holder).age.setText(String.valueOf(rev.getAge()) + " yrs");
            ((ReverseViewHolder) holder).education.setText(rev.getEducationDegree());
            ((ReverseViewHolder) holder).city.setText(rev.getLocation());
            ((ReverseViewHolder) holder).dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("from", "reverseMatching");
                    i.putExtra("customerNo", rev.getCustomerNo());
                    context.startActivity(i);
                }
            });


            ((ReverseViewHolder) holder).name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("from", "reverseMatching");
                    i.putExtra("customerNo", rev.getCustomerNo());
                    context.startActivity(i);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return reverseModelList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {


        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public class ReverseViewHolder extends RecyclerView.ViewHolder {

        private ImageView dp;
        private TextView name;
        private TextView age, education, city;
        private TextView showTextOnPicture;


        public ReverseViewHolder(View itemView) {
            super(itemView);

            dp = (ImageView) itemView.findViewById(R.id.user_profile_img);
            name = (TextView) itemView.findViewById(R.id.user_profile_name);
            age = (TextView) itemView.findViewById(R.id.user_profile_age);
            city = (TextView) itemView.findViewById(R.id.user_profile_city);
            education = (TextView) itemView.findViewById(R.id.user_profile_education);
            showTextOnPicture = (TextView) itemView.findViewById(R.id.showTextOnPicture);

        }

    }

}
