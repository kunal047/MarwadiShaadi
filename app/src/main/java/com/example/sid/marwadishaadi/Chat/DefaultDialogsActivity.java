package com.example.sid.marwadishaadi.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.R;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

//Inbox TODO top bar should work
public class DefaultDialogsActivity extends DemoDialogsActivity {
    static boolean fire = false, fire2 = false, MainFire = false, CalledOnce = false;
    String success = "", query = "";
    String name, url;
    ScheduledExecutorService scheduleTaskExecutor1, scheduleTaskExecutor;
    int count = 0, count2 = 0, time = 0;
    ArrayList<String> ListOfQueries, ListOfMessage, ListOfSender, ListOfGetter, ListOfName, ListOfUrl, ListOFMessageOn;
    ProgressDialog pgd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DialogsList dialogsList;
    private DialogsListAdapter<Dialog> dla;
    private String customer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_dialogs);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        ListOfQueries = new ArrayList<>();
        ListOfMessage = new ArrayList<>();
        ListOfSender = new ArrayList<>();
        ListOfGetter = new ArrayList<>();
        ListOfName = new ArrayList<>();
        ListOfUrl = new ArrayList<>();
        ListOFMessageOn = new ArrayList<>();
        dla = new DialogsListAdapter<Dialog>(super.imageLoader);

        dialogsList = (DialogsList) findViewById(R.id.dialogsList);
        dialogsList.setAdapter(dla);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        toolbar.setTitle("Inbox");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pgd = new ProgressDialog(DefaultDialogsActivity.this);
        pgd.setMessage("Slow Network...");
        pgd.setCancelable(false);
        pgd.show();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chatRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ListCreater().execute();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        dla.setOnDialogClickListener(new OnDialogClickListener<Dialog>() {
            @Override

            public void onDialogClick(Dialog dialog) {
                Log.e("TAG", "onDialogClick: " + dialog.getId() + "------" + dialog.getDialogName() + " item photo is " + dialog.getDialogPhoto());
                Intent intnt = new Intent(getApplicationContext(), DefaultMessagesActivity.class);
                intnt.putExtra("customerId", dialog.getUsers().get(0).getId());
                intnt.putExtra("customerName", dialog.getUsers().get(0).getName());
                intnt.putExtra("url", dialog.getUsers().get(0).getAvatar());
                startActivity(intnt);
            }
        });

        new ListCreater().execute();
    }

//TODO Noob CTF


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    private class ListCreater extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (pgd.isShowing()) {
                //dummy
            } else {
                DefaultDialogsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pgd = new ProgressDialog(DefaultDialogsActivity.this);
                        pgd.setTitle("Loading..");
                        pgd.setMessage("Slow Connection...");
                        pgd.setCancelable(false);
                        pgd.show();
                    }
                });
            }

            query = "select tbl_message.msg_from,tbl_message.msg_to,tbl_message.msg,tbl_message.msg_on,tbl_message.msg_read,tbl_user.first_name, tbl_user.surname,tbl_user_files.file_name from tbl_message inner join tbl_user on (tbl_user.customer_no!='" + customer_id + "') and (tbl_user.customer_no=tbl_message.msg_from or tbl_user.customer_no=tbl_message.msg_to) inner join tbl_user_files on (tbl_user_files.customer_no!='" + customer_id + "') and (tbl_user_files.customer_no=tbl_message.msg_from or tbl_user_files.customer_no=tbl_message.msg_to) where (tbl_message.msg_from=\"" + customer_id + "\" or tbl_message.msg_to =\"" + customer_id + "\") and tbl_user_files.file_type='profile_image' order by tbl_message.msg_on desc;";
            //inner join tbl_user on tbl_message.msg_from!="+
            Log.e("G", "initAdapter: ---query of response list is ---" + query);
//        http://10.0.0.3:8081/connect.php
//        "http://10.0.0.7:5050/GetChat"
            //TODO Changed here
            AndroidNetworking.post("http://208.91.199.50:5000/getChat")
                    .addBodyParameter("query", query)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            DefaultDialogsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pgd.dismiss();
                                }
                            });
                            Vector<String> str = new Vector<String>();
                            success = "success";
                            Log.e("Chat over list", "onResponse: of *********** getting all chat " + response);
