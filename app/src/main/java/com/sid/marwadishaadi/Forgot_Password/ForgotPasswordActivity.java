package com.sid.marwadishaadi.Forgot_Password;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.Login.LoginActivity;
import com.sid.marwadishaadi.R;

import org.json.JSONArray;
import org.json.JSONException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPasswordActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_SETTING = 105;
    public static final int CALL_PHONE_PERMISSION = 102;
    private static final String TAG = "";
    protected EditText email;
    protected Button submit;
    protected LinearLayout call_us;
    protected TextView call_us_number;
    private String user_email;
    private boolean sentmail;
    private FirebaseAnalytics mFirebaseAnalytics;
    private View view;

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

        view = getWindow().getDecorView().getRootView();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "forgot");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "button");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        call_us = (LinearLayout) findViewById(R.id.call_us);
        email = (EditText) findViewById(R.id.user_email);
        submit = (Button) findViewById(R.id.Submit_forgot);
        call_us_number = (TextView) findViewById((R.id.call_us_number));

        call_us_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // add permission here
                int permissionCheck = ContextCompat.checkSelfPermission(ForgotPasswordActivity.this, Manifest.permission.CALL_PHONE);

                if (permissionCheck == PackageManager.PERMISSION_DENIED) {

                    if (!getPermissionStatus()) {

                        Dexter.withActivity(ForgotPasswordActivity.this)
                                .withPermission(Manifest.permission.CALL_PHONE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        Call();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {

                                        setPermissionStatus();
                                        showSettings();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    }
                                }).check();
                    } else {
                        showSettings();
                    }
                } else {
                    Call();
                }


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_email = email.getText().toString();
                new ForgotPassword().execute();

            }
        });
    }

    public void Call() {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Call();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showSettings() {
        Snackbar snackbar = Snackbar
                .make(view.getRootView(), "Go to settings and grant permission", Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });

        snackbar.show();
    }

    private Boolean getPermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isPhonePermissionDenied", false);
    }

    private void setPermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isPhonePermissionDenied", true);
        edit.apply();
    }

    private class SendMail extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post(Constants.AWS_SERVER + "/sendMail")
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

            AndroidNetworking.post(Constants.AWS_SERVER + "/forgotPassword")
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


                                if (res == 1) {
                                    sentmail = true;
                                    new SendMail().execute();
                                    Toast.makeText(ForgotPasswordActivity.this, "Please check your e-mail for temporary password", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);

                                } else {
                                    sentmail = false;
                                    Toast.makeText(getApplicationContext(), "This e-mail is not registered with us", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onError(ANError error) {

                            // handle error
                        }
                    });


            return null;
        }


    }
}


