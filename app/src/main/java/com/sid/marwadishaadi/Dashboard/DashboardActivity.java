package com.sid.marwadishaadi.Dashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sid.marwadishaadi.Chat.DefaultDialogsActivity;
import com.sid.marwadishaadi.Dashboard_Favourites.FavouritesFragment;
import com.sid.marwadishaadi.Dashboard_Interest.InterestActivity;
import com.sid.marwadishaadi.Dashboard_Recent_Profiles.RecentProfilesFragment;
import com.sid.marwadishaadi.Dashboard_Reverse_Matching.Reverse_MatchingFragment;
import com.sid.marwadishaadi.Dashboard_Suggestions.SuggestionsFragment;
import com.sid.marwadishaadi.Feedback.FeedbackActivity;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.Notifications.NotificationsActivity;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Search.Search;
import com.sid.marwadishaadi.Services.ChatNotifyService;
import com.sid.marwadishaadi.Services.PhotoUploadNotifyService;
import com.sid.marwadishaadi.Settings.SettingsActivity;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {


    TextView nameDrawer;

    private DashboardSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView userdp;
    private LinearLayout interest;
    private LinearLayout inbox;
    private LinearLayout search;
    private int click = 0;
    private String customer_id, customer_gender, customer_name;
    private DatabaseReference mDatabase;
    private int notificationCount = 0;
    private MenuItem m;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(this, ChatNotifyService.class));
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, ChatNotifyService.class);
        PendingIntent pintent = PendingIntent
                .getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every three hour
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                10800 * 1000, pintent);


        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dash_toolbar);
        ImageView toolbarSearch = (ImageView) findViewById(R.id.toolbar_search);

        setSupportActionBar(toolbar);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
