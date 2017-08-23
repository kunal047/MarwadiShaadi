package com.sid.marwadishaadi.Search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sid.marwadishaadi.Dashboard_Suggestions.SuggestionDataAdapter;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.sid.marwadishaadi.Search.BottomSheet.sm;
import static com.sid.marwadishaadi.Search.Search.suggestionModelList2;

public class SearchResultsActivity extends AppCompatActivity {

    int size=0;
    public static RecyclerView recyclerView;
    private SuggestionDataAdapter suggestionAdapter;
    private AlertDialog prompt ;
    private String customer_id;
    private boolean isPaidMember;
    private String clickedID;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        Bundle bundle=getIntent().getExtras();


        Toolbar toolbar = (Toolbar) findViewById(R.id.search_results_toolbar);
        String str=bundle.get("which").toString();
        if(str.contains("advSearch")) {
            suggestionAdapter = new SuggestionDataAdapter(getApplicationContext(), suggestionModelList2, recyclerView);
            suggestionAdapter.notifyDataSetChanged();
            toolbar.setTitle("Results ("+suggestionModelList2.size()+")");
            size=suggestionModelList2.size();
        }
        else {
            suggestionAdapter = new SuggestionDataAdapter(getApplicationContext(), sm, recyclerView);
            suggestionAdapter.notifyDataSetChanged();
            toolbar.setTitle("Results ("+ sm.size()+")");
            size=sm.size();
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(suggestionAdapter);

        SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        clickedID = customer_id;

        String[] array = getResources().getStringArray(R.array.communities);

        for (int i = 0; i < 5; i++) {

            if (sharedpref.getString(array[i], null).contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                isPaidMember = true;
            }
        }

        if(size==0){
            if(!isPaidMember){
            AlertDialog.Builder buildPrompt=new AlertDialog.Builder(SearchResultsActivity.this);
            buildPrompt.setCancelable(false);
            buildPrompt.setTitle("No Result").setMessage("Sorry! No search results found.")
                    .setPositiveButton("Membership", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent= new Intent(getApplicationContext(), UpgradeMembershipActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent= new Intent(getApplicationContext(), Search.class);
                            startActivity(intent);
                        }
                    });
            prompt= buildPrompt.create();
            prompt.show();

        }
        else{
                AlertDialog.Builder buildPrompt=new AlertDialog.Builder(SearchResultsActivity.this);
                buildPrompt.setCancelable(false);
                buildPrompt.setTitle("No Result").setMessage("Sorry! No search results found.")
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent= new Intent(getApplicationContext(), Search.class);
                                startActivity(intent);
                            }
                        });
                prompt= buildPrompt.create();
                prompt.show();

            }}

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),Search.class);
        startActivity(i);
        finish();

    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Intent i=new Intent(getApplicationContext(),Search.class);
        startActivity(i);
        return true;
    }
}
