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

public class RegisterActivity extends Activity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParserClass jsonParser = new JSONParserClass();

	EditText et_name,et_email;
	Button b_register;
 
    // url to create new product
    private static String url_create_user = "http://10.0.2.2/users/register.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		et_name=(EditText)findViewById(R.id.editText_register_name);
		et_email=(EditText)findViewById(R.id.editText_register_email);
		b_register=(Button)findViewById(R.id.button_register_register);
		
		b_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new CreateNewUser().execute();
			}
		});
	}
 
    /**
     * Background Async Task to Create new product
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = et_name.getText().toString();
            String email = et_email.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), AllUsersActivity.class);
                    startActivity(i);
 
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
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
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
