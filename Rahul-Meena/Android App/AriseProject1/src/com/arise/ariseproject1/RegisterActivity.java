package com.arise.ariseproject1;
 
import static com.arise.ariseproject1.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.arise.ariseproject1.CommonUtilities.EXTRA_MESSAGE;
import static com.arise.ariseproject1.CommonUtilities.SENDER_ID;
import static com.arise.ariseproject1.CommonUtilities.SERVER_URL;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class RegisterActivity extends Activity {
	
    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
    
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
     
    // Internet detector
    ConnectionDetector cd;
     
    // UI elements
    EditText et_first_name,et_last_name,et_email,et_password;
    
    Button b_register;
    
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
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
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
        b_register = (Button) findViewById(R.id.button_register_activity_register);
         
        /*
         * Click event on Register button
         * */
        b_register.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {
            	
                // Read EditText data
                String first_name = et_first_name.getText().toString();
                String last_name = et_last_name.getText().toString();
                full_name = first_name+" "+last_name;
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                 
                // Check if user filled the form
                if(first_name.trim().length() > 0 && last_name.trim().length() > 0 && email.trim().length() > 0 && password.length() > 0){

                	// Launch Main Activity
                    Intent i = new Intent(getApplicationContext(), RegistrationResultActivity.class);
                                      
                   // Sending registration details to RegistrationResultActivity
                   i.putExtra("name",full_name);
                   i.putExtra("email", email);
                   
                	// Check if Registered or not r
                	if(registerUser(full_name, email, password)){
                		// registration successful
                		i.putExtra("success", true);
                	} else{
                		// registration unsuccessful
                		i.putExtra("success", false);
                	}
                    startActivity(i);
                    finish();
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
    public boolean registerUser(final String full_name, final String email, final String password){      
        
    	// temp boolean
    	boolean temp_return = false;
       // Make sure the device has the proper dependencies.
       GCMRegistrar.checkDevice(this);

       // Make sure the manifest was properly set - comment out this line
       // while developing the app, then uncomment it when it's ready.
       GCMRegistrar.checkManifest(this);

  //     lblMessage = (TextView) findViewById(R.id.lblMessage);
        
       registerReceiver(mHandleMessageReceiver, new IntentFilter(
               DISPLAY_MESSAGE_ACTION));
        
       // Get GCM registration id
       final String regId = GCMRegistrar.getRegistrationId(this);
       
       // Check if regid already presents
       if (regId.equals("")) {
           // Registration is not present, register now with GCM           
           GCMRegistrar.register(this, SENDER_ID);
           temp_return = true;
       } else {
           // Device is already registered on GCM
           if (GCMRegistrar.isRegisteredOnServer(this)) {
               // Skips registration.              
               Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
               temp_return = false;
           } else {
               // Try to register again, but not in the UI thread.
               // It's also necessary to cancel the thread onDestroy(),
               // hence the use of AsyncTask instead of a raw thread.
               final Context context = this;
               mRegisterTask = new AsyncTask<Void, Void, Void>() {

                   @Override
                   protected Void doInBackground(Void... params) {
                       // Register on our server
                       // On server creates a new user
                       ServerUtilities.register(context, full_name, email, password, regId);
                       return null;
                   }

                   @Override
                   protected void onPostExecute(Void result) {
                       mRegisterTask = null;
                   }

               };
               mRegisterTask.execute(null, null, null);
               temp_return = true;
           }
       }
       return true;
    }   

	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
          //  lblMessage.append(newMessage + "\n");           
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            WakeLocker.release();
        }
    };
     
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
}
