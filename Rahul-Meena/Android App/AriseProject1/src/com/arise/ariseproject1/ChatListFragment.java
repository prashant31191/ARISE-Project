package com.arise.ariseproject1;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.arise.ariseproject1.adapter.ChatSessionArrayAdapter;
import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.JSONParser;
import com.arise.ariseproject1.classes.OneComment;
import com.arise.ariseproject1.classes.SessionManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ChatListFragment extends ListFragment {

	ChatSessionArrayAdapter adapter;
	//private static Random random;
	private SessionManager session;
	private JSONParser jsonParser;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		adapter = new ChatSessionArrayAdapter(getActivity().getApplicationContext(), R.layout.listitem_discuss);
		setListAdapter(adapter);
		//random = new Random();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void send(String msg, long cpid){

		adapter.add(new OneComment(false, msg));
		getListView().setSelection(getListView().getChildCount()-1);
		SendChatMessage scm = new SendChatMessage();
		scm.execute(String.valueOf(cpid), msg);
	}

	public void receive(String msg){

		adapter.add(new OneComment(true, msg));
		getListView().setSelection(getListView().getChildCount()-1);
	}
/*
	private void addItems() {
		adapter.add(new OneComment(true, "Hello bubbles!"));

		for (int i = 0; i < 4; i++) {
			boolean left = getRandomInteger(0, 1) == 0 ? true : false;
			String words = "adsadasdas";

			adapter.add(new OneComment(left, words));
		}
	}

	private static int getRandomInteger(int aStart, int aEnd) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * random.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}

*/

    /**
     * Background Async Task to send chat message
     * */
    public class SendChatMessage extends AsyncTask<String, String, Boolean> {

        /**
         * sending chat message
         * */
        protected Boolean doInBackground(String... args) {
            // getting data from session
            long uid = session.getUserID();
            String name = session.getName();
            String image = session.getImage();
            
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonUtilities.TAG_UID, Long.toString(uid)));
            params.add(new BasicNameValuePair(CommonUtilities.TAG_NAME, name));
            params.add(new BasicNameValuePair(CommonUtilities.TAG_IMAGE, image));
            params.add(new BasicNameValuePair(CommonUtilities.TAG_MESSAGE_CPID, args[0]));
            params.add(new BasicNameValuePair(CommonUtilities.TAG_MESSAGE_CONTENT, args[1]));
            
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.SERVER_SEND_CHAT_MESSAGE_URL, "POST", params);

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
         * After completing background task show toast
         * **/
        protected void onPostExecute(Boolean result) {
            if(result){
         	   Toast.makeText(getActivity().getApplicationContext(), "msg successfully sent!", Toast.LENGTH_LONG).show();
            } else{
         	   Toast.makeText(getActivity().getApplicationContext(), "msg not sent!",Toast.LENGTH_SHORT).show();	
            }

        }
    }
}
