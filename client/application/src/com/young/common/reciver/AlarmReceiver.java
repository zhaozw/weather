package com.young.common.reciver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.CommonData;
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
	private Calendar calendar = Calendar.getInstance(); 
	private String forecastDay = "今天";
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			 JSONObject forecast;
			 String result = (String)msg.obj;
			 if(result == null){//网络连接失败，获取预报接口失败，得从预报5天天气里面获取数据。
				 try {
					JSONArray citys = new JSONArray(sp.getAllCity());
					String mCity = citys.getJSONObject(0).getString("number");
					JSONArray weathers = new JSONArray(sp.getAllWeather());
					for (int i = 0 ;i < weathers.length(); i++){
						if(mCity.equals(weathers.getJSONObject(i).getString("cityid"))){
							JSONArray forecasts = new JSONArray(weathers.getJSONObject(i).getString("forecast"));
							SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd");   
						    String   localTime   =   sDateFormat.format(getForecastDate());
							for(int j = 0; j < forecasts.length(); j++){
								String fTime = forecasts.getJSONObject(j).getString("days");
								if(fTime.equals(localTime)){
									int cityid  = forecasts.getJSONObject(j).getInt("cityid");
									 String citynm = forecasts.getJSONObject(j).getString("citynm");
									 String weather = forecasts.getJSONObject(j).getString("weather");
									 String temp_high = forecasts.getJSONObject(j).getString("temp_high");
									 String temp_low = forecasts.getJSONObject(j).getString("temp_low");
									 String winp = forecasts.getJSONObject(j).getString("winp");
									 
									 String ns = Context.NOTIFICATION_SERVICE;
									 NotificationManager mNotificationManager = (NotificationManager)AlarmReceiver.this.mContex.getSystemService(ns);
									 Intent notificationIntent = new Intent(AlarmReceiver.this.mContex, MainActivity.class);
									 PendingIntent contentIntent = PendingIntent.getActivity(AlarmReceiver.this.mContex, 0,notificationIntent, 0);
									 
									 String ContentTitle = forecastDay +" "+ getSingleWeatherDesc(weather) + " "+ temp_low + "~" + temp_high + "℃"+" " + winp ;
									 String ContentText = "";
									 String ContentInfo = citynm;
								     Notification notification = new NotificationCompat.Builder(AlarmReceiver.this.mContex)
						                 .setSmallIcon(R.drawable.notice_icon)
						                 .setTicker(ContentTitle).setContentInfo(ContentInfo)
						                 .setContentTitle(ContentTitle).setContentText(ContentText)
						                 .setContentIntent(contentIntent)
						                 .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
						                 .build();
							
								      mNotificationManager.notify(1, notification);				     								 
								}
							}
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			 }
			 else{//获取预报接口成功，把数据放置在通知里面
				 try {
					 forecast = new JSONObject(result);
					 int cityid  = forecast.getInt("cityid");
					 String citynm = forecast.getString("citynm");
					 String weather = forecast.getString("weather");
					 String temp_high = forecast.getString("temp_high");
					 String temp_low = forecast.getString("temp_low");
					 String temp_current = forecast.getString("temp_current");
					 String wind_speed = forecast.getString("wind_speed");
					 String wind = CommonData.WIND_DESC_MAP.get(wind_speed);
					 
					 String ns = Context.NOTIFICATION_SERVICE;
					 Date fDate = getForecastDate();
					 NotificationManager mNotificationManager = (NotificationManager)AlarmReceiver.this.mContex.getSystemService(ns);
					 Intent notificationIntent = new Intent(AlarmReceiver.this.mContex, MainActivity.class);
					 PendingIntent contentIntent = PendingIntent.getActivity(AlarmReceiver.this.mContex, 0,notificationIntent, 0);
					 
					 String ContentTitle = forecastDay +" "+ getSingleWeatherDesc(weather) + " "+ temp_low + "~" + temp_high + "℃"+" " + wind ;
					 String ContentText = "好天气祝您今天有个好心情！";
					 String ContentInfo = citynm;
				     Notification notification = new NotificationCompat.Builder(AlarmReceiver.this.mContex)
		                 .setSmallIcon(R.drawable.notice_icon)
		                 .setTicker(ContentTitle).setContentInfo(ContentInfo)
		                 .setContentTitle(ContentTitle).setContentText(ContentText)
		                 .setContentIntent(contentIntent)
		                 .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
		                 .build();
				      mNotificationManager.notify(1, notification);				     				 
				} catch (JSONException e) {
					e.printStackTrace();
				}
			 }
		     releaseWakeLock();
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
        			Message msg = handler.obtainMessage();
        			msg.obj = null;
        			handler.sendMessage(msg);
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
	
	private Date getForecastDate() { 
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		if(hour>19 || (hour == 18 && minute >=30 )){
			calendar.add(Calendar.DATE,1);
			forecastDay = "明天";
		}
		return calendar.getTime();		
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
	
	private String getSingleWeatherDesc(String weatherAll){
		if(weatherAll.length()<=4){
			return weatherAll;
		}
		else{
			String[] weathers = weatherAll.split("转|-");
			List<Integer> indexs = new ArrayList<Integer>();
			for(String weather : weathers){
				indexs.add(CommonData.WEATHER_SORTED_LIST.indexOf(weather));			
			}
			int minIndex = Collections.min(indexs);
			return CommonData.WEATHER_SORTED_LIST.get(minIndex);
		}
	}
		
}
