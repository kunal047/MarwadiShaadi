package com.example.sid.marwadishaadi.Dashboard_Interest_Sent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.R;

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

import static android.content.Context.MODE_PRIVATE;


public class InterestSentFragment extends Fragment {

    private static final String TAG = "InterestActivity";
    private List<InterestSentModel> interestListSent;
    private RecyclerView recyclerView;
    private InterestSentAdapter interestSentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String customer_id, customer_gender;
    private LinearLayout empty_view;
    private LinearLayout empty_view_sent;
    private ProgressBar mProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mview = inflater.inflate(R.layout.fragment_interest_received, container, false);

        empty_view = (LinearLayout) mview.findViewById(R.id.empty_view);
        empty_view_sent = (LinearLayout) mview.findViewById(R.id.empty_view_sent);

        empty_view.setVisibility(View.GONE);
        empty_view_sent.setVisibility(View.GONE);


        mProgressBar = (ProgressBar) mview.findViewById(R.id.suggestion_progress_bar);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setVisibility(View.GONE);

        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);
        recyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        interestListSent = new ArrayList<>();
        interestSentAdapter = new InterestSentAdapter(getContext(), interestListSent);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(interestSentAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        new PrepareSentInterest().execute();

        return mview;
    }

    private void refreshData() {

        new PrepareSentInterest().execute();

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

    private class PrepareSentInterest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(true);
        }


        @Override
        protected Void doInBackground(Void... params) {


            AndroidNetworking.post("http://208.91.199.50:5000/prepareSentInterest")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response

                            try {
                                mProgressBar.setVisibility(View.GONE);

                                

                                if (response.length() == 0) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            empty_view_sent.setVisibility(View.VISIBLE);
                                        }
                                    });
                                } else {

                                    empty_view_sent.setVisibility(View.GONE);
                                    interestListSent.clear();
                                    interestSentAdapter.notifyDataSetChanged();

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
                                        String cityName = array.getString(3);
                                        String education = array.getString(4);
                                        String replyAction = array.getString(5);
                                        String interestSentOn = array.getString(6);
                                        String imageUrl = array.getString(7);
                                        date = formatter.parse(interestSentOn);
                                        interestSentOn = new SimpleDateFormat("E, dd MMM yyyy").format(date);

                                        InterestSentModel interestSentModels = new InterestSentModel(customerNo, name, cityName, education, "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, replyAction, Integer.parseInt(age), "Interest sent on " + interestSentOn);

                                        if (!interestListSent.contains(interestSentModels)) {

                                            interestListSent.add(0, interestSentModels);
                                            interestSentAdapter.notifyDataSetChanged();

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
                            
                            mProgressBar.setVisibility(View.GONE);

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
