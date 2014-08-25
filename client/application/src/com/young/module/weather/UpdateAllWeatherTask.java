package com.young.module.weather;

import java.util.HashMap;
import java.util.Map;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.young.common.util.DeviceUtil;
import com.young.common.util.HttpUtil;
import com.young.common.util.L;

public class UpdateAllWeatherTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;

	public UpdateAllWeatherTask(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("mac", DeviceUtil.DEVICE_ID);
			String netResult = HttpUtil.postRequestByNVP(
					"http://106.187.94.192/weather/index.php?r=Report/SendForecast",param);//wClient.getAllWeatherInfo();
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
}