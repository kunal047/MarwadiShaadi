package com.example.sid.marwadishaadi.Dashboard_Recent_Profiles;

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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_gender;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


public class RecentProfilesFragment extends Fragment {


    private static final String TAG = "RecentProfilesFragment";
    private List<RecentModel> recentList;
    private RecyclerView recentRecyclerView;
    private RecentAdapter recentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.fragment_recent__profiles, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Recent Profiles","button");


        recentRecyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);
        recentList = new ArrayList<>();
        recentAdapter = new RecentAdapter(getContext(), recentList);
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

        new PrepareRecent().execute();
        return mview;
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
        recentAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private class PrepareRecent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.get(URL + "prepareRecent/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            try {
                                RecentModel[] recentModel = new RecentModel[response.length()];
                                for (int i = 0; i < response.length(); i++) {

                                    JSONArray array = response.getJSONArray(i);

                                    String name = array.getString(0);
                                    String dateOfBirth = array.getString(1);

                                    // Thu, 18 Jan 1990 00:00:00 GMT - Date Format
                                    DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                                    Date date = formatter.parse(dateOfBirth);
                                    System.out.println(date);

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
                                    String imageUrl = array.getString(4);
                                    String customerNo = array.getString(5);
                                    String lastOnline = array.getString(6);
                                    Log.d(TAG, "onResponse: lastonline is ------------------ " + lastOnline);


                                    recentModel[i] = new RecentModel(customerNo, name, age, education, location, lastOnline, "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl);
                                    recentList.add(recentModel[i]);
                                    recentAdapter.notifyDataSetChanged();

                                }

//                            Log.d(TAG, "onResponse: json response array is " + response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "onResponse: json response array is " + error.toString());
                            // handle error
                        }
                    });

            return null;
        }
    }
}

