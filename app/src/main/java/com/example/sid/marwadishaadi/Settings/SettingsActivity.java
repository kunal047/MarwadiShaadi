package com.example.sid.marwadishaadi.Settings;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.model.Progress;
import com.example.sid.marwadishaadi.About_Us.AboutUsActivity;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Blocked_Members.BlockedActivity;
import com.example.sid.marwadishaadi.Contact_Us.ContactUsActivity;
import com.example.sid.marwadishaadi.Faq.FaqActivity;
import com.example.sid.marwadishaadi.Login.LoginActivity;
import com.example.sid.marwadishaadi.Payment_Policy.PaymentPolicyActivity;
import com.example.sid.marwadishaadi.Privacy_Policy.PrivacyPolicyActivity;
import com.example.sid.marwadishaadi.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.HashConverter;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    protected SwitchCompat notifications;
    protected TextView reset_pass;
    protected TextView deactivate_acc;
    protected TextView delete_acc;
    protected TextView blocked;
    protected TextView logout;
    protected TextView faq;
    protected TextView aboutus;
    protected TextView privacypolicy;
    protected TextView contactus;
    protected TextView paymentpolicy;
    protected LinearLayout morelinearlayout;
    protected TextView more;
    protected AlertDialog resetbox;
    String query="",old_pass_encrypt, user_old_pass,user_new_pass;
    private String customer_id, customer_gender;
    private ProgressDialog dialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        faq = (TextView) findViewById(R.id.faq);
        aboutus = (TextView) findViewById(R.id.aboutus);
        privacypolicy= (TextView) findViewById(R.id.privacypolicy);
        paymentpolicy = (TextView) findViewById(R.id.paymentpolicy);
        contactus= (TextView) findViewById(R.id.contactus);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"FAQ","textview");

                Intent i = new Intent(SettingsActivity.this,FaqActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"About US","textview");

                Intent i = new Intent(SettingsActivity.this,AboutUsActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Privacy Policy","textview");

                Intent i = new Intent(SettingsActivity.this,PrivacyPolicyActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        paymentpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Payment Policy","textview");
                Intent i = new Intent(SettingsActivity.this,PaymentPolicyActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Contact Us","textview");

                Intent i = new Intent(SettingsActivity.this,ContactUsActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });


        notifications = (SwitchCompat) findViewById(R.id.switchNot);
        reset_pass = (TextView) findViewById(R.id.reset_password);
        deactivate_acc = (TextView) findViewById(R.id.deactivate_acc);
        delete_acc = (TextView) findViewById(R.id.delete_acc);
        blocked = (TextView) findViewById(R.id.blocked);
        logout = (TextView) findViewById(R.id.Logout);
        more = (TextView) findViewById(R.id.more);
        morelinearlayout = (LinearLayout) findViewById(R.id.morelayout);


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morelinearlayout.getVisibility() == View.GONE){
                    morelinearlayout.setVisibility(View.VISIBLE);
                }else{
                    morelinearlayout.setVisibility(View.GONE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder logout_user = new AlertDialog.Builder(SettingsActivity.this);
                logout_user.setMessage("Are you sure you want to logout ?")
                        .setIcon(R.drawable.setting_logout)
                        .setCancelable(true)
                        .setTitle("Logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // perform check
                                LoginManager.getInstance().logOut();
                                AccessToken.setCurrentAccessToken(null);
                                SharedPreferences sharedPre=getSharedPreferences("userinfo",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPre.edit();
                                editor.putBoolean("isLoggedIn",false);
                                editor.putString("email","");
                                editor.putString("password","");
                                editor.putString("customer_id","");
                                editor.commit();

                                // analytics
                                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Logout","textview");

                                Intent i=new Intent(SettingsActivity.this, LoginActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertbox = logout_user.create();
                alertbox.setTitle("Logout");
                alertbox.show();
            }
        });

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                View reset_view = getLayoutInflater().inflate(R.layout.reset_dialog,null);
                AlertDialog.Builder reset = new AlertDialog.Builder(SettingsActivity.this);
                reset.setView(reset_view);

                final EditText oldpass = (EditText) reset_view.findViewById(R.id.user_old_pass);
                final EditText newpass = (EditText) reset_view.findViewById(R.id.user_new_password);
                Button resetbutton = (Button) reset_view.findViewById(R.id.user_reset);
                final TextView call_us = (TextView) reset_view.findViewById(R.id.reset_call_number);
                final LinearLayout reset_contact = (LinearLayout) reset_view.findViewById(R.id.reset_call_us);

                resetbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // analytics
                        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Reset Password","textview");

                         user_old_pass = oldpass.getText().toString();
                         user_new_pass = newpass.getText().toString();

//                        customer_id="J1001";
                         query = "SELECT password FROM `tbl_login` WHERE customer_no=\""+customer_id+"\";";
                        new BackEnd().execute(query);


                    }
                });

                reset_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + call_us.getText().toString()));//change the number
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getApplicationContext(),"Permission_Util for Call Denied!",Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            AlertDialog.Builder discarduser = new AlertDialog.Builder(SettingsActivity.this);
                            discarduser.setMessage("Do you want to call " + call_us.getText().toString() + " ? ")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            startActivity(callIntent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            // setting up dialog box
                            AlertDialog alertbox = discarduser.create();
                            alertbox.setTitle("Contact Us");
                            alertbox.show();
                        }
                    }
                });

                 resetbox = reset.create();
                resetbox.show();
            }
        });

        deactivate_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    AlertDialog.Builder deactivate = new AlertDialog.Builder(SettingsActivity.this);
                    deactivate.setMessage("Deactivating will temporarily disable your account.")
                            .setIcon(R.drawable.setting_blocked)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // analytics
                                    Analytics_Util.logAnalytic(mFirebaseAnalytics,"Deactivate Acc","textview");

                                    Toast.makeText(SettingsActivity.this, "cool!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SettingsActivity.this, "ohh whyyy", Toast.LENGTH_SHORT).show();
                                }
                            });



                AlertDialog box = deactivate.create();
                box.setTitle("Deactivate Account ?");
                box.show();

            }
        });

        delete_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder delete = new AlertDialog.Builder(SettingsActivity.this);
                delete.setMessage("Deleting will permanently remove your account.")
                        .setIcon(R.drawable.setting_delete)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingsActivity.this, "cool!", Toast.LENGTH_SHORT).show();
                                // analytics
                                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Delete acc","textview");

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingsActivity.this, "ohh whyyy", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog box = delete.create();
                box.setTitle("Delete Account ?");
                box.show();
            }
        });

        blocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Block view","textview");

                Intent i = new Intent(SettingsActivity.this,BlockedActivity.class);
                i.putExtra("Message","setting");
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        finish();
        overridePendingTransition(R.anim.exit,0);

        return true;
    }


