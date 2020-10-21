package com.elelco.elelcomobilesalesdriza;

import android.annotation.SuppressLint;
import android.content.*;
import android.location.*;
import android.os.*;

public class customLocationManager
{
	private  int LOCATION_INTERVAL=20*1000; //millisecondi
	private  int LOCATION_DISTANCE=500;//metri
	private  int LOCATION_TIMEOUT=60000;
	private   boolean singleUpdate=false;
	private LocationManager locationManager ;
	private LocationListener locListener;


	public  interface LocationCallback {
		public void onNewLocationAvailable(Location location);
	}
	public  interface providerDisabledCallback {
		public void onProviderDisabled(String provider);
	}


//public customLocationManager(){
//	
//}
//


	public void stopUpdate(){
		try{
			locationManager.removeUpdates ( locListener);
		}catch(Exception ex){}
	}

	// calls back to calling thread, note this is for low grain: if you want higher precision, swap the 
	// contents of the else and if. Also be sure to check gps permission/settings are allowed.
	// call usually takes <10ms
	@SuppressLint("MissingPermission")
	public  void startUpdate(final Context context, final LocationCallback callback, final providerDisabledCallback onProviderDisabledCallback) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		//final LocationManager locationManager2 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//		boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//		boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && Utility.isWifiEnabled() && Utility.isAirplaneModeOff();
//		
		String provider=null;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider=locationManager.getBestProvider(criteria,true);

		if (provider==null){
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);
			provider=locationManager.getBestProvider(criteria,true);
		}

		 locListener=new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				//callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
				MainActivity.currentPosition=location;
				callback.onNewLocationAvailable(location);
			}
			@Override public void onStatusChanged(String provider, int status, Bundle extras) { }
			@Override public void onProviderEnabled(String provider) { }
			@Override public void onProviderDisabled(String provider) {

				onProviderDisabledCallback.onProviderDisabled(provider);
			}
		};

		if (provider!=null){
			if (singleUpdate) {
				locationManager.requestSingleUpdate(criteria, locListener, null);
			}else{
				locationManager.requestLocationUpdates(provider,LOCATION_INTERVAL,LOCATION_DISTANCE,locListener);
			}

		}


		if (singleUpdate && provider!=null){
			//timeout
			Runnable myRun=new Runnable(){
				public void run(){
					locationManager.removeUpdates ( locListener);
				}
			};
			Handler mhandler=new Handler();
			mhandler.postDelayed(myRun, LOCATION_TIMEOUT);
		}

	} 

	
	

	// consider returning Location instead of this dummy wrapper class
	public static class GPSCoordinates {
		public float longitude = -1;
		public float latitude = -1;

		public GPSCoordinates(float theLatitude, float theLongitude) {
			longitude = theLongitude;
			latitude = theLatitude;
		}

		public GPSCoordinates(double theLatitude, double theLongitude) {
			longitude = (float) theLongitude;
			latitude = (float) theLatitude;
		}
	}  
}
 

 
	
