package com.example.s54651719;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Alerts extends Activity {

	//ArrayList and ArrayAdapter
		private ArrayList<String> arrayList;
		private ArrayAdapter<String> adapter;
		
		//Database class file
		dbcon db = new dbcon();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alerts);
		//Setting the width and height of the window
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		int height=dm.heightPixels;
		getWindow().setLayout((int)(width*0.8), (int)(height*0.8));
		
		TextView close = (TextView)findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				finish();
			}
					
		});
		this.settingListView();
	}

	public void settingListView(){
		//Database Connection
		dbcon db = new dbcon();
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Cursor c1 = db.onCreate().rawQuery("select * from alert where (month='"+month+"' AND day>='"+day+"') OR (month>'"+month+"')", null);
			int i=0;
			String[] items = new String[c1.getCount()];
			while(c1.moveToNext()){
				items[i] = "You have a reminder on "+c1.getString(0);
				i++;
			}
			//Setting List View
			arrayList = new ArrayList<String>(Arrays.asList(items));
			adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.textView,arrayList);
			ListView list = (ListView)findViewById(R.id.alerts);
			list.setAdapter(adapter);
			db.onCreate().close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alerts, menu);
		return true;
	}

}
