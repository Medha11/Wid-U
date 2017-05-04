package com.example.stage1;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.os.Build;

public class Kyu extends ActionBarActivity implements OnClickListener{
private EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_kyu);

		
		et=(EditText) findViewById(R.id.message);
		SharedPreferences settings=getSharedPreferences("MYPREFS", 0);
		et.setText(settings.getString("tvalue", "Hey I am in danger, can you please save me?"));
		Button b=(Button) findViewById(R.id.button1);
	//b.setBackgroundColor(Color.MAGENTA);
b.setOnClickListener(this);
	}

@Override
protected void onStop()
{
	super.onStop();
	SharedPreferences settings=getSharedPreferences("MYPREFS", 0);
	SharedPreferences.Editor editor=settings.edit();
	editor.putString("tvalue", et.getText().toString());
	editor.commit();
}

@Override
public void onClick(View v) {
Intent i=new Intent(this, Main_screen.class);
startActivity(i);
	
}
}