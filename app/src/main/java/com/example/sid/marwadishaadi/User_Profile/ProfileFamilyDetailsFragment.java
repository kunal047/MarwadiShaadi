package com.example.sid.marwadishaadi.User_Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditFamilyDetailsActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.BottomSheet;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditFamilyDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


public class ProfileFamilyDetailsFragment extends Fragment {

    private static final String SOME_INTENT_FILTER_NAME = "SOME_INTENT_FILTER_NAME";
    private static int casebreak;
    private TextView edit_family;
    private TextView edit_relatives;
    private Button similar;
    private String clickedID  = customer_id;

    TextView fatherName, grandpaName, mamaSurname, fatherOccupation, fatherOccupationDetails, nativePlace, subcaste, familyType, familyStatus, relation, relativeName, relativeOccupation, relativeLocation, relativeMobile;

    public ProfileFamilyDetailsFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver someBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String r= bundle.getString("relation");
            String rn=bundle.getString("relationName");
            String ro=bundle.getString("relationOccupation");
            String rl=bundle.getString("relationLocation");
            String rm=bundle.getString("relationMobile");


            if(r!=null)
                relation.setText(r);

            if(rn!=null)
                relativeName.setText(rn);

            if(ro!=null) {
                relativeOccupation.setText(ro);
            }

            if(rl!=null)
                relativeLocation.setText(rl);

            if(rm!=null)
                relativeMobile.setText(rm);

        }
    };
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(someBroadcastReceiver,
                new IntentFilter(SOME_INTENT_FILTER_NAME));
    }
    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(someBroadcastReceiver);
        super.onPause();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_profile__family__details, container, false);
        edit_family=(TextView)mview.findViewById(R.id.family_clear);
        edit_relatives=(TextView)mview.findViewById(R.id.relatives_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        fatherName=(TextView) mview.findViewById(R.id.father_name);
        fatherOccupation=(TextView) mview.findViewById(R.id.father_occupation);
        fatherOccupationDetails=(TextView) mview.findViewById(R.id.father_occupation_details);
        familyStatus=(TextView) mview.findViewById(R.id.family_status);
        familyType=(TextView) mview.findViewById(R.id.family_type);
        nativePlace=(TextView) mview.findViewById(R.id.native_place);
        subcaste=(TextView) mview.findViewById(R.id.subcaste);
        grandpaName=(TextView) mview.findViewById(R.id.grandpa_name);
        mamaSurname=(TextView) mview.findViewById(R.id.mama_surname);
        relation=(TextView) mview.findViewById(R.id.relation);
        relativeName=(TextView) mview.findViewById(R.id.relative_name);
        relativeOccupation=(TextView) mview.findViewById(R.id.relative_occupation);
        relativeLocation=(TextView) mview.findViewById(R.id.relative_location);
        relativeMobile=(TextView) mview.findViewById(R.id.relative_mobile);

        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");
        if (data.getStringExtra("customerNo") != null) {

            clickedID = data.getStringExtra("customerNo");
            new ProfileFamilyDetails().execute(clickedID);
            edit_family.setVisibility(View.GONE);
            edit_relatives.setVisibility(View.GONE);
            Toast.makeText(getContext(), clickedID, Toast.LENGTH_SHORT).show();
        }


        new ProfileFamilyDetails().execute(clickedID);

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
        edit_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditFamilyDetailsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        edit_relatives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak=32;
                BottomSheetDialogFragment btm= new BottomSheet(3);
                btm.show(getFragmentManager(),btm.getTag());
            }
        });

        return mview;
    }


    private class ProfileFamilyDetails extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            String cus = params[0];
            AndroidNetworking.post(URL + "profileFamilyDetails")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .setTag(this)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response){

                            try {
                                JSONArray result = response.getJSONArray(0);

                                String fn= result.getString(0) + " (Father)";
                                fatherName.setText(fn);
                                String fo= result.getString(1) + " (Father)";
                                fatherOccupation.setText(fo);
                                String fod=result.getString(2) + " (Father)";
                                fatherOccupationDetails.setText(fod);
                                String fs=result.getString(3) + " Family";
                                familyStatus.setText(fs);
                                String ftv=result.getString(4) + " family with " +result.getString(5)+ " values";
                                familyType.setText(ftv);
                                String nat="Natively from "+result.getString(6);
                                nativePlace.setText(nat);
                                String sc=result.getString(7) + " (Subcaste)";
                                subcaste.setText(sc);
                                String gn=result.getString(8)+" (Grandfather)";
                                grandpaName.setText(gn);
                                String ms=result.getString(9)+" (Mama's Surname)";
                                mamaSurname.setText(ms);
                                relation.setText(result.getString(10));
                                relativeName.setText(result.getString(11));
                                relativeOccupation.setText(result.getString(12));
                                String loc="Lives in "+result.getString(13);
                                relativeLocation.setText(loc);
                                String mob="Contact: "+result.getString(14);
                                relativeMobile.setText(mob);







                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError error){
                            Log.d(TAG, "onError: *****************"+ error.toString());
                        }


                    });

            return null;
        };

    }

    public int getCasebreak()
    {
        return this.casebreak;
    }
}
