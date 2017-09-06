package com.sid.marwadishaadi.Dashboard_Recent_Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Notifications.NotificationsModel;
import com.sid.marwadishaadi.Notifications_Util;
import com.sid.marwadishaadi.OnLoadMoreListener;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * Created by USER on 02-06-2017.
 */

public class RecentAdapter extends RecyclerView.Adapter {

    private final int VIEW_PROG = 0;
    private final int VIEW_ITEM = 1;
    private boolean recentIsLoading = false;
    private Context context;
    private List<RecentModel> recentModelList;
    private String favouriteState, interestState;
    private String customer_id, customer_gender, customer_name;
    private RecyclerView recentRecyclerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private View iView;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 0;
    private int firstVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private ProgressBar progressBar;
    private boolean isPaidMember = false;


    public RecentAdapter(Context context, List<RecentModel> recentModelList, RecyclerView recyclerView) {
        this.context = context;
        this.recentModelList = recentModelList;
        this.recentRecyclerView = recyclerView;

        recentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();


                if (recentIsLoading) {
                    if (totalItemCount > previousTotal + 1) {

                        recentIsLoading = false;
                        previousTotal = totalItemCount;

                    }
                }

                if (!recentIsLoading && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))) {
                    // End has been reached
                    // Do something

                    recentIsLoading = true;
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
        recentIsLoading = false;

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {

        return recentModelList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            iView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_recent, parent, false);


            SharedPreferences sharedpref = iView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            customer_id = sharedpref.getString("customer_id", null);
            customer_gender = sharedpref.getString("gender", null);
            customer_name = sharedpref.getString("firstname", null);
            String[] array = context.getResources().getStringArray(R.array.communities);
            SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
            for (int i = 0; i < 5; i++) {
                if (communityChecker.getString(array[i], "null").contains("Yes")) {
                    isPaidMember = true;
                }
            }

            return new RecentViewHolder(iView);
        } else {
            iView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);

            SharedPreferences sharedpref = iView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            customer_id = sharedpref.getString("customer_id", null);
            customer_name = sharedpref.getString("firstname", null);
            customer_gender = sharedpref.getString("gender", null);
            return new ProgressViewHolder(iView);
        }


    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof RecentViewHolder) {
            RecentModel recentModel = recentModelList.get(position);

            ((RecentViewHolder) holder).recentUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("from", "recent");
                    i.putExtra("customerNo", recentModelList.get(position).getRecentCustomerId());

                    context.startActivity(i);
                }
            });

            ((RecentViewHolder) holder).recentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("from", "recent");
                    i.putExtra("customerNo", recentModelList.get(position).getRecentCustomerId());

                    context.startActivity(i);
                }
            });

            if (!isPaidMember) {
                Glide.with(context)
                        .load(recentModel.getRecentUserImage())
                        .placeholder(R.drawable.default_drawer)
                        .error(R.drawable.default_drawer)
                        .bitmapTransform(new BlurTransformation(context))
                        .into(((RecentViewHolder) holder).recentUserImage);
            } else {
                Glide.with(context)
                        .load(recentModel.getRecentUserImage())
                        .placeholder(R.drawable.default_drawer)
                        .error(R.drawable.default_drawer)
                        .into(((RecentViewHolder) holder).recentUserImage);

            }



            ((RecentViewHolder) holder).recentName.setText(recentModel.getRecentName() + ", ");
            ((RecentViewHolder) holder).recentAge.setText(recentModel.getRecentAge());
            ((RecentViewHolder) holder).recentOnline.setText(recentModel.getRecentOnline());
            ((RecentViewHolder) holder).recentHighestDegree.setText(recentModel.getRecentHighestDegree());
            ((RecentViewHolder) holder).recentLocation.setText(recentModel.getRecentLocation());

            if (recentModel.getRecentFavouriteStatus().contains("contain")) {
                ((RecentViewHolder) holder).sparkButtonFavourite.setChecked(false);
                ((RecentViewHolder) holder).sparkButtonFavourite.setInactiveImage(R.mipmap.heart_disable);
            } else {
                ((RecentViewHolder) holder).sparkButtonFavourite.setActiveImage(R.mipmap.ic_fav);
                ((RecentViewHolder) holder).sparkButtonFavourite.setChecked(true);
            }
            if (recentModel.getRecentInterestStatus().contains("contain")) {
                ((RecentViewHolder) holder).sparkButtonInterest.setChecked(false);
                ((RecentViewHolder) holder).sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);
            } else {
                ((RecentViewHolder) holder).sparkButtonInterest.setActiveImage(R.mipmap.ic_heart);
                ((RecentViewHolder) holder).sparkButtonInterest.setChecked(true);
            }

            ((RecentViewHolder) holder).sparkButtonFavourite.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {


                    // when its active
                    if (buttonState) {
                        ((RecentViewHolder) holder).sparkButtonFavourite.setChecked(true);
                        ((RecentViewHolder) holder).sparkButtonFavourite.setActiveImage(R.mipmap.ic_fav);

                        favouriteState = "removed";
                        new RecentAdapter.AddFavouriteFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), favouriteState);
                        Snackbar snackbar = Snackbar.make(recentRecyclerView, "Removed from Favourites", Snackbar.LENGTH_SHORT);
                        snackbar.show();


                    } else {
                        ((RecentViewHolder) holder).sparkButtonFavourite.setChecked(false);
                        ((RecentViewHolder) holder).sparkButtonFavourite.setInactiveImage(R.mipmap.heart_disable);
                        favouriteState = "added";
                        new RecentAdapter.AddFavouriteFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), favouriteState);
                        Snackbar snackbar = Snackbar.make(recentRecyclerView, "Added to Favourites", Snackbar.LENGTH_LONG);
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

            ((RecentViewHolder) holder).sparkButtonInterest.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {

                    if (buttonState) {

                        // ========================= NOTIFICATION =======================================


                        String touserid = recentModelList.get(position).getRecentCustomerId();

                        // adding it to her notifications list
                        String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                        mDatabase = FirebaseDatabase.getInstance().getReference(touserid).child("Notifications");
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                        Date currentDate = calendar.getTime();
                        String hash = String.valueOf(currentDate.hashCode());
                        final NotificationsModel notification = new NotificationsModel(hash, customer_name, date, 3, false, true, false, false, false, false, false, false, false, false);
                        mDatabase.child(hash).setValue(notification);

                        // sending push notification to her
                        // get all devices

                        mDatabases = FirebaseDatabase.getInstance().getReference(touserid).child("Devices");
                        mDatabases.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                setData(dataSnapshot);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        //======================================================================
                        interestState = "removed";
                        new RecentAdapter.AddInterestFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), interestState);
                        Snackbar snackbar = Snackbar.make(recentRecyclerView, "Removed from interest", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        ((RecentViewHolder) holder).sparkButtonInterest.setChecked(true);
                        ((RecentViewHolder) holder).sparkButtonInterest.setActiveImage(R.mipmap.ic_heart);


                    } else {

                        interestState = "added";
                        new RecentAdapter.AddInterestFromSuggestion().execute(customer_id, recentModelList.get(position).getRecentCustomerId(), interestState);
                        Snackbar snackbar = Snackbar.make(recentRecyclerView, "Interest Sent", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        ((RecentViewHolder) holder).sparkButtonInterest.setChecked(false);
                        ((RecentViewHolder) holder).sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);


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

    }

    public void setData(DataSnapshot dataSnapshot) {

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), customer_name + " sent you an Interest", "New Interest", "Interest Request");
    }

    @Override
    public int getItemCount() {
        return recentModelList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {


        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        ImageView recentUserImage;
        TextView recentName, recentAge, recentHighestDegree, recentLocation, recentOnline, recentCustomerId;
        SparkButton sparkButtonInterest, sparkButtonFavourite;


        public RecentViewHolder(final View itemView) {

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

    private class AddFavouriteFromSuggestion extends AsyncTask<String, Void, Void> {

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
