package com.arise.ariseproject1;

import com.arise.ariseproject1.classes.SessionManager;
import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class IndexActivity extends Activity implements OnClickListener {
	
	// UI elements
	Button b_register,b_log_in, b_unreg;
	
	Intent intent;
	
    // Session Manager Class
    SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index);
		
		// Referencing buttons
		b_register = (Button)findViewById(R.id.button_index_activity_register);
		b_unreg = (Button)findViewById(R.id.button_unregister);
		b_log_in = (Button)findViewById(R.id.button_index_activity_log_in);
		
		// Click event on buttons
		b_register.setOnClickListener(this);
		b_unreg.setOnClickListener(this);
		b_log_in.setOnClickListener(this);


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
		//if(GCMRegistrar.isRegistered(getApplicationContext()))
			//session.checkLogin();
		
		if(GCMRegistrar.isRegistered(getApplicationContext())){
			Log.d("Registered", "true");
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_index_activity_register:{
			
			intent = new Intent(IndexActivity.this,RegisterActivity.class);
			startActivity(intent);
			
		}
		break;
		
		// NEEDS TO BE REMOVED
		case R.id.button_unregister:{


			if(GCMRegistrar.isRegistered(getApplicationContext())){
				GCMRegistrar.unregister(getApplicationContext());
				Toast.makeText(getApplicationContext(), "successfully unregistered", Toast.LENGTH_LONG).show();
			}
		}
		// NEEDS TO BE REMOVED
		
		break;
		case R.id.button_index_activity_log_in:{
			
			intent = new Intent(IndexActivity.this,LogInActivity.class);
			startActivity(intent);
			
		}
		break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

}
