package com.young.service;

import java.net.URLEncoder;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import com.young.client.WeatherClient;
import com.young.view.WeatherInfoFragment;
import com.young.bean.City;
import com.young.bean.WeatherInfo;
import com.young.util.ConfigCache;
import com.young.util.L;
import com.young.util.XmlPullParseUtil;

public class GetWeatherTask extends AsyncTask<Void, Void, Integer> {
	private static final String BASE_URL = "http://sixweather.3gpk.net/SixWeather.aspx?city=%s";
	private static final int SUCCESS = 0;
	private static final int SUCCESS_YUJING = 1;
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
			String url = String.format(BASE_URL,
					URLEncoder.encode(mCity.getName(), "utf-8"));

			// 为了避免频繁刷新浪费流量，所以先读取内存中的信息
//			if (mApplication.getAllWeather() != null
//					&& mApplication.getAllWeather().getCity()
//							.equals(mCity.getName())) {
//				L.i("get the weather info from memory");
//				return SCUESS;// 直接返回，不继续执行
//			}
			// 再读取文件中的缓存信息
			String fileResult = ConfigCache.getUrlCache(mCity.getPinyin());// 读取文件中的缓存
			if (!TextUtils.isEmpty(fileResult)) {
				WeatherInfo allWeather = XmlPullParseUtil
						.parseWeatherInfo(fileResult);
				if (allWeather != null) {
					//mApplication.SetAllWeather(allWeather);
					L.i("get the weather info from file");
					return SUCCESS;
				}
			}
			// 最后才执行网络请求
			WeatherClient wClient = new WeatherClient(mCity.getName());
			String netResult = wClient.getWeatherInfo();
			if (!TextUtils.isEmpty(netResult)) {
				WeatherInfo allWeather = XmlPullParseUtil
						.parseWeatherInfo(netResult);
				if (allWeather != null) {
					//mApplication.SetAllWeather(allWeather);
					ConfigCache.setUrlCache(netResult, mCity.getPinyin());
					L.i("lwp", "get the weather info from network");
					String yujin = allWeather.getYujing();
					if (!TextUtils.isEmpty(yujin) && !yujin.contains("暂无预警"))
						return SUCCESS_YUJING;
					return SUCCESS;
				}
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
			//L.i(mApplication.getAllWeather().toString());
		}
	}
}