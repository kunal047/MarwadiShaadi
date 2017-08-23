package com.sid.marwadishaadi.Payment.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.Login.LoginActivity;
import com.sid.marwadishaadi.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.sid.marwadishaadi.Membership.MembershipActivity.PackageInfos;


public class StatusActivity extends Activity {
	private String query="";
	private boolean logout =true;
	private int i = 0;
	int count=0;
	private List<String> ListOfQueries;
	private String customer_id;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_status);

		SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
		customer_id = sharedpref.getString("customer_id", null);

		Intent mainIntent = getIntent();
		ListOfQueries=new ArrayList<String>();
//		TextView tv4 = (TextView) findViewById(R.id.textView1);
//		tv4.setText(mainIntent.getStringExtra("transStatus"));
		AlertDialog.Builder ad=new AlertDialog.Builder(this);
		ad.setTitle("Transaction Status");
		String stat=mainIntent.getStringExtra("transStatus");
		//String stat="Transaction Successful!";
		if(stat.contains("Transaction Successful!")){
			ad.setMessage("Now you are a Premium Member and you can see your membership details at Membership ");
			int size=PackageInfos.size();
			Calendar c= Calendar.getInstance();
//			.get(Calendar.DATE)
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = df.format(c.getTime());
			String s2;
			if(size==3)
			{
				Calendar c1= Calendar.getInstance();

				if(PackageInfos.get(0).equals("All")) {
					if (PackageInfos.get(1).equals("3months")) {
						c1.add(Calendar.DATE, 90);
					} else if (PackageInfos.get(1).equals("6months")) {
						c1.add(Calendar.DATE, 180);
					} else if (PackageInfos.get(1).equals("12months")) {
						c1.add(Calendar.DATE, 365);
					}
					s2 = df.format(c1.getTime());
					query = " update tbl_user_community_package set duration=\"" + PackageInfos.get(1) + "\" , purchase_date=\"" + formattedDate + "\" , expiry_date=\"" + s2 + "\", is_active=\"yes\" where ";
					query += " customer_no=\"" + customer_id + "\";";
					new UPDATE().execute(query,"1");
				}
				else{
					for(int i=0;i<size;i+=3)
					{

						if(PackageInfos.get(i+1).equals("3months"))
						{
							c1.add(Calendar.DATE,90);
						}else if(PackageInfos.get(i+1).equals("6months")){
							c1.add(Calendar.DATE,180);
						}else if(PackageInfos.get(i+1).equals("12months")){
							c1.add(Calendar.DATE,365);
						}
						s2=df.format(c1.getTime());

						query=" update tbl_user_community_package set duration=\""+PackageInfos.get(i+1)+"\" , purchase_date=\""+formattedDate+"\" , expiry_date=\""+s2+"\" , is_active=\"yes\" where ";
						query+=" customer_no=\""+customer_id+"\" and community=\""+PackageInfos.get(i)+"\" ;";
						new UPDATE().execute(query,"1");
					}
				}
			}
			else {
				Calendar c1= Calendar.getInstance();
				for (; i < size; i += 3) {
					if (PackageInfos.get(i + 1).equals("3months")) {
						c1.add(Calendar.DATE, 90);
					} else if (PackageInfos.get(i + 1).equals("6months")) {
						c1.add(Calendar.DATE, 180);
					} else if (PackageInfos.get(i + 1).equals("12months")) {
						c1.add(Calendar.DATE, 365);
					}
					s2 = df.format(c1.getTime());
					query = " update tbl_user_community_package set duration=\"" + PackageInfos.get(i+1) + "\" , purchase_date=\"" + formattedDate + "\" , expiry_date=\"" + s2 + "\", is_active=\"yes\" where ";
					query += " customer_no=\"" + customer_id + "\" and community=\"" + PackageInfos.get(i) + "\";";
					ListOfQueries.add(Integer.toString(i));
				}
				i=0;
				logout=true;
				final ScheduledExecutorService scheduleTaskExecute = new ScheduledThreadPoolExecutor(5);
				scheduleTaskExecute.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						if(logout) {
							if (count < ListOfQueries.size()) {
								StatusActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {

										logout=false;
										new UPDATE().execute(ListOfQueries.get(count), Integer.toString(i));
										count++;
										i += 3;
									}
								});
							} else {
								logout=true;
								scheduleTaskExecute.shutdown();
							}
						}else {

						}
					}
				},1,5, TimeUnit.SECONDS);
			}


		}else if(stat.contains("Transaction Declined!")){
			ad.setMessage("Your payment was declined.");
		}else if(stat.contains("Transaction Cancelled!")){
			ad.setMessage("Somehow payment get cancelled, No charge taken");
		}else if(stat.contains("Status Not Known!")){
			ad.setMessage("Something went wrong, Payment status is not updated. If your account is charged then we will give membership.");
		}else {
			ad.setMessage("No Payment happen, Please try Again");
		}

