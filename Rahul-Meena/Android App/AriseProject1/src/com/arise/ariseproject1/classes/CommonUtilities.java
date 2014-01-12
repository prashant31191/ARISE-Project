package com.arise.ariseproject1.classes;

import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {
     
    // give your server registration url here
    public static final String SERVER_REGISTER_URL = "http://10.0.2.2/users/register.php";

    //
	public static final String SERVER_UNREGISTER_URL = "http://10.0.2.2/users/unregister.php";
    
    // give your server user exist url here
    public static final String SERVER_USER_EXIST_URL = "http://10.0.2.2/users/user_exist.php";
    
    // give your server user exist url here
    public static final String SERVER_LOG_IN_URL = "http://10.0.2.2/users/login.php";
    
    // give your server user exist url here
    public static final String SERVER_IMAGE_URL = "http://10.0.2.2/users/photo/";
    
    // give your server update user location url here
    public static final String SERVER_UPDATE_USER_LOCATION_URL = "http://10.0.2.2/users/update_location.php";
    
    // give your server update user location url here
    public static final String SERVER_LOOK_FOR_PWCSUL_URL = "http://10.0.2.2/users/look_for_person.php";

    // give your server update user location url here
    public static final String SERVER_ADD_NEW_PWCSUL_URL = "http://10.0.2.2/users/add_new_pwcsul.php";

    //
	public static final String SERVER_SEND_CHAT_MESSAGE_URL = "http://10.0.2.2/users/photo/";
	
	//
	public static final String SERVER_SEND_RESET_PASSWORD_LINK_URL = "http://10.0.2.2/users/photo/";
	
	//
	public static final String SERVER_CHANGE_PASSWORD_URL = "http://10.0.2.2/users/photo/";
    
  	// JSON Node names
    public static final String TAG_SUCCESS = "success";

	public static final String TAG_USER = "user";
    
  	// TAGS
    public static final String TAG_UID = "uid";
    
    public static final String TAG_NAME = "name";
    
    public static final String TAG_EMAIL = "email";
    
    public static final String TAG_LOC_LONG = "loclong";
    
    public static final String TAG_LOC_LAT = "loclat";

	public static final String TAG_LOC_TIME = "loctime";
    
    public static final String TAG_IMAGE = "email";

	public static final String TAG_GCM_REG_ID = "gcmregid";

	public static final String TAG_CPID = "cpid";
    
    public static final String TAG_PWCSUL = "pwcsul";
    
    public static final String TAG_PWLUCS = "pwlucs";
    
    public static final String TAG_PERSON = "person";

	public static final String TAG_PERSON_EMAIL = "pemail";
    
    public static final String TAG_PERSON_UID = "puid";

	public static final String TAG_CURRENT_PASSWORD = "current_password";

	public static final String TAG_NEW_PASSWORD = "new_password";
    
    // GCM Message Architecture
    public static final String TAG_MESSAGE = "message";
    
    public static final String TAG_MESSAGE_FROM_UID = "from_uid";

	public static final String TAG_MESSAGE_FROM_NAME = "from_name";

	public static final String TAG_MESSAGE_FROM_IMAGE = "from_image";
    
    public static final String TAG_MESSAGE_CPID = "cpid";
    
    public static final String TAG_MESSAGE_CONTENT = "content";
    
    public static final String TAG_MESSAGE_TIME = "time";
    
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
