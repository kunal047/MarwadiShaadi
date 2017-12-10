package com.sid.marwadishaadi.Membership;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.CacheHelper;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class UpgradeMembershipActivity extends AppCompatActivity {
    public ProgressDialog dialog;
    Button upgrade;
    CardView maheshwari, agarwal, jain, khandelwal, others, no;
    TextView agarwal_duration, jain_duration, khandelwal_duration, maheshwari_duration, others_duration;
    TextView agarwal_start, jain_start, khandelwal_start, maheshwari_start, others_start;
    TextView agarwal_end, jain_end, khandelwal_end, maheshwari_end, others_end;
    TextView name, id;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String customer_id;
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;
    private de.hdodenhof.circleimageview.CircleImageView membership_photo;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_membership);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        cache = new File(getCacheDir() + "/" + "membership" + customer_id + ".srl");

        Toolbar toolbar = findViewById(R.id.upgrademembership_toolbar);
        toolbar.setTitle("Membership Status");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upgrade = findViewById(R.id.upgrade);
        maheshwari = findViewById(R.id.maheshwari);
        agarwal = findViewById(R.id.agarwal);
        jain = findViewById(R.id.jain);
        khandelwal = findViewById(R.id.khandelwal);
        others = findViewById(R.id.others);
        no = findViewById(R.id.no_membership);

        name = findViewById(R.id.membership_name);
        id = findViewById(R.id.membership_id);

        agarwal_duration = findViewById(R.id.duration_agarwal);
        agarwal_start = findViewById(R.id.start_agarwal);
        agarwal_end = findViewById(R.id.end_agarwal);

        jain_duration = findViewById(R.id.duration_jain);
        jain_start = findViewById(R.id.start_jain);
        jain_end = findViewById(R.id.end_jain);

        khandelwal_duration = findViewById(R.id.duration_khandelwal);
        khandelwal_start = findViewById(R.id.start_khandelwal);
        khandelwal_end = findViewById(R.id.end_khandelwal);

        maheshwari_duration = findViewById(R.id.duration_maheshwari);
        maheshwari_start = findViewById(R.id.start_maheshwari);
        maheshwari_end = findViewById(R.id.end_maheshwari);

        others_duration = findViewById(R.id.duration_others);
        others_start = findViewById(R.id.start_others);
        others_end = findViewById(R.id.end_others);

        membership_photo = findViewById(R.id.membership_photo);


        SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String imageURL = userinfo.getString("imageURL", null);
        if (imageURL != null) {
            Picasso.with(getApplicationContext())
                    .load(imageURL)
                    .placeholder(R.drawable.default_drawer)
                    .into(membership_photo);
        }

        jain.setVisibility(View.GONE);
        agarwal.setVisibility(View.GONE);
        khandelwal.setVisibility(View.GONE);
        maheshwari.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        no.setVisibility(View.GONE);


        // loading cached copy
        String res = CacheHelper.retrieve("membership", cache);
        if (!res.equals("")) {
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(UpgradeMembershipActivity.this, CacheHelper.generateHash(res), "membership");

                // displaying it
                JSONArray response = new JSONArray(res);
                //.makeText(UpgradeMembershipActivity.this, "Loading from cache....", .LENGTH_SHORT).show();
                parseMembership(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String query = "SELECT community, duration, date(purchase_date), date(expiry_date), is_active FROM `tbl_user_community_package` WHERE customer_no=\"" + customer_id + "\";";
        new BackEndMembership().execute(query);

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Upgrade Membership", "button");
                Intent i = new Intent(UpgradeMembershipActivity.this, MembershipActivity.class);
                startActivity(i);
            }
        });


    }

    private void parseMembership(JSONArray response) {

        if (response.length() == 0) {
            no.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < response.length(); i++) {
                JSONArray membership = null;
                try {
                    membership = response.getJSONArray(i);
                    if (membership.getString(4).contains("No")) {
                        no.setVisibility(View.VISIBLE);
                    } else if (membership.getString(0).contains("Agarwal")) {
                        agarwal.setVisibility(View.VISIBLE);

                        agarwal_duration.setText("Duration: " + membership.getString(1).replace("months", " months"));
                        agarwal_start.setText("Started On: " + membership.getString(2).replace("00:00:00 GMT", ""));
                        agarwal_end.setText("Ends On: " + membership.getString(3).replace("00:00:00 GMT", ""));
                    } else if (membership.getString(0).contains("Jain")) {
                        jain.setVisibility(View.VISIBLE);

                        jain_duration.setText("Duration: " + membership.getString(1).replace("months", " months"));
                        jain_start.setText("Started On: " + membership.getString(2).replace("00:00:00 GMT", ""));
                        jain_end.setText("Ends On: " + membership.getString(3).replace("00:00:00 GMT", ""));
                    } else if (membership.getString(0).contains("Khandelwal")) {
                        khandelwal.setVisibility(View.VISIBLE);

                        khandelwal_duration.setText("Duration: " + membership.getString(1).replace("months", " months"));
                        khandelwal_start.setText("Started On: " + membership.getString(2).replace("00:00:00 GMT", ""));
                        khandelwal_end.setText("Ends On: " + membership.getString(3).replace("00:00:00 GMT", ""));
                    } else if (membership.getString(0).contains("Maheshwari")) {
                        maheshwari.setVisibility(View.VISIBLE);

                        maheshwari_duration.setText("Duration: " + membership.getString(1).replace("months", " months"));
                        maheshwari_start.setText("Started On: " + membership.getString(2).replace("00:00:00 GMT", ""));
                        maheshwari_end.setText("Ends On: " + membership.getString(3).replace("00:00:00 GMT", ""));
                    } else if (membership.getString(0).contains("Other")) {
                        others.setVisibility(View.VISIBLE);

                        others_duration.setText("Duration: " + membership.getString(1).replace("months", " months"));
                        others_start.setText("Started On: " + membership.getString(2).replace("00:00:00 GMT", ""));
                        others_end.setText("Ends On: " + membership.getString(3).replace("00:00:00 GMT", ""));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            SharedPreferences prefs = getSharedPreferences("userinfo", MODE_PRIVATE);
            String nameOfUser = prefs.getString("firstname", null);


            name.setText("Hello, " + nameOfUser);
            id.setText("Member ID : " + customer_id);
        }
    }

    public void loadedFromNetwork(JSONArray response) {


        //saving fresh in cache
        CacheHelper.save("membership", response.toString(), cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(UpgradeMembershipActivity.this, CacheHelper.generateHash(response.toString()), "membership");

        // displaying it
        parseMembership(response);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.exit, 0);
        return true;
    }

    private class BackEndMembership extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UpgradeMembershipActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            AndroidNetworking.post(Constants.AWS_SERVER + "/MembershipStatus")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(JSONArray response) {

                            //

                            // if no change in data
                            if (isAlreadyLoadedFromCache) {

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(UpgradeMembershipActivity.this, "membership");

                                //
                                //
                                //

                                if (cacheResponseHash != null && latestResponseHash.equals(cacheResponseHash)) {
                                    //.makeText(UpgradeMembershipActivity.this, "data same found", .LENGTH_SHORT).show();
                                    return;
                                } else {

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
                            Toast.makeText(getApplicationContext(), "Network Error Occurred. Please check Internet", Toast.LENGTH_LONG);
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

        }
    }

}
