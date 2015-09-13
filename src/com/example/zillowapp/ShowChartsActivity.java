package com.example.zillowapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class ShowChartsActivity extends ActionBarActivity {

	private ImageSwitcher imageSwitcher;
	 private TextSwitcher mSwitcher1;
	 private TextSwitcher mSwitcher2;
	// Array of Image Bitmaps to Show In ImageSwitcher
	ArrayList<Bitmap> imgBitmapArray = new ArrayList<Bitmap>();
	String textToShow[]={"Historical Zestimate for the past 1 year","Historical Zestimate for the past 5 years","Historical Zestimate for the past 10 years"};
	String strAddress="";
	int messageCount = 3;
	// to keep current Index of ImageID array
	int currentIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_charts);

		
		TextView footer2 = (TextView) findViewById(R.id.zillowFooter21);
		footer2.setMovementMethod(LinkMovementMethod.getInstance());
	    
	    TextView footer3 = (TextView) findViewById(R.id.zillowFooter31);
	    footer3.setMovementMethod(LinkMovementMethod.getInstance());

		
		Intent intent = getIntent();
		String jsonString = intent.getStringExtra(ResultActivity.JSON_STRING);
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			JSONObject resultObj = jsonObj.getJSONObject("result");
			JSONObject chartsObj = jsonObj.getJSONObject("chart");
			String chart1url = chartsObj.getJSONObject("year1").getString("url");
			String chart5url = chartsObj.getJSONObject("year5")
					.getString("url");
			String chart10url = chartsObj.getJSONObject("year10")
					.getString("url");

			strAddress = resultObj.getString("street")+", "+resultObj.getString("city")+", "+resultObj.getString("state")+"-"+resultObj.getString("zipcode");
			DownloadImagesTask sendDownloadAsyncTask = new DownloadImagesTask();
			sendDownloadAsyncTask.execute(chart1url, chart5url, chart10url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_charts, menu);
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
	

	private void setImagesAfterDownload(ArrayList<File> result) {
		// get The references

		Button btnPrev = (Button) findViewById(R.id.buttonPrev);
		Button btnNext = (Button) findViewById(R.id.buttonNext);
		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		mSwitcher1 = (TextSwitcher) findViewById(R.id.textSwitcher1);
		mSwitcher2 = (TextSwitcher) findViewById(R.id.textSwitcher2);

		for (int i = 0; i < result.size(); i++) {
			Bitmap bitmap = BitmapFactory.decodeFile(result.get(i).toString());
			imgBitmapArray.add(bitmap);
		}


		// Set the ViewFactory of the ImageSwitcher that will create ImageView
		// object when asked
		imageSwitcher.setFactory(new ViewFactory() {
			public View makeView() {
				// TODO Auto-generated method stub

				// Create a new ImageView set it's properties
				ImageView imageView = new ImageView(getApplicationContext());
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				return imageView;
			}
		});
		
		// Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mSwitcher1.setFactory(new ViewFactory() {
            
            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(ShowChartsActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(20);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });

        mSwitcher2.setFactory(new ViewFactory() {
            
            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(ShowChartsActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(16);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });
//		// Declare the animations and initialize them
//		Animation in = AnimationUtils.loadAnimation(this,
//				android.R.anim.slide_in_left);
//		Animation out = AnimationUtils.loadAnimation(this,
//				android.R.anim.slide_out_right);
//
//		// set the animation type to imageSwitcher
//		imageSwitcher.setInAnimation(in);
//		imageSwitcher.setOutAnimation(out);

		
		imageSwitcher.setImageDrawable(new BitmapDrawable(imgBitmapArray.get(0)));
		mSwitcher1.setText(textToShow[0]);
		mSwitcher2.setText(strAddress);
		currentIndex++;
		
		// ClickListener for NEXT button
		// When clicked on Button ImageSwitcher will switch between Images
		// The current Image will go OUT and next Image will come in with
		// specified animation
		btnNext.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentIndex++;
				// If index reaches maximum reset it
				if (currentIndex >= messageCount){
					currentIndex=messageCount-1;
				}else{
				imageSwitcher.setImageDrawable(new BitmapDrawable(imgBitmapArray.get(currentIndex)));
				mSwitcher1.setText(textToShow[currentIndex]);
				mSwitcher2.setText(strAddress);
				
				}
			}
		});
		btnPrev.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentIndex--;
				// If index reaches maximum reset it
				if (currentIndex<0){
					currentIndex=0;
				}else{
				imageSwitcher.setImageDrawable(new BitmapDrawable(imgBitmapArray.get(currentIndex)));
				mSwitcher1.setText(textToShow[currentIndex]);
				mSwitcher2.setText(strAddress);
				}
			}
		});
		
	}

	public class DownloadImagesTask extends AsyncTask<String, Void, ArrayList<File>> {

		@Override
		protected ArrayList<File> doInBackground(String... strURL) {
			String url1=strURL[0];
			String url2=strURL[1];
			String url3=strURL[2];
			
			ArrayList<File> arrayImgFile = new ArrayList<File>();
			if(url1!=""){
				arrayImgFile.add(download_Image(url1,"year1.gif"));
			}
			if(url2!=""){
				arrayImgFile.add(download_Image(url2,"year5.gif"));
			}
			if(url3!=""){
				arrayImgFile.add(download_Image(url3,"year10.gif"));
			}
			return arrayImgFile;
		}

		@Override
		protected void onPostExecute(ArrayList<File> result) {
			setImagesAfterDownload(result);
			
		}


		private File download_Image(String imageUrl, String fileName) {
			File file = null;
			try {
				// set the download URL, a url that points to a file on the
				// internet
				// this is the file to be downloaded
				URL url = new URL(imageUrl);
				Log.d("INFORMATION..", "FILE FOUNDED....");
				// create the new connection
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();

				// set up some things on the connection
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);

				// and connect!
				urlConnection.connect();
				Log.d("INFORMATION..", "FILE CONECTED....");
				// set the path where we want to save the file
				// in this case, going to save it on the root directory of the
				// sd card.
				File SDCardRoot = Environment.getExternalStorageDirectory();
				// create a new file, specifying the path, and the filename
				// which we want to save the file as.
				file = new File(SDCardRoot, fileName);

				// this will be used to write the downloaded data into the file
				// we created
				FileOutputStream fileOutput = new FileOutputStream(file);
				Log.d("INFORMATION..", "WRINTING TO FILE DOWNLOADED...." + file);
				// this will be used in reading the data from the internet
				InputStream inputStream = urlConnection.getInputStream();

				// this is the total size of the file
				int totalSize = urlConnection.getContentLength();
				// variable to store total downloaded bytes
				int downloadedSize = 0;

				// create a buffer...
				byte[] buffer = new byte[1024];
				int bufferLength = 0; // used to store a temporary size of the
										// buffer

				// now, read through the input buffer and write the contents to
				// the file
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					// add the data in the buffer to the file in the file output
					// stream (the file on the sd card
					fileOutput.write(buffer, 0, bufferLength);
					// add up the size so we know how much is downloaded
					downloadedSize += bufferLength;
					Log.d("INFORMATION..", "FILE DOWNLOADED....");
					// this is where you would do something to report the
					// prgress, like this maybe
					// updateProgress(downloadedSize, totalSize);

				}
				// close the output stream when done
				fileOutput.close();
				Log.d("INFORMATION..", "FILE DOWNLOADING COMPLETED....");
				// catch some possible errors...
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
		}
	}
}
