package com.arise.ariseproject1.classes;

import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {

   // give your server knock knock tag
   public static final String TAG_KNOCK_KNOCK = "knockknock";
   
   // give your server knock knock code
   public static final String SERVER_KNOCK_KNOCK_CODE = "WhoIsThere?";
   
    // give your server registration url here
    public static final String SERVER_REGISTER_URL = "http://172.17.1.103/users/register.php";

    // give your server un-registration url here
	public static final String SERVER_UNREGISTER_URL = "http://172.17.1.103/users/unregister.php";
    
    // give your server user exist url here
    public static final String SERVER_USER_EXIST_URL = "http://172.17.1.103/users/user_exist.php";
    
    // give your server login url here
    public static final String SERVER_LOG_IN_URL = "http://172.17.1.103/users/login.php";
    
    // give your server upload image url here
    public static final String SERVER_UPLOAD_IMAGE_URL = "http://172.17.1.103/users/upload_image.php";
    
    // give your server images folder url here
    public static final String SERVER_IMAGE_URL = "http://172.17.1.103/users/images/";
    
    // give your server update user name url here
    public static final String SERVER_UPDATE_USER_NAME_URL = "http://172.17.1.103/users/update_name.php";
    
    // give your server update user location url here
    public static final String SERVER_UPDATE_USER_LOCATION_URL = "http://172.17.1.103/users/update_location.php";
    
    // give your server look for person to add url here
    public static final String SERVER_LOOK_FOR_PWCSUL_URL = "http://172.17.1.103/users/look_for_person.php";

    // give your server add new pwcsul url here
    public static final String SERVER_ADD_NEW_PWCSUL_URL = "http://172.17.1.103/users/add_new_pwcsul.php";;

    // give your server remove pwcsul url here
    public static final String SERVER_REMOVE_PWCSUL_URL = "http://172.17.1.103/users/remove_pwcsul.php";
    
    // give your server send chat message url here
	public static final String SERVER_SEND_CHAT_MESSAGE_URL = "http://172.17.1.103/users/send_message.php";
	
	// give your server send change password link url here
	public static final String SERVER_SEND_RESET_PASSWORD_LINK_URL = "http://172.17.1.103/users/send_change_password_link.php";
	
	// give your server set new password after login url here
	public static final String SERVER_NEW_PASSWORD_AFTER_LOG_IN_URL = "http://172.17.1.103/users/set_new_password_after_login.php";

    
    
  	// JSON TAGS
    public static final String TAG_SUCCESS = "success";

	public static final String TAG_USER = "user";
    
    public static final String TAG_UID = "uid";
    
    public static final String TAG_NAME = "name";
    
    public static final String TAG_EMAIL = "email";
    
    public static final String TAG_LOC_LONG = "loclong";
    
    public static final String TAG_LOC_LAT = "loclat";

	public static final String TAG_LOC_TIME = "loctime";
    
    public static final String TAG_IMAGE = "image";

	public static final String TAG_GCM_REG_ID = "gcm_regid";

	//public static final String TAG_CPID = "cpid";
    
    public static final String TAG_PWCSUL = "pwcsul";
    
    public static final String TAG_PWLUCS = "pwlucs";
    
    public static final String TAG_PERSON = "person";

	public static final String TAG_PERSON_EMAIL = "pemail";
    
    public static final String TAG_PERSON_UID = "puid";

	public static final String TAG_CURRENT_PASSWORD = "current_password";

	public static final String TAG_NEW_PASSWORD = "new_password";
    
    // GCM Message Architecture
    public static final String TAG_MESSAGE = "message";
    
    public static final String TAG_MESSAGE_SENDER_UID = "sender_uid";
    
    public static final String TAG_MESSAGE_SENDER_NAME = "sender_name";

	public static final String TAG_MESSAGE_RECIEVER_UID = "receiver_uid";

	public static final String TAG_MESSAGE_RECIEVER_GCM_REGID = "receiver_gcm_regid";
    
    //public static final String TAG_MESSAGE_CPID = "cpid";
    
    public static final String TAG_MESSAGE_CONTENT = "content";
    
    public static final String TAG_MESSAGE_TIME = "time";
    
    // Chat case
    public static final String TAG_CHAT_CASE = "case";
    
    // Google project id
    public static final String SENDER_ID = "850154662912"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "ARISEApp GCM";
 
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.arise.pushnotifications.DISPLAY_MESSAGE";
 
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
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
