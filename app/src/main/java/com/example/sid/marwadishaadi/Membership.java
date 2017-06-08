package com.example.sid.marwadishaadi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class Membership extends AppCompatActivity {
    RadioButton radio1, radio2, radio3, radio4, radio5, radio6, radio7, radio8, radio9, radio10, radio11, radio12, radio13, radio14, radio15, radio16, radio17, radio18;
    RadioGroup gr1, gr2, gr3, gr4, gr5, gr6;
    LinearLayout linear1,linear2,linear3,linear4,linear5,linear6;
    static int c = 0, c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0;
    String str;
    TextView dash;
    TextView amount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        c = 0; c1 = 0; c2 = 0; c3 = 0; c4 = 0; c5 = 0; c6 = 0;
        dash = (TextView) findViewById(R.id.dash);

        amount = (TextView) findViewById(R.id.amount);
        gr1 = (RadioGroup) findViewById(R.id.gr1);
        gr2 = (RadioGroup) findViewById(R.id.gr2);
        gr3 = (RadioGroup) findViewById(R.id.gr3);
        gr4 = (RadioGroup) findViewById(R.id.gr4);
        gr5 = (RadioGroup) findViewById(R.id.gr5);
        gr6 = (RadioGroup) findViewById(R.id.gr6);


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
                Intent i = new Intent(Membership.this,Dashboard.class);
                startActivity(i);
            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c1=1000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c1=2000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c1=2500;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c2=1000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c2=2000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c2=2500;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });


        radio7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c3=1000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c3=2000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });


        radio9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();


                c3=2500;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c4=1000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });
        radio11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();
                c4=2000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));

            }
        });
        radio12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c4=2500;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });

        radio13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c5=1000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });
        radio14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c5=2000;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
            }
        });
        radio15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr6.clearCheck();

                c1=2500;
                c = c1 + c2 + c3 + c4 + c5;
                amount.setText(String.valueOf(c));
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
                c1=c2=c3=c4=c5=0;
                c6=5000;
                amount.setText(String.valueOf(c6));
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
                c1=c2=c3=c4=c5=0;
                c6=10000;
                amount.setText(String.valueOf(c6));
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
                c1=c2=c3=c4=c5=0;
                c6=12500;
                amount.setText(String.valueOf(c6));
            }
        });

    }
}
