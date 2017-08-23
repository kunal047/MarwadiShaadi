package com.sid.marwadishaadi.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.Forgot_Password.ForgotPasswordActivity;
import com.sid.marwadishaadi.Notifications_Util;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Signup.SignupActivity;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity {

    public static String customer_gender;
    public ProgressDialog dialog;
    public String str = "";
    protected EditText login_email;
    protected EditText login_pass;
    protected Button login;
    protected TextView forgot;
    protected TextView signup;
    protected LoginButton fblogin;
    protected CallbackManager callbackManager;
    private boolean checker = false;
    private String email, pass;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String customer_id;

    private ImageView showPassword;
    private ImageView hidePassword;

    //    anita.k@makindia.com
    public static String HashConverter(String pswrd) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(pswrd.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length(
            ) < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        AndroidNetworking.initialize(getApplicationContext());

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        callbackManager = CallbackManager.Factory.create();


        login_email = (EditText) findViewById(R.id.login_email);
        login_pass = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login);

        showPassword = (ImageView) findViewById(R.id.show_password);
        hidePassword = (ImageView) findViewById(R.id.hide_password);


        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword.setVisibility(View.GONE);
                hidePassword.setVisibility(View.VISIBLE);
                login_pass.setTransformationMethod(null);
                login_pass.setSelection(login_pass.getText().length());

            }
        });
        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword.setVisibility(View.VISIBLE);
                hidePassword.setVisibility(View.GONE);
                login_pass.setTransformationMethod(new PasswordTransformationMethod());
                login_pass.setSelection(login_pass.getText().length());
            }
        });

