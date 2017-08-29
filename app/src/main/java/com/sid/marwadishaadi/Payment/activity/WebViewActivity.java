package com.sid.marwadishaadi.Payment.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.sid.marwadishaadi.Membership.MembershipActivity;
import com.sid.marwadishaadi.Payment.utility.AvenuesParams;
import com.sid.marwadishaadi.Payment.utility.Constants;
import com.sid.marwadishaadi.Payment.utility.RSAUtility;
import com.sid.marwadishaadi.Payment.utility.ServiceHandler;
import com.sid.marwadishaadi.Payment.utility.ServiceUtility;
import com.sid.marwadishaadi.R;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.sid.marwadishaadi.Membership.MembershipActivity.PackageInfos;

public class WebViewActivity extends Activity {
	private ProgressDialog dialog;
	Intent mainIntent;
	static  String html, encVal="";
	protected ServiceHandler sh;
	private String customer_id;


	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_webview);
		SharedPreferences sharedpref = getSharedPreferences("userinfo", MODE_PRIVATE);
		customer_id = sharedpref.getString("customer_id", null);
		mainIntent = getIntent();

		// Calling async task to get display content
		sh = new ServiceHandler();
		new RenderView().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class RenderView extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			dialog = new ProgressDialog(WebViewActivity.this);
			dialog.setMessage("Please wait...");
			dialog.setCancelable(false);
			dialog.show();
			String query="";
			int size=PackageInfos.size();
			Calendar c= Calendar.getInstance();
//			.get(Calendar.DATE)
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = df.format(c.getTime());
			String s2;
			if(size==3)
			{
				Calendar c1= Calendar.getInstance();

				if(PackageInfos.get(0).equals("All"))
				{
					if(PackageInfos.get(1).equals("3months"))
					{
						c1.add(Calendar.DATE,90);
					}else if(PackageInfos.get(1).equals("6months")){
						c1.add(Calendar.DATE,180);
					}else if(PackageInfos.get(1).equals("12months")){
						c1.add(Calendar.DATE,365);
					}
					s2=df.format(c1.getTime());
					query+=" update tbl_user_community_package set duration=\""+PackageInfos.get(1)+"\" , purchase_date=\""+formattedDate+"\" , expiry_date=\""+s2+"\", is_active=\"yes\" where ";
					query+=" customer_no=\""+customer_id+"\";";

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

						query+=" update tbl_user_community_package set duration=\""+PackageInfos.get(i+1)+"\" , purchase_date=\""+formattedDate+"\" , expiry_date=\""+s2+"\" , is_active=\"yes\" where ";
						query+=" customer_no=\""+customer_id+"\" and community=\""+PackageInfos.get(i)+"\" ;";
					}
				}
			}
			else {
				Calendar c1= Calendar.getInstance();
				for (int i = 0; i < size; i += 3) {
					if (PackageInfos.get(i + 1).equals("3months")) {
						c1.add(Calendar.DATE, 90);
					} else if (PackageInfos.get(i + 1).equals("6months")) {
						c1.add(Calendar.DATE, 180);
					} else if (PackageInfos.get(i + 1).equals("12months")) {
						c1.add(Calendar.DATE, 365);
					}
					s2 = df.format(c1.getTime());
					query += " update tbl_user_community_package set duration=\"" + PackageInfos.get(i+1) + "\" , purchase_date=\"" + formattedDate + "\" , expiry_date=\"" + s2 + "\", is_active=\"yes\" where ";
					query += " customer_no=\"" + customer_id + "\" and community=\"" + PackageInfos.get(i) + "\";";
				}
			}

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance


			// Making a request to url and getting response
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

			String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
			if(!ServiceUtility.chkNull(vResponse).equals("")
					&& ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
				StringBuffer vEncVal = new StringBuffer("");
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
				vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
				encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (dialog.isShowing())
				dialog.dismiss();

			@SuppressWarnings("unused")
			class MyJavaScriptInterface
			{
				@JavascriptInterface
				public void processHTML(String html)
				{

					// process the html as needed by the app

					String status = null;
					if(html.indexOf("Failure")!=-1){
						status = "Transaction Declined!";
					}else if(html.indexOf("Success")!=-1){
						status = "Transaction Successful!";
					}else if(html.indexOf("Aborted")!=-1){
						status = "Transaction Cancelled!";
					}else{
						status = "Status Not Known!";
					}
					//.makeText(getApplicationContext(), status, .LENGTH_SHORT).show();
					Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
					intent.putExtra("transStatus", status);
					startActivity(intent);
				}
			}
			final WebView webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
			webview.setClickable(true);
			webview.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(webview, url);
					if(url.indexOf("/ccavResponseHandler.php")!=-1){

						webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

					}
				}
				@Override
				public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
					Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);

				}
			});

			/* An instance of this class will be registered as a JavaScript interface */
			StringBuffer params = new StringBuffer();
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

			params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));

			params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL,URLEncoder.encode(encVal)));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_NAME,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_EMAIL,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_ADDRESS,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_CITY,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_STATE,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_COUNTRY,""));
			params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CUST_MOBILE,""));

			String vPostParams = params.substring(0,params.length()-1);
			try {


				params.setLength(0);

				webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
			} catch (Exception e) {
				showToast("Exception occured while opening webview.");
			}
		}
	}

	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		final AlertDialog.Builder ald=new AlertDialog.Builder(this);
		ald.setTitle("Alert"+getEmojiByUnicode(0xFE1644));
		ald.setMessage("Are you sure for cancel payment");
		ald.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				MembershipActivity ma=new MembershipActivity();
				ma.setCounts(0);
				Intent intnt=new Intent(getApplicationContext(),DashboardActivity.class);
				startActivity(intnt);
			}
		});
		ald.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		AlertDialog altr=ald.create();
		altr.show();
	}
	public String getEmojiByUnicode(int unicode){
		return new String(Character.toChars(unicode));
	}
}
