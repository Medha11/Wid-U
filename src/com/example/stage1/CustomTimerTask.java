package com.example.stage1;

import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class CustomTimerTask extends TimerTask {
	
    private Context context;
    private Handler mHandler = new Handler();
	 // Write Custom Constructor to pass Context
    public CustomTimerTask(Context con) {
        this.context = con;
    }
Main_screen m=new Main_screen();
    @Override
    public void run() {
        // TODO Auto-generated method stub

        // your code starts here.
        // I have used Thread and Handler as we can not show Toast without starting new thread when we are inside a thread.
        // As TimePicker has run() thread running., So We must show Toast through Handler.post in a new Thread. Thats how it works in Android..
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
           // Toast.makeText(context, "Repeat this", Toast.LENGTH_SHORT).show();
            m.send_message(context);
                    }
                });
            }
        }).start();

    }

}

