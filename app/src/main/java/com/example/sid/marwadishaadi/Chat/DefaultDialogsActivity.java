package com.example.sid.marwadishaadi.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;

//Inbox TODO top bar should work
public class DefaultDialogsActivity extends DemoDialogsActivity {
    static boolean fire = false, fire2 = false, MainFire = false, CalledOnce = false;
    String success = "", query = "";
    String name, url;
    ProgressDialog pgd;
    ScheduledExecutorService scheduleTaskExecutor1, scheduleTaskExecutor;
    int count = 0, count2 = 0, time = 0;
    ArrayList<String> ListOfQueries, ListOfMessage, ListOfSender, ListOfGetter, ListOfName, ListOfUrl, ListOFMessageOn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DialogsList dialogsList;
    private DialogsListAdapter<Dialog> dla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_dialogs);
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
        initAdapter();
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

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
//                            ^!^04k
                        Log.i(TAG, "run: ----------- fire is-++++ " + fire);
//                Looper.prepare();
                        try {
                            if (!fire) {
                                //Dummy TODO, you can do something if you want, ^!^04k
                                //Toast.makeText(OnClearFromRecentService.this, "You are not online", Toast.LENGTH_SHORT).show();
                                //Log.i(TAG, "run: My bro it checks here and wait for 15 sec");
                            } else {
                                Looper.prepare();
                                Log.i(TAG, "run: --- exited from here or not :::: yes ");

                                if (NameSetter()) {
                                    fire = false;
                                    MainFire = false;
                                    ListGetter(0);
                                    scheduleTaskExecutor.shutdown();
                                }
                                Looper.loop();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, "run: ---exception is******** ^!^04k " + e.toString());
                        }
