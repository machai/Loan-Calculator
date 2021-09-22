package com.example.s54651719;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class CalendaPopup extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calenda_popup);
		Button calendar = (Button)findViewById(R.id.setAlert);
		calendar.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				dbcon db = new dbcon();
				DatePicker date = (DatePicker)findViewById(R.id.datePicker);
				int day = date.getDayOfMonth();
				int month = date.getMonth();
				int year = date.getYear();
				String alertDate="";
				String [] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
				for(int num=0;num<=11;num++){
					if(num==month){
						alertDate=day+" "+months[num]+" "+year;
					}
				}
				if(alertDate.equals("")){
					Toast.makeText(getApplicationContext(), "Select date", Toast.LENGTH_LONG).show();
				}else{
					db.onCreate().execSQL("insert into alert values('"+alertDate+"','"+day+"','"+month+"','"+year+"')");
					 startActivity(new Intent(CalendaPopup.this,Mainpage.class));
					Toast.makeText(getApplicationContext(), "Alert created", Toast.LENGTH_LONG).show();
					finish();
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calenda_popup, menu);
		return false;
	}

}
