package com.young.common.service;

import java.util.Calendar;
import java.util.TimeZone;

import com.young.common.reciver.AlarmReceiver;
import com.young.common.util.L;
import com.young.common.util.T;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class ForecastService extends Service {
	
	private static final String TAG = "ForecastService"; 
	private int mHour = -1;
	private int mMinute = -1;
	public static final long DAY = 1000L * 60 * 60 * 24;

	private Handler handler = new Handler() {  
	    public void handleMessage(Message msg) {  
	        // 要做的事情  
	        super.handleMessage(msg); 
	        L.v(TAG,"1");
	    }  
	};  
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override  
    public void onCreate() {  
        L.v(TAG, "onCreate");  
        Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		if(mHour == -1 && mMinute == -1) {
			mHour = calendar.get(Calendar.HOUR_OF_DAY);
			mMinute = calendar.get(Calendar.MINUTE);
		}
		mHour = 17;
		mMinute = 40;
        
    }  
  
    @Override  
    public void onDestroy() {  
        L.v(TAG, "onDestroy");  
 
        
    }  
  
    @Override  
    public void onStart(Intent intent, int startId) {  
        L.v(TAG, "onStart");  
        Intent intentBroad = new Intent(ForecastService.this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(ForecastService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();	// 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
	 	calendar.setTimeInMillis(System.currentTimeMillis());
	 	calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
	 	calendar.set(Calendar.MINUTE, mMinute);
	 	calendar.set(Calendar.HOUR_OF_DAY, mHour);
	 	calendar.set(Calendar.SECOND, 0);
	 	calendar.set(Calendar.MILLISECOND, 0);

	 	// 选择的每天定时时间
	 	long selectTime = calendar.getTimeInMillis();	

	 	// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
	 	if(systemTime > selectTime) {
	 		Toast.makeText(ForecastService.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
	 		calendar.add(Calendar.DAY_OF_MONTH, 1);
	 		selectTime = calendar.getTimeInMillis();
	 	}

	 	// 计算现在时间到设定时间的时间差
	 	long time = selectTime - systemTime;
 		firstTime += time;

        // 进行闹铃注册
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        firstTime, 3*1000, sender);

        Log.i(TAG, "time ==== " + time + ", selectTime ===== "
    			+ selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);

        Toast.makeText(ForecastService.this, "设置重复闹铃成功! ", Toast.LENGTH_LONG).show();
    }  
    
    public class MyThread implements Runnable {  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while (true) {  
                try {  
                    Thread.sleep(1000);// 线程暂停10秒，单位毫秒  
                    Message message = new Message();  
                    message.what = 1;  
                    handler.sendMessage(message);// 发送消息  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
}
