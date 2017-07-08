package com.example.sid.marwadishaadi.Blocked_Members;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.sid.marwadishaadi.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;

public class BlockedActivity extends AppCompatActivity {
    Bundle bundle;
    private List<BlockModel> blockModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BlockAdapter blockAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);
         bundle= getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.blocked_toolbar);
        toolbar.setTitle("Blocked Members");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        recyclerView.setItemAnimator(fadeInLeftAnimator);
        blockAdapter = new BlockAdapter(getApplicationContext(), blockModelList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(BlockedActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(blockAdapter);
        prepareBlockData();
    }

    public void prepareBlockData()
    {
        String str=bundle.getString("Message");
        if(str.equals("chat")) {
            BlockModel blo = new BlockModel(bundle.getString("ID"), bundle.getString("Name"));
            blockModelList.add(blo);
            new Sync().execute("add",bundle.getString("ID"));

        }
        else{
            new Sync().execute("list");
        }

        blockAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        finish();
        overridePendingTransition(R.anim.exit,0);
        return true;
    }

class Sync extends AsyncTask<String,String,String>{
    ProgressDialog pgd;
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pgd.setMessage("Complete");
        pgd.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pgd=new ProgressDialog(BlockedActivity.this);
        pgd.setMessage("Please Wait...");
        pgd.setCanceledOnTouchOutside(false);
        pgd.setTitle("Loading");
        pgd.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String query="";

        if(strings[0].equals("list")) {
            query+="select blocked_id from tbl_block where customer_no=\""+customer_id+"\";";
        }
//        update tbl_block set blocked_id=concat (blocked_id , ' A1001 | A1002 | A1003 | ')
        else {
            query+="update tbl_block set blocked_id =concat( blocked_id , \""+strings[1]+" | \" ); ";
        }
        Log.e("TAG", "doInBackground: ------------ block query is =-------"+query );
        AndroidNetworking.post("http://208.91.199.50:5000/unblockOrBlock")
                .addBodyParameter("query", query)
                .setPriority(Priority.HIGH)
                .build()
        .getAsJSONArray(new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if(response.get(0).toString().equals("added")){
                        Toast.makeText(BlockedActivity.this, "Sorry to see this. If you need any kind of help, please contact us. ", Toast.LENGTH_LONG).show();
                    }
                    else if(response.length()>0){
                        //Layout, Indicate you did not blocked to anyone

                    }
                    else {
                        List<String> lst=new ArrayList<String>();
                        String str=response.get(0).toString();
                        char[] ar=str.toCharArray();
                        for(int i=0;i<str.length();i=i){
                            int j=i;
                            String s="";
                            while(ar[j]!='|'){
                                s+=ar[j];
                                j++;
                            }
                            i=j;
                            String quer="";
                            final String[] nam = new String[1];
                            quer+="select first_name from tbl_user where customer_no=\""+s+"\"";
                            Log.e("TAG", "onResponse: ------------ gettig name query is ------"+quer);
                            AndroidNetworking.post("http://208.91.199.50:5000/unblockOrBlock")
                                    .addBodyParameter("query", quer)
                                    .setPriority(Priority.HIGH)
                                    .build().getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        nam[0] =response.get(0).toString();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(BlockedActivity.this, "Network Error "+getEmojiByUnicode(0x1F631), Toast.LENGTH_SHORT).show();
                                  }
                            });
                            BlockModel blo = new BlockModel(s,nam[0]);
                            blockModelList.add(blo);
                            blockAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(BlockedActivity.this, "Network Error "+getEmojiByUnicode(0x1F631), Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }
}

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}
