package com.duke.photobyduke.entity;

import java.io.Serializable;

public class CinemaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cinemaTitle;
	private String cinemaPhotoUrl;
	
	public CinemaBean() {
		
	}
	
	public void setCinemaTitle(String cinemaTitle){
		this.cinemaTitle = cinemaTitle;
	}
	
	public String getCinemaTitle(){
		return cinemaTitle;
	}
	
	public void setCinemaPhotoUrl(String cinamePhotoUrl){
		this.cinemaPhotoUrl = cinamePhotoUrl;
	}
	
	public String getCinemaPhotoUrl(){
		return cinemaPhotoUrl;
	}
}
