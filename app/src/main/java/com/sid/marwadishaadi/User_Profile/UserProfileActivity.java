package com.sid.marwadishaadi.User_Profile;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.Chat.DefaultMessagesActivity;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.DeviceRegistration;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.Notifications.NotificationsModel;
import com.sid.marwadishaadi.Notifications_Util;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.BlurTransformation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class UserProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final int REQUEST_PERMISSION_SETTING = 105;
    private static final String TAG = "UserProfileActivity";
    protected ImageView pref;
    private CollapsingToolbarLayout toolbarLayout;
    private NotificationCompat.Builder notification;
    private NotificationManager notificationManager;
    private ProfilePageAdapter profilePageAdapter;
    private ViewPager userinfo;
    private ImageView imageView;
    private FloatingActionButton fav;
    private FloatingActionButton sendmsg;
    private FloatingActionButton sendinterest;
    private FloatingActionButton shareprofile;
    private FloatingActionButton sharesave;
    private FloatingActionButton editphotos;
    private FloatingActionMenu fab;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CoordinatorLayout coordinatorLayout;
    private String customer_id;
    private String clickedID = customer_id;
    private String customer_name;
    private ImageView pdfImage;
    private TextView pdfImageName, pdfImageId, pdfName, pdfAge, pdfMaritalStatus, pdfDob, pdfGender, pdfLocation, pdfContact, pdfCommunity, pdfSubcaste, pdfHeight, pdfWeight, pdfComplexion, pdfBodyType, pdfPhysicalStatus, pdfEduLevel, pdfHighestDegree, pdfInstituteName, pdfOccupation, pdfDesignation, pdfCompany, pdfIncome, pdfAbout, pdfHobby, pdfDiet, pdfDrink, pdfSmoke, pdfBirthTime, pdfBirthPlace, pdfGotra, pdfManglik, pdfMatchHoroscope, pdfFatherName, pdfFatherOccupation, pdfFatherOccupationDetail, pdfFamilyStatus, pdfFamilyType, pdfFamilyValues, pdfNativePlaces, pdfGrandfatherName, pdfMama;
    private TextView pdfDate;
    private Boolean isSelf = true;
    private Boolean isFavAdded;
    private Boolean isInterestSent;
    private Boolean isMsgSent;
    private View view;
    private String name;
    private String userid_from_deeplink;

    private boolean isPaidMember;
    private ArrayList<String> images;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private ImageView imageViewInformation;
    private String createdOn, lastActiveOn;
    private ProgressDialog progressDialog;
    private boolean hasDP;
    private TextView showTextOnPhoto;

    public static void shareApp(Context context) {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out MarwadiShaadi App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean called = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressDialog = new ProgressDialog(UserProfileActivity.this);

        progressDialog.setMessage("Loading profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Getting current ConnectionQuality
        ConnectionQuality connectionQuality = AndroidNetworking.getCurrentConnectionQuality();
        if (connectionQuality == ConnectionQuality.EXCELLENT) {
            // do something
        } else if (connectionQuality == ConnectionQuality.POOR) {
            // do something
        } else if (connectionQuality == ConnectionQuality.UNKNOWN) {
            // do something
        }
        // Getting current bandwidth
        int currentBandwidth = AndroidNetworking.getCurrentBandwidth(); // Note : if (currentBandwidth == 0) : means UNKNOWN

        imageView = findViewById(R.id.imageView);
        imageViewInformation = findViewById(R.id.imageViewInformation);

        showTextOnPhoto = findViewById(R.id.showTextOnPhoto);

        toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = getWindow().getDecorView().getRootView();

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_name = sharedpref.getString("firstname", null);
        clickedID = customer_id;

        SharedPreferences sharedPref = getSharedPreferences("userDp", MODE_PRIVATE);
        hasDP = sharedPref.getBoolean("hasDP", false);


        int communityLength = sharedpref.getInt("cal", 0);
        try {

            String[] array = getResources().getStringArray(R.array.communities);


            if (customer_id != null && array.length > 0) {

                for (int i = 0; i < communityLength; i++) {

                    if (sharedpref.getString(array[i], "No").contains("Yes")) {
                        isPaidMember = true;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }


        Intent data = getIntent();

        if (data.getStringExtra("customerNo") != null) {
            called = false;
            clickedID = data.getStringExtra("customerNo");
            new ProfilePicture().execute(clickedID);
            //Toast.makeText(UserProfileActivity.this, clickedID, Toast.LENGTH_SHORT).show();
        }

        new FetchInformation().execute();

        String deeplink = data.getStringExtra("deeplink");
        if (deeplink != null) {
            userid_from_deeplink = deeplink.substring(deeplink.lastIndexOf("/") + 1);
            clickedID = userid_from_deeplink;
            new ProfilePicture().execute(clickedID);
            Toast.makeText(UserProfileActivity.this, userid_from_deeplink, Toast.LENGTH_SHORT).show();
        }

        coordinatorLayout = findViewById(R.id.entire_ui);


        fab = findViewById(R.id.menu_yellow);
        fav = findViewById(R.id.fab_favourite);
        sendmsg = findViewById(R.id.fab_send_message);
        sendinterest = findViewById(R.id.fab_send_interest);
        shareprofile = findViewById(R.id.fab_share_profile);
        sharesave = findViewById(R.id.fab_save);

        editphotos = findViewById(R.id.fab_edit_photos);


        String from = data.getStringExtra("from");

        if ("suggestion".equals(from) | "recent".equals(from) | "reverseMatching".equals(from) | "favourites".equals(from) | "interestReceived".equals(from) | "interestSent".equals(from)) {
            editphotos.setVisibility(View.GONE);
            isSelf = false;
            new CheckStatus().execute();
        }

        if (isSelf) {
            fav.setVisibility(View.GONE);
            sendmsg.setVisibility(View.GONE);
            sendinterest.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customer_id == clickedID || isPaidMember || hasDP) {
                    Intent i = new Intent(UserProfileActivity.this, FullscreenImageActivity.class);
                    i.putExtra("customerNo", clickedID);
                    i.putExtra("from", "userprofile");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {

                    Toast.makeText(getApplicationContext(), "Upload your photo to view", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(UserProfileActivity.this, UploadPhotoActivity.class);
                    startActivity(i);
                }


            }
        });

        editphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Edit photos", "button");

                Intent i = new Intent(UserProfileActivity.this, UploadPhotoActivity.class);
                i.putExtra("from", "userprofile");
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


            }
        });


        fab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    coordinatorLayout.setBackgroundColor(Color.parseColor("#1A2d2d2d"));
                } else {
                    coordinatorLayout.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });


        if (fav.getVisibility() != View.GONE) {

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics, "Favourites", "button");
                    new AddFavouriteFromSuggestion().execute(customer_id, clickedID, "added");
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Added to Favourites", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    fav.setVisibility(View.GONE);
                }
            });
        }


        if (sendmsg.getVisibility() != View.GONE) {

            sendmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics, "Sent Message", "button");
                    int counter = 0;
                    String[] array = getApplicationContext().getResources().getStringArray(R.array.communities);
                    SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    for (int i = 0; i < 5; i++) {
                        if (communityChecker.getString(array[i], "No").contains("Yes")) {
                            counter++;
                        }
                    }
                    if (counter > 0) {
                        Intent intent = new Intent(UserProfileActivity.this, DefaultMessagesActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("customerName", customer_name);
                        extras.putString("customerId", clickedID);
                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserProfileActivity.this, " This feature is only for premium members", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserProfileActivity.this, UpgradeMembershipActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }


        if (sendinterest.getVisibility() != View.GONE) {

            sendinterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // analytics
                    Analytics_Util.logAnalytic(mFirebaseAnalytics, "Sent Interest", "button");
                    new AddInterestFromSuggestion().execute(customer_id, clickedID, "added");


                    // ========================= NOTIFICATION =======================================


                    // adding it to her notifications list
                    String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                    mDatabase = FirebaseDatabase.getInstance().getReference(clickedID).child("Notifications");
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                    Date currentDate = calendar.getTime();
                    String hash = String.valueOf(currentDate.hashCode());
                    final NotificationsModel notification = new NotificationsModel(hash, customer_name, date, 3, false, true, false, false, false, false, false, false, false, false);

                    mDatabase.child(hash).setValue(notification);

                    // sending push notification to her
                    // get all devices

                    mDatabases = FirebaseDatabase.getInstance().getReference(clickedID).child("Devices");
                    mDatabases.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            setData(dataSnapshot);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    //======================================================================


                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Interest Sent", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            });

        }


        shareprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Share Profile", "button");

                String caste = "";

                if (clickedID.charAt(0) == 'M') {
                    caste = "Maheswari";
                } else if (clickedID.charAt(0) == 'K') {
                    caste = "Khandelwal";
                } else if (clickedID.charAt(0) == 'A') {
                    caste = "Agarwal";
                } else if (clickedID.charAt(0) == 'J') {
                    caste = "Jain";
                } else if (clickedID.charAt(0) == 'O') {
                    caste = "Other";
                }


                String userid = clickedID;

                SharedPreferences sharedpref = getSharedPreferences("customername", MODE_PRIVATE);
                final String username = sharedpref.getString("name", null);

                String packageName = getPackageName();
                String weblink = "http://www.marwadishaadi.com/" + caste + "/user/candidate/" + userid;
                String domain = "https://bf5xe.app.goo.gl/";
                String link = domain + "?link=" + weblink + "&apn=" + packageName + "&afl=" + weblink + "&dfl=" + weblink;

                OnCompleteListener onCompleteListener = new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {

                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Intent sendIntent = new Intent();
                            String msg = "Hey, Check this profile of " + name + ":" + shortLink.toString();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);

                        }
                    }
                };

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse(link))
                        .buildShortDynamicLink();
                shortLinkTask.addOnCompleteListener(onCompleteListener);


            }
        });


        sharesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Save as PDF", "button");

                if (isPaidMember) {


                    // add permission here
                    int read_permissionCheck = ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    int write_permissionCheck = ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (read_permissionCheck == PackageManager.PERMISSION_DENIED || write_permissionCheck == PackageManager.PERMISSION_DENIED) {

                        // first time asks for both permission
                        if (!getReadStoragePermissionStatus() && !getWriteStoragePermissionStatus()) {

                            Dexter.withActivity(UserProfileActivity.this)
                                    .withPermissions(
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ).withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {

                                    // if both are accepted
                                    if (report.areAllPermissionsGranted()) {
                                        SaveAsPdf();

                                        // if both are rejected
                                    } else if (report.getDeniedPermissionResponses().size() == 2) {
                                        showStorageSettings();

                                        // one of them is accepted
                                    } else {
                                        List<PermissionGrantedResponse> grantedPermissions = report.getGrantedPermissionResponses();
                                        for (PermissionGrantedResponse grantedPermission : grantedPermissions) {
                                            if (grantedPermission.getPermissionName() == Manifest.permission.READ_EXTERNAL_STORAGE) {
                                                setReadStoragePermissionStatus();
                                                showStorageSettings();
                                            } else {
                                                setWriteStoragePermissionStatus();
                                                showStorageSettings();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                }
                            }).check();

                        }
                        // other times
                        // write allowed, read rejected
                        else if (!getReadStoragePermissionStatus() && getWriteStoragePermissionStatus()) {

                            Dexter.withActivity(UserProfileActivity.this)
                                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {
                                            SaveAsPdf();
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            showStorageSettings();
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                        }
                                    }).check();

                        }
                        // read allowed, write rejected
                        else if (!getWriteStoragePermissionStatus() && getReadStoragePermissionStatus()) {

                            Dexter.withActivity(UserProfileActivity.this)
                                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {
                                            SaveAsPdf();
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            showStorageSettings();
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                        }
                                    }).check();

                        } else {
                            showStorageSettings();
                        }
                    } else {
                        SaveAsPdf();
                    }


                } else {
                    Toast.makeText(UserProfileActivity.this, " This feature is only for premium members", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserProfileActivity.this, UpgradeMembershipActivity.class);
                    startActivity(intent);
                }

            }
        });


        if (called) {
            new ProfilePicture().execute(clickedID);
        }

        profilePageAdapter = new ProfilePageAdapter(getSupportFragmentManager());
        userinfo = findViewById(R.id.profile_container);
        userinfo.setAdapter(profilePageAdapter);
        userinfo.setOffscreenPageLimit(4);
        userinfo.setOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(userinfo);

    }

    private void SaveAsPdf() {
        new FetchPdfDetails().execute();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.exit, 0);
        return true;
    }

    private void showStorageSettings() {
        Snackbar snackbar = Snackbar
                .make(view.getRootView(), "Read & Write permission needed.Go to Settings to change", Snackbar.LENGTH_LONG)
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

    private Boolean getCameraPermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isCameraPermissionDenied", false);
    }

    private void setCameraPermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isCameraPermissionDenied", true);
        edit.apply();
    }

    private Boolean getReadStoragePermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isReadPermissionDenied", false);
    }

    private void setReadStoragePermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isReadPermissionDenied", true);
        edit.apply();
    }

    private Boolean getWriteStoragePermissionStatus() {
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getBoolean("isWritePermissionDenied", false);
    }

    private void setWriteStoragePermissionStatus() {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sharedpref.edit();
        edit.putBoolean("isWritePermissionDenied", true);
        edit.apply();
    }

    public void setData(DataSnapshot dataSnapshot) {

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), customer_name + " sent you an Interest", "Marwadi Shaadi: New Interest", "Interest Request");
    }

    public static class ProfilePageAdapter extends FragmentPagerAdapter {

        public ProfilePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ProfilePersonalDetailsFragment profile_personal_detailsFragment = new ProfilePersonalDetailsFragment();
                    return profile_personal_detailsFragment;
                case 1:
                    ProfileAdditionalDetailsFragment profile_additional_detailsFragment = new ProfileAdditionalDetailsFragment();
                    return profile_additional_detailsFragment;
                case 2:
                    ProfileFamilyDetailsFragment profile_family_detailsFragment = new ProfileFamilyDetailsFragment();
                    return profile_family_detailsFragment;
                case 3:
                    PartnerPreferencesFragment partnerPreferencesFragment = new PartnerPreferencesFragment();
                    return partnerPreferencesFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Personal Details";
                case 1:
                    return "Additional Details";
                case 2:
                    return "Family Details";
                case 3:
                    return "Partner Preferences";
                default:
                    return null;
            }
        }
    }

    public class ProfilePicture extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            final String cus = params[0];


            AndroidNetworking.post(Constants.AWS_SERVER + "/fetchProfilePicture")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {

                                name = response.getString(0) + " " + response.getString(1) + " (" + cus + ")";
                                images = new ArrayList<>();

                                if (response.length() > 2) {

                                    for (int i = 2; i < response.length(); i++) {


                                        images.add("http://www.marwadishaadi.com/uploads/cust_" + cus + "/thumb/" + response.getString(i).replace("[", "").replace("]", "").replace("\"", ""));
                                    }
                                }


                                toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
                                toolbarLayout.setTitle(name);

                                if (images.size() > 0) {

                                    if (customer_id == clickedID || isPaidMember || hasDP) {
                                        Glide.with(getApplicationContext()).load(images.get(0)).into(imageView);
                                        showTextOnPhoto.setVisibility(View.INVISIBLE);

                                    } else {
                                        Glide.with(getApplicationContext())
                                                .load(images.get(0))
                                                .dontAnimate()
                                                .placeholder(R.drawable.default_drawer)
                                                .error(R.drawable.default_drawer)
                                                .bitmapTransform(new BlurTransformation(getApplicationContext()))
                                                .into(imageView);
                                        showTextOnPhoto.setVisibility(View.VISIBLE);

                                    }


//                                    Picasso.with(getApplicationContext()).load(images.get(0)).fit().into(imageView);

                                } else {
                                    Glide.with(getApplicationContext()).load(R.drawable.default_drawer).into(imageView);

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            progressDialog.dismiss();
                            Toast.makeText(UserProfileActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();

                        }


                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class FetchInformation extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post(Constants.AWS_SERVER + "/fetchInformation")
                    .addBodyParameter("customerNo", clickedID)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray array = response.getJSONArray(0);
                                createdOn = array.getString(0);
                                lastActiveOn = array.getString(1);
                                if (createdOn.contains("null"))
                                    createdOn = "This is a ghost";
                                else
                                    createdOn = createdOn.substring(0, 17);
                                if (lastActiveOn.contains("null"))
                                    lastActiveOn = "Not logged in yet";
                                else
                                    lastActiveOn = lastActiveOn.substring(0, 17);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // ============================ INFORMATION ========================================

            imageViewInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewTooltip.on(imageViewInformation)
                            .align(ViewTooltip.ALIGN.START)
                            .clickToHide(true)
                            .autoHide(true, 2000)
                            .position(ViewTooltip.Position.LEFT)
                            .text("Profile Created On: " + createdOn + "\r\nLast Active On: " + lastActiveOn)
                            .textColor(Color.WHITE)
                            .color(Color.parseColor("#FB6542"))
                            .show();
                }
            });

        }
    }

    class FetchPdfDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            notification = new NotificationCompat.Builder(UserProfileActivity.this)
                    .setContentTitle("PDF Download")
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setAutoCancel(true)
                    .setProgress(0, 0, true)
                    .setContentText("Download in progress");


            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification.build());

        }

        @Override
        protected Void doInBackground(String... params) {


            AndroidNetworking.post(Constants.AWS_SERVER + "/fetchPDFDetails")
                    .addBodyParameter("customerNo", clickedID)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(final JSONArray response) {


                            AbstractViewRenderer page1 = new AbstractViewRenderer(UserProfileActivity.this, R.layout.activity_pdf) {
                                @Override
                                protected void initView(View view) {

                                    pdfImage = view.findViewById(R.id.pdfImage);
                                    pdfImageName = view.findViewById(R.id.pdfImageName);
                                    pdfImageId = view.findViewById(R.id.pdfImageId);
                                    pdfName = view.findViewById(R.id.pdfName);
                                    pdfAge = view.findViewById(R.id.pdfAge);
                                    pdfMaritalStatus = view.findViewById(R.id.pdfMaritalStatus);
                                    pdfDob = view.findViewById(R.id.pdfDob);
                                    pdfGender = view.findViewById(R.id.pdfGender);
                                    pdfLocation = view.findViewById(R.id.pdfLocation);
                                    pdfContact = view.findViewById(R.id.pdfContact);
                                    pdfCommunity = view.findViewById(R.id.pdfCommunity);
                                    pdfSubcaste = view.findViewById(R.id.pdfSubcaste);
                                    pdfAbout = view.findViewById(R.id.pdfAbout);
                                    pdfHobby = view.findViewById(R.id.pdfHobby);
                                    pdfDiet = view.findViewById(R.id.pdfDiet);
                                    pdfDrink = view.findViewById(R.id.pdfDrink);
                                    pdfSmoke = view.findViewById(R.id.pdfSmoke);
                                    pdfDate = view.findViewById(R.id.date);


                                    try {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Glide.with(UserProfileActivity.this)
                                                            .load("http://www.marwadishaadi.com/uploads/cust_" + clickedID + "/thumb/" + response.getString(0))
                                                            .into(pdfImage);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        String pdfname = "MarwadiShaadi Profile - " + response.getString(1) + " " + response.getString(2);
                                        pdfImageName.setText(pdfname);
                                        pdfImageId.setText(response.getString(3));
                                        pdfName.setText(pdfname);
                                        int age = Calendar.getInstance().get(Calendar.YEAR) - response.getInt(4);
                                        pdfAge.setText(String.valueOf(age));
                                        pdfMaritalStatus.setText(response.getString(5));
                                        pdfDob.setText(response.getString(4));
                                        pdfGender.setText(response.getString(6));
                                        pdfLocation.setText(response.getString(7));
                                        pdfContact.setText(response.getString(8));


                                        String[] c = response.getString(3).split("");
                                        String cast = "";
                                        if (c[1].equals("A")) {
                                            cast = "Agarwal";
                                        } else if (c[1].equals("K")) {
                                            cast = "Khandelwal";
                                        } else if (c[1].equals("J")) {
                                            cast = "Jain";
                                        } else if (c[1].equals("M")) {
                                            cast = "Maheshwari";
                                        } else if (c[1].equals("O")) {
                                            cast = "Other";
                                        }

                                        pdfCommunity.setText(cast);
                                        pdfSubcaste.setText(response.getString(9));
                                        pdfAbout.setText(response.getString(23));
                                        pdfHobby.setText(response.getString(24));
                                        pdfDiet.setText(response.getString(25));
                                        pdfDrink.setText(response.getString(41));
                                        pdfSmoke.setText(response.getString(26));
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        pdfDate.setText(currentDate);

                                    } catch (JSONException E) {

                                    }

                                }
                            };


                            AbstractViewRenderer page2 = new AbstractViewRenderer(UserProfileActivity.this, R.layout.pdf2) {
                                @Override
                                protected void initView(View view) {

                                    pdfHeight = view.findViewById(R.id.pdfHeight);
                                    pdfWeight = view.findViewById(R.id.pdfWeight);
                                    pdfComplexion = view.findViewById(R.id.pdfComplexion);
                                    pdfBodyType = view.findViewById(R.id.pdfBodyType);
                                    pdfPhysicalStatus = view.findViewById(R.id.pdfPhysicalStatus);
                                    pdfEduLevel = view.findViewById(R.id.pdfEduLevel);
                                    pdfHighestDegree = view.findViewById(R.id.pdfHighestDegree);
                                    pdfInstituteName = view.findViewById(R.id.pdfInstituteName);
                                    pdfOccupation = view.findViewById(R.id.pdfOccupation);
                                    pdfDesignation = view.findViewById(R.id.pdfDesignation);
                                    pdfCompany = view.findViewById(R.id.pdfCompany);
                                    pdfIncome = view.findViewById(R.id.pdfIncome);
                                    pdfBirthTime = view.findViewById(R.id.pdfBirthTime);
                                    pdfBirthPlace = view.findViewById(R.id.pdfBirthplace);
                                    pdfGotra = view.findViewById(R.id.pdfGotra);
                                    pdfManglik = view.findViewById(R.id.pdfManglik);
                                    pdfMatchHoroscope = view.findViewById(R.id.pdfMatchHoroscope);
                                    pdfFatherName = view.findViewById(R.id.pdfFatherName);
                                    pdfFatherOccupation = view.findViewById(R.id.pdfFatherOccupation);
                                    pdfFatherOccupationDetail = view.findViewById(R.id.pdfFatherOccupationDetail);
                                    pdfFamilyStatus = view.findViewById(R.id.pdfFamilyStatus);
                                    pdfFamilyType = view.findViewById(R.id.pdfFamilyType);
                                    pdfFamilyValues = view.findViewById(R.id.pdfFamilyValues);
                                    pdfNativePlaces = view.findViewById(R.id.pdfNativePlace);
                                    pdfGrandfatherName = view.findViewById(R.id.pdfGrandfatherName);
                                    pdfMama = view.findViewById(R.id.pdfMama);


                                    try {
                                        pdfHeight.setText(response.getString(10));
                                        pdfWeight.setText(response.getString(11));
                                        pdfComplexion.setText(response.getString(12));
                                        pdfBodyType.setText(response.getString(13));
                                        pdfPhysicalStatus.setText(response.getString(14));
                                        pdfEduLevel.setText(response.getString(15));
                                        pdfHighestDegree.setText(response.getString(16));
                                        if (response.getString(18) != null && !response.getString(18).isEmpty()) {
                                            String inl = response.getString(17) + ", " + response.getString(18);
                                            pdfInstituteName.setText(inl);
                                        } else {
                                            pdfInstituteName.setText(response.getString(17));
                                        }
                                        pdfOccupation.setText(response.getString(19));
                                        pdfDesignation.setText(response.getString(20));
                                        pdfCompany.setText(response.getString(21));
                                        pdfIncome.setText(response.getString(22));

                                        pdfBirthTime.setText(response.getString(27));
                                        pdfBirthPlace.setText(response.getString(28));
                                        pdfGotra.setText(response.getString(29));
                                        pdfManglik.setText(response.getString(30));
                                        pdfMatchHoroscope.setText(response.getString(31));
                                        pdfFatherName.setText(response.getString(32));
                                        pdfFatherOccupation.setText(response.getString(33));
                                        pdfFatherOccupationDetail.setText(response.getString(34));
                                        pdfFamilyStatus.setText(response.getString(35));
                                        pdfFamilyType.setText(response.getString(36));
                                        pdfFamilyValues.setText(response.getString(37));
                                        pdfNativePlaces.setText(response.getString(38));
                                        pdfGrandfatherName.setText(response.getString(39));
                                        pdfMama.setText(response.getString(40));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            };

                            page1.setReuseBitmap(true);
                            page2.setReuseBitmap(true);

                            PdfDocument doc = new PdfDocument(UserProfileActivity.this);
                            doc.addPage(page1);
                            doc.addPage(page2);
                            doc.setRenderWidth(1072);
                            doc.setRenderHeight(2000);
                            doc.setInflateOnMainThread(false);
                            doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
                            doc.setSaveDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                            try {
                                doc.setFileName(response.getString(1) + " " + response.getString(2));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            doc.setInflateOnMainThread(false);
                            doc.setListener(new PdfDocument.Callback() {
                                @Override
                                public void onComplete(File file) {

                                    Toast.makeText(UserProfileActivity.this, "Profile saved in Downloads Folder", Toast.LENGTH_SHORT).show();

                                    Uri targetUri = Uri.fromFile(file);

                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setDataAndType(targetUri, "application/pdf");
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                                    notification.setContentText("File saved");
                                    notification.setContentIntent(pendingIntent);
                                    notification.setProgress(0, 0, false);
                                    notificationManager.notify(1, notification.build());

                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });

                            Toast.makeText(UserProfileActivity.this, "Saving profile", Toast.LENGTH_SHORT).show();
                            doc.createPdf(UserProfileActivity.this);

                        }

                        @Override
                        public void onError(ANError anError) {


                        }
                    });

            return null;
        }
    }

    public class AddInterestFromSuggestion extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {


            String customerId = params[0];
            String interestId = params[1];
            String status = params[2];


            AndroidNetworking.post(Constants.AWS_SERVER + "/addInterestFromSuggestion")
                    .addBodyParameter("customerNo", customerId)
                    .addBodyParameter("interestId", interestId)
                    .addBodyParameter("status", status)
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

    public class AddFavouriteFromSuggestion extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String customerId = params[0];
            String favId = params[1];
            String favouriteState = params[2];

            AndroidNetworking.post(Constants.AWS_SERVER + "/addFavFromSuggestion")
                    .addBodyParameter("customerNo", customerId)
                    .addBodyParameter("favId", favId)
                    .addBodyParameter("status", favouriteState)
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

    public class CheckStatus extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            AndroidNetworking.post(Constants.AWS_SERVER + "/getStatus")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("clickedId", clickedID)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                customer_name = response.getString(0) + " " + response.getString(1);
                                isFavAdded = response.getString(2).equals("1");
                                isInterestSent = response.getString(3).equals("1");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (isFavAdded) {
                                if (fav.getVisibility() != View.GONE) {
                                    fav.setVisibility(View.GONE);
                                }
                            }

                            if (isInterestSent) {
                                if (sendinterest.getVisibility() != View.GONE) {
                                    sendinterest.setVisibility(View.GONE);
                                }
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            return null;
        }
    }
}

