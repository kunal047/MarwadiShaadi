package com.example.sid.marwadishaadi.User_Profile;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class  UserProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ImageListener{


    private ProfilePageAdapter profilePageAdapter;
    private ViewPager userinfo;
    private CarouselView carouselView;
    protected ImageView pref;
    private FloatingActionButton fav;
    private FloatingActionButton sendmsg;
    private FloatingActionButton sendinterest;
    private FloatingActionButton shareprofile;
    private FloatingActionButton sharesave;
    private FloatingActionButton editphotos;
    private FloatingActionMenu fab;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout toolbarLayout;

 /*   AndroidNetworking.post(URL + "prepareUserProfile")
            .addBodyParameter("customerNo", "O1057")
.setPriority(Priority.HIGH)
.build()
.getAsJSONArray(new JSONArrayRequestListener() {
        public void onResponse(JSONArray response) {
// do anything with response
            try {
                FavouriteModel[] favouriteModel = new FavouriteModel[response.length()];
                for (int i = 0; i < response.length(); i++) {

                    JSONArray array = response.getJSONArray(i);
                    String customerNo = array.getString(0);
                    String name = array.getString(1);
                    String dateOfBirth = array.getString(2);
// Thu, 18 Jan 1990 00:00:00 GMT
                    DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                    Date date = formatter.parse(dateOfBirth);
                    System.out.println(date);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);

                    String[] partsOfDate = formatedDate.split("-");
                    int day = Integer.parseInt(partsOfDate[0]);
                    int month = Integer.parseInt(partsOfDate[1]);
                    int year = Integer.parseInt(partsOfDate[2]);
                    int a = getAge(year, month, day);
                    String age = Integer.toString(a);
                    String education = array.getString(3);
                    String occupationLocation = array.getString(4);
                    String imageUrl = array.getString(5);


                    favouriteModel[i] = new FavouriteModel(customerNo, name, occupationLocation, education, Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl);

                    favouritesList.add(favouriteModel[i]);
                    favouritesAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: age of the user " + age);
                    Log.d(TAG, "onResponse: element " + i + " " + array.getString(0));


                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(ANError error) {
            Log.d(TAG, "onResponse: json response array is " + error.toString());
// handle error
        }
    });*/

    private String userid_from_deeplink;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbarLayout.setTitle("Pranay Parmar");


        Intent data = getIntent();
        String deeplink = data.getStringExtra("deeplink");

        if (deeplink!=null) {
            userid_from_deeplink = deeplink.substring(deeplink.lastIndexOf("/") + 1);
            Toast.makeText(UserProfileActivity.this, userid_from_deeplink, Toast.LENGTH_SHORT).show();
        }

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


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Favourites", "button");
            }
        });

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Sent Message", "button");
            }
        });

        sendinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Sent Interest", "button");
            }
        });

        shareprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Share Profile", "button");

                String caste = "Maheshwari";
                String userid = "M13725";
                final String username = "Siddhesh";
                String packageName = getPackageName();
                String weblink = "http://www.marwadishaadi.com/" + caste + "/user/candidate/" + userid;
                String domain = "https://bf5xe.app.goo.gl/";
                String link = domain + "?link=" + weblink + "&apn=" + packageName + "&afl="+weblink+"&dfl="+weblink;

                OnCompleteListener onCompleteListener = new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {

                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Intent sendIntent = new Intent();
                            String msg = "Hey, Check this cool profile of " + username + ":" + shortLink.toString();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,msg);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);

                        }
                    }
                };

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse(link))
                        .buildShortDynamicLink();
                shortLinkTask.addOnCompleteListener(onCompleteListener);


            }});

        sharesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Save as PDF", "button");


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
                doc.setFileName("Siddhesh-Rane-Profile");
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

        new ProfilePicture().execute();

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


    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out MarwadiShaadi App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    class ProfilePicture extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://192.168.43.143:5050/fetchProfilePicture")
                    .addBodyParameter("customerNo", "A1028")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {



                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                JSONArray array = response.getJSONArray(0);
                                String name = array.getString(1)+ " "+array.getString(2);
                                final ArrayList<String> images = new ArrayList<>();
                                for(int i =0;i<response.length();i++)
                                {
                                    images.add("http://www.marwadishaadi.com/uploads/cust_A1028/thumb/"+response.getJSONArray(i).getString(0));
                                    Log.d("blah", "onResponse: Image is************http://www.marwadishaadi.com/uploads/cust_A1028/thumb/"+response.getJSONArray(i).getString(0));

                                }

                                toolbarLayout.setTitle(name);

                                carouselView.setPageCount(images.size());
                                carouselView.setImageListener(new ImageListener() {
                                    @Override
                                    public void setImageForPosition(int position, ImageView imageView) {
                                        Picasso.with(UserProfileActivity.this)
                                                .load(images.get(position))
                                                .fit()
                                                .into(imageView);
                                    }
                                });


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
}
