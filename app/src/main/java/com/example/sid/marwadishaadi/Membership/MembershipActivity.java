package com.example.sid.marwadishaadi.Membership;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.Payment.activity.WebViewActivity;
import com.example.sid.marwadishaadi.Payment.utility.AvenuesParams;
import com.example.sid.marwadishaadi.Payment.utility.ServiceUtility;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.ContentValues.TAG;

//import org.apache.http.message.String;


public class MembershipActivity extends AppCompatActivity {

    public static Integer calculate;
    public static List<String> PackageInfos;
    //    c_code,c_type,c_discount_type,c_discount,c_used,active,c_allowed_number
    public static String pair_c_code, pair_c_type, pair_c_discount_type, pair_c_discount, pair_c_used, pair_active, pair_c_allowed_number;
    public static int counts = 0;
    static int c = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0;
    static TextView tvamount;
    static String goAway;
    public AlertDialog.Builder reset;
    public EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;
    RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10, radio11, radio12, radio13, radio14, radio15, radio16, radio17, radio18;
    RadioGroup gr1, gr2, gr3, gr4, gr5, gr6;
    LinearLayout linear1, linear2, linear3, linear4, linear5, linear6;
    int id;
    View mview;
    Button skip, coupon;
    String str;
    ProgressDialog dialog;
    TextView dash, clear1, clear2, clear3, clear4, clear5, clear6;
    private FirebaseAnalytics mFirebaseAnalytics;


    public static Integer getCalculate() {
        return calculate;
    }

    public static void setCalculate(Integer calculate) {
        MembershipActivity.calculate = calculate;
    }

    public static TextView getTvamount() {
        return tvamount;
    }

    public static void setTvamount(TextView tvamount) {
        MembershipActivity.tvamount = tvamount;
    }

    public static int getCounts() {
        return counts;
    }

    public static void setCounts(int counts) {
        MembershipActivity.counts = counts;
    }

    public static String getPair_c_code() {

        return pair_c_code;
    }

    public static void setPair_c_code(String pair_c_code) {
        MembershipActivity.pair_c_code = pair_c_code;
    }

    public static String getPair_c_type() {
        return pair_c_type;
    }

    public static void setPair_c_type(String pair_c_type) {
        MembershipActivity.pair_c_type = pair_c_type;
    }

    public static String getPair_c_discount_type() {
        return pair_c_discount_type;
    }

    public static void setPair_c_discount_type(String pair_c_discount_type) {
        MembershipActivity.pair_c_discount_type = pair_c_discount_type;
    }

    public static String getPair_c_discount() {
        return pair_c_discount;
    }

    public static void setPair_c_discount(String pair_c_discount) {
        MembershipActivity.pair_c_discount = pair_c_discount;
    }

    public static String getPair_c_used() {
        return pair_c_used;
    }

    public static void setPair_c_used(String pair_c_used) {
        MembershipActivity.pair_c_used = pair_c_used;
    }

    public static String getPair_active() {
        return pair_active;
    }

    public static void setPair_active(String pair_active) {
        MembershipActivity.pair_active = pair_active;
    }

    public static String getPair_c_allowed_number() {
        return pair_c_allowed_number;
    }

    public static void setPair_c_allowed_number(String pair_c_allowed_number) {
        MembershipActivity.pair_c_allowed_number = pair_c_allowed_number;
    }

    public EditText getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(EditText accessCode) {
        this.accessCode = accessCode;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        PackageInfos = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.membership_toolbar);
        toolbar.setTitle("Membership Status");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mview = getLayoutInflater().inflate(R.layout.activity_initial_screen, null);
        init();
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId.setText(randomNum.toString());
        skip = (Button) findViewById(R.id.skip);
        coupon = (Button) findViewById(R.id.coupon);

        c = 0;
        c1 = 0;
        c2 = 0;
        c3 = 0;
        c4 = 0;
        c5 = 0;
        c6 = 0;
        dash = (TextView) findViewById(R.id.dash);

        tvamount = (TextView) findViewById(R.id.amount);
        gr1 = (RadioGroup) findViewById(R.id.gr1);
        gr2 = (RadioGroup) findViewById(R.id.gr2);
        gr3 = (RadioGroup) findViewById(R.id.gr3);
        gr4 = (RadioGroup) findViewById(R.id.gr4);
        gr5 = (RadioGroup) findViewById(R.id.gr5);
        gr6 = (RadioGroup) findViewById(R.id.gr6);


