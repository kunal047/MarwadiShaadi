package com.example.sid.marwadishaadi.Search;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionAdapter;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.Filter;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment;
import com.example.sid.marwadishaadi.User_Profile.ProfileAdditionalDetailsFragment;
import com.example.sid.marwadishaadi.User_Profile.ProfileFamilyDetailsFragment;
import com.example.sid.marwadishaadi.User_Profile.ProfilePersonalDetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.sid.marwadishaadi.Search.Search.AIList;
import static com.example.sid.marwadishaadi.Search.Search.CastList;
import static com.example.sid.marwadishaadi.Search.Search.annualincome;
import static com.example.sid.marwadishaadi.Search.Search.countannualincome;
import static com.example.sid.marwadishaadi.Search.Search.countfamilystatus;
import static com.example.sid.marwadishaadi.Search.Search.countmaritalstatus;
import static com.example.sid.marwadishaadi.Search.Search.countphysicalstatus;
import static com.example.sid.marwadishaadi.Search.Search.countspinnerCastSearch;
import static com.example.sid.marwadishaadi.Search.Search.familystatus;
import static com.example.sid.marwadishaadi.Search.Search.familystatusList;
import static com.example.sid.marwadishaadi.Search.Search.maritalstatus;
import static com.example.sid.marwadishaadi.Search.Search.maritalstatusList;
import static com.example.sid.marwadishaadi.Search.Search.physicalstatus;
import static com.example.sid.marwadishaadi.Search.Search.physicalstatusList;
import static com.example.sid.marwadishaadi.Search.Search.spinnerCastSearch;

/**
 * Created by vivonk on 01-06-2017.
 */


public class BottomSheet extends BottomSheetDialogFragment {

    private int content;
    private View contentView;
    private UsersAdapter adapter;
    private static String s;
    private int coun;
    private static EditText fname, lname, id;
    private static int count = 0, size = 0;
    public static ArrayList<SuggestionModel> sm;
    public static SuggestionAdapter SA;
    public static String err, success;
    public ProgressDialog dialog;

    public BottomSheet() {

    }

    public BottomSheet(int i) {
        if (i == 0) {
            Search search = new Search();
            content = search.getCasebreak();
        } else if (i == 1) {
            ProfilePersonalDetailsFragment profile_personal_detailsFragment = new ProfilePersonalDetailsFragment();
            content = profile_personal_detailsFragment.getCasebreak();
        } else if (i == 2) {
            ProfileAdditionalDetailsFragment profile_additional_detailsFragment = new ProfileAdditionalDetailsFragment();
            content = profile_additional_detailsFragment.getCasebreak();
        }else if (i==4){
            Filter mfilter = new Filter();
            content=mfilter.getCasebreak();
        } else if(i == 7)
        {
            Signup_Partner_Preferences_Fragment signupPartnerPreferencesFragment = new Signup_Partner_Preferences_Fragment();
            content = signupPartnerPreferencesFragment.getCasebreak();
        }else if(i==111){
            content=111;
        }else if(i==112){
            content=112;
        }else if(i==113){
            content=113;
        } else{
            ProfileFamilyDetailsFragment profile_family_detailsFragment = new ProfileFamilyDetailsFragment();
            content = profile_family_detailsFragment.getCasebreak();
        }
    }


    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        switch (content) {

            //--------------------- EDIT PROFILE PREFERENCES ------------------------------------
            case 111:
                contentView = viewGetter(R.array.status_search_array);
                count=2;
                break;

            case 112:
                contentView = viewGetter(R.array.aincome_search_array);
                count=2;
                break;

            case 113:
                contentView = viewGetter(R.array.physicalstatus_search_array);
                count=2;
                break;

            case 0:
                break;

            //----------------- SEARCH ----------------------------------------------------------

            case 1:
                contentView =  viewGetter(R.array.caste_array);;
                count = 2;
                break;

            case 2:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_search, null);
                count = 1;
                sm = new ArrayList<>();
                SA = new SuggestionAdapter(getContext(), sm, SearchResultsActivity.recyclerView);
                break;

            case 3:
                contentView = viewGetter(R.array.status_search_array);
                count = 2;
                break;

            case 4:
                contentView =viewGetter(R.array.fstatus_search_array);
                count = 2;
                break;

            case 5:
                contentView = viewGetter(R.array.aincome_search_array);
                count = 2;
                break;

            case 6:
                contentView = viewGetter(R.array.physicalstatus_search_array);
                count = 2;
                break;


            //------------------ EDIT PROFILE -PERSONAL DETAILS -----------------------------

