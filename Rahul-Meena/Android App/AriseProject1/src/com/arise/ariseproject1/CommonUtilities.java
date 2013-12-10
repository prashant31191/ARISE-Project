package com.arise.ariseproject1;

import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {
     
    // give your server registration url here
    static final String SERVER_REGISTER_URL = "http://10.0.2.2/users/register.php";
    
    // give your server user exist url here
    static final String SERVER_USER_EXIST_URL = "http://10.0.2.2/users/user_exist.php";
    
    // Google project id
    static final String SENDER_ID = "850154662912"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";
 
    static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}