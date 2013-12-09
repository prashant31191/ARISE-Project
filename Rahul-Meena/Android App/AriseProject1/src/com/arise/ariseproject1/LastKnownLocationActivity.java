package com.arise.ariseproject1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LastKnownLocationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_last_known_location);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.last_known_location, menu);
		return true;
	}

}