private class BackEnd extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: -----query s "+ query);
        AndroidNetworking.post("http://208.91.199.50:5000/ResetPassword")
                .addBodyParameter("query", strings[0])
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse1: --------- "+response);
                            JSONArray user= null;
                            try {
                                user = response.getJSONArray(0);
                                old_pass_encrypt=HashConverter(user_old_pass);
                                if(user.getString(0).contains(old_pass_encrypt)){
                                    Log.d(TAG, "onResponse2: ------ old password is --"+old_pass_encrypt);
                                    final String quer="update tbl_login set password = \""+HashConverter(user_new_pass)+"\" where customer_no=\""+customer_id+"\";";
                                    AndroidNetworking.post("http://208.91.199.50:5000/ResetPassword")
                                            .addBodyParameter("query", quer)
                                            .setPriority(Priority.HIGH)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    Log.d(TAG, "onResponse: ********** new passord ***      "+quer);
                                                    Toast.makeText(getApplicationContext(),"Password has been changed successfully",Toast.LENGTH_LONG).show();
                                                    SettingsActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if(dialog.isShowing()){
                                                                dialog.dismiss();
                                                            }
                                                            if(resetbox.isShowing()){
                                                                resetbox.dismiss();
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    Toast.makeText(getApplicationContext(),"Network Error. Please try again.",Toast.LENGTH_LONG).show();
                                                    Log.d(TAG, "onError: ----network error  88"+ anError);
                                                    SettingsActivity.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if(dialog.isShowing()){
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }

                                else
                                    Toast.makeText(getApplicationContext(),"Entered password was incorrect. Please try again later",Toast.LENGTH_LONG).show();

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                        /*SettingsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }
                        });*/
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getApplicationContext(),"Network Error Occurred. Please check Internet",Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onError: ----network error  88  "+error);
                        SettingsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
}
