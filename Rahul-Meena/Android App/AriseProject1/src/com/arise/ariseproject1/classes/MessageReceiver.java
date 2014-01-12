package com.arise.ariseproject1.classes;

import org.json.JSONException;

import com.arise.ariseproject1.ChatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receiving push messages
 * */
public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
		// Waking up mobile if it is sleeping
		WakeLocker.acquire(context);
     
		/**
		 * Take appropriate action on this message
		 * depending upon your app requirement
		 * For now i am just displaying it on the screen
		 * */
     
		// Showing received message
		try {
			ChatActivity.newMessageReceived(newMessage);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lblMessage.append(newMessage + "\n");           
		//Toast.makeText(context, "New Message: " + newMessage, Toast.LENGTH_LONG).show();
		
		// Releasing wake lock
		WakeLocker.release();
		
	}

}
