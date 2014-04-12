package com.young.module.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.young.MyApplication;
import com.young.common.util.L;
import com.young.common.util.WeatherClient;
import com.young.entity.City;

public class GetWeatherTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;
	private String mCity;

	public GetWeatherTask(Handler handler, String city) {
		this.mHandler = handler;
		this.mCity = city;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			WeatherClient wClient = new WeatherClient(mCity);
			String netResult = wClient.getWeatherInfo();
			if (!TextUtils.isEmpty(netResult)) {
				updateOneCityWeather(mCity, netResult);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FAIL;
	}

	public void updateOneCityWeather(String city, String weather){
		boolean isUpdate = false;
		JSONArray weatherList = null;
		try {
			JSONObject newWeather = new JSONObject(weather.toString());		
			weatherList = new JSONArray(MainActivity.mSpUtil.getAllWeather().toString());			
			for(int i=0; i<weatherList.length(); i++){
				if(city.equals(weatherList.getJSONObject(i).getString("city"))){
					weatherList.put(i, newWeather);
					isUpdate = true;
					break;
				}
			}
			if(!isUpdate){
				weatherList.put(newWeather);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		L.i("weatherChange",weatherList.toString());
		MainActivity.mSpUtil.setAllWeather(weatherList.toString());
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result < 0 ){
			//mHandler.sendEmptyMessage(WeatherInfoFragment.GET_WEATHER_FAIL);
			L.i("get weather fail");
		}else{
			//mHandler.sendEmptyMessage(WeatherInfoFragment.GET_WEATHER_SCUESS);
			L.i("get weather scuess");
		}
	}
}