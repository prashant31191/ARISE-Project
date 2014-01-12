package com.arise.ariseproject1;

import com.arise.ariseproject1.classes.CommonUtilities;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ForgotPasswordResultActivity extends Activity implements OnClickListener {

	// UI elements
	private TextView tv_result;
	private Button b_login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password_result);
		
		tv_result = (TextView)findViewById(R.id.textView_activity_forgot_password_result_result);
		b_login = (Button)findViewById(R.id.button_activity_forgot_password_result_login);
		
		// add email id
		String email = getIntent().getExtras().getString(CommonUtilities.TAG_EMAIL);
		tv_result.append("\n"+email);
		
		b_login.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.button_activity_forgot_password_result_login){
			finish();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password_result, menu);
		return true;
	}

}
