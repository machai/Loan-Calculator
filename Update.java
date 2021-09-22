package com.example.s54651719;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Update extends Activity {
	dbcon db = new dbcon();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		Button update = (Button)findViewById(R.id.updateInfo);
		update.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				EditText number = (EditText)findViewById(R.id.inputContact);
				Spinner title = (Spinner)findViewById(R.id.title);
				EditText name = (EditText)findViewById(R.id.name);
				EditText surname = (EditText)findViewById(R.id.surname);
				EditText sms = (EditText)findViewById(R.id.sms);
				
				String phoneNumber = number.getText().toString();
				String title2 = title.getSelectedItem().toString();
				String name2 = name.getText().toString();
				String surname2 = surname.getText().toString();
				String sms2 = sms.getText().toString();
				
				if(!phoneNumber.matches("[0-9]+") || phoneNumber.equals("") || phoneNumber.matches("[0-9]+") && phoneNumber.length()<10 || phoneNumber.length()>10){
					Toast.makeText(getApplicationContext(), "Please enter correct phone number in 10 digits e.g 0123456789", Toast.LENGTH_LONG).show();
				}else if(title2.equals("")){
					Toast.makeText(getApplicationContext(), "Please select title", Toast.LENGTH_LONG).show();
				}else if(!name2.replace(" ","").matches("[a-zA-Z]+")){
					Toast.makeText(getApplicationContext(), "Please enter name in letters only", Toast.LENGTH_LONG).show();
				}else if(!surname2.replace(" ","").matches("[a-zA-Z]+")){
					Toast.makeText(getApplicationContext(), "Please enter surname in letters only", Toast.LENGTH_LONG).show();
				}else if(!sms2.replace(" ","").matches("[a-zA-Z]+")){
					Toast.makeText(getApplicationContext(), "Please enter sms in letters only", Toast.LENGTH_LONG).show();
				}
				else{
					String decider="No";
					Cursor number1 = db.onCreate().rawQuery("select * from emergency",null);
					while(number1.moveToNext()){
						if(number1.getCount()>0){
							decider="Yes";
						}
					}
					if(decider.equals("Yes")){
						String query = "update emergency set phoneNo='"+phoneNumber+"',title='"+title2+"',name='"+name2+"',surname='"+surname2+"',message='"+sms2+"'";
						db.onCreate().execSQL(query);
						Toast.makeText(getApplicationContext(), "Information updated", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(Update.this,Mainpage.class);
		                startActivity(intent);
		                finish();
					}else{
						String query = "insert into emergency (phoneNo,title,name,surname,message) values('"+phoneNumber+"','"+title2+"','"+name2+"','"+surname2+"','"+sms2+"')";
						db.onCreate().execSQL(query);
						Toast.makeText(getApplicationContext(), "Information added", Toast.LENGTH_LONG).show();
						 startActivity(new Intent(Update.this,Mainpage.class));
		                finish();
					}
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return false;
	}

}
