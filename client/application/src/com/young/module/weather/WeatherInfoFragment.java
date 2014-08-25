package com.young.module.weather;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.adapter.FetureWeatherAdapter;
import com.young.common.util.L;
import com.young.modules.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	
	private JSONArray forecast;
	private JSONArray scene;
	
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
		
		TextView yestodayInfo = (TextView) view.findViewById(R.id.yestodayInfo);
		
		TextView todayDate = (TextView) view.findViewById(R.id.today_date);
		TextView curentTemp = (TextView) view.findViewById(R.id.current_temp);
		TextView todayWindDir = (TextView) view.findViewById(R.id.today_wind_dir);
		TextView todayWind = (TextView) view.findViewById(R.id.today_wind);
		TextView todayHumidity = (TextView) view.findViewById(R.id.today_humidity);
		TextView todayTempLow = (TextView) view.findViewById(R.id.today_temp_low);
		TextView todayTempHigh = (TextView) view.findViewById(R.id.today_temp_high);
		TextView todayTempDesc = (TextView) view.findViewById(R.id.today_weather_desc);
		
		try {
			yestodayInfo.setText(new Date()+"刷新");
			curentTemp.setText(scene.getJSONObject(0).getString("l1"));
			todayHumidity.setText(scene.getJSONObject(0).getString("l2")+"%");
			todayWind.setText(scene.getJSONObject(0).getString("l3")+"级");
			todayWindDir.setText(scene.getJSONObject(0).getString("l4"));
			
			todayDate.setText("今天 "+dateParse(forecast.getJSONObject(0).getString("days")));
			todayTempLow.setText(forecast.getJSONObject(0).getString("temp_low")+"℃");
			todayTempHigh.setText(forecast.getJSONObject(0).getString("temp_high")+"℃");
			todayTempDesc.setText(forecast.getJSONObject(0).getString("weather"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView fetureWeatherList = (ListView)view.findViewById(R.id.fetureList);

		fwAdapter = new FetureWeatherAdapter(getActivity(), forecast); 
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
	
	private String dateParse(String dateString){
		String[] dateStrings = dateString.split("-");
		return Integer.parseInt(dateStrings[1])+"月"+dateStrings[2]+"日";		
	}
	
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	weatherInfoString = loadWeather(positionTitle);
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
			String allWeather = MainActivity.mSpUtil.getAllWeather().toString();
			JSONArray weatherList = new JSONArray(allWeather);
			for(int i=0; i<weatherList.length(); i++){
				if(cityName.equals(weatherList.getJSONObject(i).getJSONArray("forecast").getJSONObject(0).getString("citynm"))){
					currentCityWeather = weatherList.getJSONObject(i);
					forecast = weatherList.getJSONObject(i).getJSONArray("forecast");
					scene = weatherList.getJSONObject(i).getJSONArray("scene");
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return currentCityWeather.toString();
	
	}

}