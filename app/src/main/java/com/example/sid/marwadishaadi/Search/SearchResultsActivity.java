package com.example.sid.marwadishaadi.Search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionAdapter;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.example.sid.marwadishaadi.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.sid.marwadishaadi.Search.BottomSheet.sm;
import static com.example.sid.marwadishaadi.Search.Search.suggestionModelList2;

public class SearchResultsActivity extends AppCompatActivity {

    int size=0;
    private ArrayList<SuggestionModel> suggestionModelList = new ArrayList<>();
    public static RecyclerView recyclerView;
    private SuggestionAdapter suggestionAdapter;
    private AlertDialog prompt ;

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
        //        sm.size();


        Toolbar toolbar = (Toolbar) findViewById(R.id.search_results_toolbar);
        String str=bundle.get("which").toString();
        if(str.contains("advSearch")) {
            suggestionAdapter = new SuggestionAdapter(getApplicationContext(), suggestionModelList2, recyclerView);
            suggestionAdapter.notifyDataSetChanged();
            toolbar.setTitle("Results ("+suggestionModelList2.size()+")");
            size=suggestionModelList2.size();
        }
        else {
            suggestionAdapter = new SuggestionAdapter(getApplicationContext(), sm, recyclerView);
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
        if(size==0){
            AlertDialog.Builder buildPrompt=new AlertDialog.Builder(SearchResultsActivity.this);
            buildPrompt.setCancelable(false);
            buildPrompt.setTitle("No Result").setMessage("This is because you are finding partner only in one community, take membership and get more results. ")
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
//        prepareBlockData();

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
