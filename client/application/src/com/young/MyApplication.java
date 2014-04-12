package com.young;

import com.young.common.util.L;

public class MyApplication extends android.app.Application {
	
	private static MyApplication mApplication;

	public static synchronized MyApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		L.i("Application onTerminate...");
		super.onTerminate();
	}

	// 当程序在后台运行时，释放这部分最占内存的资源
	public void free() {
		System.gc();
	}

//	public void initData() {
//		mNetWorkState = NetUtil.getNetworkState(this);
//		initCityList();
//		mSpUtil = new SharePreferenceUtil(this,
//				SharePreferenceUtil.CITY_SHAREPRE_FILE);
//	}

//	private CityDB openCityDB() {
//		String path = "/data"
//				+ Environment.getDataDirectory().getAbsolutePath()
//				+ File.separator + "com.young.view" + File.separator
//				+ CityDB.CITY_DB_NAME;
//		File db = new File(path);
//		if (!db.exists() || getSharePreferenceUtil().getVersion() < 0) {
//			L.i("db is not exists");
//			try {
//				InputStream is = getAssets().open(CityDB.CITY_DB_NAME);
//				FileOutputStream fos = new FileOutputStream(db);
//				int len = -1;
//				byte[] buffer = new byte[1024];
//				while ((len = is.read(buffer)) != -1) {
//					fos.write(buffer, 0, len);
//					fos.flush();
//				}
//				fos.close();
//				is.close();
//				getSharePreferenceUtil().setVersion(1);
//				} catch (IOException e) {
//				e.printStackTrace();
//				T.showLong(mApplication, e.getMessage());
//				System.exit(0);
//			}
//		}
//		return new CityDB(this, path);
//	}
//	
//	private void initCityList() {	
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				prepareCityList();
//			}
//		}).start();
//	}
//
//	private boolean prepareCityList() {
//		mCityList = new ArrayList<City>();
//		mCityList = mCityDB.getAllCity();// 获取数据库中�?��城市
//		return true;
//	}
//
//	public synchronized CityDB getCityDB() {
//		if (mCityDB == null || !mCityDB.isOpen())
//			mCityDB = openCityDB();
//		return mCityDB;
//	}
//
//	public synchronized SharePreferenceUtil getSharePreferenceUtil() {
//		if (mSpUtil == null)
//			mSpUtil = new SharePreferenceUtil(this,
//					SharePreferenceUtil.CITY_SHAREPRE_FILE);
//		return mSpUtil;
//	}
//	
//	public List<City> getCityList() {
//		return mCityList;
//	}
//	
//	public void setAllWeather(String weathers){
//		mSpUtil.setAllWeather(weathers);
//	}
//	
//	public void updateOneCityWeather(String city, String weather){
//		boolean isUpdate = false;
//		JSONArray weatherList = null;
//		try {
//			JSONObject newWeather = new JSONObject(weather.toString());		
//			weatherList = new JSONArray(getAllWeather().toString());			
//			for(int i=0; i<weatherList.length(); i++){
//				if(city.equals(weatherList.getJSONObject(i).getString("city"))){
//					weatherList.put(i, newWeather);
//					isUpdate = true;
//					break;
//				}
//			}
//			if(!isUpdate){
//				weatherList.put(newWeather);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		L.i("weatherChange",weatherList.toString());
//		mSpUtil.setAllWeather(weatherList.toString());
//	}
//	
//	public String getAllWeather(){
//		return mSpUtil.getAllWeather();
//	}
//	
//	public String loadWeather(String cityName){
//		JSONObject currentCityWeather = new JSONObject();
//		try {
//			JSONArray weatherList = new JSONArray(mSpUtil.getAllWeather().toString());
//			for(int i=0; i<weatherList.length(); i++){
//				L.i("compare",cityName+"-"+weatherList.getJSONObject(i).getString("city"));
//				if(cityName.equals(weatherList.getJSONObject(i).getString("city"))){
//					currentCityWeather = weatherList.getJSONObject(i);
//					break;
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return currentCityWeather.toString();
//		
//	}
	
//	public List<String> loadAllCityFromSharePreference() {		
//		List<String> citys = new ArrayList<String>();
//		try {
//			JSONArray cityList = new JSONArray(mSpUtil.getAllCity().toString());
//			for(int i=0; i<cityList.length();i++){
//				String city = (String) cityList.get(i);
//				citys.add(city);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return citys;
//	}
	
//	public void saveCityChangeToSharePreference(List<String> newCitys) {
//		Gson gson = new Gson();
//		String citys = gson.toJson(newCitys);
//		L.i(citys);
//		mSpUtil.setAllCity(citys);
//	}
}
