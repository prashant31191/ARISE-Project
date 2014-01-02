package com.arise.ariseproject1.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
		//lblMessage.append(newMessage + "\n");           
		Toast.makeText(context, "New Message: " + newMessage, Toast.LENGTH_LONG).show();
		
		// Releasing wake lock
		
	}

}
