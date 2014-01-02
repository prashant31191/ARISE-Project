package com.arise.ariseproject1.classes;

import android.content.Context;
import android.location.Location;

public class PWCSUL {

	private Context context;
	private String name;
	private String email;
	private String image;
	private long uid;
	
	public PWCSUL(){
		
	}
	
	public PWCSUL(Context context, String name, String email, String image, long uid){
		this.context = context;
		this.name = name;
		this.email = email;
		this.image = image;
		this.uid = uid;
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
}

