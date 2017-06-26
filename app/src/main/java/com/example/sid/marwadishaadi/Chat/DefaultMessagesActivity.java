package com.example.sid.marwadishaadi.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;
import static com.google.android.gms.R.id.toolbar;

public class DefaultMessagesActivity extends DemoMessagesActivity
        implements MessageInput.InputListener {

    private static final String TAG = "DefaultMessagesActivity";
    public static MessagesListAdapter<Message> adapter;
    private MessagesList messagesList;
    private String customerId, customerName;
    private Menu menu;

    public static void open(Context context) {
        context.startActivity(new Intent(context, DefaultMessagesActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_messages);

        messagesList = (MessagesList) findViewById(R.id.messagesList);
        adapter = new MessagesListAdapter<>(senderId, imageLoader);

        Bundle extras = getIntent().getExtras();

        customerId = extras.getString("customerId");
        customerName = extras.getString("customerName");

        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_msg_toolbar);
        toolbar.setTitle(customerName.split(",")[0]);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MessageInput input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        super.onLoadMore(page, totalItemsCount);

    }
 @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.chat_toolbar_block,menu);
        this.menu =menu;
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
         case R.id.blocked:
                 onBlockPressed(id);
             RelativeLayout relative = (RelativeLayout) findViewById(R.id.relative_chat);
                 Snackbar snackbar = Snackbar.make(relative, "Added to Blocked List", Snackbar.LENGTH_LONG).setAction("UNBLOCK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onUnblockPressed(id);
                        }
                    });
                    snackbar.show();
                return true;
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

    private void onBlockPressed(int id) {
        String customer_id="A1008";
        MenuItem menuItem = menu.findItem(id);
        Intent intent=new Intent(DefaultMessagesActivity.this,BlockedActivity.class);
        intent.putExtra("ID",customer_id);
        intent.putExtra("Name",getSupportActionBar().getTitle());
        menuItem.setTitle("Unblock");
    }
    @Override
    public boolean onSubmit(CharSequence input) {


        Date date = new Date();
        User user = new User("0", "me", "", true);
        Message message = new Message("0", user, input.toString(), date);
        adapter.addToStart(message, true);
        messagesList.setAdapter(adapter);

        Log.d(TAG, "onSubmit: is called !!");

        String messageFromId = "0001";
        String messageToId = customerId;
        String replyTo = "0"; // default is 0
        String subject = "from mobile"; //make it fixed
        String messageString = input.toString();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String messageTime = timeStamp;
        String replyOn = "2010-01-01 01:01:01";
        String messageRead = "0"; // 0 - unread , 1 - read
        String fromDelete = ""; // yes if deleted from sender
        String toDelete = ""; // use if deleted from receiver

        AndroidNetworking.post(URL + "uploadChat")
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
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
        Log.d(TAG, "onSubmit: message from id is " + messageFromId);
        return true;
    }

}
