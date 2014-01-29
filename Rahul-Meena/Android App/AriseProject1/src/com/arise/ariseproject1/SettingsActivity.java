package com.arise.ariseproject1;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.JSONParser;
import com.arise.ariseproject1.classes.SessionManager;
import com.google.android.gcm.GCMRegistrar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener{

	private Button b_chage_password,b_unregister;
    
	private ChangePassword mChangePassword;
	private Unregister munUnregister;
	private SessionManager session;
   	// Progress Dialog
	private ProgressDialog pDialog;

	private JSONParser jsonParser = new JSONParser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		session = new SessionManager(getApplicationContext());
		setContentView(R.layout.activity_settings);
 
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        b_chage_password = (Button)findViewById(R.id.button_acitivity_settings_change_password);
        b_unregister = (Button)findViewById(R.id.button_acitivity_settings_unregister);
        
        b_chage_password.setOnClickListener(this);
        b_unregister.setOnClickListener(this);
        
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.button_acitivity_settings_change_password:{
			showDialogChagePassword();
		}
		break;
		case R.id.button_acitivity_settings_unregister:{
			showDialogUnregister();
		}
		break;
		}
	}
	
	private void showDialogChagePassword(){
		 
		// custom dialog
		final Dialog dialog = new Dialog(getApplicationContext());
		dialog.setContentView(R.layout.dialog_change_password);
		dialog.setTitle("Change Password!");

		// set the custom dialog components - text, image and button
		final EditText et_current_password = (EditText) dialog.findViewById(R.id.editText_dialog_chage_password_current_password);
		final EditText et_new_password = (EditText) dialog.findViewById(R.id.editText_dialog_chage_password_new_password);
		final EditText et_confirm_new_password = (EditText) dialog.findViewById(R.id.editText_dialog_chage_password_confirm_new_password);
		final TextView tv_pdnm = (TextView)dialog.findViewById(R.id.textView_dialog_change_password_pdnm);
		Button b_cancel = (Button) dialog.findViewById(R.id.button_dialog_chage_password_cancel);
		Button b_change = (Button) dialog.findViewById(R.id.button_dialog_chage_password_change);
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				switch (v.getId()){
				
				case R.id.button_dialog_chage_password_cancel:{
					dialog.cancel();
				}
				break;
				
				case R.id.button_dialog_chage_password_change:{
					String current_password = et_current_password.getText().toString();
					String new_password = et_new_password.getText().toString();
					String confirm_new_password = et_confirm_new_password.getText().toString();
					
					if(current_password.length()!=0 && new_password.length()!=0 && confirm_new_password.length()!=0){
						
						if(new_password.equals(confirm_new_password)){
							mChangePassword = new ChangePassword();
							mChangePassword.execute(current_password,new_password);
							
						}else{
							tv_pdnm.setVisibility(View.VISIBLE);
						}
					}
					dialog.cancel();
				}
				break;
				}
				
			}
		};
		
		b_change.setOnClickListener(ocl);
		b_cancel.setOnClickListener(ocl);

		dialog.show();
	}

	private void showDialogUnregister(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		builder.setTitle("Un-register");
		builder
            .setMessage("Do you really want to unregister?");
    builder.setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	munUnregister = new Unregister();
                	munUnregister.execute();
                    dialog.cancel();

                }
            });

    builder.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
		 
		// create alert dialog
		AlertDialog alertDialog = builder.create();

		// show it
		alertDialog.show();
	}



	   /**
	    * Background Async Task to unregsiter user
	    * */
	   public class Unregister extends AsyncTask<String, String, String> {

	       /**
	        * Before starting background thread Show Progress Dialog
	        * */
	       @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	           pDialog = new ProgressDialog(SettingsActivity.this);
	           pDialog.setMessage("Unregistering...");
	           pDialog.setIndeterminate(false);
	           pDialog.setCancelable(false);
	           pDialog.show();
	       }

	       /**
	        * unregistering
	        * */
	       protected String doInBackground(String... args) {


				if(GCMRegistrar.isRegistered(getApplicationContext())){
					GCMRegistrar.unregister(getApplicationContext());
					Toast.makeText(getApplicationContext(), "successfully unregistered", Toast.LENGTH_LONG).show();
				}
				
	           return null;
	       }

	       /**
	        * After completing background task Dismiss the progress dialog
	        * **/
	       protected void onPostExecute(Boolean result) {
	           // dismiss the dialog 
	           pDialog.dismiss();
	           if(!GCMRegistrar.isRegistered(getApplicationContext())){
	        	   Toast.makeText(getApplicationContext(), "User successfully unregistered!", Toast.LENGTH_LONG).show();
	           } else{
	        	   Toast.makeText(getApplicationContext(), "Server Error/Busy. User not unregistered yet. Try again!",Toast.LENGTH_SHORT).show();	
	           }

	       }
	   }



	   /**
	    * Background Async Task to change password
	    * */
	   public class ChangePassword extends AsyncTask<String, String, Boolean> {

	       /**
	        * Before starting background thread Show Progress Dialog
	        * */
	       @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	           pDialog = new ProgressDialog(SettingsActivity.this);
	           pDialog.setMessage("Requesting..");
	           pDialog.setIndeterminate(false);
	           pDialog.setCancelable(false);
	           pDialog.show();
	       }

	       /**
	        * requesting
	        * */
	       protected Boolean doInBackground(String... args) {
	           
	           // Building Parameters
	           List<NameValuePair> params = new ArrayList<NameValuePair>();
	           params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
	           params.add(new BasicNameValuePair(CommonUtilities.TAG_CURRENT_PASSWORD, args[0]));
	           params.add(new BasicNameValuePair(CommonUtilities.TAG_NEW_PASSWORD, args[1]));
	           
	           // getting JSON string from URL
	           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_CHANGE_PASSWORD_URL, "POST", params);

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
	        	   Toast.makeText(getApplicationContext(), "Your password has been successfully changed. Please Log-in again!", Toast.LENGTH_LONG).show();
	        	   session.logoutUser();
	           } else{
	        	   Toast.makeText(getApplicationContext(), "Server Error/Busy/Wrong current password. Please try again!",Toast.LENGTH_SHORT).show();	
	           }

	       }
	   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
