package com.example.sid.marwadishaadi.Notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Chat.DefaultDialogsActivity;
import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.Dashboard_Interest.InterestActivity;
import com.example.sid.marwadishaadi.Membership.MembershipActivity;
import com.example.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotificationsActivity extends AppCompatActivity {

    private List<NotificationsModel> notificationsModelList =new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationsAdapter notificationsAdapter;
    private View ChildView ;
    private FirebaseAnalytics mFirebaseAnalytics;

    Paint p = new Paint();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Notifications","view");

        Toolbar toolbar = (Toolbar) findViewById(R.id.notify_toolbar);
        toolbar.setTitle("Notifications");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        notificationsAdapter =  new NotificationsAdapter(this, notificationsModelList);
        recyclerView.setHasFixedSize(true);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(notificationsAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gesturedetector = new GestureDetector(NotificationsActivity.this, new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
                ChildView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if(ChildView != null && gesturedetector.onTouchEvent(motionEvent)) {
                    int position  = recyclerView.getChildAdapterPosition(ChildView);
                    NotificationsModel notificationsModel = notificationsModelList.get(position);
                    if (notificationsModel.isSuggested()){
                        Intent i = new Intent(NotificationsActivity.this, DashboardActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isPremMem()){
                        Intent i = new Intent(NotificationsActivity.this,MembershipActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isMemExp()){
                        Intent i = new Intent(NotificationsActivity.this, UpgradeMembershipActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isMsgRec()){
                        Intent i = new Intent(NotificationsActivity.this,DefaultDialogsActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isInterestAcc()){
                        Intent i = new Intent(NotificationsActivity.this,UserProfileActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isInterestRec()){
                        Intent i = new Intent(NotificationsActivity.this,InterestActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isOffers()){
                        Intent i = new Intent(NotificationsActivity.this,UpgradeMembershipActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                    else if(notificationsModel.isBday())
                    {

                        View bday_view = getLayoutInflater().inflate(R.layout.birthday_dialog, null);
                        AlertDialog.Builder bday = new AlertDialog.Builder(NotificationsActivity.this);
                        bday.setView(bday_view);
                        AlertDialog bdaybox = bday.create();
                        bdaybox.show();
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        prepareBlockData();

        Button clear = (Button) findViewById(R.id.clearnotifications);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics,"Clear Notifications","button");
                notificationsModelList.clear();
                notificationsAdapter.notifyDataSetChanged();
            }
        });


        ItemTouchHelper.SimpleCallback touchevents = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();


                if (direction == ItemTouchHelper.RIGHT | direction == ItemTouchHelper.LEFT) {
                    notificationsModelList.remove(position);
                    notificationsAdapter.notifyDataSetChanged();
                }
            }
            };



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchevents);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void prepareBlockData()
    {
        NotificationsModel ne;

        ne= new NotificationsModel("Mervin","12-9-17",3,true,false,false,false,false,false,false,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,true,false,false,false,false,false,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,true,false,false,false,false,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,false,true,false,false,false,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,false,false,true,false,false,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,false,false,false,true,false,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,false,false,false,false,true,false,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,false,false,false,false,false,true,false);
        notificationsModelList.add(ne);
        ne= new NotificationsModel("Mervin","12-9-17",3,false,false,false,false,false,false,false,false,true);
        notificationsModelList.add(ne);

        notificationsAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        overridePendingTransition(R.anim.exit,0);
        return true;
    }
}