//                        TODO id,name,photo,UserArrayList,LastMessage,UnreadCount;
                            //TODO Later change it with response.length-1 to repsonse.length

                            try {
                                dla.clear();
                                for (int i = 0; i < response.length(); i++) {
                                    final JSONArray jsnrry = response.getJSONArray(i);
                                    if (jsnrry.getString(0).contains(customer_id)) {
                                        Log.e(TAG, "onResponse: ************************************** item is at 0");
                                        if (!str.contains(jsnrry.getString(1).trim())) {
                                            //Blank code TODO nthng
                                            str.add(jsnrry.getString(1));
//                                        new Asynctask().execute(query,jsnrry.getString(1),jsnrry.getString(0),jsnrry.getString(2));
                                            //string[1]=listofgetter, string[2]=ListOfSender,string[3]=ListOfmessage
                                            url = "http://www.marwadishaadi.com/uploads/cust_" + jsnrry.getString(1) + "/thumb/" + jsnrry.getString(7);
                                            Log.e(TAG, "onResponse: URL is UUUURRRRRLLLL " + url);
                                            ArrayList<User> me = new ArrayList<User>();
                                            User usrme = new User(jsnrry.getString(1), jsnrry.getString(5), url, false);
                                            me.add(usrme);
                                            String string = jsnrry.getString(3);
                                            SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");
                                            Date date = format.parse(string);
                                            Message msg = new Message(jsnrry.getString(0), usrme, jsnrry.getString(2), date);
                                            Dialog dlg = new Dialog(jsnrry.getString(0), jsnrry.getString(5), url, me, msg, 0);
                                            //Log.e("wtf", "onPostExecute: ----- " + dlg.toString() + "----name and url is --" + s[0] + "----**** " + s[1]);
                                            Log.e(TAG, "PrepareList: id is " + dlg.getId() + " dialog message id " + dlg.getLastMessage().getId() + "dialog user is " + dlg.getUsers().get(0).getId() + " user avatar and name are " + dlg.getUsers().get(0).getAvatar() + "&&" + dlg.getUsers().get(0).getName());
                                            Log.e(TAG, "PrepareList: ---item added is " + i);
                                            dla.addItem(dlg);
                                            dla.notifyDataSetChanged();

                                        }
                                    } else {
                                        if (!str.contains(jsnrry.getString(0).trim())) {
                                            //code will not do some
                                            str.add(jsnrry.getString(0));
                                            url = "http://www.marwadishaadi.com/uploads/cust_" + jsnrry.getString(0) + "/thumb/" + jsnrry.getString(7);
                                            Log.e(TAG, "onResponse: URL is UUUURRRRRLLLL " + url);
                                            ArrayList<User> me = new ArrayList<User>();
                                            User usrme = new User(jsnrry.getString(0), jsnrry.getString(5), url, false);
                                            me.add(usrme);
                                            String string = jsnrry.getString(3);
                                            SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");
                                            Date date = format.parse(string);
                                            Message msg = new Message(jsnrry.getString(0), usrme, jsnrry.getString(2), date);
                                            Dialog dlg = new Dialog(jsnrry.getString(0), jsnrry.getString(5), url, me, msg, 0);
                                            //Log.e("wtf", "onPostExecute: ----- " + dlg.toString() + "----name and url is --" + s[0] + "----**** " + s[1]);
                                            Log.e(TAG, "PrepareList: id is " + dlg.getId() + " dialog message id " + dlg.getLastMessage().getId() + "dialog user is " + dlg.getUsers().get(0).getId() + " user avatar and name are " + dlg.getUsers().get(0).getAvatar() + "  && " + dlg.getUsers().get(0).getName());
                                            Log.e(TAG, "PrepareList: ---item added is " + i);
                                            dla.addItem(dlg);
                                            dla.notifyDataSetChanged();

                                        }
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                       /*Dialog dlg=new Dialog()
                        dla.addItem(dlg);*/
                        }


                        @Override
                        public void onError(ANError anError) {
                            DefaultDialogsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pgd.dismiss();
                                }
                            });
                            Toast.makeText(DefaultDialogsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            Log.e("Chat over list", "onResponse: of *********** getting all chat " + anError);
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
