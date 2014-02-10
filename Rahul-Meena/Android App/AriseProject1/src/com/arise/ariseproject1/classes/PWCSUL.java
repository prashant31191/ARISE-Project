package com.arise.ariseproject1.classes;

public class PWCSUL {
	private String name;
	private String email;
	private String image;
	private long uid;
	private String gcm_regid;
	
	public PWCSUL(){
		
	}
	
	public PWCSUL( String name, String email, String image, long uid, String gcm_regid){
		this.name = name;
		this.email = email;
		this.image = image;
		this.uid = uid;
		this.gcm_regid = gcm_regid;
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
		this.image =  CommonUtilities.SERVER_IMAGE_URL+image;;
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

	public void setGcmRegId(String gcmregid){
		this.gcm_regid = gcmregid;
	}
	
	public String getGcmRegId(){
		return this.gcm_regid;
	}
}

