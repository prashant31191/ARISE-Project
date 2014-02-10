package com.arise.ariseproject1.classes;

public class ChatUser {
	
	private String name;
	private String image;
	private long uid;
	private int noOfMsgs;
	private String gcm_regid;
	//private long cpid;
	

	public ChatUser(String name, String image, long uid, int noOfMsgs, String gcm_regid) {
		super();
		this.name = name;
		this.uid = uid;
		this.image =  CommonUtilities.SERVER_IMAGE_URL+image;
		this.noOfMsgs = noOfMsgs;
		this.gcm_regid = gcm_regid;
	}
	public String getName(){
		return this.name;
	}
	
	public String getImage(){
		return this.image;
	}
	
	public long getUid(){
		return this.uid;
	}

	public int getNoOfMsgs(){
		return this.noOfMsgs;
	}
	/*
	public long getCpid(){
		return this.cpid;
	}*/
	
	public void setNoOfMsgs(int noOfMsgs){
		this.noOfMsgs = noOfMsgs;
	}

	public void setGcmRegId(String gcmregid){
		this.gcm_regid = gcmregid;
	}
	
	public String getGcmRegId(){
		return this.gcm_regid;
	}
}
