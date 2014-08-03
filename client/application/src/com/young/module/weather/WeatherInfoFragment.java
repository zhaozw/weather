package com.young.module.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.adapter.FetureWeatherAdapter;
import com.young.common.util.L;
import com.young.common.util.SharePreferenceUtil;
import com.young.common.view.RingView;
import com.young.modules.R;

import android.R.integer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
	private FetureWeatherAdapter fwAdapter;
	
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
		
		DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(getActivity().WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		
		View view=View.inflate(getActivity(),R.layout.weather_current,null);		
		
		LinearLayout backgroundLayout = (LinearLayout)view.findViewById(R.id.todaybackground);
		LinearLayout.LayoutParams bllParams = new LinearLayout.LayoutParams(width, width);
		bllParams.setMargins(0, 0, 0, 0);
		backgroundLayout.setLayoutParams(bllParams);
		
		LinearLayout todayInfoLayout = (LinearLayout)view.findViewById(R.id.todayInfo);
		LinearLayout.LayoutParams tLlParams = new LinearLayout.LayoutParams(width, width);
		tLlParams.setMargins(0, -width, 0, 0);
		todayInfoLayout.setLayoutParams(tLlParams);
		
		ListView fetureWeatherList = (ListView)view.findViewById(R.id.fetureList);
		
		String testString = "[{weather_desc:'晴',weather_date:'明天 8月3日',weather_temp:'20~30'},"
				+ "{weather_desc:'晴',weather_date:'明天 8月3日',weather_temp:'20~30'},"
				+ "{weather_desc:'晴',weather_date:'明天 8月3日',weather_temp:'20~30'},"
				+ "{weather_desc:'晴',weather_date:'明天 8月3日',weather_temp:'20~30'}]";
		fwAdapter = new FetureWeatherAdapter(getActivity(), testString); 
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
		
        //view.scrollTo(0, 0);
		
		return view;
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