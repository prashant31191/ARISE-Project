package com.arise.ariseproject1;

import static com.arise.ariseproject1.CommonUtilities.SERVER_LOG_IN_URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends Activity implements OnClickListener{
	
	// UI elements
	Button b_log_in;
	CheckBox cb_rm;
	EditText et_email,et_password;
	TextView tv_fp;
	
	Intent intent;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
    
	// Session Manager Class
	SessionManager session;
   
	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
   
	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
        
       // Session Manager
       session = new SessionManager(getApplicationContext());
		
		b_log_in = (Button)findViewById(R.id.button_log_in_activity_log_in);
		cb_rm = (CheckBox)findViewById(R.id.checkBox_log_in_activity_remember_me);
		et_email = (EditText)findViewById(R.id.editText_log_in_activity_email);
		et_password = (EditText)findViewById(R.id.editText_log_in_activity_password);
		tv_fp = (TextView)findViewById(R.id.TextView_log_in_activity_forgot_password);
		
		
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
