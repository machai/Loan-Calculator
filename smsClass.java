package com.example.s54651719;

import android.app.Application;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.widget.Toast;

public class smsClass extends Application{
	dbcon db = new dbcon();
	public void emergencySMS(){
		Cursor c1 = db.onCreate().rawQuery("select * from emergency", null);
		String phoneNo = "";
		String title = "";
		String name = "";
		String surname = "";
		String message = "";
		String gps = gpsLocation();
		while(c1.moveToNext()){
			if(c1.getCount()>0){
				phoneNo=c1.getString(0);
				name=c1.getString(2);
				surname=c1.getString(3);
				title=c1.getString(1);
				message=c1.getString(4);
			}
		}
		if(!phoneNo.equals("")){
			String finalSms = "**EMERGENCY** \n "+title+" "+name+" "+surname+"\n"+message+"\n"+"GPS Location : "+gps;
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null,finalSms.toString(), null, null);
			db.onCreate().execSQL("update emergency set gps='"+gps+"'");
			Toast.makeText(getApplicationContext(), "SMS SENT", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplicationContext(), "INFORMATION STORE CLICK UPDATE TO ADD INFORMATION", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public String gpsLocation(){
		LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider  = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		String fullLocation ="No cordinates";
		if(location != null){
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			fullLocation = lat+","+lon;
		}
		return fullLocation;
	}
	
}