//		AlertDialog alrt= ad.create();
//		alrt.show();
//		alrt.setOnDismissListener(new DialogInterface.OnDismissListener() {
//			@Override
//			public void onDismiss(DialogInterface dialogInterface) {
//				Intent i=new Intent(StatusActivity.this, DashboardActivity.class);
//				startActivity(i);
//				dialogInterface.dismiss();
//			}
//		});
	}

	public void showToast(String msg) {
		Toast.makeText(this, "Toast: -----------" + msg, Toast.LENGTH_LONG).show();
	}
	private class UPDATE extends AsyncTask<String,String,String>
	{
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
				pd=new ProgressDialog(StatusActivity.this);
				pd.setMessage("Wait for a second..");
				pd.setTitle("Updating Membership");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			if(!pd.isShowing())
			{pd.show();}

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String s) {
			logout=true;

			super.onPostExecute(s);
		}

		@Override
		protected String doInBackground(final String... strings){
			if(strings[1].contains("1")) {
				AndroidNetworking.post("http://208.91.199.50:5000/updateMembership")
						.addBodyParameter("query", strings[0])
						.setPriority(Priority.HIGH)
						.build()
						.getAsString(new StringRequestListener() {
							@Override
							public void onResponse(String response) {
									Toast.makeText(StatusActivity.this, "Successfully Added as Premium Member", Toast.LENGTH_SHORT).show();
									StatusActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											LoginManager.getInstance().logOut();
											AccessToken.setCurrentAccessToken(null);
											SharedPreferences sharedPre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
											SharedPreferences.Editor editor = sharedPre.edit();
											editor.putBoolean("isLoggedIn", false);
											editor.putString("email", "");
											editor.putString("password", "");
											editor.putString("customer_id", "");
											editor.apply();
											Intent i = new Intent(getApplicationContext(), LoginActivity.class);
											startActivity(i);
										}
									});
							}

							@Override
							public void onError(ANError anError) {
								StatusActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										pd.dismiss();
									}
								});
								Toast.makeText(StatusActivity.this, "Error in updating status", Toast.LENGTH_SHORT).show();
							}
						});
			}else {

				AndroidNetworking.post("http://208.91.199.50:5000/updateMembership")
						.addBodyParameter("query", strings[0])
						.setPriority(Priority.HIGH)
						.build()
						.getAsString(new StringRequestListener() {
							@Override
							public void onResponse(String response) {

								if(i==PackageInfos.size()-3) {
									Toast.makeText(StatusActivity.this, "Successfully Added as Premium Member", Toast.LENGTH_SHORT).show();
									StatusActivity.this.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											pd.dismiss();
											LoginManager.getInstance().logOut();
											AccessToken.setCurrentAccessToken(null);
											SharedPreferences sharedPre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
											SharedPreferences.Editor editor = sharedPre.edit();
											editor.putBoolean("isLoggedIn", false);
											editor.putString("email", "");
											editor.putString("password", "");
											editor.putString("customer_id", "");
											editor.apply();
											Intent i = new Intent(getApplicationContext(), LoginActivity.class);
											startActivity(i);
										}
									});
								}
							}

							@Override
							public void onError(ANError anError) {
								StatusActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										pd.dismiss();
									}
								});
								Toast.makeText(StatusActivity.this, "Error in updating status", Toast.LENGTH_SHORT).show();
							}
						});

			}
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		Intent main=new Intent(getApplicationContext(),DashboardActivity.class);
		startActivity(main);
	}
} 
