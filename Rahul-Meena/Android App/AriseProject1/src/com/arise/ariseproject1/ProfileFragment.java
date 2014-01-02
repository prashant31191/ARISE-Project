package com.arise.ariseproject1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.adapter.LazyPWCSULAdapter;
import com.arise.ariseproject1.classes.AlertDialogManager;
import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.JSONParser;
import com.arise.ariseproject1.classes.PWCSUL;
import com.arise.ariseproject1.classes.PWLUCS;
import com.arise.ariseproject1.classes.GPSTracker;
import com.arise.ariseproject1.classes.SessionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements OnClickListener{
	
    public Context context;
    private String activityAssignedValue ="";
    private static final String STRING_VALUE ="stringValue";
	
	private Button b_get_LKL,b_add_new,b_UML;
	private ImageView iv_image;
	private ListView lv_pwcsul;
	private RelativeLayout rl_none;
	private TextView tv_name;
	private LazyPWCSULAdapter peopleAdapter;
	private DisplayImageOptions dioptions;


	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private CheckAndLogInDetails mCheckAndLogIndetails;
	private UpdateMyLocation mUpdateMyLocation;
	private AddNewPWCSUL mAddNewPWCSUL;
	private AddNewPWCSULOption mAddNewPWCSULOption;
	
    // Session Manager Class
	private SessionManager session;

    // Alert Dialog Manager
	private AlertDialogManager alert = new AlertDialogManager();
    
   	// Progress Dialog
	private ProgressDialog pDialog;

	private JSONParser jsonParser = new JSONParser();
	
    //since Fragment is Activity dependent you need Activity context in various cases
    @Override
    public void onAttach(Activity activity){
      super.onAttach(activity);
      context = getActivity();
    }

    //now on your entire fragment use context rather than getActivity()
    @Override
    public void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
     if(savedInstanceState != null)
          activityAssignedValue = savedInstanceState.getString(STRING_VALUE);
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
       super.onSaveInstanceState(outState);
       outState.putString(STRING_VALUE,activityAssignedValue);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        b_get_LKL = (Button)rootView.findViewById(R.id.button_profile_fragment_get_LKL);
        b_add_new = (Button)rootView.findViewById(R.id.button_profile_fragment_add_new);
        b_UML = (Button)rootView.findViewById(R.id.button_profile_fragment_UML);
        iv_image = (ImageView)rootView.findViewById(R.id.imageView_profile_fragment_image);
        lv_pwcsul = (ListView)rootView.findViewById(R.id.listView_profile_fragment_PWCSML);
        rl_none = (RelativeLayout)rootView.findViewById(R.id.relativeLayout_profile_fragment_none);
        tv_name = (TextView)rootView.findViewById(R.id.textView_profile_fragment_name);

        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		init();
	}



	public void init() {
		
		dioptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher)
			.imageScaleType(ImageScaleType.NONE)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisc(true)
			.build();
		
		b_get_LKL.setOnClickListener(this);
        b_add_new.setOnClickListener(this);
        b_UML.setOnClickListener(this);

        String email = new String();
        String password = new String();
        
        if(session.isLoggedIn()){
        	// get user data from session
        	HashMap<String, String> user = session.getUserDetails();
         
        	// email
        	email = user.get(SessionManager.KEY_EMAIL);
         
        	// password
        	password = user.get(SessionManager.KEY_PASS);
        } else{
            
           	// email
           	email = getActivity().getIntent().getStringExtra(SessionManager.KEY_EMAIL);
            
           	// password
           	password = getActivity().getIntent().getStringExtra(SessionManager.KEY_PASS);
        	
        }
        
        if(session.getPDValue() == 0){
        	mCheckAndLogIndetails = new CheckAndLogInDetails(context, email, password);
        	mCheckAndLogIndetails.execute();
        } else{
        	loadFromSession();
        }
	}

	private void loadFromSession() {
		
        imageLoader.displayImage(session.getImage(), iv_image,dioptions);
        tv_name.setText(session.getName());
        peopleAdapter = new LazyPWCSULAdapter(context, 0, imageLoader, dioptions);
        peopleAdapter.addAll(session.getPWCSUL());
        lv_pwcsul.setAdapter(peopleAdapter);
		
        /*if(ATjson.getJSONArray(CommonUtilities.TAG_PWCSUL) != null){
			rl_none.setVisibility(View.GONE);
		}
		*/
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){

		case R.id.button_profile_fragment_get_LKL:{
			if(session.getLocLat() != 0 && session.getLocLong() != 0 ){
				Intent intent = new Intent(context,MapActivity.class);
				startActivity(intent);
			} else{
				
			}
		}
		break;
		case R.id.button_profile_fragment_UML:{
			mUpdateMyLocation = new UpdateMyLocation();
			mUpdateMyLocation.execute();
		}
		break;
		case R.id.button_profile_fragment_add_new:{
			showDialogAddNew();
		}
		break;
		}
		
	}

	private void showDialogAddNew(){
		 
		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_add_new_pwcsul);
		dialog.setTitle("Search");

		// set the custom dialog components - text, image and button
		final EditText et_email = (EditText) dialog.findViewById(R.id.editText_dialog_add_new_pwcsul_email);
		Button b_cancel = (Button) dialog.findViewById(R.id.button_dialog_add_new_pwcsul_cancel);
		Button b_look = (Button) dialog.findViewById(R.id.button_dialog_add_new_pwcsul_look);
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				switch (v.getId()){
				
				case R.id.button_dialog_add_new_pwcsul_cancel:{
					dialog.cancel();
				}
				break;
				
				case R.id.button_dialog_add_new_pwcsul_look:{
					mAddNewPWCSULOption = new AddNewPWCSULOption();
					mAddNewPWCSULOption.execute(et_email.getText().toString());
					dialog.cancel();
				}
				break;
				}
				
			}
		};
		
		b_look.setOnClickListener(ocl);
		b_cancel.setOnClickListener(ocl);

		dialog.show();
	}

	private void showDialogNewUser(String image, String name,long uid){
		 final long personUid = uid; 
		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_new_pwcsul);
		dialog.setTitle("Search");

		// set the custom dialog components - text, image and button
		ImageView iv_image = (ImageView) dialog.findViewById(R.id.imageView_dialog_new_pwcsul_image);
		TextView tv_email = (TextView) dialog.findViewById(R.id.textView_dialog_new_pwcsul_name);
		
		imageLoader.displayImage(image, iv_image, dioptions);
		tv_email.setText(name);
		
		Button b_cancel = (Button) dialog.findViewById(R.id.button_dialog_new_pwcsul_cancel);
		Button b_add = (Button) dialog.findViewById(R.id.button_dialog_new_pwcsul_add);
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				switch (v.getId()){
				
				case R.id.button_dialog_new_pwcsul_cancel:{
					dialog.cancel();
				}
				break;
				
				case R.id.button_dialog_new_pwcsul_add:{
					mAddNewPWCSUL = new AddNewPWCSUL();
					mAddNewPWCSUL.execute(Long.toString(personUid));
					dialog.cancel();
				}
				break;
				}
				
			}
		};
		
		b_add.setOnClickListener(ocl);
		b_cancel.setOnClickListener(ocl);

		dialog.show();
	}

	private void showDialogUserNotFound(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("No User Found!");
    	builder.setPositiveButton("OK",
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
     * Background Async Task to check and log in user
     * */
   public class CheckAndLogInDetails extends AsyncTask<String, String, Boolean> {


	   private Context ATcontext; 
	   private String ATemail; 
	   private String ATpassword;
	   private JSONObject ATjson;
	   

	   public CheckAndLogInDetails(Context context,String email,String password) {  // can take other params if needed
		   this.ATcontext = context;
		   this.ATemail = email;
		   this.ATpassword = password;
       	   this.ATjson = new JSONObject();
	   }
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ATcontext);
            pDialog.setMessage("Loging In..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected Boolean doInBackground(String... args) {
 
        	// boolean return variable 
        	boolean temp_return = false;
        	
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("email", ATemail);
            params.add(new BasicNameValuePair("email", ATemail));
            params.add(new BasicNameValuePair("password", ATpassword));
 
            // getting JSON Object
            // Note that log in url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_LOG_IN_URL,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
 
                if (success == 1) {
                    // user details verified..ready to log in
                	// add user's id no
                	ATjson = json;
                	
                	JSONArray pwcsul = ATjson.getJSONArray(CommonUtilities.TAG_PWCSUL);
                	if(pwcsul != null){
                	for(int i=0; i<pwcsul.length();i++){
                		JSONObject jperson = pwcsul.getJSONObject(i);
                		PWCSUL person = new PWCSUL();
                		person.setName(jperson.getString(CommonUtilities.TAG_NAME));
                		person.setEmail(jperson.getString(CommonUtilities.TAG_EMAIL));
                		person.setImage(jperson.getString(CommonUtilities.TAG_IMAGE));
                		person.setUid(jperson.getLong(CommonUtilities.TAG_UID));
                		
                		session.getPWCSUL().add(person);
                	}
                	}
					JSONArray pwlucs = ATjson.getJSONArray(CommonUtilities.TAG_PWLUCS);
                	if(pwlucs != null){
					for(int i=0; i<pwlucs.length();i++){
                		JSONObject jperson = pwlucs.getJSONObject(i);
                		PWLUCS person = new PWLUCS();
                		person.setName(jperson.getString(CommonUtilities.TAG_NAME));
                		person.setEmail(jperson.getString(CommonUtilities.TAG_EMAIL));
                		person.setImage(jperson.getString(CommonUtilities.TAG_IMAGE));
                		person.setUid(jperson.getLong(CommonUtilities.TAG_UID));
                		person.setLocLat(jperson.getLong(CommonUtilities.TAG_LOC_LAT));
                		person.setLocLong(jperson.getLong(CommonUtilities.TAG_LOC_LONG));
                		
                		session.getPWLUCS().add(person);
                	}
                	}
                	temp_return = true;
                	
                } else {
                    // Login failed..", "Email/Password is incorrect
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
        protected void onPostExecute(Boolean result){
            // dismiss the dialog once done
            pDialog.dismiss();
            if(!result){
                // username / password doesn't match
                alert.showAlertDialog(getActivity(), "Login failed..", "Username/Password is incorrect", false);
                Intent intent = new Intent(getActivity(), IndexActivity.class);
                startActivity(intent);
                getActivity().finish();
            
            }
            else{
            	// User email/password verified and checked
            	// Go to main activity (profile) and get user acount details  
            	
                // Creating user login session
                // For testing i am stroing name, email as follow
                // Use user real data

            	try {
            		
					long uid = ATjson.getLong(CommonUtilities.TAG_UID);
	            	session.createLoginSession(uid,ATemail,ATpassword);
	            	
					String name = ATjson.getString(CommonUtilities.TAG_NAME);
					double loclong = ATjson.getDouble(CommonUtilities.TAG_LOC_LONG);
					double loclat = ATjson.getDouble(CommonUtilities.TAG_LOC_LAT);
					String image = ATjson.getString(CommonUtilities.TAG_IMAGE);
					
					session.setName(name);
					session.setLocLat(loclat);
					session.setLocLong(loclong);
					session.setImage(image);
					
					loadFromSession();

					RadarFragment radar = (RadarFragment)getFragmentManager().findFragmentById(R.layout.fragment_radar);
					radar.loadRadar(session.getPWLUCS());
					
					session.setPDValue(1);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
                }
            }
        }


   
   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   public class UpdateMyLocation extends AsyncTask<String, String, Boolean> {

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(context);
           pDialog.setMessage("Updating Location...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }

       /**
        * getting All products from url
        * */
       protected Boolean doInBackground(String... args) {
    	   
    	   double loclat = session.getLocLat(),loclong = session.getLocLong();
    	   
			// create class object
	        GPSTracker gps = new GPSTracker(context);

			// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	
	        	loclat = gps.getLatitude();
	        	loclong = gps.getLongitude();
	        	
	        	// \n is for new line
	        	Toast.makeText(context, "Your Location is - \nLat: " + loclat+ "\nLong: " + loclong, Toast.LENGTH_LONG).show();	
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	gps.showSettingsAlert();
	        }
           // getting updated data from EditTexts
           long uid = session.getUserID();
           
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_LOC_LAT, Double.toString(loclat)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_LOC_LONG, Double.toString(loclong)));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_UPDATE_USER_LOCATION_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);

               if (success == 1) {
                   // location updated
            	   session.setLocLat(loclat);
            	   session.setLocLong(loclong);
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
        	   Toast.makeText(context, "Location Successfully updated!",Toast.LENGTH_SHORT).show();	
           } else{
        	   Toast.makeText(context, "Server Error/Busy!",Toast.LENGTH_SHORT).show();	
           }

       }

   }


   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   public class AddNewPWCSULOption extends AsyncTask<String, String, Boolean> {
	   
	   private PWCSUL person;

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(context);
           pDialog.setMessage("Looking...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }

       /**
        * getting All products from url
        * */
       protected Boolean doInBackground(String... args) {
           // getting updated data from EditTexts
           long uid = session.getUserID();
           
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_EMAIL, args[0].toString()));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_LOOK_FOR_PWCSUL_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);

               if (success == 1) {
                   // get person details
            	   JSONObject jsonPerson = json.getJSONObject(CommonUtilities.TAG_PERSON);
            	   person = new PWCSUL();
            	   person.setName(jsonPerson.getString(CommonUtilities.TAG_NAME));
            	   person.setImage(jsonPerson.getString(CommonUtilities.TAG_IMAGE));
            	   person.setEmail(jsonPerson.getString(CommonUtilities.TAG_EMAIL));
            	   person.setUid(jsonPerson.getLong(CommonUtilities.TAG_UID));
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
        	   showDialogNewUser(person.getImage(),person.getName(),person.getUid());
           } else{
        	   showDialogUserNotFound();
           }

       }

   }



   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   public class AddNewPWCSUL extends AsyncTask<String, String, Boolean> {
	   
	   private PWCSUL person;

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(context);
           pDialog.setMessage("Adding...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }

       /**
        * getting All products from url
        * */
       protected Boolean doInBackground(String... args) {
           // getting updated data from EditTexts
           long uid = session.getUserID();
           
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_PERSON_UID, args[0].toString()));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_ADD_PWCSUL_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);

               if (success == 1) {
                   // get person details
            	   JSONObject jsonPerson = json.getJSONObject(CommonUtilities.TAG_PERSON);
            	   person = new PWCSUL();
            	   person.setName(jsonPerson.getString(CommonUtilities.TAG_NAME));
            	   person.setImage(jsonPerson.getString(CommonUtilities.TAG_IMAGE));
            	   person.setEmail(jsonPerson.getString(CommonUtilities.TAG_EMAIL));
            	   person.setUid(jsonPerson.getLong(CommonUtilities.TAG_UID));
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
        	   peopleAdapter.add(person);
        	   lv_pwcsul.setSelection(lv_pwcsul.getChildCount()-1);
        	   Toast.makeText(context, "User successfully added!", Toast.LENGTH_LONG).show();
           } else{
        	   Toast.makeText(context, "Server Error/Busy!",Toast.LENGTH_SHORT).show();	
           }

       }
   }
   
}