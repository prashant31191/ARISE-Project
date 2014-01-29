package com.arise.ariseproject1.classes;

import org.json.JSONException;
import org.json.JSONObject;

public class PWLUCS {
	
	private String name;
	private String email;
	private String image;
	private String gcmregid;
	private long uid;
	//private long cpid;
	private double loclat;
	private double loclong;
	private String loctime;
	
	public PWLUCS(){
		
	}
	
	public PWLUCS(JSONObject person) throws JSONException{
		this.name = person.getString(CommonUtilities.TAG_NAME);
		this.email = person.getString(CommonUtilities.TAG_EMAIL);
		this.image = person.getString(CommonUtilities.TAG_IMAGE);
		this.gcmregid = person.getString(CommonUtilities.TAG_GCM_REG_ID);
		this.uid = person.getLong(CommonUtilities.TAG_UID);
		//this.cpid = person.getLong(CommonUtilities.TAG_CPID);
		this.loclat = person.getDouble(CommonUtilities.TAG_LOC_LAT);
		this.loclong= person.getDouble(CommonUtilities.TAG_LOC_LONG);
		this.loctime= person.getString(CommonUtilities.TAG_LOC_TIME);
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

	public void setGcmRegId(String gcmregid){
		this.gcmregid = gcmregid;
	}
	
	public String getGcmRegId(){
		return this.gcmregid;
	}

	public void setUid(long uid){
		this.uid = uid;
	}
	
	public long getUid(){
		return this.uid;
	}
	/*

	public void setCpid(long cpid){
		this.cpid = cpid;
	}
	
	public long getCpid(){
		return this.cpid;
	}*/
	
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
	
	public void setLocTime(String loctime){
		this.loctime = loctime;
	}

	public String getLocTime() {
		// TODO Auto-generated method stub
		return this.loctime;
	}
	
}
