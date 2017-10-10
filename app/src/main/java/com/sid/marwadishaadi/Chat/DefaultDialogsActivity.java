package com.sid.marwadishaadi.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.R;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener;
import com.stfalcon.chatkit.utils.DateFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

//Inbox TODO top bar should work
public class DefaultDialogsActivity extends DemoDialogsActivity implements DateFormatter.Formatter {
    String success = "", query = "";
    String name, url;
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
        dla = new DialogsListAdapter<>(super.imageLoader);

        dialogsList = (DialogsList) findViewById(R.id.dialogsList);
        dla.setDatesFormatter(this);

        dialogsList.setAdapter(dla);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        toolbar.setTitle("Inbox");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pgd = new ProgressDialog(DefaultDialogsActivity.this);
        pgd.setMessage("Loading your messages...");
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
                Intent intnt = new Intent(getApplicationContext(), DefaultMessagesActivity.class);
                intnt.putExtra("customerId", dialog.getUsers().get(0).getId());
                intnt.putExtra("customerName", dialog.getDialogName());
                intnt.putExtra("url", dialog.getUsers().get(0).getAvatar());
                startActivity(intnt);
            }
        });


        new ListCreater().execute();
    }

//TODO Noob CTF

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else if (DateFormatter.isCurrentYear(date)) {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
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
//                        pgd.setTitle("Loading..");
                        pgd.setMessage("Loading your messages...");
                        pgd.setCancelable(false);
                        pgd.show();
                    }
                });
            }

//            query = "SELECT tbl_message.msg_from,tbl_message.msg_to,tbl_message.msg,tbl_message.msg_on,tbl_message.msg_read,tbl_user.first_name, tbl_user.surname,tbl_user_files.file_name from tbl_message inner join tbl_user on  (tbl_user.customer_no=tbl_message.msg_from or tbl_user.customer_no=tbl_message.msg_to) inner join tbl_user_files on (tbl_user_files.customer_no=tbl_message.msg_from or tbl_user_files.customer_no=tbl_message.msg_to) where (tbl_message.msg_from=\"" + customer_id + "\" or tbl_message.msg_to =\"" + customer_id + "\") and tbl_user_files.file_type='profile_image' order by tbl_message.msg_on desc;";
            query = "SELECT tbl_message.msg_from,tbl_message.msg_to,tbl_message.msg,tbl_message.msg_on,tbl_message.msg_read,tbl_user.first_name, tbl_user.surname,tbl_user_files.file_name from tbl_message inner join tbl_user on (tbl_user.customer_no!='" + customer_id + "') and (tbl_user.customer_no=tbl_message.msg_from or tbl_user.customer_no=tbl_message.msg_to) inner join tbl_user_files on (tbl_user_files.customer_no!='" + customer_id + "') and (tbl_user_files.customer_no=tbl_message.msg_from or tbl_user_files.customer_no=tbl_message.msg_to) where (tbl_message.msg_from=\"" + customer_id + "\" or tbl_message.msg_to =\"" + customer_id + "\") and tbl_user_files.file_type='profile_image' order by tbl_message.msg_on desc;";
            //inner join tbl_user on tbl_message.msg_from!="+

            //TODO Changed here
            AndroidNetworking.post(Constants.AWS_SERVER + "/getChat")
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

//                        TODO id,name,photo,UserArrayList,LastMessage,UnreadCount;
                            //TODO Later change it with response.length-1 to repsonse.length

                            try {
                                dla.clear();
                                for (int i = 0; i < response.length(); i++) {
                                    final JSONArray jsnrry = response.getJSONArray(i);
                                    if (jsnrry.getString(0).contains(customer_id)) {
                                        if (!str.contains(jsnrry.getString(1).trim())) {
                                            //Blank code TODO nthng
                                            str.add(jsnrry.getString(1));
//                                        new Asynctask().execute(query,jsnrry.getString(1),jsnrry.getString(0),jsnrry.getString(2));
                                            //string[1]=listofgetter, string[2]=ListOfSender,string[3]=ListOfmessage
                                            url = "http://www.marwadishaadi.com/uploads/cust_" + jsnrry.getString(1) + "/thumb/" + jsnrry.getString(7);

                                            ArrayList<User> me = new ArrayList<>();

                                            User usrme = new User(jsnrry.getString(1), jsnrry.getString(5), url, false);


                                            me.add(usrme);
                                            String string = jsnrry.getString(3);
//                                            SimpleDateFormat format = new SimpleDateFormat("dd MMM");
                                            SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");
                                            Date date = format.parse(string);
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(date);
                                            cal.add(Calendar.HOUR_OF_DAY, -5);
                                            cal.add(Calendar.MINUTE, -30);
                                            date = cal.getTime();
                                            DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
                                            format.setTimeZone(TimeZone.getTimeZone("IST"));
                                            Message msg = new Message(jsnrry.getString(0), usrme, jsnrry.getString(2), date);
                                            Dialog dlg = new Dialog(jsnrry.getString(0), jsnrry.getString(5) + " " + jsnrry.getString(6), url, me, msg, 0);
                                            //


                                            dla.addItem(dlg);
                                            dla.notifyDataSetChanged();

                                        }
                                    } else {
                                        if (!str.contains(jsnrry.getString(0).trim())) {
                                            //code will not do some
                                            str.add(jsnrry.getString(0));
                                            url = "http://www.marwadishaadi.com/uploads/cust_" + jsnrry.getString(0) + "/thumb/" + jsnrry.getString(7);

                                            ArrayList<User> me = new ArrayList<User>();
                                            User usrme = new User(jsnrry.getString(0), jsnrry.getString(5), url, false);
                                            me.add(usrme);
                                            String string = jsnrry.getString(3);
                                            SimpleDateFormat format = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z");
                                            Date date = format.parse(string);
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(date);
                                            cal.add(Calendar.HOUR_OF_DAY, -5);
                                            cal.add(Calendar.MINUTE, -30);
                                            date = cal.getTime();
                                            DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
                                            format.setTimeZone(TimeZone.getTimeZone("IST"));
                                            Message msg = new Message(jsnrry.getString(0), usrme, jsnrry.getString(2), date);
                                            Dialog dlg = new Dialog(jsnrry.getString(0), jsnrry.getString(5), url, me, msg, 0);
                                            //


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
