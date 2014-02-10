package com.arise.ariseproject1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements OnClickListener{
	

	public static boolean isImageUpdated = false;
	
    public Context context;
    private String activityAssignedValue ="";
    private static final String STRING_VALUE ="stringValue";
	
	private Button b_get_LKL,b_add_new,b_UML;
	private ImageView iv_image;
	private ListView lv_pwcsul;
	//private RelativeLayout rl_none;
	private TextView tv_name;
	private LazyPWCSULAdapter peopleAdapter;
	private DisplayImageOptions dioptions;


	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private CheckAndLogInDetails mCheckAndLogIndetails;
	private UpdateMyLocation mUpdateMyLocation;
	private AddNewPWCSUL mAddNewPWCSUL;
	private AddNewPWCSULOption mAddNewPWCSULOption;
	private RemoveAPWCSUL mRemoveAPWCSUL;
	
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
        //rl_none = (RelativeLayout)rootView.findViewById(R.id.relativeLayout_profile_fragment_none);
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
			.showImageOnLoading(R.drawable.default_image)
			.showImageForEmptyUri(R.drawable.default_image)
			.showImageOnFail(R.drawable.default_image)
			.imageScaleType(ImageScaleType.NONE)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.cacheOnDisc(true)
			.build();
		session = new SessionManager(context);
		b_get_LKL.setOnClickListener(this);
        b_add_new.setOnClickListener(this);
        b_UML.setOnClickListener(this);
        iv_image.setOnClickListener(this);
        tv_name.setOnClickListener(this);

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
        	mCheckAndLogIndetails = new CheckAndLogInDetails(email, password);
        	mCheckAndLogIndetails.execute();
        } else{
        	loadFromSession();
        }
	}

	private void loadFromSession() {
		
        imageLoader.displayImage(session.getImage(), iv_image,dioptions);
        tv_name.setText(session.getName());
        if(!session.getPWCSUL().isEmpty()){
        	peopleAdapter = new LazyPWCSULAdapter(context, 0, imageLoader, dioptions);
        	peopleAdapter.addAll(session.getPWCSUL());
        	lv_pwcsul.setAdapter(peopleAdapter);
        	lv_pwcsul.setOnItemSelectedListener(new OnItemSelectedListener() {

        		@Override
        		public void onItemSelected(AdapterView<?> parent, View view,
        				int position, long id) {
        			showDialogToRemovePWCSUL(position);
        		}

        		@Override
        		public void onNothingSelected(AdapterView<?> arg0) {
        			// TODO Auto-generated method stub
				
        		}
        	});
		}
		
        /*if(ATuser.getJSONArray(CommonUtilities.TAG_PWCSUL) != null){
			rl_none.setVisibility(View.GONE);
		}
		*/
	}

	private void showDialogToRemovePWCSUL(final int position) {
		// custom dialog
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_remove_person);
		dialog.setTitle("Search");

		// set the custom dialog components - text, image and button
		ImageView iv_image = (ImageView) dialog.findViewById(R.id.imageView_dialog_remove_person_image);
		TextView tv_email = (TextView) dialog.findViewById(R.id.textView_dialog_remove_person_name);
		
		String image = peopleAdapter.getItem(position).getImage();
		String name = peopleAdapter.getItem(position).getName();
		final String puid = String.valueOf(peopleAdapter.getItem(position).getUid());
		
		imageLoader.displayImage(image, iv_image, dioptions);
		tv_email.setText(name);
		
		Button b_cancel = (Button) dialog.findViewById(R.id.button_dialog_remove_person_cancel);
		Button b_remove = (Button) dialog.findViewById(R.id.button_dialog_remove_person_remove);
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				switch (v.getId()){
				
				case R.id.button_dialog_remove_person_cancel:{
					dialog.cancel();
				}
				break;
				
				case R.id.button_dialog_remove_person_remove:{
					mRemoveAPWCSUL = new RemoveAPWCSUL();
					mRemoveAPWCSUL.execute(puid,String.valueOf(position));
					dialog.cancel();
				}
				break;
				}
				
			}
		};
		
		b_remove.setOnClickListener(ocl);
		b_cancel.setOnClickListener(ocl);

		dialog.show();
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		
		case R.id.imageView_profile_fragment_image:{
			Intent intent = new Intent(getActivity(),ChangePictureActivity.class);
			startActivity(intent);
		}
		break;
		case R.id.textView_profile_fragment_name:{
			showDialogEditName();
		}
		break;
		case R.id.button_profile_fragment_get_LKL:{
			if(session.getLocLat() != 0 && session.getLocLong() != 0 ){
				Intent intent = new Intent(context,MapActivity.class);
				startActivity(intent);
			} else{
				alert.showAlertDialog(getActivity(), "Not Found!","Please Update your location", false);	
			}
		}
		break;
		case R.id.button_profile_fragment_UML:{
	    	   
	    	double loclat = session.getLocLat(),loclong = session.getLocLong();
	    	   
			// create class object
		    GPSTracker gps = new GPSTracker(context);

			// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	
		       	loclat = gps.getLatitude();
		       	loclong = gps.getLongitude();
		       	
		       	// \n is for new line
		        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm a",Locale.US);
		        String time = df.format(Calendar.getInstance().getTime());
		       	Log.d("location", loclat+ " "+ loclong+ " "+ time);
				mUpdateMyLocation = new UpdateMyLocation();
				mUpdateMyLocation.execute(String.valueOf(loclat),String.valueOf(loclong),time);
		       	//Toast.makeText(context, "Your Location is - \nLat: " + loclat+ "\nLong: " + loclong, Toast.LENGTH_LONG).show();	
		    }else{
		       	// can't get location
		       	// GPS or Network is not enabled
		       	// Ask user to enable GPS/network in settings
		       	gps.showSettingsAlert();
		    }
		}
		break;
		case R.id.button_profile_fragment_add_new:{
			showDialogAddNew();
		}
		break;
		}
		
	}

	private void showDialogEditName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Edit Name");
		final EditText et_name = new EditText(context);
		et_name.setText(session.getName());
		builder.setView(et_name);
		builder.setPositiveButton("Update",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	updateName(et_name.getText().toString());
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

	protected void updateName(String name) {

		UpdateName mUpdateName = new UpdateName();
		mUpdateName.execute(name);
		
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
 
    /**
     * Background Async Task to check and log in user
     * */
   public class CheckAndLogInDetails extends AsyncTask<String, String, Boolean> {

	   private String ATemail; 
	   private String ATpassword;
	   private String message;
	   private int state;
	   private JSONObject ATjson;
	   private JSONObject ATuser;
	   

	   public CheckAndLogInDetails(String email,String password) {  // can take other params if needed
		   this.ATemail = email;
		   this.ATpassword = password;
       	   this.ATjson= new JSONObject();
       	   this.ATuser= new JSONObject();
	   }
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loging In..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected Boolean doInBackground(String... args) {
        	
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("email", ATemail);
            Log.d("pass", ATpassword);
            params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
            params.add(new BasicNameValuePair("email", ATemail));
            params.add(new BasicNameValuePair("password", ATpassword));
 
            // getting JSON Object
            // Note that log in url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_LOG_IN_URL,
                    "POST", params);
 
            // check log cat fro response
            //Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
				message = json.getString(CommonUtilities.TAG_MESSAGE);
				state = json.getInt("state");
                Log.d(CommonUtilities.TAG_SUCCESS, success+"");
                Log.d(CommonUtilities.TAG_MESSAGE, message);
                Log.d("state", ""+state);
                if (success == 1) {
                    // user details verified..ready to log in
                	// add user's id no
                	ATjson = json;
                	
                	ATuser = ATjson.getJSONObject(CommonUtilities.TAG_USER);
                	if(!ATuser.isNull(CommonUtilities.TAG_PWCSUL)){
                	JSONArray pwcsul = ATuser.getJSONArray(CommonUtilities.TAG_PWCSUL);
                	for(int i=0; i<pwcsul.length();i++){
                		JSONObject jperson = pwcsul.getJSONObject(i);
                		PWCSUL person = new PWCSUL();
                		person.setName(jperson.getString(CommonUtilities.TAG_NAME));
                		person.setEmail(jperson.getString(CommonUtilities.TAG_EMAIL));
                		person.setImage(jperson.getString(CommonUtilities.TAG_IMAGE));
                		person.setUid(jperson.getLong(CommonUtilities.TAG_UID));
                 	   	person.setGcmRegId(jperson.getString(CommonUtilities.TAG_GCM_REG_ID));
                		
                		session.getPWCSUL().add(person);
                	}
                	}
                	if(!ATuser.isNull(CommonUtilities.TAG_PWLUCS)){
					JSONArray pwlucs = ATuser.getJSONArray(CommonUtilities.TAG_PWLUCS);
					for(int i=0; i<pwlucs.length();i++){
                		JSONObject jperson = pwlucs.getJSONObject(i);
                		PWLUCS person = new PWLUCS();
                		person.setName(jperson.getString(CommonUtilities.TAG_NAME));
                		person.setEmail(jperson.getString(CommonUtilities.TAG_EMAIL));
                		person.setImage(jperson.getString(CommonUtilities.TAG_IMAGE));
                		person.setGcmRegId(jperson.getString(CommonUtilities.TAG_GCM_REG_ID));
                		person.setUid(jperson.getLong(CommonUtilities.TAG_UID));
                		//person.setCpid(jperson.getLong(CommonUtilities.TAG_CPID));
                		person.setLocLat(jperson.getLong(CommonUtilities.TAG_LOC_LAT));
                		person.setLocLong(jperson.getLong(CommonUtilities.TAG_LOC_LONG));
                		person.setLocTime(jperson.getString(CommonUtilities.TAG_LOC_TIME));
                		
                		session.getPWLUCS().add(person);
                	}
                	}
                	return true;
                	
                } else {
                    // Login failed..", "Email/Password is incorrect
                	return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return false;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result){
            // dismiss the dialog once done
            pDialog.dismiss();
            if(!result){
            	// username / password doesn't match
		        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		        
		        // Setting Dialog Title
		        alertDialog.setTitle("Log In Failed!");
		 
		        // Setting Dialog Message
		        alertDialog.setMessage(message);
		        // Setting OK Button
		        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	getActivity().finish();
		            	dialog.cancel();
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
            
            }
            else{
            	// User email/password verified and checked
            	// Go to main activity (profile) and get user acount details  
            	
                // Creating user login session
                // For testing i am stroing name, email as follow
                // Use user real data

            	try {
            		
					long uid = ATuser.getLong(CommonUtilities.TAG_UID);
					session.createLoginSession(uid,ATemail,ATpassword);
	            	
					String name = ATuser.getString(CommonUtilities.TAG_NAME);
					double loclong = ATuser.getDouble(CommonUtilities.TAG_LOC_LONG);
					double loclat = ATuser.getDouble(CommonUtilities.TAG_LOC_LAT);
					String loctime = ATuser.getString(CommonUtilities.TAG_LOC_TIME);
					String image = ATuser.getString(CommonUtilities.TAG_IMAGE);

	                Log.d("details", name+" "+loclat+" "+loclong+" "+loctime+ " "+image);
					session.setName(name);
					session.setLocLat(loclat);
					session.setLocLong(loclong);
					session.setLocTime(loctime);
					session.setImage(image);
					
					loadFromSession();

					RadarFragment radar = (RadarFragment)getFragmentManager().findFragmentById(R.layout.fragment_radar);
					if(!session.getPWLUCS().isEmpty())
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

	   private String message = new String();
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
           // getting updated data from EditTexts
           long uid = session.getUserID();
           
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_LOC_LAT, args[0]));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_LOC_LONG, args[1]));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_LOC_TIME, args[2]));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_UPDATE_USER_LOCATION_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);
               message = json.getString(CommonUtilities.TAG_MESSAGE);

               if (success == 1) {
                   // location updated
            	   session.setLocLat(Double.valueOf(args[0]));
            	   session.setLocLong(Double.valueOf(args[1]));
            	   session.setLocTime(args[2]);
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
               alert.showAlertDialog(getActivity(), "Location update failed..",message, false);	
           }

       }

   }


   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   public class AddNewPWCSULOption extends AsyncTask<String, String, Boolean> {
	   
	   private PWCSUL person;
	   private String message = "";

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
           // the users id so that this email could not be produced in the result
           params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_PERSON_EMAIL, args[0].toString()));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_LOOK_FOR_PWCSUL_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);
               message = json.getString(CommonUtilities.TAG_MESSAGE);
               Log.d("message", message);
               if (success == 1) {
                   // get person details
            	   JSONObject jsonPerson = json.getJSONObject(CommonUtilities.TAG_PERSON);
            	   person = new PWCSUL();
            	   person.setName(jsonPerson.getString(CommonUtilities.TAG_NAME));
            	   person.setImage(jsonPerson.getString(CommonUtilities.TAG_IMAGE));
            	   person.setEmail(jsonPerson.getString(CommonUtilities.TAG_EMAIL));
            	   person.setUid(jsonPerson.getLong(CommonUtilities.TAG_UID));
            	   person.setGcmRegId(jsonPerson.getString(CommonUtilities.TAG_GCM_REG_ID));
            	   return true;
            	   
               } else {
            	   return false;
       	        
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return false;
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
               alert.showAlertDialog(getActivity(), "No user found!",message, false);	
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
           params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_PERSON_UID, args[0].toString()));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_ADD_NEW_PWCSUL_URL, "POST", params);

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
            	   person.setGcmRegId(jsonPerson.getString(CommonUtilities.TAG_GCM_REG_ID));
            	   return true;
            	   
               } else {
            	   return false;
       	        
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return false;
       }

       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(Boolean result) {
           // dismiss the dialog after getting all products
           pDialog.dismiss();
           if(result){
        	   peopleAdapter.add(person);
        	   peopleAdapter.notifyDataSetChanged();
        	   lv_pwcsul.setSelection(lv_pwcsul.getChildCount()-1);
        	   Toast.makeText(context, "User successfully added!", Toast.LENGTH_LONG).show();
           } else{
               alert.showAlertDialog(getActivity(), "Server Error/Busy!","Please try again.", false);
           }

       }
   }



   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   public class UpdateName extends AsyncTask<String, String, Boolean> {

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(context);
           pDialog.setMessage("Updating...");
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
           params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_NAME, args[0]));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_UPDATE_USER_NAME_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);

               if (success == 1) {
            	   session.setName(args[0]);
            	   return true;
            	   
               } else {
            	   return false;
       	        
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return false;
       }

       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(Boolean result) {
           // dismiss the dialog after getting all products
           pDialog.dismiss();
           if(result){
        	   tv_name.setText(session.getName());
        	   Toast.makeText(context, "Name successfully updated!", Toast.LENGTH_LONG).show();
           } else{
               alert.showAlertDialog(getActivity(), "Server Error/Busy!","Please try again.", false);
           }

       }
   }


   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   public class RemoveAPWCSUL extends AsyncTask<String, String, Boolean> {
	   
	   private int listPosition;

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(context);
           pDialog.setMessage("Removing...");
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
           params.add(new BasicNameValuePair(CommonUtilities.TAG_KNOCK_KNOCK, CommonUtilities.SERVER_KNOCK_KNOCK_CODE));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
           params.add(new BasicNameValuePair(CommonUtilities.TAG_PERSON_UID, args[0].toString()));
           
           // getting JSON string from URL
           JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_REMOVE_PWCSUL_URL, "POST", params);

           // Check your log cat for JSON reponse
           Log.d("Response: ", json.toString());

           try {
               // Checking for SUCCESS TAG
               int success = json.getInt(CommonUtilities.TAG_SUCCESS);

               if (success == 1) {
            	   listPosition = Integer.valueOf(args[1]);
            	   return true;
            	   
               } else {
            	   return false;
       	        
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return false;
       }

       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(Boolean result) {
           // dismiss the dialog after getting all products
           pDialog.dismiss();
           if(result){
        	   peopleAdapter.removeItem(listPosition);
        	   peopleAdapter.notifyDataSetChanged();
        	   Toast.makeText(context, "Person successfully removed!", Toast.LENGTH_LONG).show();
           } else{
               alert.showAlertDialog(getActivity(), "Server Error/Busy!","Please try again.", false);
           }

       }
   }



   @Override
   public void onResume() {
	   if(isImageUpdated){
		   imageLoader.clearDiscCache();
		   imageLoader.displayImage(session.getImage(), iv_image, dioptions);
		   isImageUpdated = false;
	   }
	   super.onResume();
   }


}