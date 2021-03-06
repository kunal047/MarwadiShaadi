package com.sid.marwadishaadi.Dashboard_Suggestions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sid.marwadishaadi.Chat.DefaultMessagesActivity;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lawrence Dalmet on 31-05-2017.
 */


public class SuggestionDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SuggestionDataAdapter";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context context;
    public ProgressBar progressBar;
    private View iView;
    private List<SuggestionModel> suggestionModelList;
    private RecyclerView rv;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String favouriteState, interestState;
    private String customer_id, customer_name;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private boolean isLoading;
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private String[] array;
    private boolean isPaidMember;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean hasDP;

    public SuggestionDataAdapter(Context context, final List<SuggestionModel> suggestionModelList, RecyclerView recyclerView) {

        this.suggestionModelList = suggestionModelList;
        this.context = context;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.rv = recyclerView;

        if (context.toString().contains("Dashboard")) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);


                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    totalItemCount = linearLayoutManager.getItemCount();

                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();


                    if (lastVisibleItem != -1 && !isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            try {
                                mOnLoadMoreListener.onLoadMore();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        isLoading = true;
                    }


                }
            });

        }


    }

    public void setLoaded() {
        isLoading = false;
    }

    //
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {

        return suggestionModelList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            iView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.suggestions_row, parent, false);

            SharedPreferences sharedpref = iView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            customer_id = sharedpref.getString("customer_id", null);
            customer_name = sharedpref.getString("firstname", null);

            array = context.getResources().getStringArray(R.array.communities);
            SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
            for (int i = 0; i < 5; i++) {
                if (communityChecker.getString(array[i], "null").contains("Yes")) {
                    isPaidMember = true;
                }
            }
            SharedPreferences sharedPref = iView.getContext().getSharedPreferences("userDp", MODE_PRIVATE);
            hasDP = sharedPref.getBoolean("hasDP", false);


            return new SuggestionViewHolder(iView);
        } else {
            iView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_loading_item, parent, false);
            SharedPreferences sharedpref = iView.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            customer_id = sharedpref.getString("customer_id", null);
            customer_name = sharedpref.getString("firstname", null);

            SharedPreferences sharedPref = iView.getContext().getSharedPreferences("userDp", MODE_PRIVATE);
            hasDP = sharedPref.getBoolean("hasDP", false);


            return new ProgressViewHolder(iView);
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof SuggestionViewHolder) {


            SuggestionModel suggest = suggestionModelList.get(position);


            String ag = suggest.getName() + ", " + suggest.getAge();
            String cd = "None";
            if (suggest.getDesignation().length() > 0 && suggest.getComapany().length() == 0) {
                cd = suggest.getDesignation();
            } else if (suggest.getDesignation().length() == 0 && suggest.getComapany().length() > 0) {
                cd = suggest.getComapany();
            } else if (suggest.getDesignation().length() > 0 && suggest.getComapany().length() > 0) {
                cd = suggest.getComapany() + ", " + suggest.getDesignation();
            } else if (suggest.getDesignation().length() == 0 && suggest.getComapany().length() == 0) {
                cd = "Not Mentioned";
            }

            ((SuggestionViewHolder) holder).name.setText(ag);
            ((SuggestionViewHolder) holder).cusId.setText(suggest.getCusId());

            if (isPaidMember || hasDP) {

                Glide.with(context)
                        .load(suggest.getImgAdd())
                        .placeholder(R.drawable.default_drawer)
                        .error(R.drawable.default_drawer)
                        .into(((SuggestionViewHolder) holder).imgAdd);
                ((SuggestionViewHolder) holder).showTextOnPicture.setVisibility(View.GONE);

            } else {

                Glide.with(context)
                        .load(suggest.getImgAdd())
                        .placeholder(R.drawable.default_drawer)
                        .error(R.drawable.default_drawer)
                        .bitmapTransform(new BlurTransformation(context))
                        .into(((SuggestionViewHolder) holder).imgAdd);
                ((SuggestionViewHolder) holder).showTextOnPicture.setVisibility(View.VISIBLE);

            }


            ((SuggestionViewHolder) holder).height.setText(suggest.getHeight());
            ((SuggestionViewHolder) holder).workLoc.setText(suggest.getWorkLoc());

            ((SuggestionViewHolder) holder).annInc.setText(suggest.getAnnInc().replace("000000", "0L").replace("00000", "L"));

            ((SuggestionViewHolder) holder).hometown.setText(suggest.getHometown());
            ((SuggestionViewHolder) holder).mariSta.setText(suggest.getMariSta());
            ((SuggestionViewHolder) holder).company.setText(cd);
            ((SuggestionViewHolder) holder).highDeg.setText(suggest.getHighDeg());

            if (suggest.getFavouriteStatus().toCharArray()[0] == '1') {


                ((SuggestionViewHolder) holder).sparkButtonFav.setChecked(false);
                ((SuggestionViewHolder) holder).sparkButtonFav.setInactiveImage(R.mipmap.heart_disable);
            } else {
                ((SuggestionViewHolder) holder).sparkButtonFav.setActiveImage(R.mipmap.ic_fav);
                ((SuggestionViewHolder) holder).sparkButtonFav.setChecked(true);
            }
            if (!suggest.getInterestStatus().contains("Not")) {
                ((SuggestionViewHolder) holder).sparkButtonInterest.setChecked(false);
                ((SuggestionViewHolder) holder).sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);
            } else {
                ((SuggestionViewHolder) holder).sparkButtonInterest.setInactiveImage(R.mipmap.ic_heart);
                ((SuggestionViewHolder) holder).sparkButtonInterest.setChecked(true);

            }


            ((SuggestionViewHolder) holder).sparkButtonFav.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {


                    // when its active
                    if (buttonState) {

                        favouriteState = "removed";
                        new AddFavouriteFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), favouriteState);

                        ((SuggestionViewHolder) holder).sparkButtonFav.setChecked(true);
                        ((SuggestionViewHolder) holder).sparkButtonFav.setActiveImage(R.mipmap.ic_fav);

                        Snackbar snackbar = Snackbar.make(rv, "Removed from Favourites", Snackbar.LENGTH_SHORT);
                        snackbar.show();


                    } else {


                        favouriteState = "added";
                        new AddFavouriteFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), favouriteState);

                        ((SuggestionViewHolder) holder).sparkButtonFav.setChecked(false);
                        ((SuggestionViewHolder) holder).sparkButtonFav.setInactiveImage(R.mipmap.heart_disable);

                        Snackbar snackbar = Snackbar.make(rv, "Added to Favourites", Snackbar.LENGTH_LONG);
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

            ((SuggestionViewHolder) holder).sparkButtonInterest.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {


                    if (buttonState) {

                        // ========================= NOTIFICATION =======================================


                        String touserid = suggestionModelList.get(position).getCusId();

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
                        new AddInterestFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), interestState);
                        Snackbar snackbar = Snackbar.make(rv, "Removed from interest", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        ((SuggestionViewHolder) holder).sparkButtonInterest.setChecked(true);
                        ((SuggestionViewHolder) holder).sparkButtonInterest.setActiveImage(R.mipmap.ic_heart);


                    } else {

                        interestState = "added";
                        new AddInterestFromSuggestion().execute(customer_id, suggestionModelList.get(position).getCusId(), interestState);
                        Snackbar snackbar = Snackbar.make(rv, "Interest Sent", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        ((SuggestionViewHolder) holder).sparkButtonInterest.setChecked(false);
                        ((SuggestionViewHolder) holder).sparkButtonInterest.setInactiveImage(R.mipmap.heart_disable1);


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
//        Notifications_Util
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), customer_name + " sent you an Interest", "Marwadi Shaadi: New Interest", "Interest Request");
    }

    @Override
    public int getItemCount() {

        return suggestionModelList == null ? 0 : suggestionModelList.size();
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {


        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {

        TextView name, cusId, highDeg, workLoc, height, company, annInc, mariSta, hometown, showTextOnPicture;
        ImageView imgAdd;
        SparkButton sparkButtonChat, sparkButtonInterest, sparkButtonFav;


        public SuggestionViewHolder(View view) {

            super(view);
            showTextOnPicture =  view.findViewById(R.id.textViewToUploadPicture);
            name = view.findViewById(R.id.name);
            cusId = view.findViewById(R.id.cusId);
            imgAdd = view.findViewById(R.id.imgAdd);
            highDeg = view.findViewById(R.id.highDeg);
            workLoc = view.findViewById(R.id.workLoc);
            height = view.findViewById(R.id.height);
            company = view.findViewById(R.id.company);
            annInc = view.findViewById(R.id.annInc);
            mariSta = view.findViewById(R.id.mariSta);
            hometown = view.findViewById(R.id.hometown);
            sparkButtonChat = view.findViewById(R.id.chat);
            sparkButtonFav = view.findViewById(R.id.fav);
            sparkButtonInterest = view.findViewById(R.id.interest);


            imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("customerNo", cusId.getText());
                    i.putExtra("from", "suggestion");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserProfileActivity.class);
                    i.putExtra("customerNo", cusId.getText());
                    i.putExtra("from", "suggestion");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            sparkButtonChat.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {


                    int counter = 0;

                    SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(context);
                    for (int i = 0; i < 5; i++) {
                        if (communityChecker.getString(array[i], "null").contains("Yes")) {
                            counter++;
                        }
                    }
                    if (counter > 0) {
                        Intent intent = new Intent(context, DefaultMessagesActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("customerName", name.getText().toString());
                        extras.putString("customerId", cusId.getText().toString());
                        intent.putExtras(extras);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "This feature is only for premium members", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, UpgradeMembershipActivity.class);
                        context.startActivity(intent);
                    }
                }


                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });

        }
    }

    public class AddInterestFromSuggestion extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            String customerId = params[0];
            String interestId = params[1];
            String status = params[2];
            AndroidNetworking.post(Constants.AWS_SERVER + "/addInterestFromSuggestion")
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

    public class AddFavouriteFromSuggestion extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String customerId = params[0];
            String favId = params[1];

            AndroidNetworking.post(Constants.AWS_SERVER + "/addFavFromSuggestion")
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
