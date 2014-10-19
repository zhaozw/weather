package com.young.common.reciver;

import com.young.common.util.L;
import com.young.module.weather.MainActivity;
import com.young.modules.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 
 * @ClassName: AlarmReceiver  
 * @Description: 闹铃时间到了会进入这个广播，这个时候可以做一些该做的业务。
 * @author HuHood
 * @date 2013-11-25 下午4:44:30  
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
    public void onReceive(Context context, Intent intent) {
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(ns);

		 Toast.makeText(context, "你设置的闹钟时间到了", Toast.LENGTH_SHORT).show(); 
		 
		 //定义通知栏展现的内容信息
		 int icon = R.drawable.ic_launcher;
		 CharSequence tickerText = "我的通知栏标题";
		 long when = System.currentTimeMillis();
		 Notification notification = new Notification(icon, tickerText, when);

		 //定义下拉通知栏时要展现的内容信息

		 CharSequence contentTitle = "我的通知栏标展开标题";
		 CharSequence contentText = "我的通知栏展开详细内容";
		 Intent notificationIntent = new Intent(context, MainActivity.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
		 notification.setLatestEventInfo(context, contentTitle, contentText,contentIntent); 
		 //用mNotificationManager的notify方法通知用户生成标题栏消息通知

		 mNotificationManager.notify(1, notification);
	}
}
