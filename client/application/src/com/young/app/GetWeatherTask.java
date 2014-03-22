package com.young.app;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.young.client.WeatherClient;
import com.young.view.WeatherInfoFragment;
import com.young.bean.City;
import com.young.util.L;

public class GetWeatherTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;
	private City mCity;
	private Application mApplication;

	public GetWeatherTask(Handler handler, City city) {
		this.mHandler = handler;
		this.mCity = city;
		mApplication = Application.getInstance();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			WeatherClient wClient = new WeatherClient(mCity.getName());
			String netResult = wClient.getWeatherInfo();
			if (!TextUtils.isEmpty(netResult)) {
				mApplication.setAllWeather(netResult);
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FAIL;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result < 0 ){
			mHandler.sendEmptyMessage(WeatherInfoFragment.GET_WEATHER_FAIL);// 获取天气信息失败
			L.i("get weather fail");
		}else{
			mHandler.sendEmptyMessage(WeatherInfoFragment.GET_WEATHER_SCUESS);// 获取天气信息成功，通知主线程更新
			L.i("get weather scuess");
			L.i(mApplication.getAllWeather());
		}
	}
}