            case 12:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_education, null);
                break;
            case 13:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_profession, null);
                break;


            // ----------------- EDIT PROFILE - ADDITIONAL DETAILS --------------------------

            case 21:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_about_me, null);
                break;
            case 22:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_hobbies, null);
                break;
            case 23:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_lifestyle, null);
                break;
            case 24:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_horoscope, null);
                break;

            //-------------------- EDIT PROFILE - FAMILY DETAILS -----------------------------

            case 32:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_relatives, null);
                break;

            default:
                contentView = View.inflate(getContext(), R.layout.custom_list_view, null);
                break;

        }



        // SETTING VIEW

        dialog.setContentView(contentView);


        //------------------ SEARCH -> BY ID/NAME ---------------------------------------

        if (count == 1) {

            Button srch = (Button) contentView.findViewById(R.id.search_by_id_name_search_button);
            srch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fname = (EditText) contentView.findViewById(R.id.search_by_first_name);
                    lname = (EditText) contentView.findViewById(R.id.search_by_last_name);
                    id = (EditText) contentView.findViewById(R.id.search_by_id);
                    String strfname, strlname, strid;
                    strfname = fname.getText().toString();
                    strlname = lname.getText().toString();
                    strid = id.getText().toString();
                    if (!strid.trim().isEmpty() & (!strlname.trim().isEmpty() | !strfname.trim().isEmpty())) {
                        Toast.makeText(getContext(), " Please use either ID or Name ", Toast.LENGTH_SHORT).show();
                        fname.setText("");
                        lname.setText("");
                        id.setText("");
                    } else if (!strid.trim().isEmpty()) {
                        new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation  from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.customer_no=\"" + strid + "\" ; ");
                        if (success == "success") {
                            if (size == 0) {
                                Toast.makeText(getContext(), "No result found on these query", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                i.putExtra("COUNT", size);
                                size = 0;
                                fname.setText("");
                                lname.setText("");
                                id.setText("");
                                startActivity(i);

                            }
                        } else {
                            Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        }
                    } else if ((!strlname.trim().isEmpty() & !strfname.trim().isEmpty())) {
                        new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation  from tbl_user  INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.first_name=\" " + strfname + "\"and tbl_user.surname=\"" + strlname + "\" order by created_on asc ;");
                        if (success == "success") {
                            if (size == 0) {
                                Toast.makeText(getContext(), "No result found on these query", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                i.putExtra("COUNT", size);
                                size = 0;
                                fname.setText("");
                                lname.setText("");
                                id.setText("");
                                startActivity(i);

                            }
                        } else {
                            Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        }
                    } else if ((!strlname.trim().isEmpty() | !strfname.trim().isEmpty())) {

                        if (!strlname.trim().isEmpty()) {
                            new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.surname=\"" + strlname + "\"   order by created_on asc ;");
                            if (success == "success") {
                                if (size == 0) {

                                    Toast.makeText(getContext(), "No result found on these query", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                    i.putExtra("COUNT", size);
                                    Log.d(TAG, "onClick: sizzw is ************************************* " + Integer.toString(size));
                                    fname.setText("");
                                    lname.setText("");
                                    id.setText("");
                                    size = 0;
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.first_name=\"" + strfname + "\"   order by created_on asc;");


                            if (success == "success") {
                                if (size == 0) {
                                    Toast.makeText(getContext(), "No results found! ", Toast.LENGTH_SHORT).show();
                                } else {

                                    Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                    i.putExtra("COUNT", size);
                                    fname.setText("");
                                    lname.setText("");
                                    id.setText("");
                                    size = 0;
                                    i.putExtra("which","second");
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        Toast.makeText(getContext(), "Search detail can't be empty", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        } else if (count == 2) {
            coun = 0;
            Button btn = (Button) contentView.findViewById(R.id.ok);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> result = new ArrayList<String>();
                    for (User p : adapter.getBox()) {
                        if (p.box) {
                            coun++;
                            result.add(p.name);

                        }
                    }
                    switch (content) {
                        case 1:
                            spinnerCastSearch.setText(result.toString());
                            CastList = result;
                            countspinnerCastSearch = coun;
                            break;
                        case 3:
                            maritalstatus.setText(result.toString());
                            maritalstatusList = result;
                            countmaritalstatus = coun;
                            break;
                        case 4:
                            familystatus.setText(result.toString());
                            familystatusList = result;
                            countfamilystatus = coun;
                            break;
                        case 5:
                            annualincome.setText(result.toString());
                            AIList = result;
                            countannualincome = coun;
                            break;
                        case 6:
                            physicalstatus.setText(result.toString());
                            physicalstatusList = (result);
                            countphysicalstatus = coun;
                            break;
                    }
                    dialog.dismiss();
                }
            });


        }
    }

    class BackNd extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(getContext());
            dialog.setMessage("Please Wait...");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "response query is  ----------" + strings[0]);
            AndroidNetworking.post("http://192.168.43.61:5050/searchById")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String s = response.toString();
                            Log.d(TAG, "onResponse:----------------" + s);
                            size = response.length();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONArray user = response.getJSONArray(i);

                                    Calendar calender = Calendar.getInstance();
                                    int year = calender.get(Calendar.YEAR);
                                    SuggestionModel suggestionModel = new SuggestionModel(year - (int) user.get(0), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), user.get(8).toString(), user.get(9).toString(), user.get(10).toString(), user.get(11).toString());
                                    sm.add(suggestionModel);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            success = "success";

                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "onError: --------------------- error is " + error);
                            err = "Network Error, Check Connection";
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    private View viewGetter(int array) {

        ArrayList<User> arrayOfUsers = new ArrayList<>();
        String[] str = getResources().getStringArray(array);
        for (int i = 0; i < str.length; i++) {
            User user = new User(str[i], "null");
            arrayOfUsers.add(user);
        }
        adapter = new UsersAdapter(getContext(), arrayOfUsers);
        View view = View.inflate(getContext(), R.layout.custom_list_view, null);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        return view;
    }
}

