package com.example.sid.marwadishaadi.User_Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Upload_User_Photos.UploadPhotoActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class  UserProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        ImageListener, GoogleApiClient.OnConnectionFailedListener {

    private static LinearLayout linearlayout;
    private ProgressDialog progessdialog;
    private Bitmap bitmap;
    private ProfilePageAdapter profilePageAdapter;
    private ViewPager userinfo;
    private CarouselView carouselView;
    protected ImageView pref;
    private GoogleApiClient mGoogleApiClient;
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
    private FrameLayout frameLayout;

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


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();


        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    Toast.makeText(UserProfileActivity.this,deepLink, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("Deep link", "getInvitation: no deep link found.");
                                }
                            }
                        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("Siddhesh Rane");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.entire_ui);
        frameLayout = (FrameLayout) findViewById(R.id.fabmenu);

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
                Toast.makeText(getApplicationContext(),"yay", Toast.LENGTH_SHORT).show();
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

          /*      progessdialog = new ProgressDialog(UserProfileActivity.this);
                progessdialog.setMessage("Please Wait");
                View mview = getLayoutInflater().inflate(R.layout.dummy,null);
                linearlayout = (LinearLayout) mview.findViewById(R.id.pdfdata);
                linearlayout.measure(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                        bitmap = loadBitmapFromView(linearlayout.getMeasuredWidth(),linearlayout.getMeasuredHeight());
                        createPdf();
*/

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

      /*  pref= (ImageView) findViewById(R.id.profile_user_preferences);
        pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View preferences_view = getLayoutInflater().inflate(R.layout.profile_preferences,null);

                AlertDialog.Builder prefs = new AlertDialog.Builder(UserProfileActivity.this);
                prefs.setTitle("Partner Signup_Partner_Preferences_Fragment");
                prefs.setView(preferences_view);

                // creating
                AlertDialog uprefs = prefs.create();
                uprefs.setTitle("Partner Signup_Partner_Preferences_Fragment");
                uprefs.show();
            }
        });*/

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

  /*  public static Bitmap loadBitmapFromView(int width,int height) {
        Bitmap b = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        linearlayout.draw(c);

        return b;
    }
    private void createPdf(){


        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();

        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        float height = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHeight = (int) height, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth,convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);
        paint.setColor(Color.BLUE);

        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            progessdialog.dismiss();
            Toast.makeText(UserProfileActivity.this, "PDF created", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(UserProfileActivity.this, PdfViewActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();
    }

    public static void shareApp(Context context)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out MarwadiShaadi App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }*/
}