        clear1 = (TextView) findViewById(R.id.g1reset);
        clear2 = (TextView) findViewById(R.id.g2reset);
        clear3 = (TextView) findViewById(R.id.g3reset);
        clear4 = (TextView) findViewById(R.id.g4reset);
        clear5 = (TextView) findViewById(R.id.g5reset);
        clear6 = (TextView) findViewById(R.id.g6reset);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MembershipActivity.this, DashboardActivity.class);
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Skip Membership", "button");
                startActivity(i);
            }
        });

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counts == 0) {
                    View reset_view = getLayoutInflater().inflate(R.layout.coupon_dialog, null);
                    reset = new AlertDialog.Builder(MembershipActivity.this);
                    reset.setView(reset_view);

                    final EditText code = (EditText) reset_view.findViewById(R.id.code);

                    Button redeem = (Button) reset_view.findViewById(R.id.redeem);


                    redeem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (counts == 0) {
                                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Coupon Code", "button");
                                if (tvamount.getText().toString().trim().equals("0")) {
                                    Toast.makeText(MembershipActivity.this, "Please select at least one package to redeem coupon" + getEmojiByUnicode(0x1F60E), Toast.LENGTH_SHORT).show();
                                } else {
                                    String user_code = code.getText().toString().trim();
                                    if (user_code.trim().equals("")) {
                                        Toast.makeText(MembershipActivity.this, "Please Enter coupon code" + getEmojiByUnicode(0x1F611), Toast.LENGTH_SHORT).show();
                                    } else {
                                        new BCKND().execute(user_code);
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Already Added one Coupon", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    AlertDialog resetbox = reset.create();
                    resetbox.show();

                } else {
//                    .makeText(getApplicationContext(),"Already Added one coupon code"+getEmojiByUnicode(0x1F636),.LENGTH_SHORT).show();
                    AlertDialog.Builder ald = new AlertDialog.Builder(view.getRootView().getContext());
                    ald.setTitle("Coupon Code details are");
                    String ad = "";
                    if (pair_c_discount_type.equals("Percent")) {
                        ad = "%";
                    } else {
                        ad = "Rs.";
                    }
                    //                                            +"coupon type : "+pair_c_type+"\n"
                    ald.setMessage("coupon code :" + pair_c_code + "\n" + "Discount Type : " + pair_c_discount_type + "\n" + "Discount : " + pair_c_discount + ad + "\n" + "Discount will added when go for payment");
                    AlertDialog resetbox = ald.create();
                    resetbox.show();
                }
            }

        });


        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gr1.clearCheck();
                c = c2 + c3 + c4 + c5;
                c1 = 0;
                tvamount.setText(String.valueOf(c));

            }
        });

        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr2.clearCheck();
                c = c1 + c3 + c4 + c5;
                c2 = 0;
                tvamount.setText(String.valueOf(c));
            }
        });

        clear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr3.clearCheck();
                c = c2 + c1 + c4 + c5;
                c3 = 0;
                tvamount.setText(String.valueOf(c));
            }
        });

        clear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr4.clearCheck();
                c = c2 + c3 + c1 + c5;
                c4 = 0;
                tvamount.setText(String.valueOf(c));
            }
        });

        clear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr5.clearCheck();
                c = c2 + c3 + c4 + c1;
                c5 = 0;
                tvamount.setText(String.valueOf(c));
            }
        });

        clear6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c = c6 = 0;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio1 = (RadioButton) findViewById(R.id.radio1);
        radio2 = (RadioButton) findViewById(R.id.radio2);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        radio5 = (RadioButton) findViewById(R.id.radio5);
        radio6 = (RadioButton) findViewById(R.id.radio6);
        radio7 = (RadioButton) findViewById(R.id.radio7);

        radio8 = (RadioButton) findViewById(R.id.radio8);
        radio9 = (RadioButton) findViewById(R.id.radio9);
        radio10 = (RadioButton) findViewById(R.id.radio10);
        radio11 = (RadioButton) findViewById(R.id.radio11);
        radio12 = (RadioButton) findViewById(R.id.radio12);
        radio13 = (RadioButton) findViewById(R.id.radio13);
        radio14 = (RadioButton) findViewById(R.id.radio14);
        radio15 = (RadioButton) findViewById(R.id.radio15);
        radio16 = (RadioButton) findViewById(R.id.radio16);
        radio17 = (RadioButton) findViewById(R.id.radio17);
        radio18 = (RadioButton) findViewById(R.id.radio18);

        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvamount.getText().toString().equals("0")) {
                    Toast.makeText(MembershipActivity.this, "Please select at least one plan " + getEmojiByUnicode(0x1F633), Toast.LENGTH_SHORT).show();
                } else {
                    if (counts == 1) {
                        tvamount.setText(Integer.toString(calculate));
                    }
                    Intent intent = new Intent(MembershipActivity.this, WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(tvamount.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
//
                    checker();

                    startActivity(intent);
                    finish();

                }

            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c1 = 1000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c1 = 2000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c1 = 2500;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c2 = 1000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c2 = 2000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c2 = 2500;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });


        radio7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c3 = 1000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c3 = 2000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });


        radio9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();


                c3 = 2500;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c4 = 1000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });
        radio11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c4 = 2000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));

            }
        });
        radio12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c4 = 2500;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });

        radio13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c5 = 1000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });
        radio14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c5 = 2000;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });
        radio15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c5 = 2500;
                c = c1 + c2 + c3 + c4 + c5;
                tvamount.setText(String.valueOf(c));
            }
        });
        radio16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gr1.clearCheck();
                gr2.clearCheck();
                gr3.clearCheck();
                gr4.clearCheck();
                gr5.clearCheck();
                c1 = c2 = c3 = c4 = c5 = 0;
                c6 = 5000;
                tvamount.setText(String.valueOf(c6));
            }
        });
        radio17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr1.clearCheck();
                gr2.clearCheck();
                gr3.clearCheck();
                gr4.clearCheck();
                gr5.clearCheck();
                c1 = c2 = c3 = c4 = c5 = 0;
                c6 = 10000;
                tvamount.setText(String.valueOf(c6));
            }
        });
        radio18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gr1.clearCheck();
                gr2.clearCheck();
                gr3.clearCheck();
                gr4.clearCheck();
                gr5.clearCheck();
                c1 = c2 = c3 = c4 = c5 = 0;
                c6 = 12500;
                tvamount.setText(String.valueOf(c6));
            }
        });

    }

    private void init() {
        accessCode = (EditText) mview.findViewById(R.id.accessCode);
        merchantId = (EditText) mview.findViewById(R.id.merchantId);
        orderId = (EditText) mview.findViewById(R.id.orderId);
        currency = (EditText) mview.findViewById(R.id.currency);
        rsaKeyUrl = (EditText) mview.findViewById(R.id.rsaUrl);
        redirectUrl = (EditText) mview.findViewById(R.id.redirectUrl);
        cancelUrl = (EditText) mview.findViewById(R.id.cancelUrl);
    }


    @Override
    public boolean onSupportNavigateUp() {
        counts = 0;
        finish();
        overridePendingTransition(R.anim.exit, 0);
        return true;
    }

    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    void checker() {
        if (radio1.isChecked()) {
            PackageInfos.add("Maheshwari");
            String str;
            PackageInfos.add("3months");
            PackageInfos.add("1000");
        }
        if (radio2.isChecked()) {
            PackageInfos.add("Maheshwari");
            PackageInfos.add("6months");
            PackageInfos.add("2000");
        }
        if (radio3.isChecked()) {
            PackageInfos.add("Maheshwari");
            PackageInfos.add("12months");
            PackageInfos.add("2500");
        }
        if (radio4.isChecked()) {
            PackageInfos.add("Agarwal");
            PackageInfos.add("3months");
            PackageInfos.add("1000");
        }
        if (radio5.isChecked()) {
            PackageInfos.add("Agarwal");
            PackageInfos.add("6months");
            PackageInfos.add("2000");
        }
        if (radio6.isChecked()) {
            PackageInfos.add("Agarwal");
            PackageInfos.add("12months");
            PackageInfos.add("2500");
        }
        if (radio7.isChecked()) {
            PackageInfos.add("Jain");
            PackageInfos.add("3months");
            PackageInfos.add("1000");
        }
        if (radio8.isChecked()) {
            PackageInfos.add("Jain");
            PackageInfos.add("6months");
            PackageInfos.add("2000");
        }
        if (radio9.isChecked()) {
            PackageInfos.add("Jain");
            PackageInfos.add("12months");
            PackageInfos.add("2500");
        }
        if (radio10.isChecked()) {
            PackageInfos.add("Khandelwal");
            PackageInfos.add("3months");
            PackageInfos.add("1000");
        }
        if (radio11.isChecked()) {
            PackageInfos.add("Khandelwal");
            PackageInfos.add("6months");
            PackageInfos.add("2000");
        }
        if (radio12.isChecked()) {
            PackageInfos.add("Khandelwal");
            PackageInfos.add("12months");
            PackageInfos.add("2500");
        }
        if (radio13.isChecked()) {
            PackageInfos.add("Other");
            PackageInfos.add("3months");
            PackageInfos.add("1000");
        }
        if (radio14.isChecked()) {
            PackageInfos.add("Other");
            PackageInfos.add("6months");
            PackageInfos.add("2000");
        }
        if (radio15.isChecked()) {
            PackageInfos.add("Other");
            PackageInfos.add("12months");
            PackageInfos.add("2500");
        }
        if (radio16.isChecked()) {
            PackageInfos.add("All");
            PackageInfos.add("3months");
            PackageInfos.add("5000");
        }
        if (radio17.isChecked()) {
            PackageInfos.add("All");
            PackageInfos.add("6months");
            PackageInfos.add("10000");
        }
        if (radio18.isChecked()) {
            PackageInfos.add("All");
            PackageInfos.add("12months");
            PackageInfos.add("12500");
        }
    }

    @Override
    public void onBackPressed() {
        counts = 0;
        super.onBackPressed();

    }

    class BCKND extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getApplicationContext());
            pd.setMessage("Checking coupon code");
            pd.setCanceledOnTouchOutside(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String query = "SELECT c_code,c_type,c_discount_type,c_discount,c_used,active,c_allowed_number FROM tbl_coupans where c_code=\"" + strings[0] + "\"";

            AndroidNetworking.post("http://10.0.0.21:5050/checkCoupon")
                    .addBodyParameter("query", query)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //query is SELECT c_code,c_type,c_discount_type,c_discount,c_used,active,c_allowed_number FROM `tbl_coupans` where c_code="coupon_code"

                            try {
                                if (response.getString(0).equals("no")) {
                                    Toast.makeText(MembershipActivity.this, "No coupon code found" + getEmojiByUnicode(0x1F60E), Toast.LENGTH_LONG).show();
                                    goAway = "NO";
                                } else {
                                    try {
                                        pair_c_code = response.get(0).toString();
                                        pair_c_type = response.get(1).toString();
                                        pair_c_discount_type = response.get(2).toString();
                                        pair_c_discount = response.get(3).toString();
                                        pair_c_used = response.get(4).toString();
                                        pair_active = response.get(5).toString();
                                        pair_c_allowed_number = response.get(6).toString();
                                        Integer allow = Integer.parseInt(pair_c_allowed_number), used = Integer.parseInt(pair_c_used), amnt = Integer.parseInt(pair_c_discount);
                                        if (pair_active.equals("Yes") & (allow > used | allow == 0)) {

                                                    /*AlertDialog.Builder ald = new AlertDialog.Builder(getApplicationContext());
                                                    ald.setTitle("Coupon Code details are");
                                                    String ad = "";
                                                    if (pair_c_discount_type.equals("Percent")) {
                                                        ad = "%";
                                                    }
    //                                            +"coupon type : "+pair_c_type+"\n"
                                                    ald.setMessage("coupon code :" + pair_c_code + "\n" + "Discount Type : " + pair_c_discount_type + "\n" + "Discount : " + pair_c_discount + ad + "\n" + "");
                                                    ald.show();*/
                                            calculate = Integer.parseInt(tvamount.getText().toString());
                                            if (pair_c_discount_type.equals("Amount")) {
                                                calculate = calculate - Integer.parseInt(pair_c_discount);
                                            } else if (pair_c_discount_type.equals("Percent")) {
                                                calculate = (calculate * (100 - Integer.parseInt(pair_c_discount))) / 100;
                                            }
                                            goAway = "GO";
                                            //tvamount.setText(Integer.toString(calculate));
                                            counts = 1;

                                            Toast.makeText(MembershipActivity.this, "Added coupon successfully Discount will added when go for payment " + getEmojiByUnicode(0x1F60A), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MembershipActivity.this, "This coupon code is no longer available" + getEmojiByUnicode(0x1F620), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(MembershipActivity.this, "Network Error " + getEmojiByUnicode(0x1F631), Toast.LENGTH_SHORT).show();

                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
//                    pd.dismiss();

            super.onPostExecute(s);

                    /*Intent intent = new Intent(MembershipActivity.this, WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(tvamount.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
//

                    startActivity(intent);
                    finish();*/
        }
    }
}