//        fblogin = (LoginButton) findViewById(R.id.fb_login_button);
//        fblogin.setReadPermissions(Arrays.asList("email", "user_photos"));
//        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//
//
//                        try {
//                            String first_name = object.getString("first_name");
//                            String last_name = object.getString("last_name");
//                            String gender = object.getString("gender");
//                            String name = object.getString("name");
//
//                            // check must be performed here
//                            //String email = object.getString("email");
//                            String birthday = object.getString("birthday");
//                            Toast.makeText(getApplicationContext(), first_name + last_name + gender + birthday, Toast.LENGTH_LONG).show();
//
//                            // MUST GO TO dashboard
//                            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
//                            startActivity(i);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,about,birthday,middle_name,first_name,last_name,email,gender,name,relationship_status");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//
//
//        });

        forgot = (TextView) findViewById(R.id.forgot_link);
        signup = (TextView) findViewById(R.id.signup_link);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Forgot", "button");
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Signup", "button");
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
                                     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                     @Override
                                     public void onClick(View v) {

                                         email = login_email.getText().toString();
                                         pass = login_pass.getText().toString();

                                         if (!email.trim().matches("^[M|A|J|K|O][0-9]{4,6}")  | EmailChecker(email) & !pass.trim().isEmpty()) {
                                             pass = HashConverter(pass);
                                             dialog.setMessage("Please Wait...");
                                             dialog.show();

                                             char charEmail = email.charAt(0);
                                             char character = email.charAt(0);
                                             if ((int) charEmail < 123 & (int) charEmail > 96) {
                                                 charEmail = (char) ((int) charEmail - 32);
                                                 email = email.replace(character, charEmail);

                                             }

                                             new BackGround().execute("email", email, pass);
                                             final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5);
                                             scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     try {
                                                         checker = true;

                                                         if (!checker) {

                                                         } else {
                                                             if (Looper.myLooper() == null) {
                                                                 Looper.prepare();
                                                             }


                                                             if (str.equals("success")) {

                                                                 SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                                 SharedPreferences.Editor editors = userinfo.edit();
                                                                 editors.putBoolean("isLoggedIn", true);
                                                                 editors.putString("id", customer_id);
                                                                 editors.apply();

                                                                 SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
                                                                 SharedPreferences.Editor editor = sharedpref.edit();
                                                                 editor.putBoolean("isLoggedIn", true);
                                                                 editor.putString("email", email);
                                                                 editor.putString("password", pass);
                                                                 editor.putString("customer_id", customer_id);
                                                                 editor.putString("gender", customer_gender);
                                                                 editor.apply();
                                                                 dialog.dismiss();

                                                                 registerMe();

                                                                 Intent deeplink_data = getIntent();
                                                                 String deeplink = deeplink_data.getStringExtra("deeplink");
                                                                 if (deeplink != null) {
                                                                     Intent i = new Intent(LoginActivity.this, UserProfileActivity.class);
                                                                     i.putExtra("deeplink", deeplink);
                                                                     startActivity(i);
                                                                     overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                                                 }

                                                                 Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                                                 startActivity(i);
                                                                 overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                                                             } else if (str.equals("failure")) {
                                                                 Toast.makeText(LoginActivity.this, "Your Email or Password is incorrect, Please try again !!", Toast.LENGTH_SHORT).show();
                                                                 dialog.dismiss();
                                                                 scheduledExecutorService.shutdown();
                                                             } else if (str.equals("----")) {
                                                                 //                        android.app.Dialog dlg=new android.app.Dialog(getApplicationContext(),R.layout.error);
                                                                 Toast.makeText(LoginActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                                                                 dialog.dismiss();
                                                                 scheduledExecutorService.shutdown();

                                                             } else {
                                                                 Toast.makeText(getApplicationContext(), "Please enter correct Email Address", Toast.LENGTH_SHORT).show();
                                                                 dialog.dismiss();
                                                                 scheduledExecutorService.shutdown();
                                                             }
                                                             Looper.loop();
                                                         }
                                                     } catch (Exception e) {

                                                     }
                                                 }
                                             }, 1, 3, TimeUnit.SECONDS);

                                             // analytics
                                             Analytics_Util.logAnalytic(mFirebaseAnalytics, "Login", "button");

                                             // rest
                                         } else if ((email.trim().contains("M") | email.trim().contains("m") | email.trim().contains("a") | email.trim().contains("A") | email.trim().contains("o") | email.trim().contains("O") | email.trim().contains("J") | email.trim().contains("j") | email.trim().contains("K") | email.trim().contains("k")) & !pass.trim().isEmpty()) {
                                             dialog.setMessage("Please Wait...");
                                             dialog.show();
                                             pass = HashConverter(pass);
                                             new BackGround().execute("user_id", email, pass);

                                             final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5);
                                             scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     try {

                                                         if (!checker) {

                                                         } else {
                                                             if (Looper.myLooper() == null) {
                                                                 Looper.prepare();
                                                             }


                                                             if (str.equals("success")) {
                                                                 SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                                 SharedPreferences.Editor editors = userinfo.edit();
                                                                 editors.putBoolean("isLoggedIn", true);
                                                                 editors.putString("id", customer_id);
                                                                 editors.apply();

                                                                 SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
                                                                 SharedPreferences.Editor editor = sharedpref.edit();
                                                                 editor.putBoolean("isLoggedIn", true);
                                                                 editor.putString("email", email);
                                                                 editor.putString("password", pass);
                                                                 editor.putString("customer_id", customer_id);
                                                                 editor.putString("gender", customer_gender);
                                                                 editor.apply();
                                                                 dialog.dismiss();


                                                                 registerMe();
                                                                 Intent deeplink_data = getIntent();
                                                                 String deeplink = deeplink_data.getStringExtra("deeplink");
                                                                 if (deeplink != null) {
                                                                     Intent i = new Intent(LoginActivity.this, UserProfileActivity.class);
                                                                     i.putExtra("deeplink", deeplink);
                                                                     startActivity(i);
                                                                     overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                                                 }


                                                                 Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                                                 startActivity(i);
                                                                 overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                                                             } else if (str.equals("failure")) {
                                                                 Toast.makeText(LoginActivity.this, "Your Email or Password is incorrect, Please try again !!", Toast.LENGTH_SHORT).show();
                                                                 dialog.dismiss();
                                                                 scheduledExecutorService.shutdown();
                                                             } else if (str.equals("----")) {
                                                                 //                        android.app.Dialog dlg=new android.app.Dialog(getApplicationContext(),R.layout.error);
                                                                 Toast.makeText(LoginActivity.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                                                                 dialog.dismiss();
                                                                 scheduledExecutorService.shutdown();

                                                             } else {
                                                                 Toast.makeText(getApplicationContext(), "Please enter correct password or User ID", Toast.LENGTH_SHORT).show();
                                                                 dialog.dismiss();
                                                                 scheduledExecutorService.shutdown();
                                                             }
                                                             Looper.loop();
                                                         }
                                                     } catch (Exception e) {

                                                     }
                                                 }
                                             }, 1, 3, TimeUnit.SECONDS);
                                             Analytics_Util.logAnalytic(mFirebaseAnalytics, "Login", "button");

                                         } else {
                                             Toast.makeText(LoginActivity.this, "Please enter email address or userId", Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 }
        );
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    boolean EmailChecker(String s) {
        String EMAIL_REGEX = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

    public void registerMe() {


        // registering device
        SharedPreferences token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String registration_id = token.getString("device_token", null);
        if (registration_id != null) {
            Notifications_Util.RegisterDevice(customer_id, registration_id);
        } else {
            Notifications_Util.RegisterDevice(customer_id, FirebaseInstanceId.getInstance().getToken());
        }
    }

    private class BackGround extends AsyncTask<String, String, String> {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            if (strings[0].contains("email")) {
                AndroidNetworking.post("http://208.91.199.50:5000/checkLogin/{check}")
                        .addPathParameter("check", "email")
                        .addBodyParameter("email", strings[1])
                        .addBodyParameter("password", strings[2])
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {


                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });


                                    str = response.getString(0);
                                    if (str.contains("success")) {
                                        customer_id = response.getString(1);
                                        customer_gender = response.getString(2);

                                        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = saved_values.edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("customer_id", customer_id);
                                        editor.putString("gender", customer_gender);
                                        editor.putString("firstname", response.getString(3));
                                        editor.putString("surname", response.getString(4));
                                        JSONArray communityArray = response.getJSONArray(5);

                                        for (int i = 0; i < 5; i++) {
                                            editor.putString(communityArray.getJSONArray(i).getString(0), communityArray.getJSONArray(i).getString(1));
                                        }
                                        editor.apply();

                                        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
                                        editor = sharedpref.edit();

                                        editor.putString("customer_id", customer_id);
                                        editor.putString("gender", customer_gender);
                                        editor.putString("firstname", response.getString(3));
                                        editor.putString("surname", response.getString(4));

                                        communityArray = response.getJSONArray(5);

                                        for (int i = 0; i < 5; i++) {
                                            editor.putString(communityArray.getJSONArray(i).getString(0), communityArray.getJSONArray(i).getString(1));
                                        }
                                        editor.apply();

                                        registerMe();
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                });

                                Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                AndroidNetworking.post("http://208.91.199.50:5000/checkLogin/{check}")
                        .addPathParameter("check", "id")
                        .addBodyParameter("email", strings[1])
                        .addBodyParameter("password", strings[2])
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                    str = response.getString(0);
                                    checker = true;
                                    if (str.contains("success")) {
                                        customer_id = response.getString(1);
                                        customer_gender = response.getString(2);

                                        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = saved_values.edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.putString("customer_id", customer_id);
                                        editor.putString("gender", customer_gender);
                                        editor.putString("firstname", response.getString(3));
                                        editor.putString("surname", response.getString(4));
                                        JSONArray communityArray = response.getJSONArray(5);

                                        for (int i = 0; i < 5; i++) {
                                            editor.putString(communityArray.getJSONArray(i).getString(0), communityArray.getJSONArray(i).getString(1));
                                        }
                                        editor.apply();

                                        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
                                        editor = sharedpref.edit();
                                        editor.putString("customer_id", customer_id);
                                        editor.putString("gender", customer_gender);
                                        editor.putString("firstname", response.getString(3));
                                        editor.putString("surname", response.getString(4));
                                        communityArray = response.getJSONArray(5);

                                        for (int i = 0; i < 5; i++) {
                                            editor.putString(communityArray.getJSONArray(i).getString(0), communityArray.getJSONArray(i).getString(1));
                                        }
                                        editor.apply();

                                        registerMe();
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please enter correct email address or password", Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError error) {

                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                });

                                Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();


                            }
                        });
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    }
}

