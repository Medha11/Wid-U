package com.example.stage1;
 
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnClickListener{
	 MyCustomAdapter dataAdapter = null;
	 int count=0;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
		
	  //Generate list View from ArrayList
	  displayListView();
	 Button b=(Button) findViewById(R.id.button1);
	b.setOnClickListener(this);
	b.setTextColor(Color.BLACK);
	 }
	
	 ArrayList<Cont> cont_arr = new ArrayList<Cont>();
	 private void displayListView() {
		// TODO Auto-generated method stub
        Cursor c=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		 
		int i=0;
	SQLiteDatabase db = openOrCreateDatabase("MyDB1" , MODE_PRIVATE, null);
   	db.execSQL("CREATE TABLE IF NOT EXISTS MyTable1 (PhoneNo VARCHAR, Name VARCHAR);");
     	Cursor z=db.rawQuery("SELECT * FROM MyTable1", null);
     	count+=z.getCount();
     	Log.d("COUNT",Integer.toString(count));
		while(c.moveToNext())
	        {
			String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
	        	int nameIndex=c.getColumnIndex(PhoneLookup.DISPLAY_NAME);
	        	String name=c.getString(nameIndex);
	        	Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                        new String[]{id}, null);
	        	//This does not display the contacts which do not have phone number
	        	while (pCur.moveToNext()) {//loops till it gets a new phone number
	        		//so in this case multiple phone numbers are also displayed
                     String phone = pCur.getString(
                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                     boolean value=false;
     	       
     	       z.moveToFirst();
     	       if(count!=0)
     	   		{do
     	   		{	
     	   			if(name.equalsIgnoreCase(z.getString(z.getColumnIndex("Name"))))
     	   			value=true;
     	   			
     	   	}while(z.moveToNext());}
     	
     	        	Cont cont_obj = new Cont(phone,name,value);
     		  cont_arr.add(cont_obj);
     		 db.close();
               }
               pCur.close();

		  i++;
		  if(i==200)
			  break;
	        }
		  dataAdapter = new MyCustomAdapter(this,
				    R.layout.contacts_list, cont_arr);
				  ListView listView = (ListView) findViewById(R.id.listView1);
				  // Assign adapter to ListView
				  listView.setAdapter(dataAdapter);

	}

private class MyCustomAdapter extends ArrayAdapter<Cont> {
	private ArrayList<Cont> cont_arr;

	public MyCustomAdapter(Context context, int textViewResourceId,
			ArrayList<Cont> cont_arr) {
		super(context, textViewResourceId,cont_arr);
		this.cont_arr=new ArrayList<Cont>();
		this.cont_arr.addAll(cont_arr);
		// TODO Auto-generated constructor stub
	}
	
	 private class ViewHolder {
		   TextView name;
		   CheckBox check;
		  }
	 @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	 
	   ViewHolder holder = null;
	   Log.v("ConvertView", String.valueOf(position));
	 
	   if (convertView == null) {
	   LayoutInflater vi = (LayoutInflater)getSystemService(
	     Context.LAYOUT_INFLATER_SERVICE);
	   convertView = vi.inflate(R.layout.contacts_list, null);

	   holder = new ViewHolder();
	   holder.name = (TextView) convertView.findViewById(R.id.name);
	   holder.check = (CheckBox) convertView.findViewById(R.id.checkBox1);
	   convertView.setTag(holder);
	 
	    holder.check.setOnClickListener( new View.OnClickListener() {  
	     public void onClick(View v) {  
	      CheckBox cb = (CheckBox) v ;  
	     
	      Cont cont_obj2 = (Cont) cb.getTag();
	      if(count>=5 && cont_obj2.isSelected()==false)
	      {cb.setChecked(false);
	    	  Toast.makeText(getApplicationContext(), "Can't select more than 5", Toast.LENGTH_SHORT).show();
	      }
	      else
	      {
	    	  String val;
	     if(cont_obj2.isSelected())
	     {	 count--;
	     val="removed from";
	     }else
	     { count++;
	     val="added as an";
	     }  //if(cont_obj2.isSelected()==false)
	    	// count++;
	     
	      Toast.makeText(getApplicationContext(),
	       cont_obj2.getName() +" has been "+val+" emergency contact", 
	       Toast.LENGTH_SHORT).show();
	      //if(count>5)
	    	//  cb.setChecked(false);
	      } cont_obj2.setSelected(cb.isChecked());
	      

	      Log.d("COUNTER",Integer.toString(count));
	     }
	    });  
	   } 
	   else {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   Cont cont_obj2 = cont_arr.get(position);
		   holder.name.setText(cont_obj2.getName()+" - "+cont_obj2.getphno());
		   holder.check.setChecked(cont_obj2.isSelected());
		   holder.check.setTag(cont_obj2);
		   return convertView;
		  }
		 }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	if(v.getId()==R.id.button1)
	{Cont obj;
	ContentValues insobj=new ContentValues();
		int i,n=cont_arr.size();
	SQLiteDatabase db = openOrCreateDatabase("MyDB1" , MODE_PRIVATE, null);
		db.execSQL("DELETE FROM MyTable1;");
		for(i=0;i<n;i++)
		{
			
		obj=cont_arr.get(i);
		String na=obj.name;
		String ph=obj.phno;
				
		db.execSQL("CREATE TABLE IF NOT EXISTS MyTable1 (PhoneNo VARCHAR, Name VARCHAR);");
		if(obj.selected == true )
		{
		insobj.put("PhoneNo", ph);
	insobj.put("Name", na);
		db.insert("MyTable1", null, insobj);
	//db.execSQL("INSERT INTO MyTable1 VALUES ("+ph+", "+na+");");
		
		}
		}
		Cursor c=db.rawQuery("SELECT * FROM MyTable1", null);
		c.moveToFirst();
		if(c.getCount()!=0)
		{do
		{		Log.d("FIRST",c.getString(c.getColumnIndex("PhoneNo")));
		}while(c.moveToNext());
		}
		db.close();
		
	}
	Intent a=new Intent(this, Main_screen.class);
	startActivity(a);
	
}	
}