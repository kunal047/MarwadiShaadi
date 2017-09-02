package com.sid.marwadishaadi.User_Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sid.marwadishaadi.CacheHelper;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Search.BottomSheet;
import com.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditFamilyDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getCacheDir;


public class ProfileFamilyDetailsFragment extends Fragment {

    private static final String SOME_INTENT_FILTER_NAME = "SOME_INTENT_FILTER_NAME";
    private static int casebreak;
    TextView fatherName, grandpaName, mamaSurname, fatherOccupation, fatherOccupationDetails, nativePlace, subcaste, familyType, familyStatus, relation, relativeName, relativeOccupation, relativeLocation, relativeMobile;
    private TextView edit_family;
    private TextView edit_relatives;
    private Button similar;
    private String clickedID, customer_id;
    private boolean isPaidMember;
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;

    private BroadcastReceiver someBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String r = bundle.getString("relation");
            String rn = bundle.getString("relationName");
            String ro = bundle.getString("relationOccupation");
            String rl = bundle.getString("relationLocation");
            String rm = bundle.getString("relationMobile");


            if (r != null)
                relation.setText(r);

            if (rn != null)
                relativeName.setText(rn);

            if (ro != null) {
                relativeOccupation.setText(ro);
            }

            if (rl != null)
                relativeLocation.setText(rl);

