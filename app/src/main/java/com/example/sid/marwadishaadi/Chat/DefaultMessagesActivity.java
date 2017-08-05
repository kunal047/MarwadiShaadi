package com.example.sid.marwadishaadi.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Blocked_Members.BlockedActivity;
import com.example.sid.marwadishaadi.DeviceRegistration;
import com.example.sid.marwadishaadi.Notifications.NotificationsModel;
import com.example.sid.marwadishaadi.Notifications_Util;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


//TODO check whether user is already blocked or not , also chat should be static not network dynamic

public class DefaultMessagesActivity extends DemoMessagesActivity
        implements MessageInput.InputListener {
    private static final String TAG = "DefaultMessagesActivity";
    public static MessagesListAdapter<Message> adapter;
    public List<Message> ml;
    Toolbar toolbar;
    RelativeLayout relative;
    ProgressDialog pgd;
    String url;
    private MessagesList messagesList;
    private String customer_id, customerId, customerName;
    private Menu menu;
    private Handler handler = new Handler();
    private String query;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabases;
    private String customer_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);
        String query = "";
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        String first_name = sharedpref.getString("firstname",null);
        String last_name = sharedpref.getString("surname",null);
        customer_name = first_name + " " +last_name;

        if (extras.getString("customerId") != null) {
            customerId = extras.getString("customerId");
        }
        if (extras.getString("customerName") != null) {
            customerName = extras.getString("customerName");

        }
        ml = new ArrayList<>();
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        adapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesList.setAdapter(adapter);
        query = "update tbl_message set msg_read=1 where (msg_to=\"" + customer_id + "\" and msg_from=\"" + customerId + "\" ) ;";
        new SetSeen().execute(query);
//        or (msg_from=""+customerId+"\" and msg_to=\""+customer_id+"")INNER JOIN tbl_user on msg_to=customer_no

        //TODO Add this method in python file and check query with different users. Save URL in every activity not at sharedPreference,Also change jsonObject to jsonArray


//
        query = "SELECT msg_on,msg_read,msg,msg_from,msg_to FROM `tbl_message`  where (msg_from=\"" + customer_id + "\" and msg_to =\"" + customerId + "\") or ( msg_from=\"" + customerId + "\" and msg_to =\"" + customer_id + "\") order by msg_on asc";

        pgd = new ProgressDialog(this);
        pgd.setTitle("Wait a while");
        pgd.setMessage("Waiting for connection...");
        pgd.show();


        toolbar = (Toolbar) findViewById(R.id.chat_msg_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                i.putExtra("customerNo", customerId);
                i.putExtra("from", "chat");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });

        toolbar.setTitle(customerName.split(",")[0]);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        if (pgd.isShowing())
            pgd.dismiss();

        new FetchingMessages().execute(query);
