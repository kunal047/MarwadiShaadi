package com.example.sid.marwadishaadi.Dashboard_Suggestions;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Filter;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_gender;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


public class SuggestionsFragment extends Fragment {

    private static final String TAG = "SuggestionsFragment";
    private List<SuggestionModel> suggestionModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SuggestionAdapter suggestionAdapter;
    private TextView editprefs;
    private TextView filters;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_suggestions, container, false);


        editprefs = (TextView) mview.findViewById(R.id.preference);
        filters = (TextView) mview.findViewById(R.id.filter);

        editprefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), EditPreferencesActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        filters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getContext(), Filter.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });


        recyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) mview.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        suggestionAdapter = new SuggestionAdapter(getContext(), suggestionModelList, recyclerView);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(suggestionAdapter);
        new PrepareSuggestions().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        return mview;
    }

    private void refreshContent() {

        suggestionModelList.clear();
        new PrepareSuggestions().execute();
        suggestionAdapter.notifyDataSetChanged();
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

    private class PrepareSuggestions extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd=new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            pd.setMessage("Please wait..");
            pd.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.get(URL + "prepareSuggestions/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // do anything with response
                            try {

                                Log.d(TAG, "onResponse: response from ");

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
                                    int a = getAge(year, month, day);
                                    String age = Integer.toString(a);
                                    String education = array.getString(3);
                                    String occupationLocation = array.getString(4);
                                    String imageUrl = array.getString(5);

                                    String height = array.getString(6);
                                    double feet = Double.parseDouble(height) / 30.48;
                                    double inches = (Double.parseDouble(height) / 2.54) - ((int) feet * 12);
                                    height = (int) feet + "'" + (int) inches;

                                    String occupationCompany = array.getString(7);
                                    String occupationDesignation = array.getString(8);
                                    String annualIncome = array.getString(9);
                                    annualIncome = annualIncome.replaceAll("[^-?0-9]+", " ");
                                    List<String> incomeArray = Arrays.asList(annualIncome.trim().split(" "));
                                    Log.d(TAG, "onResponse: income array is " + incomeArray);
                                    if (annualIncome.contains("Upto")) {
                                        annualIncome = "Upto 3L";
                                    } else if (annualIncome.contains("Above")) {
                                        annualIncome = "Above 50L";

                                    } else if (incomeArray.size() == 3) {
                                        Log.d(TAG, "onResponse: when three");
                                        double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                                        double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                                        annualIncome = (int) first + "L - " + (int) second + "L";
                                    } else {
                                        annualIncome = "No Income mentioned.";
                                    }


                                    String maritalStatus = array.getString(10);
                                    String hometown = array.getString(11);

                                    String favouriteStatus = array.getString(12);
                                    String interestStatus = array.getString(13);


                                    SuggestionModel suggestionModel = new SuggestionModel(Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, name, customerNo, education, occupationLocation, height, occupationCompany, annualIncome, maritalStatus, hometown, occupationDesignation, favouriteStatus, interestStatus);



                                    suggestionModelList.add(suggestionModel);
                                    suggestionAdapter.notifyDataSetChanged();



                                }

                                Log.d(TAG, "onResponse: size of mpdel list ----------------------------- " + suggestionModelList.size());

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
            pd.dismiss();
            super.onPostExecute(aVoid);
            suggestionAdapter.notifyDataSetChanged();
        }
    }

}
