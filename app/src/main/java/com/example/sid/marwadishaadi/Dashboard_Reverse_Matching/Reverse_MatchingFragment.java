package com.example.sid.marwadishaadi.Dashboard_Reverse_Matching;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;



public class Reverse_MatchingFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    private List<ReverseModel> reverseModelList = new ArrayList<>();
    private RecyclerView reverseRecyclerView;
    private ReverseAdapter reverseAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview =  inflater.inflate(R.layout.fragment_reverse__matching, container, false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());


        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Reverse Matching","view");

        reverseRecyclerView = (RecyclerView) mview.findViewById(R.id.swipe_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout)mview.findViewById(R.id.swipe);

        reverseRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        FadeInLeftAnimator fadeInLeftAnimator = new FadeInLeftAnimator();
        reverseRecyclerView.setItemAnimator(fadeInLeftAnimator);
        reverseAdapter = new ReverseAdapter(reverseModelList,getContext());
        reverseRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        reverseRecyclerView.setAdapter(reverseAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        getData();
        return mview;
    }

    private void refreshContent() {
        ReverseModel rev1 = new ReverseModel("https://lh3.googleusercontent.com/-_2FnlCFcgfA/WTUS3yDAXII/AAAAAAAAIHg/IRXv4NgTIcMlyoDaGfr8IYdVR2y_9ccugCK8B/s512/2017-06-05.jpg","Arya Stark","Mumbai"," B.Pharm.",20);
        reverseModelList.add(rev1);

        ReverseModel rev2 = new ReverseModel("https://lh3.googleusercontent.com/-XcUWlzpPfc8/WTUTyRwiTXI/AAAAAAAAIHs/E4zMBLDuWLwdiZ93WuMjQPfk5Ols_HZTwCK8B/s512/2017-06-05.jpg","Mervin Dalmet","Pune","MBA",20);
        reverseModelList.add(rev2);

        ReverseModel rev3 = new ReverseModel("https://scontent.fbom1-1.fna.fbcdn.net/v/t1.0-9/542932_1870229913255094_1073152164360738877_n.jpg?oh=414cb9683ac4daa8f7a67f63316215f6&oe=59ABFA15","Nirmal Saraswat","Delhi","M.Tech",20);
        reverseModelList.add(rev3);

        ReverseModel rev4 = new ReverseModel("https://lh3.googleusercontent.com/-_2FnlCFcgfA/WTUS3yDAXII/AAAAAAAAIHg/IRXv4NgTIcMlyoDaGfr8IYdVR2y_9ccugCK8B/s512/2017-06-05.jpg","Pranay Parmar","Delhi","M.Tech",20);
        reverseModelList.add(rev4);
        reverseAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getData(){


        ReverseModel rev5 = new ReverseModel("https://avatars2.githubusercontent.com/u/13920107?v=3&s=460","Siddhesh Rane","Delhi","M.Tech",20);
        reverseModelList.add(rev5);

        ReverseModel rev6 = new ReverseModel("https://lh3.googleusercontent.com/-V1g8Z_BDz08/WTUm1so2uPI/AAAAAAAAIIA/Dd_KRjoj0oosdISwTRTKcFVgO_YLkbAmgCK8B/s512/2017-06-05.jpg","Vatsal Mankodi","Delhi","M.Tech",20);
        reverseModelList.add(rev6);

        ReverseModel rev7 = new ReverseModel("https://lh3.googleusercontent.com/-_FDXYHeFNkI/WTUnfm1hDII/AAAAAAAAIII/10qU1JXB_twFY6HO9Z7um-sY8EmLiqCBgCK8B/s512/2017-06-05.jpg","Chinmayee Waze","Delhi","M.Tech",20);
        reverseModelList.add(rev7);

        ReverseModel rev8 = new ReverseModel("https://lh3.googleusercontent.com/-goP7QgrSZko/WTUnI_sU6xI/AAAAAAAAIIE/etkE4Lx_roc4nyeJY-PHf7ZzJ819YA3jACK8B/s512/2017-06-05.jpg","Saloni Agrawal","Delhi","M.Tech",20);
        reverseModelList.add(rev8);

        ReverseModel rev9 = new ReverseModel("https://lh3.googleusercontent.com/-fuehKVkteYg/WTUn0PG0exI/AAAAAAAAIIM/FDP13YTCdqguOHXL08kP5pdxlPDnzZXtwCK8B/s512/2017-06-05.jpg","Rutuja Bagul","Delhi","M.Tech",20);
        reverseModelList.add(rev9);

        reverseAdapter.notifyDataSetChanged();
    }

}
