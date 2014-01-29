package com.arise.ariseproject1;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.JSONParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity implements OnClickListener {

	// UI elements
	EditText et_email;
	Button b_reset_password,b_cancel;
    
   	// Progress Dialog
	private ProgressDialog pDialog;

	private JSONParser jsonParser = new JSONParser();
	
	private SendResetPasswordLink msenResetPasswordLink;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		et_email = (EditText)findViewById(R.id.editText_activity_forgot_pssword_email);
		b_reset_password = (Button)findViewById(R.id.button_activity_forgot_password_reset_password);
		b_cancel = (Button)findViewById(R.id.button_activity_forgot_password_cancel);
		
		b_reset_password.setOnClickListener(this);
		b_cancel.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.button_activity_forgot_password_reset_password:{
			String email = et_email.getText().toString();
			
			msenResetPasswordLink = new SendResetPasswordLink();
			msenResetPasswordLink.execute(email);
			
		}
		break;
		
		case R.id.button_activity_forgot_password_cancel:{
			// close this activity
			finish();
		}
		break;
		}
		
	}



	   /**
	    * Background Async Task to send reset password link
	    * */
	   public class SendResetPasswordLink extends AsyncTask<String, String, Boolean> {
		   
		   private String email = new String();

	       /**
	        * Before starting background thread Show Progress Dialog
	        * */
	       @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	           pDialog = new ProgressDialog(ForgotPasswordActivity.this);
	           pDialog.setMessage("Sending link..");
	           pDialog.setIndeterminate(false);
	           pDialog.setCancelable(false);
	           pDialog.show();
	       }

	       /**
	        * getting All products from url
	        * */
	       protected Boolean doInBackground(String... args) {
	    	   email = args[0];
	           // Building Parameters
	           List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
	           params.add(new BasicNameValuePair(CommonUtilities.TAG_EMAIL, email));
	           
	           // getting JSON string from URL
	           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_SEND_RESET_PASSWORD_LINK_URL, "POST", params);

	           // Check your log cat for JSON reponse
	           Log.d("Response: ", json.toString());

	           try {
	               // Checking for SUCCESS TAG
	               int success = json.getInt(CommonUtilities.TAG_SUCCESS);

	               if (success == 1) {
	            	   return true;
	            	   
	               } else {
	            	   return false;
	       	        
	               }
	           } catch (JSONException e) {
	               e.printStackTrace();
	           }

	           return null;
	       }

	       /**
	        * After completing background task Dismiss the progress dialog
	        * **/
	       protected void onPostExecute(Boolean result) {
	           // dismiss the dialog after getting all products
	           pDialog.dismiss();
	           if(result){
	        	   Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPasswordResultActivity.class);
	        	   intent.putExtra(CommonUtilities.TAG_EMAIL, email);
	        	   startActivity(intent);
	        	   finish();
	           } else{
	        	   Toast.makeText(getApplicationContext(), "Server Error/Busy..Try again!",Toast.LENGTH_SHORT).show();	
	           }

	       }
	   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
		return true;
	}

}
