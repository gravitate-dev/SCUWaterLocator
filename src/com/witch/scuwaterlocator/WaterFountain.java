package com.witch.scuwaterlocator;

public class WaterFountain {
	
	public WaterFountain( String name, double latitude, double longitude, double temp, double pressure, double taste)
	{
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.temp = temp;
		this.pressure = pressure;
		this.taste = taste;
	}
	
	public WaterFountain() {
		// TODO Auto-generated constructor stub
	}

	public WaterFountain( WaterFountain wf ) {
		WaterFountain temp = new WaterFountain(wf.getName(), wf.getLatitude(), wf.getLongitude(), wf.getTemp(), wf.getPress(), wf.getTaste()); 
	}

	public void setDistanceFromMe( double my_longitude, double my_latitude )
	{
		//i can do sqrt a^2 + b^2
		double a = Math.pow(Math.abs(my_longitude-this.longitude),2);
		double b = Math.pow(Math.abs(my_latitude-this.latitude),2);
		
		this.distanceFromMe = Math.sqrt(a+b)*1000;
	}
	
	public double getLongitude(){return longitude;}	
	public double getLatitude(){return latitude;}
	public String getName(){return name;}
	public double getTemp(){return temp;}
	public double getPress(){return pressure;}
	public double getTaste(){return taste;}
	
	
	
	
	public double getDistanceFromMe()
	{
		return distanceFromMe;
	}
	
	@Override
    public String toString() {
        return this.name; // + this.distanceFromMe + " away from you";
    }
	
	private String name;
	private	double longitude,latitude;
	private double distanceFromMe;
	private double temp,pressure,taste;

}
