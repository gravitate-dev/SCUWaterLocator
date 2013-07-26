package com.witch.scuwaterlocator;

import com.example.scuwaterlocator.R;
import com.witch.scuwaterlocator.MySurfaceView;
import com.witch.scuwaterlocator.WaterFountain;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener, SensorEventListener   {

	/* this class implements LocationListener, which listens for both
	 * changes in the location of the device and changes in the status
	 * of the GPS system.
	 * */
	
	WaterFountain target = new WaterFountain();
	static final String tag = "Main"; // for Log
	
	TextView txtInfo;
	LocationManager lm; 
	StringBuilder sb;
	int noOfFixes = 0;
	RatingBar tempRatingBar,pressureRatingBar,tasteRatingBar;
	MySurfaceView mySV;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    
    private boolean hasStartedListOnce = false;
    

	float[] rotationMatrix = new float[9];
	float[] lastAcceleration = new float[5];
	float[] lastMagneticField = new float[5];
	float[] orientMatrix = new float[3];
	float[] remapMatrix = new float[9];
	
	public float location_accuracy;
    
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
		
		txtInfo = (TextView) findViewById(R.id.txt_info);
		tempRatingBar = (RatingBar) findViewById(R.id.ratingBar1);
		pressureRatingBar = (RatingBar) findViewById(R.id.ratingBar2);
		tasteRatingBar = (RatingBar) findViewById(R.id.ratingBar3);
		
		mySV = (MySurfaceView) findViewById(R.id.imagesurface);
		

		final Handler handler = new Handler();
		
		Runnable runnable = new Runnable() {
			float[] m_rotationMatrix = new float[9];
			float[] m_orientation = new float[9];
			public void run() {
				while(true){
					//Do time consuming stuff
		 
					//The handler schedules the new runnable on the UI thread
					handler.post(new Runnable() {
						@Override
						public void run() {
							//Update UI
							
							mySV.invalidate();
						}
					});
					//Add some downtime
					try {
						Thread.sleep(100);
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		new Thread(runnable).start();
		  
		mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	                          SensorManager.SENSOR_DELAY_NORMAL);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Button launchList_button = (Button)findViewById(R.id.button1);
		launchList_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent();
				myIntent.setClass(MainActivity.this,WaterList.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(myIntent, 1);
				
			}
		}
		);
		
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         String resultName=data.getStringExtra("name");
		         double resultLatitude=(double)Double.parseDouble(data.getStringExtra("lat"));
		         double resultLongitude=(double)Double.parseDouble(data.getStringExtra("long"));
		         double resultTemp=(double)Double.parseDouble(data.getStringExtra("temp"));
		         double resultPressure=(double)Double.parseDouble(data.getStringExtra("pressure"));
		         double resultTaste=(double)Double.parseDouble(data.getStringExtra("taste"));
		         //String name, double latitude, double longitude, double temp, double pressure, double taste)
		         target = new WaterFountain(resultName,resultLatitude,resultLongitude,resultTemp,resultPressure,resultTaste);
		         tempRatingBar.setRating((float) resultTemp);
		         pressureRatingBar.setRating((float) resultPressure);
		         tasteRatingBar.setRating((float) resultTaste);
		         Log.v(tag,target.toString());
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code on no result return 
		     }
		  }
		}//onActivityResult
	private WaterFountain WaterFountain(String resultName,
			double resultLatitude, double resultLongitude, double resultTemp,
			double resultPressure, double resultTaste) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onResume() {  
        
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	                          SensorManager.SENSOR_DELAY_NORMAL);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		/* GPS, as it turns out, consumes battery like crazy */
		lm.removeUpdates(this);
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v(tag, "Location Changed");

		sb = new StringBuilder(512);
		
		noOfFixes++;

		/* display some of the data in the TextView */

		sb.append("No. of Fixes: ");
		sb.append(noOfFixes);
		sb.append("\r\n");
		
		sb.append("Accuracy: ");
		sb.append(location.getAccuracy());
		sb.append("\r\n");
		/*
		sb.append("Longitude: ");
		sb.append(location.getLongitude());
		sb.append("\r\n");

		sb.append("Latitude: ");
		sb.append(location.getLatitude());
		sb.append("\r\n");
/*
		sb.append("Altitiude: ");
		sb.append(location.getAltitude());
		sb.append("\r\n");

		sb.append("Accuracy: ");
		sb.append(location.getAccuracy());
		sb.append("\r\n");

		sb.append("Timestamp: ");
		sb.append(location.getTime());
		sb.append("\r\n");
		*/
		float orientation = 0;
		if (target!=null){
			target.setDistanceFromMe(location.getLongitude(),location.getLatitude());
		Location dest = new Location("GPS");
		dest.setLatitude(target.getLatitude());
		dest.setLongitude(target.getLongitude());
		location.bearingTo(dest);
		mySV.distance_to_water = target.getDistanceFromMe();
		
		mySV.bearing_degree = location.bearingTo(dest);
		//31
		//accruracy less than 10 is good
		}
		location_accuracy = location.getAccuracy();
		if (location_accuracy > 10.0)
		{
			Toast.makeText(this, "GPS Signal Poor, Stand outside with a clear view of the sky", Toast.LENGTH_LONG).show();
		} else {
			if (hasStartedListOnce==false) {
				hasStartedListOnce = true;
				Intent myIntent = new Intent();
				myIntent.setClass(MainActivity.this,WaterList.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				myIntent.putExtra("gps_lat", location.getLatitude());
				myIntent.putExtra("gps_lon", location.getLongitude());
				startActivityForResult(myIntent, 1);
			}
		}
		// txtInfo.setText(sb.toString());
	}

	@Override
	public void onProviderDisabled(String provider) {
		/* this is called if/when the GPS is disabled in settings */
		Log.v(tag, "Disabled");
		
		Toast.makeText(this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
		/* bring up the GPS settings */
		Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v(tag, "Enabled");
		Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* This is called when the GPS status alters */
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(tag, "Status Changed: Out of Service");
			Toast.makeText(this, "Status Changed: Out of Service",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(tag, "Status Changed: Temporarily Unavailable");
			Toast.makeText(this, "Status Changed: Temporarily Unavailable",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:
			Log.v(tag, "Status Changed: Available");
			Toast.makeText(this, "Status Changed: Available",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onStop() {
		
		//finish();
		super.onStop();
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType()==Sensor.TYPE_ORIENTATION) {
				mySV.orientation_degree = -event.values[0];
	      }
	}

}
