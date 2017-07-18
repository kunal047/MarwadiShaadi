package com.example.sid.marwadishaadi.Dashboard_Favourites;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pdfjet.Line;

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


public class FavouritesFragment extends Fragment {

    private static final String TAG = "FavouritesFragment";
    private static final int MODE_PRIVATE = 0;
    private List<FavouriteModel> favouritesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FavouritesAdapter favouritesAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String customer_id;
    private LinearLayout empty_view;
    private ProgressBar mProgressBar;

//    private TextView favouriteZero;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.swipe_to_refresh, container, false);
        empty_view = (LinearLayout) mview.findViewById(R.id.empty_view_favourites);
        empty_view.setVisibility(View.GONE);
//        favouriteZero = (TextView) mview.findViewById(R.id.favouriteZero);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Favourites","view");

//        mProgressBar = (ProgressBar) mview.findViewById(R.id.suggestion_progress_bar);
//        mProgressBar.setIndeterminate(false);
//        mProgressBar.setVisibility(View.GONE);

        recyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);
        favouritesAdapter = new FavouritesAdapter(getContext(), favouritesList,empty_view);

        recyclerView.setHasFixedSize(true);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView.setAdapter(favouritesAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });


        new PrepareFavourites().execute();
        return mview;
    }

    private void refreshContent() {
        new PrepareFavourites().execute();
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

    private class PrepareFavourites extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressBar.setVisibility(View.VISIBLE);
//            mProgressBar.setIndeterminate(true);
        }

        @Override
        protected Void doInBackground(String... params) {


            AndroidNetworking.post("http://208.91.199.50:5000/prepareFavourites")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            try {

//                                mProgressBar.setVisibility(View.GONE);

                                if(response.length() == 0){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            empty_view.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else{

                                    empty_view.setVisibility(View.GONE);
                                    favouritesList.clear();
                                    favouritesAdapter.notifyDataSetChanged();
                                    for (int i = 0; i < response.length(); i++) {

                                        JSONArray array = response.getJSONArray(i);
                                        String customerNo = array.getString(0);
                                        String name = array.getString(1);
                                        String dateOfBirth = array.getString(2);
//                                Thu, 18 Jan 1990 00:00:00 GMT
                                        DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
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
                                        String occupationLocation = array.getString(4);
                                        String imageUrl = array.getString(5);


                                        FavouriteModel favouriteModel = new FavouriteModel(customerNo, name, occupationLocation, education, Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl);

                                        if (!favouritesList.contains(favouriteModel)){
                                            favouritesList.add(0, favouriteModel);
                                        }

                                    }


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
//                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            mProgressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
