package com.young.app;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.young.client.WeatherClient;
import com.young.view.MainActivity;
import com.young.view.WeatherInfoFragment;
import com.young.util.L;

public class UpdateAllWeatherTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;
	private String[] mCitys;
	private Application mApplication;

	public UpdateAllWeatherTask(Handler handler, String[] citys) {
		this.mHandler = handler;
		this.mCitys = citys;
		mApplication = Application.getInstance();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			WeatherClient wClient = new WeatherClient(mCitys);
			String netResult = wClient.getAllWeatherInfo();
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
			mHandler.sendEmptyMessage(MainActivity.UPDATE_WEATHER_FAIL);// 获取天气信息失败
			L.i("get weather fail");
		}else{
			mHandler.sendEmptyMessage(MainActivity.UPDATE_WEATHER_SCUESS);// 获取天气信息成功，通知主线程更新
			L.i("get weather scuess");
		}
	}
}