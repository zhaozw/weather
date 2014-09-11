package com.young.common.util;

import java.util.UUID;

import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	
	public static String DEVICE_ID; 
	
	public static String getDeviceId(Context context, ContentResolver resolver) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(resolver, android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

}
