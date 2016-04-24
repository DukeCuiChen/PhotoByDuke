package com.duke.photobyduke.entity;

public class URLGetRecentInfo {
	private String id;
	private String owner;
	private String secret;
	private String server;
	private String farm;
	private String title;
	private String ispublic;
	private String isfriend;
	private String isfamily;

	public URLGetRecentInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOwner(String owner){
		this.owner = owner;
	}
	
	public String getOwner(){
		return owner;
	}
	
	public void setSecret(String secret){
		this.secret = secret;
	}
	
	public String getSecret(){
		return secret;
	}
	
	public void setServer(String server){
		this.server = server;
	}
	
	public String getServer(){
		return server;
	}
	
	public void setFarm(String farm){
		this.farm = farm;
	}
	
	public String getFarm(){
		return farm;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setIsFriend(String isfriend){
		this.isfriend = isfriend;
	}
	
	public String getIsFriend(){
		return isfriend;
	}
	
	public void setIsPublic(String ispublic ){
		this.ispublic = ispublic;
	}
	
	public String getIsPublic(){
		return ispublic;
	}
	
	public void setIsFamily(String isfamily){
		this.isfamily = isfamily;
	}
	
	public String getIsFamily(){
		return isfamily;
	}
}
