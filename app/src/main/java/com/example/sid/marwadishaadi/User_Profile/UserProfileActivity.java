package com.example.sid.marwadishaadi.User_Profile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class  UserProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        ImageListener{


    private ProfilePageAdapter profilePageAdapter;
    private ViewPager userinfo;
    private CarouselView carouselView;
    private int[] sampleImages = {R.drawable.profile, R.drawable.profile, R.drawable.profile};
    private FloatingActionButton fav;
    private FloatingActionButton sendmsg;
    private FloatingActionButton sendinterest;
    private FloatingActionButton shareprofile;
    private FloatingActionButton sharesave;
    private FloatingActionButton editphotos;
    private FloatingActionMenu fab;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("Siddhesh Rane");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.entire_ui);

        fab = (FloatingActionMenu) findViewById(R.id.menu_yellow);
        fav = (FloatingActionButton) findViewById(R.id.fab_favourite);
        sendmsg = (FloatingActionButton) findViewById(R.id.fab_send_message);
        sendinterest = (FloatingActionButton) findViewById(R.id.fab_send_interest);
        shareprofile = (FloatingActionButton) findViewById(R.id.fab_share_profile);
        sharesave = (FloatingActionButton) findViewById(R.id.fab_save);

        editphotos = (FloatingActionButton) findViewById(R.id.fab_edit_photos);

        editphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Edit photos","button");

                Intent i = new Intent(UserProfileActivity.this,UploadPhotoActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
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



        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Favourites","button");
            }
        });

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Sent Message","button");
            }
        });

        sendinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Sent Interest","button");
            }
        });

        shareprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Share Profile","button");

                // TODO: 23-Jun-17 Replace caste and id of user you want to share #kunal
                String caste = "Maheshwari";
                String userid = "M13725";
                String username = "Siddhesh";
                String packageName = getPackageName();
                String weblink="http://www.marwadishaadi.com/"+caste+"/user/candidate/"+userid;
                String domain = "https://bf5xe.app.goo.gl/";
                String link = domain + "?link="+weblink+"&apn="+packageName+"&ibi=com.example.ios&isi=12345";
                Intent sendIntent = new Intent();
                String msg = "Hey, Check this cool profile of "+username+":\n"+ link;
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        sharesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Save as PDF","button");


                final NotificationCompat.Builder notification = new NotificationCompat.Builder(UserProfileActivity.this)
                        .setContentTitle("Pdf Download")
                        .setSmallIcon(R.drawable.ic_action_drawer_notification)
                        .setAutoCancel(true)
                        .setProgress(0,0,true)
                        .setContentText("Download in progress");


                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1,notification.build());

                AbstractViewRenderer page1 = new AbstractViewRenderer(UserProfileActivity.this, R.layout.activity_pdf) {
                    @Override
                    protected void initView(View view) {}
                };
                page1.setReuseBitmap(true);


                PdfDocument doc  = new PdfDocument(UserProfileActivity.this);
                doc.addPage(page1);
                doc.setRenderWidth(1072);
                doc.setRenderHeight(2000);
                doc.setOrientation(PdfDocument.A4_MODE.PORTRAIT);
                doc.setSaveDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                doc.setFileName("SiddheshRaneProfile");
                doc.setInflateOnMainThread(false);
                doc.setListener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");

                        Toast.makeText(UserProfileActivity.this, "SiddheshRaneProfile.pdf saved in Downloads Folder", Toast.LENGTH_SHORT).show();

                        Uri targetUri = Uri.fromFile(file);

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(targetUri, "application/pdf");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                        notification.setContentText("Download complete");
                        notification.setContentIntent(pendingIntent);
                        notification.setProgress(0,0,false);
                        notificationManager.notify(1,notification.build());

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                    }
                });

                Toast.makeText(UserProfileActivity.this, "Download Started", Toast.LENGTH_SHORT).show();
                doc.createPdf(UserProfileActivity.this);

            }
        });


        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(this);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
            }
        });


        profilePageAdapter = new ProfilePageAdapter(getSupportFragmentManager());
        userinfo = (ViewPager) findViewById(R.id.profile_container);
        userinfo.setAdapter(profilePageAdapter);
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
        imageView.setImageResource(sampleImages[position]);

    }


    public static class ProfilePageAdapter extends FragmentPagerAdapter{

        public ProfilePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
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
                    PartnerPreferencesFragment partnerPreferencesFragment= new PartnerPreferencesFragment();
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
            switch (position){
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


    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out MarwadiShaadi App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
