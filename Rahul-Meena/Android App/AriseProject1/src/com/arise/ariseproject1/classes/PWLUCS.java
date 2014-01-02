package com.arise.ariseproject1.classes;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class PWLUCS {

	private Context context;
	private String name;
	private String email;
	private String image;
	private long uid;
	private double loclat;
	private double loclong;
	
	public PWLUCS(){
		
	}
	
	public PWLUCS(Context context, JSONObject person) throws JSONException{
		this.context = context;
		this.name = person.getString(CommonUtilities.TAG_NAME);
		this.email = person.getString(CommonUtilities.TAG_EMAIL);
		this.image = person.getString(CommonUtilities.TAG_IMAGE);
		this.uid = person.getLong(CommonUtilities.TAG_UID);
		this.loclat = person.getDouble(CommonUtilities.TAG_LOC_LAT);
		this.loclong= person.getDouble(CommonUtilities.TAG_LOC_LONG);
	}

	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return this.email;
	}

	public void setImage(String image){
		this.image = image;
	}
	
	public String getImage(){
		return this.image;
	}

	public void setUid(long uid){
		this.uid = uid;
	}
	
	public long getUid(){
		return this.uid;
	}
	
	public void setLocLat(double loclat){
		this.loclat = loclat;
	}
	
	public double getLocLat(){
		return loclat;
	}
	
	public void setLocLong(double loclong){
		this.loclong = loclong;
	}
	
	public double getLoclong(){
		return this.loclong;
	}
	
}
