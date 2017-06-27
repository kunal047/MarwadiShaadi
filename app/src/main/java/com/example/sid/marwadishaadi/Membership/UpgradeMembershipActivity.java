package com.example.sid.marwadishaadi.Membership;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.Search;
import com.example.sid.marwadishaadi.Search.SearchResultsActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meg7.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.Search.Search.suggestionModelList2;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


public class UpgradeMembershipActivity extends AppCompatActivity {
    Button upgrade;
    CardView maheshwari, agarwal, jain, khandelwal, others,no;

    private FirebaseAnalytics mFirebaseAnalytics;
    TextView agarwal_duration, jain_duration, khandelwal_duration, maheshwari_duration, others_duration;
    TextView agarwal_start, jain_start, khandelwal_start, maheshwari_start, others_start;
    TextView agarwal_end, jain_end, khandelwal_end, maheshwari_end, others_end;
    TextView name,id;
    public ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_membership);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.upgrademembership_toolbar);
        toolbar.setTitle("Membership Status");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upgrade = (Button)findViewById(R.id.upgrade);
        maheshwari = (CardView) findViewById(R.id.maheshwari);
        agarwal = (CardView) findViewById(R.id.agarwal);
        jain = (CardView) findViewById(R.id.jain);
        khandelwal = (CardView) findViewById(R.id.khandelwal);
        others = (CardView) findViewById(R.id.others);
        no=(CardView)findViewById(R.id.no_membership);

        name=(TextView)findViewById(R.id.membership_name);
        id=(TextView)findViewById(R.id.membership_id);

        agarwal_duration=(TextView)findViewById(R.id.duration_agarwal);
        agarwal_start=(TextView)findViewById(R.id.start_agarwal);
        agarwal_end=(TextView)findViewById(R.id.end_agarwal);

        jain_duration=(TextView)findViewById(R.id.duration_jain);
        jain_start=(TextView)findViewById(R.id.start_jain);
        jain_end=(TextView)findViewById(R.id.end_jain);

        khandelwal_duration=(TextView)findViewById(R.id.duration_khandelwal);
        khandelwal_start=(TextView)findViewById(R.id.start_khandelwal);
        khandelwal_end=(TextView)findViewById(R.id.end_khandelwal);

        maheshwari_duration=(TextView)findViewById(R.id.duration_maheshwari);
        maheshwari_start=(TextView)findViewById(R.id.start_maheshwari);
        maheshwari_end=(TextView)findViewById(R.id.end_maheshwari);

        others_duration=(TextView)findViewById(R.id.duration_others);
        others_start=(TextView)findViewById(R.id.start_others);
        others_end=(TextView)findViewById(R.id.end_others);

        jain.setVisibility(View.GONE);
        agarwal.setVisibility(View.GONE);
        khandelwal.setVisibility(View.GONE);
        maheshwari.setVisibility(View.GONE);
        others.setVisibility(View.GONE);
        no.setVisibility(View.GONE);

        customer_id="A1001";
        String query = "SELECT community, duration, date(purchase_date), date(expiry_date), is_active FROM `tbl_user_community_package` WHERE customer_no=\""+customer_id+"\";";
        Log.d(TAG, "onCreate: ----- query is "+query);
        new BackEndMembership().execute(query);

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Upgrade Membership","button");
                Intent i = new Intent(UpgradeMembershipActivity.this,MembershipActivity.class);
                startActivity(i);
            }
        });



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
            AndroidNetworking.post(URL + "MembershipStatus")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.e(TAG, "onResponse: -------------- "+response.toString());
                            if (response.length()==0){
                                no.setVisibility(View.VISIBLE);
                            }
                            else {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONArray membership = null;
                                    try {
                                        membership = response.getJSONArray(i);
                                        if (membership.getString(4).contains("No")) {
                                            no.setVisibility(View.VISIBLE);
                                        } else if (membership.getString(0).contains("Agarwal")) {
                                            agarwal.setVisibility(View.VISIBLE);

                                            agarwal_duration.setText("Duration: " + membership.getString(1));
                                            agarwal_start.setText("Started On: " + membership.getString(2));
                                            agarwal_end.setText("Ends On: " + membership.getString(3));
                                        } else if (membership.getString(0).contains("Jain")) {
                                            jain.setVisibility(View.VISIBLE);

                                            jain_duration.setText("Duration: " + membership.getString(1));
                                            jain_start.setText("Started On: " + membership.getString(2));
                                            jain_end.setText("Ends On: " + membership.getString(3));
                                        } else if (membership.getString(0).contains("Khandelwal")) {
                                            khandelwal.setVisibility(View.VISIBLE);

                                            khandelwal_duration.setText("Duration: " + membership.getString(1));
                                            khandelwal_start.setText("Started On: " + membership.getString(2));
                                            khandelwal_end.setText("Ends On: " + membership.getString(3));
                                        } else if (membership.getString(0).contains("Maheshwari")) {
                                            maheshwari.setVisibility(View.VISIBLE);

                                            maheshwari_duration.setText("Duration: " + membership.getString(1));
                                            maheshwari_start.setText("Started On: " + membership.getString(2));
                                            maheshwari_end.setText("Ends On: " + membership.getString(3));
                                        } else if (membership.getString(0).contains("Other")) {
                                            others.setVisibility(View.VISIBLE);

                                            others_duration.setText("Duration: " + membership.getString(1));
                                            others_start.setText("Started On: " + membership.getString(2));
                                            others_end.setText("Ends On: " + membership.getString(3));
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            Toast.makeText(getApplicationContext(),"Network Error Occurred. Please check Internet",Toast.LENGTH_LONG);
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
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.exit,0);
        return true;
    }

}
