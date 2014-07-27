package com.young.module.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.util.L;
import com.young.common.util.SharePreferenceUtil;
import com.young.modules.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class WeatherInfoFragment extends Fragment {

	public static final int GET_WEATHER_SCUESS = 1;
	public static final int GET_WEATHER_FAIL = 0;
	private static final String ARG_POSITION = "position";
	private static final String ARG_POSITION_TITLE = "positionTitle";

	private int position;
	private String positionTitle;
	private String weatherInfoString;
	private TextView v;
	
	public static WeatherInfoFragment newInstance(int position, String positionTitle) {
		WeatherInfoFragment f = new WeatherInfoFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		b.putString(ARG_POSITION_TITLE, positionTitle);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());

		v = new TextView(getActivity());
		params.setMargins(0, margin, 0, 0);
		//params.setMargins(margin, margin, margin, margin);
		v.setLayoutParams(params);
		v.setGravity(Gravity.CENTER);
		v.setBackgroundColor(Color.rgb(103,172,250));
		v.setText(weatherInfoString);

		fl.addView(v);
		return fl;
	}
	
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	weatherInfoString = loadWeather(positionTitle);
        	//v.setText("WEATHER CHANGED " + (position + 1) + "------" + weatherInfoString);
        } else {
        }
    }
	
	private void initData(){
		position = getArguments().getInt(ARG_POSITION);
		positionTitle = getArguments().getString(ARG_POSITION_TITLE);
		weatherInfoString = loadWeather(positionTitle);
	}
	
	public String loadWeather(String cityName){
		if("添加城市".equals(cityName)){
			return "请添加城市";
		}
		JSONObject currentCityWeather = new JSONObject();
		try {
			String allWeather = "[{city:厦门},{city:北京},{city:上海}]";//MainActivity.mSpUtil.getAllWeather().toString();
			JSONArray weatherList = new JSONArray(allWeather);
			for(int i=0; i<weatherList.length(); i++){
				if(cityName.equals(weatherList.getJSONObject(i).getString("city"))){
					currentCityWeather = weatherList.getJSONObject(i);
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return currentCityWeather.toString();
	
	}

}