package com.sid.marwadishaadi.Notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.sid.marwadishaadi.Analytics_Util;
import com.sid.marwadishaadi.Chat.DefaultDialogsActivity;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.Dashboard_Interest.InterestActivity;
import com.sid.marwadishaadi.Membership.MembershipActivity;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference mDatabase;
    private DatabaseReference count;
    private String customer_id;
    private LinearLayout empty_view;
    private int counts = 0;
    private boolean isdata;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);




        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        if (customer_id !=null){
            mDatabase = FirebaseDatabase.getInstance().getReference(customer_id).child("Notifications");
            count = FirebaseDatabase.getInstance().getReference(customer_id);
        }


        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Notifications","view");



        Toolbar toolbar = (Toolbar) findViewById(R.id.notify_toolbar);


        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empty_view = (LinearLayout) findViewById(R.id.empty_view_notifications);
        empty_view.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        notificationsAdapter =  new NotificationsAdapter(this, notificationsModelList);
        recyclerView.setHasFixedSize(true);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(notificationsAdapter);
        recyclerView.setVisibility(View.GONE);


        isdata = false;
        count.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int size = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    counts++;
                    if (snap.getKey().equals("Notifications")){
                        isdata=true;
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                    }
                    if (counts==size && !isdata ){
                        empty_view.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (counts==0){
            empty_view.setVisibility(View.VISIBLE);
        }

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

                    mDatabase = FirebaseDatabase.getInstance().getReference(customer_id).child("Notifications");
                    mDatabase.child(notificationsModel.getId()).child("isRead").setValue(true);


                    if (notificationsModel.isSuggested()){

                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this, DashboardActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isPremMem()){

                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this,MembershipActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isMemExp()){

                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this, UpgradeMembershipActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isMsgRec()){

                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this,DefaultDialogsActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isInterestAcc()){

                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this,UserProfileActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isInterestRec()){
                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this,InterestActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else if(notificationsModel.isOffers()){

                        notificationsModel.setRead(true);
                        Intent i = new Intent(NotificationsActivity.this,UpgradeMembershipActivity.class);
                        startActivity(i);
                        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }
                    else if(notificationsModel.isBday())
                    {
                        notificationsModel.setRead(true);
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



        ItemTouchHelper.SimpleCallback touchevents = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();


                if (direction == ItemTouchHelper.RIGHT | direction == ItemTouchHelper.LEFT) {

                    mDatabase = FirebaseDatabase.getInstance().getReference(customer_id).child("Notifications");
                    mDatabase.child(notificationsModelList.get(position).getId()).removeValue();
                    notificationsModelList.remove(position);
                    notificationsAdapter.notifyDataSetChanged();
                }
            }
            };



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchevents);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void setData(DataSnapshot dataSnapshot){

            NotificationsModel notificationsModel = dataSnapshot.getValue(NotificationsModel.class);
            notificationsModelList.add(notificationsModel);
            notificationsAdapter.notifyDataSetChanged();

    }

    public void prepareBlockData()
    {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    setData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    setData(dataSnapshot);
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

    /*    NotificationsModel ne;

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

        notificationsAdapter.notifyDataSetChanged();*/
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        finish();
        overridePendingTransition(R.anim.exit,0);
        return true;
    }
}
