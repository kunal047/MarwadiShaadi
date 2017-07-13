package com.example.sid.marwadishaadi.Chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
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
import com.example.sid.marwadishaadi.R;
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


//TODO check whether user is already blocked or not , also chat should be static not network dynamic

public class DefaultMessagesActivity extends DemoMessagesActivity
        implements MessageInput.InputListener {
    Toolbar toolbar;
    private static final String TAG = "DefaultMessagesActivity";
    public static MessagesListAdapter<Message> adapter;
    public List<Message> ml;

    RelativeLayout relative;
    ProgressDialog pgd;
    String url;
    private MessagesList messagesList;
    private String customer_id, customerId, customerName;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);
        String query="";
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        customerId = extras.getString("customerId");
        customerName = extras.getString("customerName");
        ml=new ArrayList<>();
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        adapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesList.setAdapter(adapter);
        query+="update tbl_message set msg_read=1 where ( msg_from=\""+customer_id+"\" and msg_to =\""+customerId+ "\" ) or (msg_to=\""+customer_id+"\" and msg_from=\""+customerId+"\" ) ;";
//        or (msg_from=""+customerId+"\" and msg_to=\""+customer_id+"")INNER JOIN tbl_user on msg_to=customer_no

        //TODO Add this method in python file and check query with different users. Save URL in every activity not at sharedPreference,Also change jsonObject to jsonArray
        query = "SELECT msg_on,msg_read,msg,msg_from,msg_to FROM `tbl_message`  where (msg_from=\"" + customer_id + "\" and msg_to =\"" + customerId + "\") or ( msg_from=\"" + customerId + "\" and msg_to =\"" + customer_id + "\") order by msg_on asc";
        Log.e(TAG, "onCreate: ------------query is ----"+query );
        pgd=new ProgressDialog(this);
        pgd.setTitle("Wait a while");
        pgd.setMessage("Slow connection...");
        pgd.show();
        AndroidNetworking.post("http://208.91.199.50:5000/getChat")
                .addBodyParameter("query",query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "onResponse: ----------response of creating list is \n"+response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONArray jsnrry= response.getJSONArray(i);
                                String string = jsnrry.getString(0);
                                SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");
                                Date date = format.parse(string);
                                Log.e(TAG, "onResponse: date is "+ jsnrry.getString(0));

                                Message message;
                                if(jsnrry.getString(3).contains(customerId)){
                                    User user = new User("1",customerName,null,true);
                                message = new Message(jsnrry.getString(3), user, jsnrry.getString(2),date);
                                   // Log.e(TAG, "onResponse: Add it in left" );
                                }
                                else{
                                    User user = new User("0",customerName,null,true);
                                    message = new Message(jsnrry.getString(3), user, jsnrry.getString(2),date);
                                   // Log.e(TAG, "onResponse: Add it in Right" );
                                }

                                adapter.addToStart(message, true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DefaultMessagesActivity.this, "Network Error ", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: ----------error of creating list is \n"+anError);
                    }
                });
//        adapter.addToEnd(ml,true);




         toolbar = (Toolbar) findViewById(R.id.chat_msg_toolbar);
        toolbar.setTitle(customerName.split(",")[0]);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        if(pgd.isShowing())
           pgd.dismiss();
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
        Intent intent=new Intent(DefaultMessagesActivity.this,BlockedActivity.class);
        intent.putExtra("ID",customerId);
        intent.putExtra("Name",toolbar.getTitle());
        menuItem.setTitle("Unblock");
    }

      @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),DefaultDialogsActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onSubmit(CharSequence input) {


        Date date = new Date();
        User user = new User("0",customerName, null, true);
        Message message = new Message("0", user, input.toString(), date);
        adapter.addToStart(message, true);
        messagesList.setAdapter(adapter);
        Log.d(TAG, "onSubmit: is called !!");

        String messageFromId = customer_id;
        String messageToId = customerId;
        String replyTo = "0"; // default is 0
        String subject = "from mobile"; //make it fixed
        String messageString = input.toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");
        String messageTime = timeStamp;
        Log.e(TAG, "onSubmit: time sent is ----- "+timeStamp);
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
                .addBodyParameter("messageOn", messageTime)
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
                        Log.e(TAG, "onResponse: ----------response of creating list is \n"+response);
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error TODO
                        Toast.makeText(DefaultMessagesActivity.this, "Network Error...", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: error is "+ error );
                    }
                });
        Log.d(TAG, "onSubmit: message from id is " + messageFromId);
        return true;
    }
}
