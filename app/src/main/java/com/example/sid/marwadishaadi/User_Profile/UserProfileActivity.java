package com.example.sid.marwadishaadi.User_Profile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Chat.DefaultMessagesActivity;
import com.example.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class UserProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ImageListener {

    public static final int REQUEST_PERMISSION_SETTING = 105;
    private static final String TAG = "UserProfileActivity";
    protected ImageView pref;
    CollapsingToolbarLayout toolbarLayout;
    NotificationCompat.Builder notification;
    NotificationManager notificationManager;
    private ProfilePageAdapter profilePageAdapter;
    private ViewPager userinfo;
    private CarouselView carouselView;
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
    private String userid_from_deeplink;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        view = getWindow().getDecorView().getRootView();

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        clickedID = customer_id;

        Intent data = getIntent();
        String deeplink = data.getStringExtra("deeplink");
        if (data.getStringExtra("customerNo") != null) {
            called = false;
            clickedID = data.getStringExtra("customerNo");
            new ProfilePicture().execute(clickedID);
            //Toast.makeText(UserProfileActivity.this, clickedID, Toast.LENGTH_SHORT).show();
        }

        if (deeplink != null) {
            userid_from_deeplink = deeplink.substring(deeplink.lastIndexOf("/") + 1);
            //  Toast.makeText(UserProfileActivity.this, userid_from_deeplink, Toast.LENGTH_SHORT).show();
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.entire_ui);


        fab = (FloatingActionMenu) findViewById(R.id.menu_yellow);
        fav = (FloatingActionButton) findViewById(R.id.fab_favourite);
        sendmsg = (FloatingActionButton) findViewById(R.id.fab_send_message);
        sendinterest = (FloatingActionButton) findViewById(R.id.fab_send_interest);
        shareprofile = (FloatingActionButton) findViewById(R.id.fab_share_profile);
        sharesave = (FloatingActionButton) findViewById(R.id.fab_save);

        editphotos = (FloatingActionButton) findViewById(R.id.fab_edit_photos);


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

        editphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Edit photos", "button");

                Intent i = new Intent(UserProfileActivity.this, UploadPhotoActivity.class);
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
                        if (communityChecker.getString(array[i], null).contains("Yes")) {
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
                            String msg = "Hey, Check this profile of " + username + ":" + shortLink.toString();
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


                notification = new NotificationCompat.Builder(UserProfileActivity.this)
                        .setContentTitle("Pdf Download")
                        .setSmallIcon(R.drawable.ic_action_drawer_notification)
                        .setAutoCancel(true)
                        .setProgress(0, 0, true)
                        .setContentText("Download in progress");


                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification.build());

                new FetchPdfDetails().execute();

            }
        });


        carouselView = (CarouselView) findViewById(R.id.carouselView);

        if (called) {
            new ProfilePicture().execute(clickedID);
        }

        profilePageAdapter = new ProfilePageAdapter(getSupportFragmentManager());
        userinfo = (ViewPager) findViewById(R.id.profile_container);
        userinfo.setAdapter(profilePageAdapter);
        userinfo.setOffscreenPageLimit(4);
        userinfo.setOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(userinfo);


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
    public void setImageForPosition(int position, ImageView imageView) {

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
        protected Void doInBackground(String... params) {

            final String cus = params[0];


            AndroidNetworking.post("http://208.91.199.50:5000/fetchProfilePicture")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {

                                String name = response.getString(0) + " " + response.getString(1);
                                final ArrayList<String> images = new ArrayList<>();

                                if (response.length() > 2) {

                                    for (int i = 2; i < response.length(); i++) {

                                        images.add("http://www.marwadishaadi.com/uploads/cust_" + cus + "/thumb/" + response.getString(i).replace("[ ", "").replace("]", "").replace("\"", ""));
                                    }
                                }


                                toolbarLayout.setTitle(name);


                                carouselView.setImageListener(new ImageListener() {


                                    @Override
                                    public void setImageForPosition(int position, ImageView imageView) {

                                        Picasso.with(UserProfileActivity.this)
                                                .load(images.get(position).replace("[", ""))
                                                .fit()
                                                .into(imageView);
                                    }
                                });
                                carouselView.setPageCount(images.size());


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
    }


    class FetchPdfDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            AndroidNetworking.post("http://208.91.199.50:5000/fetchPDFDetails")
                    .addBodyParameter("customerNo", clickedID)
                    .setPriority(Priority.HIGH)
                    .setTag(TAG)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(final JSONArray response) {


                            AbstractViewRenderer page1 = new AbstractViewRenderer(UserProfileActivity.this, R.layout.activity_pdf) {
                                @Override
                                protected void initView(View view) {

                                    pdfImage = (ImageView) view.findViewById(R.id.pdfImage);
                                    pdfImageName = (TextView) view.findViewById(R.id.pdfImageName);
                                    pdfImageId = (TextView) view.findViewById(R.id.pdfImageId);
                                    pdfName = (TextView) view.findViewById(R.id.pdfName);
                                    pdfAge = (TextView) view.findViewById(R.id.pdfAge);
                                    pdfMaritalStatus = (TextView) view.findViewById(R.id.pdfMaritalStatus);
                                    pdfDob = (TextView) view.findViewById(R.id.pdfDob);
                                    pdfGender = (TextView) view.findViewById(R.id.pdfGender);
                                    pdfLocation = (TextView) view.findViewById(R.id.pdfLocation);
                                    pdfContact = (TextView) view.findViewById(R.id.pdfContact);
                                    pdfCommunity = (TextView) view.findViewById(R.id.pdfCommunity);
                                    pdfSubcaste = (TextView) view.findViewById(R.id.pdfSubcaste);
                                    pdfHeight = (TextView) view.findViewById(R.id.pdfHeight);
                                    pdfWeight = (TextView) view.findViewById(R.id.pdfWeight);
                                    pdfComplexion = (TextView) view.findViewById(R.id.pdfComplexion);
                                    pdfBodyType = (TextView) view.findViewById(R.id.pdfBodyType);
                                    pdfPhysicalStatus = (TextView) view.findViewById(R.id.pdfPhysicalStatus);
                                    pdfEduLevel = (TextView) view.findViewById(R.id.pdfEduLevel);
                                    pdfHighestDegree = (TextView) view.findViewById(R.id.pdfHighestDegree);
                                    pdfInstituteName = (TextView) view.findViewById(R.id.pdfInstituteName);
                                    pdfOccupation = (TextView) view.findViewById(R.id.pdfOccupation);
                                    pdfDesignation = (TextView) view.findViewById(R.id.pdfDesignation);
                                    pdfCompany = (TextView) view.findViewById(R.id.pdfCompany);
                                    pdfIncome = (TextView) view.findViewById(R.id.pdfIncome);
                                    pdfAbout = (TextView) view.findViewById(R.id.pdfAbout);
                                    pdfHobby = (TextView) view.findViewById(R.id.pdfHobby);
                                    pdfDiet = (TextView) view.findViewById(R.id.pdfDiet);
                                    pdfDrink = (TextView) view.findViewById(R.id.pdfDrink);
                                    pdfSmoke = (TextView) view.findViewById(R.id.pdfSmoke);
                                    pdfBirthTime = (TextView) view.findViewById(R.id.pdfBirthTime);
                                    pdfBirthPlace = (TextView) view.findViewById(R.id.pdfBirthplace);
                                    pdfGotra = (TextView) view.findViewById(R.id.pdfGotra);
                                    pdfManglik = (TextView) view.findViewById(R.id.pdfManglik);
                                    pdfMatchHoroscope = (TextView) view.findViewById(R.id.pdfMatchHoroscope);
                                    pdfFatherName = (TextView) view.findViewById(R.id.pdfFatherName);
                                    pdfFatherOccupation = (TextView) view.findViewById(R.id.pdfFatherOccupation);
                                    pdfFatherOccupationDetail = (TextView) view.findViewById(R.id.pdfFatherOccupationDetail);
                                    pdfFamilyStatus = (TextView) view.findViewById(R.id.pdfFamilyStatus);
                                    pdfFamilyType = (TextView) view.findViewById(R.id.pdfFamilyType);
                                    pdfFamilyValues = (TextView) view.findViewById(R.id.pdfFamilyValues);
                                    pdfNativePlaces = (TextView) view.findViewById(R.id.pdfNativePlace);
                                    pdfGrandfatherName = (TextView) view.findViewById(R.id.pdfGrandfatherName);
                                    pdfMama = (TextView) view.findViewById(R.id.pdfMama);
                                    pdfDate = (TextView) view.findViewById(R.id.date);


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

                                        String pdfname = response.getString(1) + " " + response.getString(2);
                                        pdfImageName.setText(pdfname);
                                        pdfImageId.setText(response.getString(3));
                                        pdfName.setText(pdfname);
                                        int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(response.getString(4));
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
                                        pdfAbout.setText(response.getString(23));
                                        pdfHobby.setText(response.getString(24));
                                        pdfDiet.setText(response.getString(25));
                                        pdfDrink.setText(response.getString(41));
                                        pdfSmoke.setText(response.getString(26));
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
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        pdfDate.setText(currentDate);

                                    } catch (JSONException E) {

                                    }

                                }
                            };
                            page1.setReuseBitmap(true);


                            PdfDocument doc = new PdfDocument(UserProfileActivity.this);
                            doc.addPage(page1);
                            doc.setRenderWidth(1072);
                            doc.setRenderHeight(2000);
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
                                    Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");

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
                                    Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
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


            AndroidNetworking.post("http://208.91.199.50:5000/addInterestFromSuggestion")
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

            AndroidNetworking.post("http://208.91.199.50:5000/addFavFromSuggestion")
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


            AndroidNetworking.post("http://208.91.199.50:5000/getStatus")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("clickedId", clickedID)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                customer_name = response.getString(0);
                                isFavAdded = response.getString(1).equals("1");
                                isInterestSent = response.getString(2).equals("1");


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

