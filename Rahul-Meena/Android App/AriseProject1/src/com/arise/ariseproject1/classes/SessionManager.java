package com.arise.ariseproject1.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.arise.ariseproject1.LogInActivity;
import com.arise.ariseproject1.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
   // Shared Preferences
   SharedPreferences pref;
    
   // Editor for Shared preferences
   Editor editor;
    
   // Context
   Context _context;
    
   // Shared pref mode
   int PRIVATE_MODE = 0;
    
   // Sharedpref file name
   private static final String PREF_NAME = "MyPref";
    
   // All Shared Preferences Keys
   private static final String IS_LOGIN = "IsLoggedIn";
    
   // User name (make variable public to access from outside)
   public static final String KEY_UID = "uid";
    
   // Email address (make variable public to access from outside)
   public static final String KEY_EMAIL = "email";
   
   // Password (make variable private to deny access from outside)
   public static final String KEY_PASS = "password";
   
   public static int PROFILE_DOWNLOADED = 0; // 0 - no, 1 - yes
   
   public static String name = new String();
   
   public static double loclat = 0;
   
   public static double loclong = 0;
   
   public static String loctime = new String();
   
   public static String image = new String();
   
   public static List<PWLUCS> pwlucs;
   
   public static List<PWCSUL> pwcsul;
    
   // Constructor
   public SessionManager(Context context){
       this._context = context;
       pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
       editor = pref.edit();
       editor.commit();
   }
   /**
    * Set value for constant
    */
   public void setPDValue(int value){
	   SessionManager.PROFILE_DOWNLOADED = value;
   }
   /**
    * Get value of const
    */
   public int getPDValue(){
	   return SessionManager.PROFILE_DOWNLOADED;
   }
   /**
    * Set name of user
    */
   public void setName(String name){
	   SessionManager.name = name;
   }
   /**
    * Get name of user
    */
   public String getName(){
	   return SessionManager.name;
   }
   /**
    * Set loclat of user
    */
   public void setLocLat(double loclat){
	   SessionManager.loclat = loclat;
   }
   /**
    * Get loclat of user
    */
   public double getLocLat(){
	   return SessionManager.loclat;
   }
   /**
    * Set loclong of user
    */
   public void setLocLong(double loclong){
	   SessionManager.loclong = loclong;
   }
   /**
    * Get loclong of user
    */
   public double getLocLong(){
	   return SessionManager.loclong;
   }
   /**
    * Set loclat of user
    */
   public void setLocTime(String loctime){
	   SessionManager.loctime = loctime;
   }
   /**
    * Get loclat of user
    */
   public String getLocTime(){
	   return SessionManager.loctime;
   }
   /**
    * Set image of user
    */
   public void setImage(String image){
	   SessionManager.image = image;
   }
   /**
    * Get image of user
    */
   public String getImage(){
	   return SessionManager.image;
   }
   
   
   /**
    * Set pwlucs list
    */
   public void setPWLUCS(List<PWLUCS> pwlucs){
	   SessionManager.pwlucs = new ArrayList<PWLUCS>();
	   SessionManager.pwlucs = pwlucs; 
   }
   /**
    * Get pwlucs list
    */
   public List<PWLUCS> getPWLUCS(){
	   return SessionManager.pwlucs; 
   }
   /**
    * Set pwcsul list
    */
   public void setPWCSUL(List<PWCSUL> pwcsul){
	   SessionManager.pwcsul = new ArrayList<PWCSUL>();
	   SessionManager.pwcsul = pwcsul; 
   }
   /**
    * Get pwcsul list
    */
   public List<PWCSUL> getPWCSUL(){
	   return SessionManager.pwcsul;
   }
    
   /**
    * Create login session
    * */
   public void createLoginSession(long uid, String email, String password){
       // Storing login value as TRUE
       editor.putBoolean(IS_LOGIN, true);
        
       // Storing long in pref
       editor.putLong(KEY_UID, uid);
        
       // Storing email in pref
       editor.putString(KEY_EMAIL, email);
       
      // Storing password in pref
      editor.putString(KEY_PASS, password);
        
       // commit changes
       editor.commit();
   }
    
   /**
    * Check login method wil check user login status
    * If false it will redirect user to login page
    * Else won't do anything
    * */
   public void checkLogin(){
       // Check login status
       if(this.isLoggedIn()){
           // user is logged in redirect him to Main Activity
           Intent i = new Intent(_context, MainActivity.class);
           // Closing all the Activities
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
           // Add new Flag to start new Activity
           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
           // Staring Login Activity
           _context.startActivity(i);
       } else{

           // user is logged in redirect him to Main Activity
           Intent i = new Intent(_context, LogInActivity.class);
           // Closing all the Activities
           i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
           // Add new Flag to start new Activity
           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
           // Staring Login Activity
           _context.startActivity(i);
       }
        
   }
    
    
    
   /**
    * Get stored session data
    * */
   public HashMap<String, String> getUserDetails(){
       HashMap<String, String> user = new HashMap<String, String>();
        
       // user email id
       user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
       
       // user password
       user.put(KEY_PASS, pref.getString(KEY_PASS, null));
        
       // return user
       return user;
   }
   
  /**
   * Get user email
   * */
  public String getUserEmail(){
       
      // return email
      return pref.getString(KEY_EMAIL, null);
  }
  
 /**
  * Get user id
  * */
 public long getUserID(){
      
     // return email
     return pref.getLong(KEY_UID, 0);
 }
    
   /**
    * Clear session details
    * */
   public void logoutUser(){
       // Clearing all data from Shared Preferences
       editor.clear();
       editor.commit();
       
       // After logout redirect user to Loing Activity
       Intent i = new Intent(_context, LogInActivity.class);
       // Closing all the Activities
       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
       // Add new Flag to start new Activity
       i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
       // Staring Login Activity
       _context.startActivity(i);
   }
    
   /**
    * Quick check for login
    * **/
   // Get Login State
   public boolean isLoggedIn(){
       return pref.getBoolean(IS_LOGIN, false);
   }
}