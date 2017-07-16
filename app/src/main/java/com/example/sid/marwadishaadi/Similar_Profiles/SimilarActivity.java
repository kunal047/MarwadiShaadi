package com.example.sid.marwadishaadi.Similar_Profiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Dashboard_Reverse_Matching.ReverseModel;
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
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Lawrence Dalmet on 13-06-2017.
 */

public class SimilarActivity extends AppCompatActivity{

    private static final String TAG = "SimilarActivity";
    private FirebaseAnalytics mFirebaseAnalytics;
    private SimilarAdapter similarAdapter;
    private List<SimilarModel> similarModelList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private String customer_id, customer_gender;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);
        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Similar Profiles","button");

        Toolbar toolbar = (Toolbar) findViewById(R.id.similar_toolbar);
        toolbar.setTitle("Similar Profiles");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        similarAdapter = new SimilarAdapter(similarModelList,getApplicationContext());
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(similarAdapter);
        recyclerView.setHasFixedSize(true);

        new PrepareSimilar().execute();

    }


    private class PrepareSimilar extends AsyncTask<Void, Void, Void> {
//        ProgressDialog pd = new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute() {
//            pd.setMessage("Please wait..");
//            pd.show();
//            super.onPreExecute();

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


        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.get("http://208.91.199.50:5000/prepareSimilar/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .setPriority(Priority.HIGH)
                    .getResponseOnlyIfCached()
                    .setMaxAgeCacheControl(0, TimeUnit.SECONDS)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            try {

                                Log.d(TAG, "onResponse: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + response.toString());

                                for (int i = 0; i < response.length(); i++) {

                                    JSONArray array = response.getJSONArray(i);
                                    String customerNo = array.getString(0);
                                    String firstName = array.getString(1);
                                    String surName = array.getString(2);
                                    String name = firstName + " " + surName;

                                    String dateOfBirth = array.getString(3);
//                                Thu, 18 Jan 1990 00:00:00 GMT
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
                                    int age = getAge(year, month, day);
                                    String education = array.getString(4);

                                    String cityName = array.getString(5);
                                    String stateName = array.getString(6);
                                    String occupationLocation = cityName + ", " + stateName;

                                    String imageUrl = array.getString(7);

                                    SimilarModel similarModel = new SimilarModel(name, occupationLocation, education, "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, String.valueOf(age) + " years",customerNo);
                                    similarModelList.add(similarModel);
                                    similarAdapter.notifyDataSetChanged();

                                }

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

        @Override
        protected void onPostExecute(Void aVoid) {
//            pd.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
