package com.arise.ariseproject1;

import com.arise.ariseproject1.classes.AlertDialogManager;
import com.arise.ariseproject1.classes.JSONParser;
import com.arise.ariseproject1.classes.SessionManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends Activity implements OnClickListener{
	
	// UI elements
	Button b_log_in;
	EditText et_email,et_password;
	TextView tv_fp;
	
	Intent intent;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
    
	// Session Manager Class
	SessionManager session;
	
	JSONParser jsonParser = new JSONParser();
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		
       // Session Manager
       session = new SessionManager(getApplicationContext());
		
		b_log_in = (Button)findViewById(R.id.button_log_in_activity_log_in);
		et_email = (EditText)findViewById(R.id.editText_log_in_activity_email);
		et_password = (EditText)findViewById(R.id.editText_log_in_activity_password);
        et_email.setText("rahul_lookout@outlook.com");
        et_password.setText("pass");
		tv_fp = (TextView)findViewById(R.id.TextView_log_in_activity_forgot_password);
		
		
		b_log_in.setOnClickListener(this);
		tv_fp.setOnClickListener(this);
		
		
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.button_log_in_activity_log_in:{
			
            // Get username, password from EditText
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            
            // Check if username, password is filled                
            if(email.trim().length() > 0 && password.trim().length() > 0){
            	// user entered details
            	// check and login if correct
                
            	// Starting MainActivity
                  
      			Intent intent = new Intent(LogInActivity.this,MainActivity.class);
      			intent.putExtra(SessionManager.KEY_EMAIL,email );
      			intent.putExtra(SessionManager.KEY_PASS, password);
      			startActivity(intent);
      			finish();
            	
            } else{
                // user didn't entered username or password
                // Show alert asking him to enter the details
                alert.showAlertDialog(LogInActivity.this, "Login failed..", "Please enter username and password", false);
            }
			
		}
		break;
		case R.id.TextView_log_in_activity_forgot_password:{
			
			intent = new Intent(LogInActivity.this,ForgotPasswordActivity.class);
			startActivity(intent);
			
		}
		break;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
}
