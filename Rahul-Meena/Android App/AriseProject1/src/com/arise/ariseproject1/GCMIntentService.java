package com.arise.ariseproject1;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
 
import com.arise.ariseproject1.R;
import com.arise.ariseproject1.classes.CommonUtilities;
import com.arise.ariseproject1.classes.ServerUtilities;
import com.google.android.gcm.GCMBaseIntentService;
 
import static com.arise.ariseproject1.classes.CommonUtilities.SENDER_ID;
 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String TAG = "GCMIntentService";
 
    public GCMIntentService() {
        super(SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        Toast.makeText(context,"Your device registred with GCM",Toast.LENGTH_LONG).show();
        Log.d("NAME", RegisterActivity.full_name);
        ServerUtilities.register(context, RegisterActivity.full_name, RegisterActivity.email,RegisterActivity.password, registrationId);
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        Toast.makeText(context, getString(R.string.gcm_unregistered),Toast.LENGTH_LONG).show();
        ServerUtilities.unregister(context, registrationId);
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString(CommonUtilities.TAG_MESSAGE);
        
        CommonUtilities.displayMessage(context, message);
	    // notifies user
	    try {
			generateNotification(context, message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        //displayMessage(context, message);
        // notifies user
        try {
			generateNotification(context, message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        //displayMessage(context, getString(R.string.gcm_error, errorId));
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
       // displayMessage(context, getString(R.string.gcm_recoverable_error,errorId));
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     * @throws JSONException 
     */
    private static void generateNotification(Context context, String message) throws JSONException { 
    	
		JSONObject jObject = new JSONObject(message);
	  //String from_uid = jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_UID);
		String from_name = jObject.getString(CommonUtilities.TAG_MESSAGE_SENDER_NAME);
      //String cpid = jObject.getString(CommonUtilities.TAG_MESSAGE_CPID);
		String content = jObject.getString(CommonUtilities.TAG_MESSAGE_CONTENT);
	  //String time = jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_UID);
		
       /* HashMap<String, String> map = new HashMap<String, String>();
        
        map.put(CommonUtilities.TAG_MESSAGE_FROM_UID, jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_UID));
        map.put(CommonUtilities.TAG_MESSAGE_FROM_NAME, jObject.getString(CommonUtilities.TAG_MESSAGE_FROM_NAME));
        map.put(CommonUtilities.TAG_MESSAGE_CPID, jObject.getString(CommonUtilities.TAG_MESSAGE_CPID));
        map.put(CommonUtilities.TAG_MESSAGE_CONTENT, jObject.getString(CommonUtilities.TAG_MESSAGE_CONTENT));
        map.put(CommonUtilities.TAG_MESSAGE_TIME, jObject.getString(CommonUtilities.TAG_MESSAGE_TIME));
        */
        int icon = R.drawable.ic_launcher;
        String title = "new message from "+from_name;
        long when = System.currentTimeMillis();
        
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notification = new Notification(icon, map, when);
         
        Intent notificationIntent = new Intent(context, ChatActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder =  
                new NotificationCompat.Builder(context)  
                .setSmallIcon(icon)  
                .setContentTitle(title)  
                .setContentText(content)
        		.setWhen(when)
        		.setContentIntent(intent);
        
        Notification notification = builder.build();
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      
 
    }
 
}
