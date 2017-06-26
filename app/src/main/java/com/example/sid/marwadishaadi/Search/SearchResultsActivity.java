package com.example.sid.marwadishaadi.Search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionAdapter;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.sid.marwadishaadi.Search.BottomSheet.sm;
import static com.example.sid.marwadishaadi.Search.Search.suggestionModelList2;

public class SearchResultsActivity extends AppCompatActivity {


    private ArrayList<SuggestionModel> suggestionModelList = new ArrayList<>();
    public static RecyclerView recyclerView;
    private SuggestionAdapter suggestionAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle bundle=getIntent().getExtras();

//        sm.size();
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_results_toolbar);
        String str=bundle.get("which").toString();
        if(str.equals("advSearch")) {
            suggestionAdapter = new SuggestionAdapter(getApplicationContext(), suggestionModelList2, recyclerView);
            suggestionAdapter.notifyDataSetChanged();
            toolbar.setTitle("Results ("+suggestionAdapter.getItemCount()+")");
        }
        else {
            suggestionAdapter = new SuggestionAdapter(getApplicationContext(), sm, recyclerView);
            suggestionAdapter.notifyDataSetChanged();
            toolbar.setTitle("Results ("+ suggestionAdapter.getItemCount()+")");
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(suggestionAdapter);

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
