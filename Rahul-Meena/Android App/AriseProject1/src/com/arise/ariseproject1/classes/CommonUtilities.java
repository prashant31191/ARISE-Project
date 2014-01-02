package com.arise.ariseproject1.classes;

import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {
     
    // give your server registration url here
    public static final String SERVER_REGISTER_URL = "http://10.0.2.2/users/register.php";
    
    // give your server user exist url here
    public static final String SERVER_USER_EXIST_URL = "http://10.0.2.2/users/user_exist.php";
    
    // give your server user exist url here
    public static final String SERVER_LOG_IN_URL = "http://10.0.2.2/users/log_in.php";
    
    // give your server user exist url here
    public static final String SERVER_IMAGE_URL = "http://10.0.2.2/users/photo/";
    
    // give your server update user location url here
    public static final String SERVER_UPDATE_USER_LOCATION_URL = "http://10.0.2.2/users/photo/";
    
    // give your server update user location url here
    public static final String SERVER_LOOK_FOR_PWCSUL_URL = "http://10.0.2.2/users/photo/";

    // give your server update user location url here
    public static final String SERVER_ADD_PWCSUL_URL = "http://10.0.2.2/users/photo/";
    
  	// JSON Node names
    public static final String TAG_SUCCESS = "success";
    
  	// TAGS
    public static final String TAG_UID = "uid";
    
    public static final String TAG_NAME = "name";
    
    public static final String TAG_EMAIL = "email";
    
    public static final String TAG_LOC_LONG = "loclong";
    
    public static final String TAG_LOC_LAT = "loclat";
    
    public static final String TAG_IMAGE = "email";
    
    public static final String TAG_PWCSUL = "pwcsul";
    
    public static final String TAG_PWLUCS = "pwlucs";
    
    public static final String TAG_PERSON = "person";
    
    public static final String TAG_PERSON_UID = "puid";
    
    // Chat case
    public static final String TAG_CHAT_CASE = "case";
    
    // Google project id
    public static final String SENDER_ID = "850154662912"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";
 
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
 
    public static final String EXTRA_MESSAGE = "message";
 
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
