package com.example.sid.marwadishaadi.Dashboard_Suggestions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.CacheHelper;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getCacheDir;


public class SuggestionsFragment extends Fragment {

    private List<SuggestionModel> suggestionModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SuggestionAdapter suggestionAdapter;
    private TextView editprefs;
    private TextView filters;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String customer_id, customer_gender;
    private LinearLayout empty_view_suggestions;
    private ProgressDialog progressDialog;
    private String res = "";
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_suggestions, container, false);
        empty_view_suggestions = (LinearLayout) mview.findViewById(R.id.empty_view_suggestions);
        empty_view_suggestions.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading your suggestions...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        cache = new File(getCacheDir() + "/" + "suggestions" +customer_id+ ".srl");

        String[] array = getResources().getStringArray(R.array.communities);

        SharedPreferences communityChecker = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        if (communityChecker!=null) {
            for (int i = 0; i < 5; i++) {

                if (communityChecker.getString(array[i], null).contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                    res += " OR tbl_user.customer_no LIKE '" + array[i].toCharArray()[0] + "%'";
                }
            }
        }
        editprefs = (TextView) mview.findViewById(R.id.preference);
//        filters = (TextView) mview.findViewById(R.id.filter);

        editprefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), EditPreferencesActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

//        filters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent i = new Intent(getContext(), Filter.class);
//                startActivity(i);
//                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//
//            }
//        });


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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        // loading cached copy
        String res = CacheHelper.retrieve("suggestions",cache);
        if(!res.equals("")){
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(getContext(),CacheHelper.generateHash(res),"suggestions");

                // displaying it
                JSONArray response = new JSONArray(res);
               // .makeText(getContext(), "Loading from cache....", .LENGTH_SHORT).show();
                parseSuggestions(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        new PrepareSuggestions().execute();




        return mview;
    }

    private void refreshContent() {
        new PrepareSuggestions().execute();
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/prepareSuggestions/{customerNo}/{gender}")
                    .addPathParameter("customerNo", customer_id)
                    .addPathParameter("gender", customer_gender)
                    .addBodyParameter("membership", res)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {


                           //

                            // if no change in data
                            if (isAlreadyLoadedFromCache){

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(getContext(),"suggestions");

                               //
                               //
                               //

                                if (cacheResponseHash!=null && latestResponseHash.equals(cacheResponseHash)){
                                   // .makeText(getContext(), "data same found", .LENGTH_SHORT).show();
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
                                }
                            });
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            swipeRefreshLayout.setRefreshing(false);
            progressDialog.dismiss();
        }
    }

    public void loadedFromNetwork(JSONArray response){


        //saving fresh in cache
        CacheHelper.save("suggestions",response.toString(),cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(getContext(),CacheHelper.generateHash(response.toString()),"suggestions");

        // displaying it
        parseSuggestions(response);

    }
    public void parseSuggestions(JSONArray response){


        try {


            suggestionModelList.clear();
            suggestionAdapter.notifyDataSetChanged();

            if (response.length() == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        empty_view_suggestions.setVisibility(View.VISIBLE);
                    }
                });
            } else {


                empty_view_suggestions.setVisibility(View.GONE);


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

                    String height = array.getString(5);
                    double feet = Double.parseDouble(height) / 30.48;
                    double inches = (Double.parseDouble(height) / 2.54) - ((int) feet * 12);
                    height = (int) feet + "'" + (int) inches;

                    String occupationCompany = array.getString(6);
                    String occupationDesignation = array.getString(7);
                    String annualIncome = array.getString(8);
                    annualIncome = annualIncome.replaceAll("[^-?0-9]+", " ");
                    List<String> incomeArray = Arrays.asList(annualIncome.trim().split(" "));

                    if (annualIncome.contains("Upto")) {
                        annualIncome = "Upto 3L";
                    } else if (annualIncome.contains("Above")) {
                        annualIncome = "Above 50L";

                    } else if (incomeArray.size() == 3) {

                        double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                        double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                        annualIncome = (int) first + "L - " + (int) second + "L";
                    } else {
                        annualIncome = "No Income mentioned.";
                    }


                    String maritalStatus = array.getString(9);
                    String homeName = array.getString(10);
                    String stateName = array.getString(11);
                    String hometown = homeName + ", " + stateName;
                    String surname = array.getString(12);
                    String imageUrl = array.getString(13);
                    String favouriteStatus = array.getString(14);
                    String interestStatus = array.getString(15);
                    name = name + " " + surname;

                    SuggestionModel suggestionModel = new SuggestionModel(Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, name, customerNo, education, occupationLocation, height, occupationCompany, annualIncome, maritalStatus, hometown, occupationDesignation, favouriteStatus, interestStatus);

                    if (!suggestionModelList.contains(suggestionModel) && imageUrl.contains("null")) {
                        suggestionModelList.add(suggestionModel);
                        suggestionAdapter.notifyDataSetChanged();

                    } else {
                        suggestionModelList.add(0, suggestionModel);
                        suggestionAdapter.notifyDataSetChanged();

                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