//                Looper.loop();
                    }
                }, 1, 2, TimeUnit.SECONDS);
        scheduleTaskExecutor1 = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor1.scheduleAtFixedRate(new Runnable() {
            public void run() {
//                            ^!^04k
                Log.i(TAG, "run: ----------- fire is-++++ " + fire2 + " MainFire is " + MainFire);
//                Looper.prepare();
                try {
                    if (!fire2 && !MainFire) {
                        //Dummy TODO, you can do something if you want, ^!^04k
                        //Toast.makeText(OnClearFromRecentService.this, "You are not online", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "run: My bro it checks here and wait for 3 sec");
                    } else {
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        /*Log.i(TAG, "run: --- exited from here or not :::: yes in ListGetter Method ");
                        Log.i(TAG, "run: --- exited time is now "+time);*/
                        fire2 = false;
                        MainFire = true;
                        PrepareList();
                    }
                } catch (Exception e) {
                    Log.i(TAG, "run: ---exception is******** ^!^04k " + e.toString());
                }
//                Looper.loop();
            }
        }, 1, 5, TimeUnit.SECONDS);
    }

    //TODO Noob CTF
    void PrepareList() throws ParseException {
        CalledOnce = true;
        Log.e(TAG, "PrepareList: size of ListOfQuery is  " + ListOfQueries.size());
       /* str[0]=name;
        str[1]=url;
        str[2]=strings[1];
        str[3]=strings[2];
        str[4]=strings[3];*/
        //string[1]=listofgetter, string[2]=ListOfSender,string[3]=ListOfmessage
        Log.e(TAG, "PrepareList: why i not get called");
        for (int i = 0; i < ListOfQueries.size(); i++) {
            ArrayList<User> me = new ArrayList<User>();
            User usrme = new User(ListOfGetter.get(i), ListOfName.get(i), ListOfUrl.get(i), false);
            me.add(usrme);
            String string = ListOFMessageOn.get(i);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(string);
            Message msg = new Message(ListOfSender.get(i), usrme, ListOfMessage.get(i), date);
            Dialog dlg = new Dialog(ListOfSender.get(i), ListOfName.get(i), ListOfUrl.get(i), me, msg, 0);
            //Log.e("wtf", "onPostExecute: ----- " + dlg.toString() + "----name and url is --" + s[0] + "----**** " + s[1]);
            Log.e(TAG, "PrepareList: id is " + dlg.getId() + " dialog message id " + dlg.getLastMessage().getId() + "dialog user is " + dlg.getUsers().get(0).getId() + " user avatar and name are " + dlg.getUsers().get(0).getAvatar() + "&&" + dlg.getUsers().get(0).getName());
            Log.e(TAG, "PrepareList: ---item added is " + i);

            dla.addItem(dlg);
            dla.notifyDataSetChanged();
        }
        if (pgd.isShowing()) {
            pgd.dismiss();
        }

        scheduleTaskExecutor1.shutdown();
        /*View vg = findViewById (R.id.dialogsList);
        vg.invalidate();*/
    }

    void ListGetter(int counter) {
        Log.e(TAG, "ListGetter: -------- I get called my boy and count is " + count + "Query is -- ***** " + ListOfQueries.get(count));
        AndroidNetworking.get("http://10.0.0.5:8081/connect.php?query=" + ListOfQueries.get(count))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            count++;
                            JSONArray jsn = response.getJSONArray(0);
                            name = jsn.getString(0);
                            url = "http://www.marwadishaadi.com/uploads/cust_" + ListOfGetter.get(count - 1) + "/thumb/" + jsn.getString(2);
                            Log.e("TAG", "onResponse: ---name and urls are ----" + name + "*****" + url);
                            ListOfName.add(jsn.getString(0));
                            ListOfUrl.add(url);
                            if (count == ListOfQueries.size()) {
                                fire2 = true;
                                Log.e(TAG, "onResponse: when it will called");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (count < ListOfQueries.size()) {
                            ListGetter(0);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        scheduleTaskExecutor.shutdown();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    boolean NameSetter() {
        int total = ListOfQueries.size();
        Log.e("TAG", "NameSetter: Item count is , I execute first than you " + total);
        return total > 0;
    }

    private void initAdapter() {


        if (pgd.isShowing()) {
            //dummy
        } else {
            pgd = new ProgressDialog(DefaultDialogsActivity.this);
            pgd.setTitle("Loading..");
            pgd.setMessage("Slow Connection...");
            pgd.setCancelable(false);
            pgd.show();
        }
       /* dialogsAdapter = new DialogsListAdapter<>(super.imageLoader);
//        super.dialogsAdapter.setItems(Dialog);
        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);*/

//        inner join tbl_user on msg_to=customer_no or msg_from = customer_no
        query = "select msg_from,msg_to,msg,msg_on,msg_read from tbl_message  where msg_from=\"" + customer_id + "\" or msg_to =\"" + customer_id + "\" order by msg_on desc;";
        Log.e("G", "initAdapter: ---query of response list is ---" + query);
//        http://10.0.0.3:8081/connect.php
//        "http://10.0.0.7:5050/GetChat"
        //TODO Changed here
        AndroidNetworking.get("http://10.0.0.5:8081/connect.php?query=" + query)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Vector<String> str = new Vector<String>();
                        success = "success";
                        Log.e("Chat over list", "onResponse: of *********** getting all chat " + response);
//                        TODO id,name,photo,UserArrayList,LastMessage,UnreadCount;
                        //TODO Later change it with response.length-1 to repsonse.length
                        for (int i = 0; i < response.length() - 1; i++) {
                            try {
                                final JSONArray jsnrry = response.getJSONArray(i);
                                if (jsnrry.getString(0).contains(customer_id)) {
                                    if (str.contains(jsnrry.getString(1).trim())) {
                                        //Blank code TODO nthng
                                        count2++;
                                        if (count2 == response.length() - 1) {
                                            Log.e(TAG, "onResponse: --fire become true");
                                            fire = true;
                                        }
                                    } else {
                                        str.add(jsnrry.getString(1).trim());
                                        String query = "";
                                        query += "select first_name, surname,tbl_user_files.file_name from tbl_user inner join tbl_user_files on tbl_user.customer_no=tbl_user_files.customer_no where tbl_user_files.file_type=\"profile_image\" and tbl_user.customer_no=\"" + jsnrry.getString(1).trim() + "\" limit 1;";

//                                        new Asynctask().execute(query,jsnrry.getString(1),jsnrry.getString(0),jsnrry.getString(2));
                                        //string[1]=listofgetter, string[2]=ListOfSender,string[3]=ListOfmessage
                                        Log.e("TAG", "onResponse: ----ith query----" + i + " th  ** query is " + query);
                                        ListOfQueries.add(query);
                                        ListOfMessage.add(jsnrry.getString(2));
                                        ListOfSender.add(jsnrry.getString(0));
                                        ListOfGetter.add(jsnrry.getString(1));
                                        ListOFMessageOn.add(jsnrry.getString(3));
                                        count2++;
                                        if (count2 == response.length() - 1) {
                                            Log.e(TAG, "onResponse: --fire become true");
                                            fire = true;
                                        }
                                    }
                                } else {
                                    if (str.contains(jsnrry.getString(0).trim())) {
                                        //code will not do some
                                        count2++;
                                        if (count2 == response.length() - 1) {
                                            Log.e(TAG, "onResponse: --fire become true");
                                            fire = true;
                                        }
                                    } else {
                                        str.add(jsnrry.getString(0).trim());
                                        ArrayList<User> me = new ArrayList<User>();
                                        String query = "";
                                        query += "select first_name, surname,tbl_user_files.file_name from tbl_user inner join tbl_user_files on tbl_user.customer_no=tbl_user_files.customer_no where tbl_user_files.file_type=\"profile_image\" and tbl_user.customer_no=\"" + jsnrry.getString(0).trim() + "\" limit 1;";
                                        Log.e("Image", "onResponse: image query is --" + query);
//                                        new Asynctask().execute(query,jsnrry.getString(0),jsnrry.getString(0),jsnrry.getString(2));
                                        Log.e("TAG", "onResponse: ----ith query----" + i + " th  ** query is " + query);
                                        ListOfQueries.add(query);
                                        ListOfMessage.add(jsnrry.getString(2));
                                        ListOfSender.add(jsnrry.getString(0));
                                        ListOfGetter.add(jsnrry.getString(0));
                                        ListOFMessageOn.add(jsnrry.getString(3));
                                        count2++;
                                        //TODO Change code of length
                                        if (count2 == response.length() - 1) {
                                            Log.e(TAG, "onResponse: --fire become true");
                                            fire = true;
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                       /*Dialog dlg=new Dialog()
                        dla.addItem(dlg);*/
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DefaultDialogsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        Log.e("Chat over list", "onResponse: of *********** getting all chat " + anError);
                    }
                });
    }

}
