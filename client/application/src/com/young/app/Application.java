package com.young.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import com.google.gson.Gson;  

import com.young.app.Application;
import com.young.bean.City;
import com.young.db.CityDB;
import com.young.util.L;
import com.young.util.NetUtil;
import com.young.util.SharePreferenceUtil;
import com.young.util.T;

public class Application extends android.app.Application {
	public static final int CITY_LIST_SCUESS = 100;
	private static Application mApplication;
	private CityDB mCityDB;
	private List<City> mCityList;
	private SharePreferenceUtil mSpUtil;
	public static int mNetWorkState;

	public static synchronized Application getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		mCityDB = openCityDB();
		initData();
	}

	@Override
	public void onTerminate() {
		L.i("Application onTerminate...");
		super.onTerminate();
		if (mCityDB != null && mCityDB.isOpen())
			mCityDB.close();
	}

	// 当程序在后台运行时，释放这部分最占内存的资源
	public void free() {
		mCityList = null;
		System.gc();
	}

	public void initData() {
		mNetWorkState = NetUtil.getNetworkState(this);
		initCityList();
		mSpUtil = new SharePreferenceUtil(this,
				SharePreferenceUtil.CITY_SHAREPRE_FILE);
	}

	private CityDB openCityDB() {
		String path = "/data"
				+ Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + "com.young.view" + File.separator
				+ CityDB.CITY_DB_NAME;
		File db = new File(path);
		if (!db.exists() || getSharePreferenceUtil().getVersion() < 0) {
			L.i("db is not exists");
			try {
				InputStream is = getAssets().open(CityDB.CITY_DB_NAME);
				FileOutputStream fos = new FileOutputStream(db);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
				getSharePreferenceUtil().setVersion(1);
				} catch (IOException e) {
				e.printStackTrace();
				T.showLong(mApplication, e.getMessage());
				System.exit(0);
			}
		}
		return new CityDB(this, path);
	}
	
	private void initCityList() {	
		new Thread(new Runnable() {
			@Override
			public void run() {
				prepareCityList();
			}
		}).start();
	}

	private boolean prepareCityList() {
		mCityList = new ArrayList<City>();
		mCityList = mCityDB.getAllCity();// 获取数据库中�?��城市
		return true;
	}

	public synchronized CityDB getCityDB() {
		if (mCityDB == null || !mCityDB.isOpen())
			mCityDB = openCityDB();
		return mCityDB;
	}

	public synchronized SharePreferenceUtil getSharePreferenceUtil() {
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this,
					SharePreferenceUtil.CITY_SHAREPRE_FILE);
		return mSpUtil;
	}
	
	public List<City> getCityList() {
		return mCityList;
	}
	
	public void setAllWeather(String weathers){
		mSpUtil.setAllWeather(weathers);
	}
	
	public String getAllWeather(){
		return mSpUtil.getAllWeather();
	}
	
	public String loadWeather(int cityIndex){
		JSONObject currentCityWeather = new JSONObject();
		try {
			L.i(mSpUtil.getAllWeather().toString());
			//JSONObject allWeather = new JSONObject("{allWeather:[{weather:'test1'},{weather:'test2'},{weather:'test3'}]}");
			JSONArray weatherList = new JSONArray(mSpUtil.getAllWeather().toString());
			currentCityWeather = weatherList.getJSONObject(cityIndex);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentCityWeather.toString();
		
	}
	
	public List<String> loadAllCityFromSharePreference() {		
		List<String> citys = new ArrayList<String>();
		try {
			L.i(mSpUtil.getAllCity().toString());
			//JSONObject allCity = new JSONObject("{allCity:[{city:'test1'},{city:'test2'},{city:'test3'}]}");
			JSONArray cityList = new JSONArray(mSpUtil.getAllCity().toString());
			for(int i=0; i<cityList.length();i++){
				String city = (String) cityList.get(i);
				citys.add(city);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return citys;
	}
	
	public void saveCityChangeToSharePreference(List<String> newCitys) {
		Gson gson = new Gson();
		String citys = gson.toJson(newCitys);
		L.i(citys);
		mSpUtil.setAllCity(citys);
	}
}