//        adapter.addToEnd(ml,true);


    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        super.onLoadMore(page, totalItemsCount);
    }

    /*@Override
       public boolean onCreateOptionsMenu(Menu menu) {

           getMenuInflater().inflate(R.menu.chat_toolbar_block,menu);
           this.menu =menu;
           return super.onCreateOptionsMenu(menu);
       }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//         case R.id.blocked:
//
//                 Snackbar snackbar = Snackbar.make(relative, "Added to Blocked List", Snackbar.LENGTH_LONG).setAction("UNBLOCK", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            onUnblockPressed(id);
//                        }
//                    });
//                    snackbar.show();
//             onBlockPressed(id);
//                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onUnblockPressed(int id) {
        MenuItem menuItem = menu.findItem(id);
        menuItem.setTitle("Block");
    }

    //TODO Unblock should be intended to block_list
    private void onBlockPressed(int id) {
        String customer_id = "A1008";
        MenuItem menuItem = menu.findItem(id);
        Intent intent = new Intent(DefaultMessagesActivity.this, BlockedActivity.class);
        intent.putExtra("ID", customerId);
        intent.putExtra("Name", toolbar.getTitle());
        menuItem.setTitle("Unblock");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.exit, 0);
        return true;
    }

    @Override
    public boolean onSubmit(CharSequence input) {


        Date date = new Date();
        User user = new User("0", customerName, null, true);
        Message message = new Message("0", user, input.toString(), date);
        adapter.addToStart(message, true);
        messagesList.setAdapter(adapter);


        final String messageFromId = customer_id;
        final String messageToId = customerId;
        String replyTo = "0"; // default is 0
        String subject = "from mobile"; //make it fixed
        String messageString = input.toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance(TimeZone.getTimeZone("IST")).getTime());
//SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");

        String replyOn = "2010-01-01 01:01:01";
        String messageRead = "0"; // 0 - unread , 1 - read
        String fromDelete = ""; // yes if deleted from sender
        String toDelete = ""; // use if deleted from receiver
        AndroidNetworking.post("http://208.91.199.50:5000/uploadChat")
                .addBodyParameter("messageFromId", messageFromId)
                .addBodyParameter("messageToId", messageToId)
                .addBodyParameter("replyTo", replyTo)
                .addBodyParameter("subject", subject)
                .addBodyParameter("message", messageString)
                .addBodyParameter("messageOn", timeStamp)
                .addBodyParameter("replyOn", replyOn)
                .addBodyParameter("messageRead", messageRead)
                .addBodyParameter("fromDelete", fromDelete)
                .addBodyParameter("toDelete", toDelete)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response TODO





                        // adding it to her notifications list
                        String date = String.valueOf(DateFormat.format("dd-MM-yyyy", new Date()));
                        mDatabase = FirebaseDatabase.getInstance().getReference(messageToId).child("Notifications");
                        final NotificationsModel notification= new NotificationsModel(customer_name,date,3,false,false,false,true,false,false,false,false,false);
                        String hash = String.valueOf(notification.hashCode());
                        mDatabase.child(hash).setValue(notification);

                        // sending push notification to her
                        // get all devices

                        mDatabases = FirebaseDatabase.getInstance().getReference(messageToId).child("Devices");
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

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error TODO
                        Toast.makeText(DefaultMessagesActivity.this, "Network Error...", Toast.LENGTH_SHORT).show();

                    }
                });

        return true;
    }
    public void setData(DataSnapshot dataSnapshot){

        // looping through all the devices and sending push notification to each of 'em
        DeviceRegistration device = dataSnapshot.getValue(DeviceRegistration.class);
        Notifications_Util.SendNotification(device.getDevice_id(), toolbar.getTitle() + " sent you an Message", "New Message", "Message");
    }

    private class FetchingMessages extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            query = params[0];
            AndroidNetworking.post("http://208.91.199.50:5000/getChat")
                    .addBodyParameter("query", query)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONArray jsnrry = response.getJSONArray(i);
                                    String string = jsnrry.getString(0);
                                    SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
                                    Date date = format.parse(string);

                                    Calendar cal=Calendar.getInstance();
                                    cal.setTime(date);
                                    cal.add(Calendar.HOUR_OF_DAY,-5);
                                    cal.add(Calendar.MINUTE,-30);
                                    date=cal.getTime();

                                    Message message;
                                    if (jsnrry.getString(3).contains(customerId)) {
                                        User user = new User("1", customerName, null, true);
                                        message = new Message(jsnrry.getString(3), user, jsnrry.getString(2), date);
                                        //
                                    } else {
                                        User user = new User("0", customerName, null, true);
                                        message = new Message(jsnrry.getString(3), user, jsnrry.getString(2), date);
                                        //
                                    }

                                    adapter.addToStart(message, true);
                                } catch (JSONException | ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(DefaultMessagesActivity.this, "Network Error ", Toast.LENGTH_SHORT).show();

                        }
                    });
            return null;
        }
    }
    private class SetSeen extends AsyncTask<String ,String,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            AndroidNetworking.post("http://208.91.199.50:5000/unblock")
                    .addBodyParameter("query",strings[0])
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                if(response.getString(0).contains("success"))
                                {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(DefaultMessagesActivity.this, "Network or Server Error", Toast.LENGTH_SHORT).show();

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
