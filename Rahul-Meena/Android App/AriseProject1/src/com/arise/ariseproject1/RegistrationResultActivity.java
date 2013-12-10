package com.arise.ariseproject1;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class RegistrationResultActivity extends Activity {
	
	// UI elements
	TextView tv_output;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_result);
        
       // Getting name, email from intent
       Intent i = getIntent();
        
       String name = i.getStringExtra("name");
       String email = i.getStringExtra("email");
       boolean success = i.getBooleanExtra("success", false);
       // referencing ui elements
       tv_output = (TextView) findViewById(R.id.textView_regsitration_result_output);
       
       // if success
       if(success){
    	   tv_output.setText("Dear "+name+", your account is successfully registered and a confirmation link is sent to your following email "+email+".\n"+"Please verify your account before Signing In.");
       } else {
    	   tv_output.setText("Unfortunately an error occured! Please try again.");
       }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration_result, menu);
		return true;
	}

}
