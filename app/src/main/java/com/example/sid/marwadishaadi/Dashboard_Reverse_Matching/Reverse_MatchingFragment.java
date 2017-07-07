package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_gender;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


public class Reverse_MatchingFragment extends Fragment {

    private static final String TAG = "Reverse_MatchingFragmen";
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<ReverseModel> reverseModelList = new ArrayList<>();
    private RecyclerView reverseRecyclerView;
    private ReverseAdapter reverseAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_reverse__matching, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics, "Reverse Matching", "view");

        reverseRecyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);

        reverseRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        reverseRecyclerView.setItemAnimator(fadeInLeftAnimator);
        reverseAdapter = new ReverseAdapter(reverseModelList, getContext());
        reverseRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        reverseRecyclerView.setAdapter(reverseAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        new PrepareReverse().execute();

        return mview;
    }

    private void refreshContent() {

        reverseModelList.clear();
        reverseAdapter.notifyDataSetChanged();
        new PrepareReverse().execute();
        swipeRefreshLayout.setRefreshing(false);
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


    private class PrepareReverse extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            pd.setTitle("Please wait..");
            pd.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.get(URL + "prepareReverse/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                }
                            });

                            try {

                                reverseModelList.clear();
                                reverseAdapter.notifyDataSetChanged();

                                Log.d(TAG, "onResponse: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + response.toString());
                                Log.d(TAG, "onResponse: before filling " + reverseModelList.toString());

                                for (int i = 0; i < response.length(); i++) {

                                    JSONArray array = response.getJSONArray(i);
                                    String customerNo = array.getString(0);
                                    String name = array.getString(1);
                                    String dateOfBirth = array.getString(2);
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
                                    String education = array.getString(3);
                                    String occupationLocation = array.getString(4);
                                    String imageUrl = array.getString(5);

                                    ReverseModel reverseModel = new ReverseModel("http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, name, customerNo,age , education, occupationLocation);
                                    reverseModelList.add(reverseModel);
                                    reverseAdapter.notifyDataSetChanged();

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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                }
                            });

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
