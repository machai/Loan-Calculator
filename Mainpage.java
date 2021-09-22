package com.example.s54651719;

import java.lang.reflect.Method;
import java.util.Calendar;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Mainpage extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpage);
		
		TextView exit = (TextView)findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Button decline = (Button)findViewById(R.id.decline);
		decline.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				decline();
			}
		});
		
		Button accept = (Button)findViewById(R.id.accept);
		accept.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				accept();
			}
		});
		
		Button callInput = (Button)findViewById(R.id.callInput);
		callInput.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				EditText number = (EditText)findViewById(R.id.inputContact);
				String contactNumber = number.getText().toString();
				if(contactNumber.equals("") || !contactNumber.matches("[0-9]+") && contactNumber.length()<10 || contactNumber.length()>10){
					Toast.makeText(getApplicationContext(), "Enter contact number in 10 digits number e.g 0123456789", Toast.LENGTH_LONG).show();
				}else{
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+contactNumber));
					startActivity(callIntent);
				}
			}
		});
		
		Button callContacts = (Button)findViewById(R.id.callContacts);
		callContacts.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, 2015);
			}
		});
		
		Button update = (Button)findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				 startActivity(new Intent(Mainpage.this,Update.class));
				 finish();
			}
		});
		
		Button emegencyBtn = (Button)findViewById(R.id.emergencyButton);
		emegencyBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				smsClass connection = (smsClass)getApplicationContext();
				connection.emergencySMS();
			}
		});
		
		Button calendar = (Button)findViewById(R.id.calendarBtn);
		calendar.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				 startActivity(new Intent(Mainpage.this,CalendaPopup.class));
				 finish();
			}
		});
		
		this.checkForAlerts();
	}
	
	public void checkForAlerts(){
		//Database Connection
		dbcon db = new dbcon();
		Calendar calendar1 = Calendar.getInstance();
		int month = calendar1.get(Calendar.MONTH);
		int day = calendar1.get(Calendar.DAY_OF_MONTH);
		Cursor c1 = db.onCreate().rawQuery("select * from alert where (month='"+month+"' AND day>='"+day+"') OR (month>'"+month+"')", null);
		if(c1.getCount()>0){
			startActivity(new Intent(Mainpage.this,Alerts.class));
		}
		db.onCreate().close();
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case (2015) :
	      if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor c =  getContentResolver().query(contactData, null, null, null, null);
	        if (c.moveToFirst()) {
	          String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	          // TODO Whatever you want to do with the selected contact name.
	          Intent callIntent = new Intent(Intent.ACTION_CALL);
	          callIntent.setData(Uri.parse("tel:"+name));
	          startActivity(callIntent);
	        }
	      }
	      break;
	  }
	}
	
	public void decline(){
		try {
            TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            try {
                Class c = Class.forName(tm.getClass().getName());
                Method m = c.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
                c = Class.forName(telephonyService.getClass().getName()); // Get its class
                m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
                m.setAccessible(true); // Make it accessible
                m.invoke(telephonyService); // invoke endCall()

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
	}
	
	public void accept(){
		// set the logging tag constant; you probably want to change this
		final String LOG_TAG = "TelephonyAnswer";

		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

		try {
		    if (tm == null) {
		        // this will be easier for debugging later on
		        throw new NullPointerException("tm == null");
		    }

		    // do reflection magic
		    tm.getClass().getMethod("answerRingingCall").invoke(tm);
		} catch (Exception e) {
		    // we catch it all as the following things could happen:
		    // NoSuchMethodException, if the answerRingingCall() is missing
		    // SecurityException, if the security manager is not happy
		    // IllegalAccessException, if the method is not accessible
		    // IllegalArgumentException, if the method expected other arguments
		    // InvocationTargetException, if the method threw itself
		    // NullPointerException, if something was a null value along the way
		    // ExceptionInInitializerError, if initialization failed
		    // something more crazy, if anything else breaks

		    // TODO decide how to handle this state
		    // you probably want to set some failure state/go to fallback
		    Log.e(LOG_TAG, "Unable to use the Telephony Manager directly.", e);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainpage, menu);
		return false;
	}

}
