package com.example.sid.marwadishaadi.Forgot_Password;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Notif_Message;
import com.example.sid.marwadishaadi.Notifications_Util;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "";
    protected EditText email;
    protected Button submit;
    protected LinearLayout call_us;
    protected TextView call_us_number;
    private String user_email;
    private boolean sentmail;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"forgot");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"button");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);





        call_us = (LinearLayout) findViewById(R.id.call_us);
        email = (EditText) findViewById(R.id.user_email);
        submit = (Button) findViewById(R.id.Submit_forgot);
        call_us_number = (TextView) findViewById((R.id.call_us_number));

        call_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + call_us_number.getText().toString()));//change the number
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "Permission for Call Denied!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    AlertDialog.Builder discarduser = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    discarduser.setMessage("Do you want to call " + call_us_number.getText().toString() + " ? ")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked --------------------------------------------- ");
                System.out.println("adfsjlkadsjklfj klasjfk as as ");
                user_email = email.getText().toString();
                new ForgotPassword().execute();

            }
        });
    }

    private class SendMail extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: called ----------------------------");
            AndroidNetworking.post("http://192.168.43.143:5050/sendMail")
                    .addBodyParameter("forgotPassEmail", user_email)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }
    }


    private class ForgotPassword extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://192.168.43.143:5050/forgotPassword")
                    .addBodyParameter("forgotPassEmail", user_email)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray result = response.getJSONArray(0);
                                int res = Integer.parseInt(result.get(0).toString());
                                Log.d(TAG, "onResponse: ------------------------ " + res);

                                if (res == 1) {
                                    sentmail = true;
                                    Log.d(TAG, "onResponse: in response ^^^^^^^^^^^^^^^^ ");
                                    new SendMail().execute();
                                    Log.d(TAG, "onResponse: end of response ");

                                } else {
                                     sentmail = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "onError: errr ------------- " + error.toString());
                            // handle error
                        }
                    });





            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!sentmail) {
                Toast.makeText(getApplicationContext(), "This email is not registered with us", Toast.LENGTH_LONG).show();
            }
        }
    }
}
               /* new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {






                       /* if (user_email.equals()) {


                        } else {
                            Toast.makeText(getApplicationContext(), "This email-id is not registered!", Toast.LENGTH_SHORT);
                        }
                        return null;
                    }
            }.execute();

            }
        });


    }

}*/
