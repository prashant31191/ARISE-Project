package my.example.tempariseproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;

public class RadarActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParserClass jParser = new JSONParserClass();
 
    // url to get all products list
    private static String url_get_user_location = "http://10.0.2.2/users/receivelocation.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "user";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_LOCATION = "location";
    
    // products JSONArray
    JSONArray users = null;

	Button b_look;
	EditText et_email;
	TextView tv_location;
	String location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radar);
		
		et_email=(EditText)findViewById(R.id.editText_radar_email);
		b_look=(Button)findViewById(R.id.button_radar_look);
		tv_location=(TextView)findViewById(R.id.textView_radar_location);
		
		b_look.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
 
				new GetLocation().execute();
			}
		});
	}
 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class GetLocation extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RadarActivity.this);
            pDialog.setMessage("Loading users. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // getting updated data from EditTexts
            String email = et_email.getText().toString();
            
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_get_user_location, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    users = json.getJSONArray(TAG_USER);
 
                    // looping through All Products
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);
 
                        // Storing each json item in variable
                        String name = c.getString(TAG_NAME)+"\n"+ c.getString(TAG_LOCATION);
 
                        // adding to location
                        
                        location = name;
                        
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into TextView
                     * */
                	tv_location.setText(location);
                }
            });
 
        }
 
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.radar, menu);
		return true;
	}

}
