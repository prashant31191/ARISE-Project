package com.arise.ariseproject1;
 
import static com.arise.ariseproject1.classes.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.arise.ariseproject1.classes.CommonUtilities.SENDER_ID;
import static com.arise.ariseproject1.classes.CommonUtilities.SERVER_REGISTER_URL;
import static com.arise.ariseproject1.classes.CommonUtilities.SERVER_USER_EXIST_URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.MessageReceiver;
import com.arise.ariseproject1.classes.AlertDialogManager;
import com.arise.ariseproject1.classes.ConnectionDetector;
import com.arise.ariseproject1.classes.JSONParser;
import com.arise.ariseproject1.classes.ServerUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class RegisterActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
	
    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
    
    // Asyntask
    CheckAndRegisterUser mCheckAndRegisterTask;
     
    // Internet detector
    ConnectionDetector cd;
     
    // UI elements
    EditText et_first_name,et_last_name,et_email,et_password;
    
    TextView tv_email_error, tv_device_error;
    
    Button b_register;
    MessageReceiver mMessageReceiver = null;
    
   public static String full_name;
   public static String email;
   public static String password;
   public static String gcm_regid;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         
        cd = new ConnectionDetector(getApplicationContext());
 
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(RegisterActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
 
        // Check if GCM configuration is set
        if (SERVER_REGISTER_URL == null || SENDER_ID == null || SERVER_REGISTER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
             return;
        }
         
        et_first_name = (EditText) findViewById(R.id.editText_register_activity_first_name);
        et_last_name = (EditText) findViewById(R.id.editText_register_activity_last_name);
        et_email = (EditText) findViewById(R.id.editText_register_activity_email);
        et_password = (EditText) findViewById(R.id.editText_register_activity_password);
        
        et_first_name.setText("rahul");
        et_last_name.setText("meena");
        et_email.setText("rahul_lookout@outlook.com");
        et_password.setText("pass");
        b_register = (Button) findViewById(R.id.button_register_activity_register);
        tv_email_error = (TextView) findViewById(R.id.textView_regsiter_activity_email_error);
        tv_device_error = (TextView) findViewById(R.id.textView_regsiter_activity_device_error);
         
        /*
         * Click event on Register button
         * */
        b_register.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {
            	
            	// Hide email and device error if already there
            	tv_email_error.setVisibility(View.GONE);
            	tv_device_error.setVisibility(View.GONE);
            	
                // Read EditText data
                String first_name = et_first_name.getText().toString();
                String last_name = et_last_name.getText().toString();
                full_name = first_name+" "+last_name;
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                	
                // Check if user filled the form
                if(first_name.trim().length() > 0 && last_name.trim().length() > 0 && email.trim().length() > 0 && password.length() > 0){

                    if (GCMRegistrar.isRegisteredOnServer(RegisterActivity.this)) {
                       tv_device_error.setVisibility(View.VISIBLE);
                    }else{
                	// Register only if email is not already registered
                    mCheckAndRegisterTask = new CheckAndRegisterUser();
                    mCheckAndRegisterTask.execute();
                    }
                }else{
                    // user doen't filled that data
                    // ask him to fill the form
                    alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
                }
            }
            
        });
    } 

	/**
     * Register user on GCM and send data to our private server too
     * */
    public void registerUser(final String full_name, final String email, final String password){      
    	
    	// Make sure the device has the proper dependencies.
       GCMRegistrar.checkDevice(this);

       // Make sure the manifest was properly set - comment out this line
       // while developing the app, then uncomment it when it's ready.
       GCMRegistrar.checkManifest(this);

       // Get GCM registration id
       final String regId = GCMRegistrar.getRegistrationId(this);
       Log.d("regId", regId+"out");
  //     lblMessage = (TextView) findViewById(R.id.lblMessage);
        
       // Check if regid already presents
       if (regId.equals("")) {             
           Log.d("regId   in", regId+"in");
           // Registration is not present, register now with GCM           
           GCMRegistrar.register(this, SENDER_ID);
       } else {
           // Device is already registered on GCM
           if (GCMRegistrar.isRegisteredOnServer(this)) {
               // Skips registration.              
               Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
           } else {
    		   final Context context = this;
    		   ServerUtilities.register(context, full_name, email, password, regId);
           }
       }
    }   
   /*  
    @Override
    protected void onDestroy() {
        if (mCheckAndRegisterTask != null) {
            mCheckAndRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
    */
 
    /**
     * Background Async Task to check if user already exist
     * */
   public class CheckAndRegisterUser extends AsyncTask<String, String, Boolean> {

 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Register User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected Boolean doInBackground(String... args) {
        	
        	// Return value
        	boolean temp_return = false;
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("email", email);
            params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
            params.add(new BasicNameValuePair("email", email));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(SERVER_USER_EXIST_URL,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // user email already exist
                	temp_return = true;
                	
                } else {
                    // New user email
                	// Register User
                	registerUser(full_name, email, password);
                	Log.d("registering", "yes");
                	temp_return = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return temp_return;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(result){
                // Device is already registered on GCM
            	tv_email_error.setVisibility(View.VISIBLE);
            }
            else{
            	// Launch Main Activity
                mMessageReceiver = new MessageReceiver();
                registerReceiver(mMessageReceiver, new IntentFilter(
                      DISPLAY_MESSAGE_ACTION));
                Log.d("Broadcast", "registered");
                String regIdd = GCMRegistrar.getRegistrationId(RegisterActivity.this);
                Log.d("regIdd", regIdd+"DF");
        		if(!GCMRegistrar.isRegistered(RegisterActivity.this)){
        			Log.d("Registered", "false");
        		}
        		if(!GCMRegistrar.isRegisteredOnServer(RegisterActivity.this)){
        			Log.d("Registered on server", "false");
        		}
                Intent i = new Intent(RegisterActivity.this, RegistrationResultActivity.class);
                                  
                // Sending registration details to RegistrationResultActivity
                i.putExtra("name",full_name);
                i.putExtra("email", email);
                i.putExtra("success", true);
                startActivity(i);
                finish();
            }
        }
 
    }

	@Override
	protected void onDestroy() {
	    if (pDialog != null) {
	        pDialog.dismiss();
	        pDialog = null;
	    }
	    super.onDestroy();
	}

	@Override
	protected void onPause() {
	       try {
	    	   if(mMessageReceiver != null){
	           unregisterReceiver(mMessageReceiver);
	           GCMRegistrar.onDestroy(this);
	    	   }
	       } catch (Exception e) {
	           Log.e("UnRegister Receiver Error", "> " + e.getMessage());
	       }
		super.onPause();
	}
   
}
