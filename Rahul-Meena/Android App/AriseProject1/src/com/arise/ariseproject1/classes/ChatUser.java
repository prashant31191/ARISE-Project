package com.arise.ariseproject1.classes;

public class ChatUser {
	
	private String name;
	private String image;
	private long uid;
	private int noOfMsgs;
	private long cpid;
	

	public ChatUser(String name, String image, long uid,long cpid, int noOfMsgs) {
		super();
		this.name = name;
		this.uid = uid;
		this.noOfMsgs = noOfMsgs;
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
	
	public long getCpid(){
		return this.cpid;
	}
	
	public void setNoOfMsgs(int noOfMsgs){
		this.noOfMsgs = noOfMsgs;
	}
}
