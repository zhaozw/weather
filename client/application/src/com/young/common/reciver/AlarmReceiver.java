package com.young.common.reciver;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.young.common.util.HttpUtil;
import com.young.common.util.SharePreferenceUtil;
import com.young.modules.weather.MainActivity;
import com.young.modules.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
	
	private Context mContex;
	private Intent mIntent;
	private SharePreferenceUtil sp;
	private WakeLock wakeLock = null; 
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			 String result = (String)msg.obj;
			 JSONObject forecast;
			try {
				 forecast = new JSONObject(result);
				 int cityid  = forecast.getInt("cityid");
				 String citynm = forecast.getString("citynm");
				 String weather = forecast.getString("weather");
				 String temp_high = forecast.getString("temp_high");
				 String temp_low = forecast.getString("temp_low");
				 String temp_current = forecast.getString("temp_current");
				 
				 String ns = Context.NOTIFICATION_SERVICE;
				 NotificationManager mNotificationManager = (NotificationManager)AlarmReceiver.this.mContex.getSystemService(ns);
				 Intent notificationIntent = new Intent(AlarmReceiver.this.mContex, MainActivity.class);
				 PendingIntent contentIntent = PendingIntent.getActivity(AlarmReceiver.this.mContex, 0,notificationIntent, 0);
				 
				 String ContentTitle = "今天 " + weather + " " + temp_low + "℃~ " + temp_high + "℃ 风力";
				 String ContentText = "请注意天气变化";
				 String ContentInfo = citynm;
			     Notification notification = new NotificationCompat.Builder(AlarmReceiver.this.mContex)
	                 .setSmallIcon(R.drawable.notice_icon)
	                 .setTicker(ContentTitle).setContentInfo(ContentInfo)
	                 .setContentTitle(ContentTitle).setContentText(ContentText)
	                 .setContentIntent(contentIntent)
	                 .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
	                 .build();
			      mNotificationManager.notify(1, notification);
			     
			      releaseWakeLock();
				 
			} catch (JSONException e) {
				e.printStackTrace();
			}
			 
		}
	};
	
	Runnable runnable = new Runnable() {  
        @Override  
        public void run() {//run()在新的线程中运行
        	Map<String, String> param = new HashMap<String, String>();
        	String mCitys = AlarmReceiver.this.sp.getAllCity();
        	if(mCitys.equals("[]")){
        		return;
        	}
        	else{       	
        		try {
        			JSONArray citys = new JSONArray(mCitys);
        			String mCity = citys.getJSONObject(0).getString("number");
        			System.out.println(citys);
        			param.put("location", mCity);
        			String netResult = HttpUtil.postRequestByNVP(
						"http://106.187.94.192/weather/index.php?r=PushService/SendForecast", param);
        			Message msg = handler.obtainMessage();
        			msg.obj = netResult;
        			handler.sendMessage(msg);
        			System.out.println(netResult);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        }
	};
   
	private void acquireWakeLock() { 
        if (null == wakeLock) { 
            PowerManager pm = (PowerManager)this.mContex.getSystemService(Context.POWER_SERVICE); 
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK 
                    | PowerManager.ON_AFTER_RELEASE, getClass() 
                    .getCanonicalName()); 
            if (null != wakeLock) { 
                wakeLock.acquire(); 
            } 
        } 
    } 
   
	private void releaseWakeLock() { 
        if (null != wakeLock && wakeLock.isHeld()) { 
            wakeLock.release(); 
            wakeLock = null; 
        } 
    }
	
	@Override
    public void onReceive(Context context, Intent intent) {
		 this.mContex = context;
		 this.mIntent = intent;
		 acquireWakeLock();
		 sp = new SharePreferenceUtil(context);
		 Thread mThread = new Thread(runnable);  
         mThread.start();//线程启动  
	}
		
}
