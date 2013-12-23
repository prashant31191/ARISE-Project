package com.arise.ariseproject1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.adapter.LazyPeopleAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements OnClickListener{
	
    public Context context;
    private String activityAssignedValue ="";
    private static final String STRING_VALUE ="stringValue";
	
	private Button b_get_LKL,b_add_new,b_UML;
	private ImageView iv_image;
	private ListView lv_pwcsul;
	private RelativeLayout rl_none;
	private TextView tv_name;
	private LazyPeopleAdapter peopleAdapter;
	private DisplayImageOptions dioptions;


	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private CheckAndLogInDetails mCheckAndLogIndetails;
	
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
        
    	mCheckAndLogIndetails = new CheckAndLogInDetails(context, email, password);
    	mCheckAndLogIndetails.execute();
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){

		case R.id.button_profile_fragment_get_LKL:{
			
		}
		break;
		case R.id.button_profile_fragment_UML:{
			
		}
		break;
		case R.id.button_profile_fragment_add_new:{
			
		}
		break;
		}
		
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
					String image_link = ATjson.getString(CommonUtilities.TAG_IMAGE);
					JSONArray pwcsul = ATjson.getJSONArray(CommonUtilities.TAG_PWCSUL);
					JSONArray pwlucs = ATjson.getJSONArray(CommonUtilities.TAG_PWLUCS);

			        imageLoader.displayImage(image_link, iv_image,dioptions);
			        tv_name.setText(name);
					peopleAdapter = new LazyPeopleAdapter(ATcontext, pwcsul, getActivity().getLayoutInflater(),imageLoader, dioptions);
					lv_pwcsul.setAdapter(peopleAdapter);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
                }
            }
        }



}