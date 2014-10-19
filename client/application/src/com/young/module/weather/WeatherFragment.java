package com.young.module.weather;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.young.common.CommonData;
import com.young.common.adapter.FetureWeatherAdapter;
import com.young.common.util.DateUtil;
import com.young.common.util.L;
import com.young.entity.City;
import com.young.modules.R;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WeatherFragment extends Fragment {

	public static final int GET_WEATHER_SCUESS = 1;
	public static final int GET_WEATHER_FAIL = 0;
	public static final String ARG_POSITION = "position";
	public static final String ARG_POSITION_TITLE = "positionTitle";

	private int position;
	private String positionTitle;
	private FetureWeatherAdapter fwAdapter;
	private int width = 0;
	private FragmentActivity fa;

	private JSONArray forecast = new JSONArray();
	private JSONArray scene = new JSONArray();

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadWeather(positionTitle);

		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getActivity()
				.getSystemService(getActivity().WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		fa = getActivity();
		View view = inflater.inflate(R.layout.weather_current, null);
		return renderView(view);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getPositionTitle() {
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

	public View renderView(View viewIn) {
		View view = viewIn;
		LinearLayout backgroundLayout = (LinearLayout) view
				.findViewById(R.id.todaybackground);
		LinearLayout.LayoutParams bllParams = new LinearLayout.LayoutParams(
				width, width);
		bllParams.setMargins(0, 0, 0, 0);
		backgroundLayout.setLayoutParams(bllParams);

		LinearLayout todayInfoLayout = (LinearLayout) view
				.findViewById(R.id.todayInfo);
		LinearLayout.LayoutParams tLlParams = new LinearLayout.LayoutParams(
				width, width);
		tLlParams.setMargins(0, -width, 0, 0);
		todayInfoLayout.setLayoutParams(tLlParams);

		TextView todayDate = (TextView) view.findViewById(R.id.today_date);

		ImageView weatherImg = (ImageView) view
				.findViewById(R.id.today_weather_img);
		TextView todayWindDir = (TextView) view
				.findViewById(R.id.today_wind_dir);
		TextView todayWind = (TextView) view.findViewById(R.id.today_wind);
		TextView todayFeltTemp = (TextView) view
				.findViewById(R.id.today_felt_temp);
		TextView todayHumidity = (TextView) view
				.findViewById(R.id.today_humidity);
		final TextView todayTempLow = (TextView) view
				.findViewById(R.id.today_temp_low);
		TextView todayTempHigh = (TextView) view
				.findViewById(R.id.today_temp_high);
		final TextView todayTempDesc = (TextView) view
				.findViewById(R.id.today_weather_desc);

		final TextView currentTempView = (TextView) view
				.findViewById(R.id.current_temp_center);
		LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(
				width, width);
		currentTempView.setPadding(0, 20, 0, 0);

		ViewTreeObserver vto = todayTempLow.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				int height = todayTempLow.getMeasuredHeight();
				currentTempView.setPadding(0, 0, 0, height / 2);
				return true;
			}
		});

		cParams.setMargins(0, -width, 0, 0);
		currentTempView.setLayoutParams(cParams);

		try {
			forecast = DateUtil.sortJsonArrayByDate(forecast, "days");
			JSONObject today = DateUtil.getSomeDay(forecast, 0);
			currentTempView.setText(scene.getJSONObject(0).getString("l1"));// +"°");
			todayHumidity.setText(getHumidityDesc(scene.getJSONObject(0)
					.getString("l2")));
			todayWind.setText(CommonData.WIND_DESC_MAP.get(scene.getJSONObject(0)
					.getString("l3")));
			todayWindDir.setText(CommonData.WIND_DIR_MAP.get(scene
					.getJSONObject(0).getString("l4")));
			String feltTempStr = scene.getJSONObject(0).getString(
					"feltTemperature");
			todayFeltTemp.setText(getFeltTempDesc(feltTempStr, 10));

			todayDate.setText(" 今天  "
					+ DateUtil.dateParse(today.getString("days")));
			todayTempLow.setText(today.getString("temp_low") + "℃");
			todayTempHigh.setText(today.getString("temp_high") + "℃");
			String weatherDesc = today.getString("weather");
			todayTempDesc.setText(weatherDesc);

			String singleWeather = getSingleWeatherDesc(weatherDesc);
			if (CommonData.WEATHER_MAP.get(singleWeather) != null) {
				weatherImg
						.setBackgroundResource((Integer) CommonData.WEATHER_MAP
								.get(singleWeather));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView fetureWeatherList = (ListView) view
				.findViewById(R.id.fetureList);

		if (forecast != null) {

			fwAdapter = new FetureWeatherAdapter(fa, forecast);
			fetureWeatherList.setAdapter(fwAdapter);

			int totalHeight = 0;
			for (int i = 0; i < fwAdapter.getCount(); i++) {
				View listItem = fwAdapter.getView(i, null, fetureWeatherList);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = fetureWeatherList.getLayoutParams();
			params.height = totalHeight;
			fetureWeatherList.setLayoutParams(params);

			fetureWeatherList.setFocusable(false);
		}

		return view;
	}
	
	private String getSingleWeatherDesc(String weatherAll){
		String[] weathers = weatherAll.split("转|-");
		List<Integer> indexs = new ArrayList<Integer>();
		for(String weather : weathers){
			indexs.add(CommonData.WEATHER_SORTED_LIST.indexOf(weather));			
		}
		int minIndex = Collections.min(indexs);
		return CommonData.WEATHER_SORTED_LIST.get(minIndex);
	}

	private String getHumidityDesc(String humidityStr) {
		int humidity = Integer.parseInt(humidityStr);
		return CommonData.HUMIDITY_DESC[humidity / 20];
	}

	private String getFeltTempDesc(String feltTempStr, int nowMonth) {
		double feltTemp = Double.parseDouble(feltTempStr);
		if (nowMonth >= 3 && nowMonth <= 8) { // summer
			for (String temp : CommonData.SUMMER_FELT_TEMP_LIST) {
				if (feltTemp < Double.parseDouble(temp)) {
					return CommonData.SUMMER_FELT_TEMP_DESC[CommonData.SUMMER_FELT_TEMP_LIST
							.indexOf(temp)];
				}
			}
			return CommonData.SUMMER_FELT_TEMP_DESC[CommonData.SUMMER_FELT_TEMP_DESC.length - 1];
		} else {
			for (String temp : CommonData.WINTER_FELT_TEMP_LIST) {
				if (feltTemp < Double.parseDouble(temp)) {
					return CommonData.WINTER_FELT_TEMP_DESC[CommonData.WINTER_FELT_TEMP_LIST
							.indexOf(temp)];
				}
			}
			return CommonData.WINTER_FELT_TEMP_DESC[CommonData.WINTER_FELT_TEMP_DESC.length - 1];
		}
	}

	public List<City> loadAllCityFromSharePreference() {
		List<City> citys = new ArrayList<City>();
		try {
			Gson gson = new Gson();
			Type lt = new TypeToken<List<City>>() {
			}.getType();
			citys = gson.fromJson(MainActivity.mSpUtil.getAllCity().toString(),
					lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return citys;
	}

	public void loadWeather(String cityName) {
		JSONObject currentCityWeather = new JSONObject();
		try {
			String allWeather = MainActivity.mSpUtil.getAllWeather().toString();
			JSONArray weatherList = new JSONArray(allWeather);
			for (int i = 0; i < weatherList.length(); i++) {
				if (cityName.equals(weatherList.getJSONObject(i)
						.getJSONArray("forecast").getJSONObject(0)
						.getString("citynm"))) {
					currentCityWeather = weatherList.getJSONObject(i);
					forecast = currentCityWeather.getJSONArray("forecast");
					scene = currentCityWeather.getJSONArray("scene");
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}