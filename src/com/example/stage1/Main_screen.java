package com.example.stage1;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Build;
import android.telephony.SmsManager;

public class Main_screen extends ActionBarActivity implements OnClickListener{
	int flag=0,flag_repeat=0;
	Button alert,bt,kyu;
	MediaPlayer mp=null;
	Timer timer=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);

alert=(Button) findViewById(R.id.alert);
alert.setOnClickListener(this);
		Button b2= (Button) findViewById(R.id.send);
b2.setOnClickListener(this);
bt= (Button) findViewById(R.id.bt);
bt.setOnClickListener(this);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id==R.id.instructions)
		{
			Intent inst=new Intent(this,Instructions.class);
			startActivity(inst);
		}
		if(id==R.id.app_info)
		{
			Intent info=new Intent(this,Info.class );
			startActivity(info);
		}
		if (id == R.id.manage_cont) {
			Intent i= new Intent(this, MainActivity.class);
			startActivity(i);
			
		}
		if(id==R.id.edit)
		{
			Intent a=new Intent(this, Kyu.class);
			startActivity(a);
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_screen,
					container, false);
			return rootView;
		}
	}
int count;
public void send_message(Context c)
{
	SharedPreferences settings=c.getSharedPreferences("MYPREFS",Context.MODE_PRIVATE);
	String msg=settings.getString("tvalue", "Hey you are my emergency contact, I am in danger, can you please save me? ");
	Log.d("msg",msg);
	//For retrieving GPS location
	LongLat ll=new LongLat(c);
	if(ll.canGetLocation){
		double lat=ll.getLatitude(); // returns latitude
		double longi=ll.getLongitude(); // returns longitude
Log.d("lati",Double.toString(lat));
Log.d("longi",Double.toString(longi));
//Toast.makeText(this, Double.toString(lat)+"  "+Double.toString(longi),Toast.LENGTH_SHORT).show();
		Geocoder geocoder= new Geocoder(c, Locale.ENGLISH);
		   try {
               
              //Place your latitude and longitude
//				   Toast.makeText(this, "no error till lat long",Toast.LENGTH_SHORT).show();
              List<Address> addresses = geocoder.getFromLocation(lat,longi, 1);
//              Toast.makeText(this, "resolved address",Toast.LENGTH_SHORT).show();
              msg=msg+"I was last present near -";
              if(addresses != null) {
               
                  try{
                	  Log.d("in try","noerror");
                	  int f=addresses.size();
		                 Log.d("size",Integer.toString(f));

                	  Address fetchedAddress = addresses.get(0);
                 		                  StringBuilder strAddress = new StringBuilder();
                
                  for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                  }
                  msg+=strAddress.toString();
	             // Toast.makeText(this, strAddress.toString(), Toast.LENGTH_LONG).show();
  				}
              catch(Exception e)
              {
            	  Log.d("Excptn","in catch");
            	  msg+="Latitude="+Double.toString(lat)+" Longitude="+Double.toString(longi);
            	  //Toast.makeText(this, "in catch", Toast.LENGTH_SHORT).show();
            	  
              }
              }
              else
              {

            	  msg+="Latitude="+Double.toString(lat)+" Longitude="+Double.toString(longi);
            	  //Toast.makeText(this, "no location found", Toast.LENGTH_LONG).show();
              }
              
        }
        catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
            	  msg+="Latitude="+Double.toString(lat)+" Longitude="+Double.toString(longi);
                 //Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
                 
        }
		
	}
	else
		ll.showSettingsAlert();
		ll.stopUsingGPS();
	
//	Toast.makeText(c, msg,Toast.LENGTH_LONG).show();
	              
	Log.d("Mgs scn",msg);
	SQLiteDatabase db = c.openOrCreateDatabase("MyDB1" , MODE_PRIVATE, null);
   	db.execSQL("CREATE TABLE IF NOT EXISTS MyTable1 (PhoneNo VARCHAR, Name VARCHAR);");
   	Cursor z=db.rawQuery("SELECT * FROM MyTable1", null);
 	count=z.getCount();
 	z.moveToFirst();
 	Log.d("Countsend",Integer.toString(count));
 	if(count==0)
 		return;
 	do{
 		String phoneNo=z.getString(z.getColumnIndex("PhoneNo"));
 		
 		
 		try{
 			SmsManager sms=SmsManager.getDefault();
 			sms.sendTextMessage(phoneNo, null , msg , null, null);
 			Log.d("COUNT","HOW MANY TIMES");
 			Toast.makeText(c, "SMS Sent to "+z.getString(z.getColumnIndex("Name")),Toast.LENGTH_SHORT ).show();
 		}catch(Exception e)
 		{
 			Toast.makeText(c, "SMS failed try again.",Toast.LENGTH_SHORT ).show();
 			e.printStackTrace();
 		}
 	}
 	while(z.moveToNext());
 //	Toast.makeText(c, "check", Toast.LENGTH_LONG).show();
}
	@Override
	public void onClick(View v) {
	
		if(v.getId()==R.id.send)
		{
			//send_message();
			Timer timer = new Timer();
			TimerTask display = new CustomTimerTask(Main_screen.this);
			timer.scheduleAtFixedRate(display, 0, 5000);
			try {
			   
			    TimeUnit.MILLISECONDS.sleep(800);
			   	} catch (InterruptedException e)
			   	{
			    //Handle exception
			   	}
			timer.cancel();
	
		
	     	}
		if(v.getId()==R.id.alert)
		{
			if(mp==null)
			mp=MediaPlayer.create(this,R.raw.golu);
		
		if(!mp.isPlaying())
		{alert.setText("Stop alert sound");
			mp.start();
		}
		else
			{
			alert.setText("Sound alert");
			mp.stop();
			mp=null;
			}
		}
		
		if(v.getId()==R.id.bt)
		{
			if(timer==null)
			{timer = new Timer();
		        TimerTask updateProfile = new CustomTimerTask(this);
		        timer.scheduleAtFixedRate(updateProfile, 0, 10000);
		        bt.setText("Stop sending messages");
		}
			else
			{
				timer.cancel();
				timer=null;
				 bt.setText("Send repeated messages");
			       
			}
		}
	}
}