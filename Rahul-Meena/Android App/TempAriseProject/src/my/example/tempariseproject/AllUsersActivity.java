package my.example.tempariseproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AllUsersActivity extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParserClass jParser = new JSONParserClass();
 
    ArrayList<HashMap<String, String>> usersList;
 
    // url to get all products list
    private static String url_all_users = "http://10.0.2.2/users/allusers.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERS = "users";
    private static final String TAG_UID = "uid";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_LOCATION = "location";
 
    // products JSONArray
    JSONArray users = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
 
        // Hashmap for ListView
        usersList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllUsers().execute();
 
        // Get listview
        ListView lv = getListView();
 
    
    }
 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllUsers extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllUsersActivity.this);
            pDialog.setMessage("Loading users. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_users, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    users = json.getJSONArray(TAG_USERS);
 
                    // looping through All Products
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_UID);
                        String name = c.getString(TAG_NAME)+"\n"+c.getString(TAG_EMAIL)+"\n"+ c.getString(TAG_LOCATION);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_UID, id);
                        map.put(TAG_NAME, name);
 
                        // adding HashList to ArrayList
                        usersList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            RegisterActivity.class);
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
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllUsersActivity.this, usersList,
                            R.layout.list_item, new String[] { TAG_UID,
                                    TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}
