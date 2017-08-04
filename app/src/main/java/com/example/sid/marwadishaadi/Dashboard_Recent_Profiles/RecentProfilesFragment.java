package com.example.sid.marwadishaadi.Dashboard_Recent_Profiles;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.CacheHelper;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

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


    private static final String TAG = "RecentProfilesFragment";
    private List<RecentModel> recentList;
    private List<RecentModel> recentListWithoutPic;
    private RecyclerView recentRecyclerView;
    private RecentAdapter recentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String customer_id, customer_gender;
    private LinearLayout empty_view_recent;
    private String res = "";
    private ProgressDialog progressDialog;
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_recent__profiles, container, false);
        empty_view_recent = (LinearLayout) mview.findViewById(R.id.empty_view_recent);
        empty_view_recent.setVisibility(View.GONE);

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        cache = new File(getCacheDir() + "/" + "recentprofiles" +customer_id+ ".srl");

        String[] array = getResources().getStringArray(R.array.communities);

        SharedPreferences communityChecker = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);

        if (communityChecker!=null) {
            for (int i = 0; i < 5; i++) {

                if (communityChecker.getString(array[i], null).contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                    res += " OR tbl_user.customer_no LIKE '" + array[i].toCharArray()[0] + "%'";
                }
            }
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics, "Recent Profiles", "button");

        recentRecyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);
        recentList = new ArrayList<>();
        recentListWithoutPic = new ArrayList<>();
        recentAdapter = new RecentAdapter(getContext(), recentList, recentRecyclerView);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recentRecyclerView.setItemAnimator(fadeInLeftAnimator);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recentRecyclerView.setLayoutManager(layoutManager);
        recentRecyclerView.setAdapter(recentAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading recent profiles...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // loading cached copy
        String res = CacheHelper.retrieve("recent_profiles",cache);
        if(!res.equals("")){
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(getContext(),CacheHelper.generateHash(res),"recent_profiles");

                // displaying it
                JSONArray response = new JSONArray(res);
                Toast.makeText(getContext(), "Loading from cache....", Toast.LENGTH_SHORT).show();
                parseRecentProfiles(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        new PrepareRecent().execute();
        return mview;
    }

    private void parseRecentProfiles(JSONArray response) {

        try {

            recentList.clear();
            recentAdapter.notifyDataSetChanged();

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

                    String name = array.getString(0);
                    String dateOfBirth = array.getString(1);

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

                    String education = array.getString(2);
                    String location = array.getString(3);
                    String customerNo = array.getString(4);
                    String createdOn = array.getString(5);
                    String surname = array.getString(6);
                    String imageUrl = array.getString(7);
                    String favouriteStatus = array.getString(8);
                    String recentStatus = array.getString(9);
                    name = name + " " + surname;

                    date = formatter.parse(createdOn);
                    long diff = now.getTime() - date.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);
                    int diffInDays = (int) ((now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));

                    if (diffInDays >= 365) {
                        createdOn = "More than a year ago";
                    } else if (diffInDays > 1) {
//                                        System.err.println("Difference in number of days (2) : " + diffInDays);
                        createdOn = diffInDays + " days ago";
                    } else if (diffHours >= 1) {
                        createdOn = diffHours + " hours ago";
                    } else if ((diffHours < 1) && (diffMinutes >= 1)) {
//                                        System.err.println("minutes");
                        createdOn = diffMinutes + " minutes ago";
                    } else if (diffSeconds < 60) {
                        createdOn = diffSeconds + " seconds ago";
                    }


                    RecentModel recentModel = new RecentModel(customerNo, name, age, education, location, createdOn, "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, favouriteStatus, recentStatus);

                    if (!recentList.contains(recentModel) && imageUrl.contains("null")) {

                        recentListWithoutPic.add(recentModel);

                    } else if (!recentList.contains(recentModel)) {
                        recentList.add(recentModel);
                        recentAdapter.notifyDataSetChanged();
                    }

                }

                recentList.addAll(recentListWithoutPic);
                recentAdapter.notifyDataSetChanged();

            }

//
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
        new PrepareRecent().execute();

    }

    private class PrepareRecent extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/prepareRecent/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .addBodyParameter("membership", res)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response


                            Log.d("recent_profiles",response.toString());

                            // if no change in data
                            if (isAlreadyLoadedFromCache){

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(getContext(),"recent_profiles");

                                Log.d("latest",latestResponseHash);
                                Log.d("cached",cacheResponseHash);
                                Log.d("isSame",latestResponseHash.equals(cacheResponseHash) + "");

                                if (cacheResponseHash!=null && latestResponseHash.equals(cacheResponseHash)){
                                    Toast.makeText(getContext(), "data same found", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{

                                    // hash not matched
                                    loadedFromNetwork(response);
                                }
                            }else{
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
                                    progressDialog.dismiss();
                                    //here
                                }
                            });
                        }
                    });

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void loadedFromNetwork(JSONArray response){


        //saving fresh in cache
        CacheHelper.save("recent_profiles",response.toString(),cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(getContext(),CacheHelper.generateHash(response.toString()),"recent_profiles");

        // displaying it
        parseRecentProfiles(response);

    }
}
