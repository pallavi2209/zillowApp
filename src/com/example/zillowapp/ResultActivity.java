package com.example.zillowapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class ResultActivity extends TabActivity {

	public static final String JSON_STRING = "jsonString";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		// create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        Intent intent = getIntent();
        String jsonString = intent.getStringExtra(MainActivity.JSON_OBJECT);

        TabSpec tab1 = tabHost.newTabSpec("TableTab");
        TabSpec tab2 = tabHost.newTabSpec("ChartsTab");

       // Set the Tab name and Activity
       // that will be opened when particular Tab will be selected
        
        Intent basicInfoIntent = new Intent(this,ShowTableActivity.class);
        basicInfoIntent.putExtra(JSON_STRING, jsonString);
        tab1.setIndicator("Basic Info");
        tab1.setContent(basicInfoIntent);
       
        Intent chartsInfoIntent = new Intent(this,ShowChartsActivity.class);
        chartsInfoIntent.putExtra(JSON_STRING, jsonString);
        tab2.setIndicator("Historical Zestimates");
        tab2.setContent(chartsInfoIntent);
       
        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
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
}
