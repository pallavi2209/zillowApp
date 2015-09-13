package com.example.zillowapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	public static final String TAG = MainActivity.class.getSimpleName();
	TextView tvNoResult;
	TextView tvStreetErr;
	TextView tvCityErr;
	TextView tvStateErr;
	Spinner mySpinner;
	
	TextWatcher streetfieldValidatorTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
    		String strStreet = etStreet.getText().toString();
    		if(strStreet.trim().matches("")){
    			tvStreetErr.setVisibility(View.VISIBLE);
    		}else{
    			tvStreetErr.setVisibility(View.GONE);
    		}
        }

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
    };
    
	TextWatcher cityfieldValidatorTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
    		String strCity = etCity.getText().toString();
    		if(strCity.trim().matches("")){
    			tvCityErr.setVisibility(View.VISIBLE);
    		}else{
    			tvCityErr.setVisibility(View.GONE);
    		}
        }

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
    };
    
    
    

    


	 EditText etStreet;
	private EditText etCity;	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "hello");
		setContentView(R.layout.activity_main);
tvNoResult=(TextView) findViewById(R.id.lblNoResult);
tvStreetErr=(TextView) findViewById(R.id.lblStreetErr);
tvCityErr=(TextView) findViewById(R.id.lblCityErr);
tvStateErr=(TextView) findViewById(R.id.lblStateErr);
etStreet = (EditText) findViewById(R.id.txtStreet);
etCity = (EditText) findViewById(R.id.txtCity);
mySpinner = (Spinner) findViewById(R.id.spinnerState);


		tvNoResult.setVisibility(View.GONE);
		tvStreetErr.setVisibility(View.GONE);
		tvCityErr.setVisibility(View.GONE);
		tvStateErr.setVisibility(View.GONE);
		etStreet.addTextChangedListener(streetfieldValidatorTextWatcher);
		etCity.addTextChangedListener(cityfieldValidatorTextWatcher);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				String strState = mySpinner.getSelectedItem().toString();
				if(strState.matches("Choose State")){
//					tvStateErr.setVisibility(View.VISIBLE);

				}else{
					tvStateErr.setVisibility(View.GONE);
				}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }

		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called when the user touches the button */
	public void onSubmitClick(View view) {
		tvStreetErr.setVisibility(View.GONE);
		tvCityErr.setVisibility(View.GONE);
		tvStateErr.setVisibility(View.GONE);
		tvNoResult.setVisibility(View.GONE);
		

		String strStreet = etStreet.getText().toString();
		boolean allCorrect=true;
		if(strStreet.matches("")){
			tvStreetErr.setVisibility(View.VISIBLE);
			allCorrect=false;
		}


		String strCity = etCity.getText().toString();
		if(strCity.matches("")){
			tvCityErr.setVisibility(View.VISIBLE);
			allCorrect=false;
		}


		String strState = mySpinner.getSelectedItem().toString();
		if(strState.matches("Choose State")){
			tvStateErr.setVisibility(View.VISIBLE);
			allCorrect=false;
		}else{
			tvStateErr.setVisibility(View.GONE);
		}
		if(allCorrect){
		getOutputFromURL(strStreet, strCity, strState);
		}
		// Intent intent = new Intent(this, ResultActivity.class);
		// // intent.putExtra(JSON_OBJECT, obj.toString());
		// Log.d(TAG, "startActivity");
		// startActivity(intent);
	}

	public static final String JSON_OBJECT = "jsonObject";

	private void parseJson(String resultData) {

		JSONObject obj;
		try {
			obj = new JSONObject(resultData);

			String isSuccess = obj.getString("success");
			if (isSuccess.equals("0")) {
				tvNoResult.setVisibility(View.VISIBLE);
				//add code here
			} else {
				Intent intent = new Intent(this, ResultActivity.class);
				intent.putExtra(JSON_OBJECT, obj.toString());
				Log.d(TAG, "startActivity");
				startActivity(intent);

				// JSONObject resultObj = obj.getJSONObject("result");
				// if(resultObj.has("homedetails")){
				// mapData.put("homedetails",
				// resultObj.getString("homedetails"));
				// }else{
				// mapData.put("homedetails", "N/A");
				// }
			}

			// Log.d(TAG, mapData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static String printKeyHash(Activity context) {
//		PackageInfo packageInfo;
//		String key = null;
//		try {
//
//			//getting application package name, as defined in manifest
//			String packageName = context.getApplicationContext().getPackageName();
//
//			//Retriving package info
//			packageInfo = context.getPackageManager().getPackageInfo(packageName,
//					PackageManager.GET_SIGNATURES);
//			
//			Log.e("Package Name=", context.getApplicationContext().getPackageName());
//			
//			for (Signature signature : packageInfo.signatures) {
//				MessageDigest md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				key = new String(Base64.encode(md.digest(), 0));
//			
//				// String key = new String(Base64.encodeBytes(md.digest()));
//				Log.e("Key Hash=", key);
//
//			}
//		} catch (NameNotFoundException e1) {
//			Log.e("Name not found", e1.toString());
//		}
//
//		catch (NoSuchAlgorithmException e) {
//			Log.e("No such an algorithm", e.toString());
//		} catch (Exception e) {
//			Log.e("Exception", e.toString());
//		}
//
//		return key;
//	}

	private void getOutputFromURL(String strStreet, String strCity,
			String strState) {

		class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... params) {

				String paramStreet = params[0];
				String paramCity = params[1];
				String paramState = params[2];
				// Log.d(TAG, "*** doInBackground ** paramStreet " + paramStreet
				// + " paramCity :" + paramCity);

				HttpClient httpClient = new DefaultHttpClient();
//				HttpPost httpPost = new HttpPost(
//						"http://default-environment-jpejxmkkxq.elasticbeanstalk.com/?street=2636+Menlo+Ave&city=Los+Angeles&state=CA");
				HttpPost httpPost = new HttpPost(
						"http://zillow-app.elasticbeanstalk.com/");

				BasicNameValuePair streetBasicNameValuePair = new BasicNameValuePair(
						"street", paramStreet);
				BasicNameValuePair cityBasicNameValuePAir = new BasicNameValuePair(
						"city", paramCity);
				BasicNameValuePair stateBasicNameValuePAir = new BasicNameValuePair(
						"state", paramState);

				List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
				nameValuePairList.add(streetBasicNameValuePair);
				nameValuePairList.add(cityBasicNameValuePAir);
				nameValuePairList.add(stateBasicNameValuePAir);

				try {
					UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
							nameValuePairList, HTTP.UTF_8);

					httpPost.setEntity(urlEncodedFormEntity);
					httpPost.addHeader("Content-Type",
							"application/x-www-form-urlencoded");

					try {
						// Log.d(TAG,
						// "before response"+urlEncodedFormEntity.toString());
						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						// Log.d(TAG, "got response");

						InputStream inputStream = httpResponse.getEntity()
								.getContent();
						InputStreamReader inputStreamReader = new InputStreamReader(
								inputStream);
						BufferedReader bufferedReader = new BufferedReader(
								inputStreamReader);

						StringBuilder stringBuilder = new StringBuilder();
						String bufferedStrChunk = null;

						while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
							stringBuilder.append(bufferedStrChunk);
						}
						Log.d(TAG, stringBuilder.toString());
						return stringBuilder.toString();

					} catch (ClientProtocolException cpe) {
						// Log.d(TAG, "First Exception caz of HttpResponese :" +
						// cpe);
						// System.out.println("First Exception caz of HttpResponese :"
						// + cpe);
						cpe.printStackTrace();
					} catch (IOException ioe) {
						// Log.d(TAG, "Second Exception caz of HttpResponse :" +
						// ioe);
						// System.out.println("Second Exception caz of HttpResponse :"
						// + ioe);
						ioe.printStackTrace();
					}

				} catch (UnsupportedEncodingException uee) {
					// Log.d(TAG,
					// "An Exception given because of UrlEncodedFormEntity argument :"
					// + uee);
					// System.out.println();
					uee.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String resultData) {
				super.onPostExecute(resultData);

				parseJson(resultData);

				// Log.d(TAG, result);
				// if(result.equals("working")){
				// Toast.makeText(getApplicationContext(),
				// "HTTP POST is working...", Toast.LENGTH_LONG).show();
				// }else{
				// Toast.makeText(getApplicationContext(),
				// "Invalid POST req...", Toast.LENGTH_LONG).show();
				// }
			}

		}

		SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
		sendPostReqAsyncTask.execute(strStreet, strCity, strState);
	}
}
