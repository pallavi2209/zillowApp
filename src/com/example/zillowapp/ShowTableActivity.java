package com.example.zillowapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class ShowTableActivity extends FragmentActivity{

	Activity activity = null;
	boolean isFbShare = false;
	String jsonString = "";
	public static final String TAG = MainActivity.class.getSimpleName();
	final Context context = this;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);

	}

	private Session.StatusCallback callback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			Log.d(TAG,
					"In session callback " + isFbShare + " " + state.isOpened());
			if (state.isOpened() && isFbShare) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set title
				alertDialogBuilder.setTitle("Post to Facebook");

				// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								})
						.setPositiveButton("Post Property Details",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											publishFeedDialog(jsonString);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_table);
		
		TextView footer2 = (TextView) findViewById(R.id.zillowFooter2);
		footer2.setMovementMethod(LinkMovementMethod.getInstance());
	    
	    TextView footer3 = (TextView) findViewById(R.id.zillowFooter3);
	    footer3.setMovementMethod(LinkMovementMethod.getInstance());

		Intent intent = getIntent();
		jsonString = intent.getStringExtra(ResultActivity.JSON_STRING);
		Button publishButton = (Button) findViewById(R.id.btnFB);

		publishButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.d(TAG, "share clicked");
				if (Session.getActiveSession() == null
						|| !Session.getActiveSession().isOpened()) {
					isFbShare = true;
					Session.openActiveSession(ShowTableActivity.this, true,
							callback);
				} else {

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							context);

					// set title
					alertDialogBuilder.setTitle("Post to Facebook");

					// set dialog message
					alertDialogBuilder
							.setCancelable(false)
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									})
							.setPositiveButton("Post Property Details",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											try {
												publishFeedDialog(jsonString);
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}
			}
		});


		activity = (Activity) this;

		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			JSONObject resultObj = jsonObj.getJSONObject("result");

			TextView tvHeader = (TextView) findViewById(R.id.tvHeader);
			tvHeader.setText("See more details on Zillow:");

			TextView tvPropertyTypeVal = (TextView) findViewById(R.id.tvPropertyTypeVal);
			tvPropertyTypeVal.setText(resultObj.getString("useCode"));

			TextView tvHomeDetails = (TextView) findViewById(R.id.tvHomeDetails);
			tvHomeDetails.setMovementMethod(LinkMovementMethod.getInstance());
			String strAddress = resultObj.getString("street") + ", "
					+ resultObj.getString("city") + ", "
					+ resultObj.getString("state") + "-"
					+ resultObj.getString("zipcode");
			String strLink = resultObj.getString("homedetails");
			String strHTML = "<a href=\"" + strLink + "\">" + strAddress
					+ "</a>";
			tvHomeDetails.setText(Html.fromHtml(strHTML));

			TextView tvyearBuiltVal = (TextView) findViewById(R.id.tvyearBuiltVal);
			tvyearBuiltVal.setText(resultObj.getString("yearBuilt"));

			TextView tvlotSizeSqFtVal = (TextView) findViewById(R.id.tvlotSizeSqFtVal);
			tvlotSizeSqFtVal.setText(resultObj.getString("lotSizeSqFt"));

			TextView tvfinishedSqFtVal = (TextView) findViewById(R.id.tvfinishedSqFtVal);
			tvfinishedSqFtVal.setText(resultObj.getString("finishedSqFt"));

			TextView tvbathroomVal = (TextView) findViewById(R.id.tvbathroomVal);
			tvbathroomVal.setText(resultObj.getString("bathrooms"));

			TextView tvbedroomsVal = (TextView) findViewById(R.id.tvbedroomsVal);
			tvbedroomsVal.setText(resultObj.getString("bedrooms"));

			TextView tvtaxAssessmentYearVal = (TextView) findViewById(R.id.tvtaxAssessmentYearVal);
			tvtaxAssessmentYearVal.setText(resultObj
					.getString("taxAssessmentYear"));

			TextView tvtaxAssessmentVal = (TextView) findViewById(R.id.tvtaxAssessmentVal);
			tvtaxAssessmentVal.setText(resultObj.getString("taxAssessment"));

			TextView tvlastSoldPriceVal = (TextView) findViewById(R.id.tvlastSoldPriceVal);
			tvlastSoldPriceVal.setText(resultObj.getString("lastSoldPrice"));

			TextView tvlastSoldDateVal = (TextView) findViewById(R.id.tvlastSoldDateVal);
			tvlastSoldDateVal.setText(resultObj.getString("lastSoldDate"));

			TextView tvestimateLastUpdate = (TextView) findViewById(R.id.tvestimateLastUpdate);
			tvestimateLastUpdate
					.setText(Html
							.fromHtml("Zestimate<sup>&reg;</sup> Property Estimate as of ")
							+ resultObj.getString("estimateLastUpdate"));

			TextView tvestimateAmount = (TextView) findViewById(R.id.tvestimateAmount);
			tvestimateAmount.setText(resultObj.getString("estimateAmount"));

			TextView tvestimateValueChangeVal = (TextView) findViewById(R.id.tvestimateValueChangeVal);
