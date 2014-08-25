package com.young.module.location;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.young.common.util.DeviceUtil;
import com.young.common.util.HttpUtil;
import com.young.common.util.L;
import com.young.entity.City;
import com.young.module.weather.MainActivity;

public class ChangeMyCitiesTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;
	private City mCity;
	private String cityOp;
	
	private List<City> myCities;

	public ChangeMyCitiesTask(Handler handler, City city, String cityOp) {
		this.mHandler = handler;
		this.mCity = city;
		this.cityOp = cityOp;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("mac", DeviceUtil.DEVICE_ID);
			param.put("location", mCity.getNumber());
			String netResult = HttpUtil.postRequestByNVP(
					"http://106.187.94.192/weather/index.php?r="+cityOp,param);
			if (!TextUtils.isEmpty(netResult)) {
				myCities = loadAllCityFromSharePreference();
				if(cityOp.indexOf("merge") >= 0){
					myCities.add(mCity);
					saveCityChangeToSharePreference(myCities);
					String weatherResult = HttpUtil.postRequestByNVP(
							"http://106.187.94.192/weather/index.php?r=ReportOne/SendForecast",param);
					if (!TextUtils.isEmpty(weatherResult)) {
						String allWeather = MainActivity.mSpUtil.getAllWeather().toString();
						JSONArray weatherList = new JSONArray(allWeather);
						weatherList.put(new JSONObject(weatherResult));
						MainActivity.mSpUtil.setAllWeather(weatherList.toString());
					}
				}
				
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FAIL;
	}
	
	public List<City> loadAllCityFromSharePreference() {		
		List<City> citys = new ArrayList<City>();
		try {
			Gson gson = new Gson();
			Type lt=new TypeToken<List<City>>(){}.getType();
			citys=gson.fromJson(MainActivity.mSpUtil.getAllCity().toString(),lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return citys;
	}
	
	private void saveCityChangeToSharePreference(List<City> newCitys) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		String citys = gson.toJson(newCitys);
		L.i(citys);
		MainActivity.mSpUtil.setAllCity(citys);		
	}


	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result < 0 ){
			mHandler.sendEmptyMessage(CitySearchActivity.UPDATE_CITY_FAIL);
			L.i("update city fail");
		}else{
			mHandler.sendEmptyMessage(CitySearchActivity.UPDATE_CITY_SCUESS);
			L.i("update city success");
		}
	}
}