package com.arise.ariseproject1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TempActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.temp, menu);
		return true;
	}

}
