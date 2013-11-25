package my.example.tempariseproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class UMLActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParserClass jsonParser = new JSONParserClass();
 
    // url to update location
    private static final String url_update_location = "http://10.0.2.2/users/sendlocation.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_LOCATION = "location";
    
	EditText et_email,et_location;
	Button b_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uml);
		
		et_email=(EditText)findViewById(R.id.editText_uml_email);
		et_location=(EditText)findViewById(R.id.editText_uml_location);
		b_update=(Button)findViewById(R.id.button_uml_update);
		
		b_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                // starting background task to update product
                new UpdateLocation().execute();
			}
		});
	}
 
    /**
     * Background Async Task to  Save product Details
     * */
    class UpdateLocation extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UMLActivity.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
            String email = et_email.getText().toString();
            String location = et_location.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            params.add(new BasicNameValuePair(TAG_LOCATION, location));
 
            // sending modified data through http request
            // Notice that update product url accepts POST method
            
            JSONObject json = jsonParser.makeHttpRequest(url_update_location,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                	Log.d("Update", "Successful");
                    finish();
                } else {
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.uml, menu);
		return true;
	}

}