            if (rm != null)
                relativeMobile.setText(rm);

        }
    };

    public ProfileFamilyDetailsFragment() {
        // Required empty public constructor
    }

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
        final View mview = inflater.inflate(R.layout.fragment_profile__family__details, container, false);


        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", "");
        clickedID = customer_id;

        String[] array = getResources().getStringArray(R.array.communities);

        for (int i = 0; i < 5; i++) {

            if (sharedpref.getString(array[i], "No").contains("Yes") && array[i].toCharArray()[0] != customer_id.toCharArray()[0]) {
                isPaidMember = true;
            }
        }

        edit_family = (TextView) mview.findViewById(R.id.family_clear);
        edit_relatives = (TextView) mview.findViewById(R.id.relatives_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        fatherName = (TextView) mview.findViewById(R.id.father_name);
        fatherOccupation = (TextView) mview.findViewById(R.id.father_occupation);
        fatherOccupationDetails = (TextView) mview.findViewById(R.id.father_occupation_details);
        familyStatus = (TextView) mview.findViewById(R.id.family_status);
        familyType = (TextView) mview.findViewById(R.id.family_type);
        nativePlace = (TextView) mview.findViewById(R.id.native_place);
        subcaste = (TextView) mview.findViewById(R.id.subcaste);
        grandpaName = (TextView) mview.findViewById(R.id.grandpa_name);
        mamaSurname = (TextView) mview.findViewById(R.id.mama_surname);
        relation = (TextView) mview.findViewById(R.id.relation);
        relativeName = (TextView) mview.findViewById(R.id.relative_name);
        relativeOccupation = (TextView) mview.findViewById(R.id.relative_occupation);
        relativeLocation = (TextView) mview.findViewById(R.id.relative_location);
        relativeMobile = (TextView) mview.findViewById(R.id.relative_mobile);


        fatherName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Father)", "").trim().length() == 0 && customer_id != clickedID) {
                    fatherName.setVisibility(View.GONE);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        fatherOccupation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Father)", "").trim().length() == 0 && customer_id != clickedID) {
                    fatherOccupation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fatherOccupationDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Father)", "").trim().length() == 0 && customer_id != clickedID) {
                    fatherOccupationDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        familyStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    familyStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        familyType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    familyType.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        nativePlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Natively from )", "").trim().length() == 0 && customer_id != clickedID) {
                    nativePlace.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        subcaste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Subcaste)", "").trim().length() == 0 && customer_id != clickedID) {
                    subcaste.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        grandpaName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Grandfather)", "").trim().length() == 0 && customer_id != clickedID) {
                    grandpaName.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mamaSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().replace("(Mama's Surname)", "").trim().length() == 0 && customer_id != clickedID) {
                    mamaSurname.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        relation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    relation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        relativeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    relativeName.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        relativeOccupation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    relativeOccupation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        relativeLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    relativeLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        relativeMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 9 && customer_id != clickedID) {
                    relativeMobile.setVisibility(View.GONE);
                } else {
                    if (!isPaidMember) {
                        float radius = relativeMobile.getTextSize() / 3;
                        BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                        relativeMobile.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        relativeMobile.getPaint().setMaskFilter(filter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        relativeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPaidMember) {


                    Toast.makeText(getContext(), "This feature is available only for paid members", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getContext(), UpgradeMembershipActivity.class);
                    getContext().startActivity(intent);

                    Snackbar snackbar = Snackbar
                            .make(mview, "Become a paid member.", Snackbar.LENGTH_LONG)
                            .setAction("GO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                } else {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String phoneNo = "tel:" + relativeMobile.getText().toString();
                    intent.setData(Uri.parse(phoneNo));
                    startActivity(intent);

                }
            }
        });

        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");
        if (data.getStringExtra("customerNo") != null) {

            clickedID = data.getStringExtra("customerNo");

        }

        if ("suggestion".equals(from) | "recent".equals(from) | "reverseMatching".equals(from) | "favourites".equals(from) | "interestReceived".equals(from) | "interestSent".equals(from) | "similar".equals(from)) {

            edit_family.setVisibility(View.GONE);
            edit_relatives.setVisibility(View.GONE);

        }

        if (customer_id.equals(clickedID)) {
            similar.setVisibility(View.GONE);
        }

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                i.putExtra("similarOf",clickedID);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        edit_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditFamilyDetailsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        edit_relatives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 32;
                BottomSheetDialogFragment btm = new BottomSheet(3);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });


        cache = new File(getCacheDir() + "/" + "familyprofile" +clickedID+ ".srl");

        // loading cached copy
        String res = CacheHelper.retrieve("familyprofile",cache);
        if(!res.equals("")){
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(getContext(),CacheHelper.generateHash(res),"familyprofile");

                // displaying it
                JSONArray response = new JSONArray(res);
                // Toast.makeText(getContext(), "Loading from cache....", Toast.LENGTH_SHORT).show();
                parseFamilyProfile(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        new ProfileFamilyDetails().execute(clickedID);

        return mview;
    }

    private void parseFamilyProfile(JSONArray response) {

        try {
            JSONArray result = response.getJSONArray(0);

            String fn = result.getString(0) + " (Father)";
            fatherName.setText(fn);
            String fo = result.getString(1) + " (Father)";
            fatherOccupation.setText(fo);
            String fod = result.getString(2) + " (Father)";
            fatherOccupationDetails.setText(fod);
            String fs = result.getString(3) + " Family";
            familyStatus.setText(fs);
            String ftv = result.getString(4) + " family with " + result.getString(5) + " values";
            familyType.setText(ftv);
            String nat = "Natively from " + result.getString(6);
            nativePlace.setText(nat);
            String sc = result.getString(7) + " (Subcaste)";
            subcaste.setText(sc);
            String gn = result.getString(8) + " (Grandfather)";
            grandpaName.setText(gn);
            String ms = result.getString(9) + " (Mama's Surname)";
            mamaSurname.setText(ms);
            relation.setText(result.getString(10));
            relativeName.setText(result.getString(11));
            relativeOccupation.setText(result.getString(12));
            String loc = result.getString(13);
            relativeLocation.setText(loc);
            String mob = result.getString(14);
            relativeMobile.setText(mob);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCasebreak() {
        return this.casebreak;
    }

    public void loadedFromNetwork(JSONArray response){


        //saving fresh in cache
        CacheHelper.save("familyprofile",response.toString(),cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(getContext(),CacheHelper.generateHash(response.toString()),"familyprofile");

        // displaying it
        parseFamilyProfile(response);

    }

    private class ProfileFamilyDetails extends AsyncTask<String, Void, Void> {
        @Override

        protected Void doInBackground(String... strings) {
            String cus = strings[0];
            AndroidNetworking.post("http://208.91.199.50:5000/profileFamilyDetails")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .setTag(this)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {


                            //

                            // if no change in data
                            if (isAlreadyLoadedFromCache){

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(getContext(),"familyprofile");

                                //
                                //
                                //

                                if (cacheResponseHash!=null && latestResponseHash.equals(cacheResponseHash)){
                                    // Toast.makeText(getContext(), "data same found", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{

                                    // hash not matched
                                    loadedFromNetwork(response);
                                }
                            }else{
                                // first time load
                                loadedFromNetwork(response);
                            }

                        }

                        @Override
                        public void onError(ANError error) {

                        }


                    });

            return null;
        }



    }
}