//                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
//                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // not connected to the internet
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
//        Intent intent = getIntent();
//        mChosenContinueMethod = intent.getIntExtra(CONTINUE_METHOD, OVERLAY_METHOD);
//
//        mEnterAnimation = new AlphaAnimation(0f, 1f);
//        mEnterAnimation.setDuration(600);
//        mEnterAnimation.setFillAfter(true);
//
//        mExitAnimation = new AlphaAnimation(1f, 0f);
//        mExitAnimation.setDuration(600);
//        mExitAnimation.setFillAfter(true);
//
//        mChainTourGuide = ChainTourGuide.init(this).with(TourGuide.Technique.Click)
//                .setPointer(new Pointer())
//                .setToolTip(new ToolTip()
//                        .setTitle("Suggestion")
//                        .setDescription("Get your suggestion")
//                        .setGravity(Gravity.RIGHT))
//                .setOverlay(new Overlay()
//                        .setEnterAnimation(mEnterAnimation)
//                        .setExitAnimation(mExitAnimation));


        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        customer_gender = sharedpref.getString("gender", null);

        if (customer_id != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(customer_id).child("Notifications");
        }


        notificationCount = 0;
        //
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        m = menu.findItem(R.id.nav_notifications);

        FirebaseDatabase.getInstance().getReference().child(customer_id).child("Notifications")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("isRead").getValue() != null && snapshot.child("isRead").getValue().toString().contains("false")) {
                                notificationCount = notificationCount + 1;
                                if (notificationCount == 0) {
                                    m.setTitle("Notifications");
                                } else {
                                    m.setTitle("Notifications ( " + notificationCount + " )");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        toolbarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Search.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View mview = navigationView.getHeaderView(0);
        nameDrawer = (TextView) mview.findViewById((R.id.name_drawer));
        userdp = (ImageView) mview.findViewById(R.id.user_dp);

        if (userdp.getDrawable() == getResources().getDrawable(R.drawable.default_drawer)) {

            SharedPreferences sharedPref = getSharedPreferences("userDp", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("dashboard", "noDp");
            editor.apply();
        }
        //fetch dp and name
        new FetchDrawer().execute();

        userdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, UserProfileActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        nameDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, UserProfileActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        interest = (LinearLayout) mview.findViewById(R.id.nav_interest);
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, InterestActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });


        inbox = (LinearLayout) mview.findViewById(R.id.nav_inbox);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0;
                String[] array = getResources().getStringArray(R.array.communities);
                SharedPreferences communityChecker = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                for (int i = 0; i < 5; i++) {

                    if (communityChecker.getString(array[i], "No").contains("Yes")) {
                        counter++;
                    }
                }
                if (counter > 0) {
                    Intent i = new Intent(DashboardActivity.this, DefaultDialogsActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                } else {
                    Toast.makeText(DashboardActivity.this, " This feature is only for premium members", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), UpgradeMembershipActivity.class);
                    startActivity(intent);
                }
            }
        });


        search = (LinearLayout) mview.findViewById(R.id.nav_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, Search.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new DashboardSectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.dash_container);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.dash_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            click++;
            if (click == 1) {
                Toast.makeText(DashboardActivity.this, "Please press back button twice to exit", Toast.LENGTH_SHORT).show();
            } else if (click >= 2) {
                click = 0;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here .
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            onBackPressed();
        } else if (id == R.id.nav_interest) {
            Intent i = new Intent(DashboardActivity.this, InterestActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (id == R.id.nav_notifications) {
            Intent i = new Intent(DashboardActivity.this, NotificationsActivity.class);
            if (notificationCount == 0 || notificationCount < 0) {
                i.putExtra("count", -1);
            } else {
                i.putExtra("count", notificationCount);
            }
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (id == R.id.nav_membership) {
            Intent i = new Intent(DashboardActivity.this, UpgradeMembershipActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(DashboardActivity.this, SettingsActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else if (id == R.id.rate_us) {

            try {

                Uri uri = Uri.parse("market://details?id=com.sid.marwadishaadi");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);

            } catch (ActivityNotFoundException e) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.sid.marwadishaadi")));

            }

        } else {
            if (id == R.id.nav_feedback) {
                Intent i = new Intent(DashboardActivity.this, FeedbackActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        switch (position) {
            case 0:
                getSupportActionBar().setTitle("Suggestions ");
                break;
            case 1:
                getSupportActionBar().setTitle("Recent Profiles ");
                break;
            case 2:
                getSupportActionBar().setTitle("Reverse Matching ");
                break;
            case 3:
                getSupportActionBar().setTitle("Favourites");
                break;
           /* case 4:
                getSupportActionBar().setTitle("Super Match ");
                break;*/


        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class DashboardSectionsPagerAdapter extends FragmentPagerAdapter {


        public DashboardSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    SuggestionsFragment suggestionsFragment = new SuggestionsFragment();
                    return suggestionsFragment;
                case 1:
                    RecentProfilesFragment recent_profilesFragment = new RecentProfilesFragment();
                    return recent_profilesFragment;
                case 2:
                    Reverse_MatchingFragment reverse_matchingFragment = new Reverse_MatchingFragment();
                    return reverse_matchingFragment;
                case 3:
                    FavouritesFragment favouritesFragment = new FavouritesFragment();
                    return favouritesFragment;
               /*case 4:
                    SuperMatchFragment superMatchFragment = new SuperMatchFragment();
                    return superMatchFragment;*/

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
                    return "Suggestions";
                case 1:
                    return "Recent Profiles";
                case 2:
                    return "Reverse Matching";
                case 3:
                    return "Favourites";
               /* case 4:
                    return "Super Match";*/

                default:
                    return null;
            }
        }
    }


    public class FetchDrawer extends AsyncTask<Void, Void, Void>

    {


        @Override
        protected Void doInBackground(Void... params) {


            AndroidNetworking.post("http://208.91.199.50:5000/fetchProfilePictureDrawer")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {


                        @Override
                        public void onResponse(JSONArray response) {

                            try {

                                String name = response.getString(0) + " " + response.getString(1);
                                if (response.length() == 3) {

                                    String imageURL = "http://www.marwadishaadi.com/uploads/cust_" + customer_id + "/thumb/" + response.getString(2);

                                    SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editors = userinfo.edit();
                                    editors.putString("imageURL", imageURL);
                                    editors.putString("nameOfCustomer", name);
                                    editors.apply();

                                    if (response.getString(2).contains("null")) {
                                        Calendar sevendayalarm = Calendar.getInstance();

                                        sevendayalarm.add(Calendar.DATE, 3);

                                        Intent intent = new Intent(getApplicationContext(), PhotoUploadNotifyService.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 001, intent, 0);

                                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                        am.set(AlarmManager.RTC_WAKEUP, sevendayalarm.getTimeInMillis(), pendingIntent);
                                    }

                                    Picasso.with(getApplicationContext())
                                            .load(imageURL)
                                            .placeholder(R.drawable.default_drawer)
                                            .into(userdp);

//                                    keep below commented thing and remove above code if there is error
//
//
//                                    Picasso.with(getApplicationContext()).load("http://www.marwadishaadi.com/uploads/cust_"+customer_id+"/thumb/" + response.getString(2)).into(userdp);
                                }
                                nameDrawer.setText(name);


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