//			ImageView imgEstChange = (ImageView) findViewById(R.id.imageViewEstChange);
			if (resultObj.getString("estimateValueChangeSign").equals("+")) {
				tvestimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_g, 0, 0, 0);
//				imgEstChange.setImageResource(R.drawable.up_g);
			} else if (resultObj.getString("estimateValueChangeSign").equals(
					"-")) {
				tvestimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_r, 0, 0, 0);
//				imgEstChange.setImageResource(R.drawable.down_r);
			}

			
			tvestimateValueChangeVal.setText(resultObj
					.getString("estimateValueChange"));

			TextView tvestimateValuationRangeVal = (TextView) findViewById(R.id.tvestimateValuationRangeVal);
			tvestimateValuationRangeVal.setText(resultObj
					.getString("estimateValuationRangeLow")
					+ "-"
					+ resultObj.getString("estimateValuationRangeHigh"));

			TextView tvrestimateLastUpdate = (TextView) findViewById(R.id.tvrestimateLastUpdate);
			tvrestimateLastUpdate
					.setText(Html
							.fromHtml("Zestimate<sup>&reg;</sup> Property Estimate as of ")
							+ resultObj.getString("restimateLastUpdate"));

			TextView tvrestimateAmount = (TextView) findViewById(R.id.tvrestimateAmount);
			tvrestimateAmount.setText(resultObj.getString("restimateAmount"));

//			Drawable up_img=getResources().getDrawable(R.drawable.up_g);
			TextView tvrestimateValueChangeVal = (TextView) findViewById(R.id.tvrestimateValueChangeVal);
//			ImageView imgREstChange = (ImageView) findViewById(R.id.imageViewREstChange);
			if (resultObj.getString("restimateValueChangeSign").equals("+")) {
				tvrestimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.up_g, 0, 0, 0);
//				imgREstChange.setImageResource(R.drawable.up_g);
			} else if (resultObj.getString("restimateValueChangeSign").equals(
					"-")) {
				tvrestimateValueChangeVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_r, 0, 0, 0);
//				imgREstChange.setImageResource(R.drawable.down_r);
			}

			
			tvrestimateValueChangeVal.setText(resultObj
					.getString("restimateValueChange"));

			TextView tvrestimateValuationRangeVal = (TextView) findViewById(R.id.tvrestimateValuationRangeVal);
			tvrestimateValuationRangeVal.setText(resultObj
					.getString("restimateValuationRangeLow")
					+ "-"
					+ resultObj.getString("restimateValuationRangeHigh"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_table, menu);
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

	private void publishFeedDialog(String json_string) throws JSONException {
		JSONObject jsonObj = new JSONObject(json_string);
		JSONObject resultObj = jsonObj.getJSONObject("result");

		JSONObject chartsObj = jsonObj.getJSONObject("chart");
		String chart1url = chartsObj.getJSONObject("year1").getString("url");
		String strAddress = resultObj.getString("street") + ", "
				+ resultObj.getString("city") + ", "
				+ resultObj.getString("state") + "-"
				+ resultObj.getString("zipcode");
		String desc = "Last Sold Price: "
				+ resultObj.getString("lastSoldPrice")
				+ ", 30 Days Overall Change: "
				+ resultObj.getString("estimateValueChangeSign")
				+ resultObj.getString("estimateValueChange");
		String strLink = resultObj.getString("homedetails");
		Bundle params = new Bundle();
		params.putString("name", strAddress);
		params.putString("caption", "Property Information from Zillow.com");
		params.putString("description", desc);
		params.putString("link", strLink);
		params.putString("picture", chart1url);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(),
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getActivity(),
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(
										getActivity().getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(
									getActivity().getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

	private Context getActivity() {
		// TODO Auto-generated method stub
		return ShowTableActivity.this;
	}
}
