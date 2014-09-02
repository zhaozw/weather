package com.young.common.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import android.content.Context;
import android.util.Log;

public class LocationUtil {
	
	private static Context mContext;
	private static LocationClient mLocationClient;
	private static SharePreferenceUtil sp ;
	
	public static void startGetLocation(Context context){
		mContext = context;
		mLocationClient = new LocationClient(mContext);
		sp = new SharePreferenceUtil(mContext);
		mLocationClient.registerLocationListener(new MyLocationListenner());  
		setLocationOption();
		mLocationClient.start();
	}
	
	
	private static void setLocationOption(){  
        LocationClientOption option = new LocationClientOption();  
        
        option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式,省电模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
             
        mLocationClient.setLocOption(option);  
          
    }  
	
	private static class MyLocationListenner implements BDLocationListener {  
        @Override  
        //接收位置信息  
        public void onReceiveLocation(BDLocation location) {  
            if (location == null)  
                return ;  
            JSONObject address = new JSONObject();
            try {
				address.put("City", location.getCity());
	            address.put("District", location.getDistrict());	           
	            sp.setLBS(address.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

            Log.i("Location", address.toString());  
            
            mLocationClient.stop();
            mLocationClient = null;
        }  
    }  
}
