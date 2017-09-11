package com.sid.marwadishaadi.Dashboard_Recent_Profiles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.CacheHelper;
import com.sid.marwadishaadi.OnLoadMoreListener;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getCacheDir;


public class RecentProfilesFragment extends Fragment {

    private static int recent_page_no = 0;
    private List<RecentModel> recentList;
    private List<RecentModel> recentListWithoutPic;
    private RecyclerView recentRecyclerView;
    private RecentAdapter recentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String customer_id, customer_gender;
    private LinearLayout empty_view_recent;
    private String res = "";
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;
    private ProgressBar loading;
    private TextView textViewPreference, textViewSortBy;
    private RecyclerView.LayoutManager layoutManager;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            recentRecyclerView.setAdapter(null);
            recentRecyclerView.setLayoutManager(null);
            recentRecyclerView.setAdapter(recentAdapter);
            recentRecyclerView.setLayoutManager(layoutManager);
            recentAdapter.notifyDataSetChanged();
            recentList.clear();
            recent_page_no = 0;
            new PrepareRecent().execute();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_recent__profiles, container, false);
        empty_view_recent = (LinearLayout) mview.findViewById(R.id.empty_view_recent);
        empty_view_recent.setVisibility(View.GONE);

        loading = (ProgressBar) mview.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);


        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        cache = new File(getCacheDir() + "/" + "recentprofiles" + customer_id + ".srl");

        String[] array = getResources().getStringArray(R.array.communities);

        SharedPreferences communityChecker = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);

        if (customer_id != null && communityChecker != null) {
            for (int i = 0; i < 5; i++) {

                if (communityChecker.getString(array[i], "No").contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                    res += " OR tbl_user.customer_no LIKE '" + array[i].toCharArray()[0] + "%'";
                }
            }
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics, "Recent Profiles", "button");

        recentRecyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);

        textViewPreference = (TextView) mview.findViewById(R.id.preferenceOfRecent);
        textViewSortBy = (TextView) mview.findViewById(R.id.sortbyOfRecent);

        recentList = new ArrayList<>();
        recentListWithoutPic = new ArrayList<>();
        recentAdapter = new RecentAdapter(getContext(), recentList, recentRecyclerView);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recentRecyclerView.setItemAnimator(fadeInLeftAnimator);
        layoutManager = new LinearLayoutManager(getContext());
        recentRecyclerView.setLayoutManager(layoutManager);
        recentRecyclerView.setAdapter(recentAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        textViewPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditPreferencesActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        textViewSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SortByRecent.class);
                startActivityForResult(i, 2);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });


        // loading cached copy
        String res = CacheHelper.retrieve("recent_profiles", cache);
        if (!res.equals("")) {
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(getContext(), CacheHelper.generateHash(res), "recent_profiles");

                // displaying it
                JSONArray response = new JSONArray(res);
                // .makeText(getContext(), "Loading from cache....", .LENGTH_SHORT).show();
                parseRecentProfiles(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new PrepareRecent().execute();

        recentAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() throws InterruptedException {

                recentList.add(null);
                recentAdapter.notifyDataSetChanged();

                Thread.sleep(1000);

                recentList.remove(recentList.size() - 1);
                recentAdapter.notifyDataSetChanged();

                new PrepareRecent().execute();
            }
        });


        return mview;
    }

    private void parseRecentProfiles(JSONArray response) {

        loading.setVisibility(View.GONE);

        try {

            if (response.length() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        empty_view_recent.setVisibility(View.VISIBLE);
                    }
                });
            } else {

                empty_view_recent.setVisibility(View.GONE);


                for (int i = 0; i < response.length(); i++) {

                    JSONArray array = response.getJSONArray(i);

                    String lastActiveOn = array.getString(0);

                    String name = array.getString(1);
                    String dateOfBirth = array.getString(2);

                    // Thu, 18 Jan 1990 00:00:00 GMT - Date Format
                    DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                    Date now = new Date();
                    Date date = formatter.parse(dateOfBirth);


                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);

                    String[] partsOfDate = formatedDate.split("-");
                    int day = Integer.parseInt(partsOfDate[0]);
                    int month = Integer.parseInt(partsOfDate[1]);
                    int year = Integer.parseInt(partsOfDate[2]);
                    int a = getAge(year, month, day);
                    String age = Integer.toString(a);

                    String education = array.getString(3);
                    String location = array.getString(4);
                    String customerNo = array.getString(5);
                    String createdOn = array.getString(6);
                    String surname = array.getString(7);
                    String imageUrl = array.getString(8);
                    String favouriteStatus = array.getString(9);
                    String recentStatus = array.getString(10);
                    name = name + " " + surname;

                    SharedPreferences sortBy = getActivity().getSharedPreferences("sort_by_recent", MODE_PRIVATE);
                    String recentSort = sortBy.getString("sortBy", "Registered");
                    if (recentSort.contains("Active")) {
                        date = formatter.parse(lastActiveOn);
                        createdOn = "Last Active ";
                    } else {
                        date = formatter.parse(createdOn);
                        createdOn = "";
                    }


                    long diff = now.getTime() - date.getTime();
                    int diffSeconds = (int) diff / 1000 % 60;
                    int diffMinutes = (int) diff / (60 * 1000) % 60;
                    int diffHours = (int) diff / (60 * 60 * 1000);
                    int diffInDays = (int) ((now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));

                    if (diffInDays >= 15) {
                        createdOn += "A few days ago";
                    } else if (diffInDays > 1) {
//                                        System.err.println("Difference in number of days (2) : " + diffInDays);
                        createdOn += diffInDays + " days ago";
                    } else if (diffInDays == 1) {
//                                        System.err.println("Difference in number of days (2) : " + diffInDays);
                        createdOn += diffInDays + " day ago";
                    } else if (diffHours >= 1) {
                        if (diffHours == 1) {
                            createdOn += diffHours + " hour ago";
                        } else {
                            createdOn += diffHours + " hours ago";
                        }
                    } else if ((diffHours < 1) && (diffMinutes >= 1)) {
                        createdOn += diffMinutes + " minutes ago";
                    } else if (diffSeconds > 0 && diffSeconds < 60) {
                        createdOn += diffSeconds + " seconds ago";
                    } else if (diffSeconds < 0) {
                        createdOn += "A few seconds ago";
                    }


                    RecentModel recentModel = new RecentModel(customerNo, name, age, education, location, createdOn, "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, favouriteStatus, recentStatus);

                    recentList.add(recentModel);
                    recentAdapter.notifyDataSetChanged();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public int getAge(int DOByear, int DOBmonth, int DOBday) {

        int age;

        final Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = 1 + calenderToday.get(Calendar.MONTH);
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);

        age = currentYear - DOByear;

        if (DOBmonth > currentMonth) {
            --age;
        } else if (DOBmonth == currentMonth) {
            if (DOBday > todayDay) {
                --age;
            }
        }
        return age;
    }

    private void refreshData() {

        recent_page_no = 0;
        new PrepareRecent().execute();

    }

    public void loadedFromNetwork(JSONArray response) {




        //saving fresh in cache
        CacheHelper.save("recent_profiles", response.toString(), cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(getContext(), CacheHelper.generateHash(response.toString()), "recent_profiles");

        // displaying it
        parseRecentProfiles(response);

    }

    private class PrepareRecent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

//            if (recent_page_no == 0) {
//                recentList.clear();
//                recentAdapter.notifyDataSetChanged();
//            }

            SharedPreferences sortBy = getActivity().getSharedPreferences("sort_by_recent", MODE_PRIVATE);

            String recentShowPhotos = sortBy.getString("showPhotos", "yes");
            String recentSort = sortBy.getString("sortBy", "Registered");

            AndroidNetworking.post("http://208.91.199.50:5000/prepareRecent/{customerNo}/{gender}/{page}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .addPathParameter("page", String.valueOf(recent_page_no))
                    .addBodyParameter("membership", res)
                    .addBodyParameter("showPhotos", recentShowPhotos)
                    .addBodyParameter("recentSort", recentSort)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response

                            recent_page_no++;

                            // if no change in data
                            if (isAlreadyLoadedFromCache) {

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(getContext(), "recent_profiles");

                                //
                                //
                                //

                                if (cacheResponseHash != null && latestResponseHash.equals(cacheResponseHash)) {
                                    //  .makeText(getContext(), "data same found", .LENGTH_SHORT).show();
                                    return;
                                } else {

                                    if (recent_page_no == 0) {
                                        recentList.clear();
                                        recentAdapter.notifyDataSetChanged();
                                    }

                                    // hash not matched
                                    loadedFromNetwork(response);
                                }
                            } else {

                                // first time load
                                loadedFromNetwork(response);
                            }


                        }

                        @Override
                        public void onError(ANError error) {

                            // handle error
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //here
                                    loading.setVisibility(View.GONE);
                                }
                            });
                        }
                    });

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recentAdapter.setLoaded();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
