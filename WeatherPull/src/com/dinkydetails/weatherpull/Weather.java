package com.dinkydetails.weatherpull;

public class Weather {
	
	private String zip = null;
	
	private String city = null;
	
	private String state = null;
	
	private String feelslike = null;
	
	private float temp_f = 0.0F;
	
	
	public Weather() {
		// TODO Auto-generated constructor stub
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFeelslike() {
		return feelslike;
	}

	public void setFeelslike(String feelslike) {
		this.feelslike = feelslike;
	}

	public float getTemp_f() {
		return temp_f;
	}

	public void setTemp_f(float temp_f) {
		this.temp_f = temp_f;
	}

}
