package com.young.module.weather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.young.MyApplication;
import com.young.common.util.L;
import com.young.common.util.WeatherClient;

public class UpdateAllWeatherTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;
	private String[] mCitys;

	public UpdateAllWeatherTask(Handler handler, String[] citys) {
		this.mHandler = handler;
		this.mCitys = citys;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			WeatherClient wClient = new WeatherClient(mCitys);
			String netResult = wClient.getAllWeatherInfo();
			if (!TextUtils.isEmpty(netResult)) {
				setAllWeather(netResult);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FAIL;
	}

	private void setAllWeather(String weathers) {
		// TODO Auto-generated method stub
		MainActivity.mSpUtil.setAllWeather(weathers);
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result < 0 ){
			mHandler.sendEmptyMessage(MainActivity.UPDATE_WEATHER_FAIL);
			L.i("get weather fail");
		}else{
			mHandler.sendEmptyMessage(MainActivity.UPDATE_WEATHER_SCUESS);
			L.i("get weather scuess");
		}
	}
	
	public List<String> loadAllCityFromSharePreference() {		
		List<String> citys = new ArrayList<String>();
		try {
			JSONArray cityList = new JSONArray(MainActivity.mSpUtil.getAllCity().toString());
			for(int i=0; i<cityList.length();i++){
				String city = (String) cityList.get(i);
				citys.add(city);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return citys;
	}
}