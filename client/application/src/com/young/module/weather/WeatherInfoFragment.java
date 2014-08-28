package com.young.module.weather;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.adapter.FetureWeatherAdapter;
import com.young.common.util.DateUtil;
import com.young.common.util.L;
import com.young.modules.R;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WeatherInfoFragment extends Fragment {

	public static final int GET_WEATHER_SCUESS = 1;
	public static final int GET_WEATHER_FAIL = 0;
	private static final String ARG_POSITION = "position";
	private static final String ARG_POSITION_TITLE = "positionTitle";
	private static final Map<String, String> WIND_DIR_MAP = new HashMap<String, String>();
	private static final Map<String, String> WIND_MAP = new HashMap<String, String>();

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
		
		WIND_DIR_MAP.put("0", "无持续风向");
		WIND_DIR_MAP.put("1", "东北风");
		WIND_DIR_MAP.put("2", "东风");
		WIND_DIR_MAP.put("3", "东南风");
		WIND_DIR_MAP.put("4", "南风");
		WIND_DIR_MAP.put("5", "西南风");
		WIND_DIR_MAP.put("6", "西风");
		WIND_DIR_MAP.put("7", "西北风");
		WIND_DIR_MAP.put("8", "北风");
		WIND_DIR_MAP.put("9", "旋转风");
		
		WIND_MAP.put("0", "微风");
		WIND_MAP.put("1", "3-4级");
		WIND_MAP.put("2", "4-5级");
		WIND_MAP.put("3", "5-6级");
		WIND_MAP.put("4", "6-7级");
		WIND_MAP.put("5", "7-8级");
		WIND_MAP.put("6", "8-9级");
		WIND_MAP.put("7", "9-10级");
		WIND_MAP.put("8", "10-11级");
		WIND_MAP.put("9", "11-12级");
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
		
		TextView todayDate = (TextView) view.findViewById(R.id.today_date);
		TextView yestodayInfo = (TextView) view.findViewById(R.id.yestodayInfo);
		
		
		//TextView curentTemp = (TextView) view.findViewById(R.id.current_temp);
		TextView todayWindDir = (TextView) view.findViewById(R.id.today_wind_dir);
		TextView todayWind = (TextView) view.findViewById(R.id.today_wind);
		TextView todayHumidity = (TextView) view.findViewById(R.id.today_humidity);
		final TextView todayTempLow = (TextView) view.findViewById(R.id.today_temp_low);
		TextView todayTempHigh = (TextView) view.findViewById(R.id.today_temp_high);
		final TextView todayTempDesc = (TextView) view.findViewById(R.id.today_weather_desc);
		
		final TextView currentTempView = (TextView)view.findViewById(R.id.current_temp_center);
		LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(width, width);
		currentTempView.setPadding(0, 20, 0, 0);
		
		ViewTreeObserver vto = todayTempLow.getViewTreeObserver();  
	    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {  
	        public boolean onPreDraw() {  
	            int height = todayTempLow.getMeasuredHeight();  
	            L.i("height", ""+height); 
	            currentTempView.setPadding(0, 0, 0, height/2);
	            return true;  
	        }  
	    });  
		
		cParams.setMargins(0, -width, 0, 0);
		currentTempView.setLayoutParams(cParams);
		
		
		try {
			forecast = DateUtil.sortJsonArrayByDate(forecast, "days");
			yestodayInfo.setText("昨天温度:"+forecast.getJSONObject(0).getString("temp_low")+"°~"+forecast.getJSONObject(0).getString("temp_high")+"°");
			currentTempView.setText(scene.getJSONObject(0).getString("l1"));//+"°");
			todayHumidity.setText(scene.getJSONObject(0).getString("l2")+"%");
			todayWind.setText(WIND_MAP.get(scene.getJSONObject(0).getString("l3")));
			todayWindDir.setText(WIND_DIR_MAP.get(scene.getJSONObject(0).getString("l4")));
			
			todayDate.setText("今天 "+DateUtil.dateParse(forecast.getJSONObject(1).getString("days")));
			todayTempLow.setText(forecast.getJSONObject(1).getString("temp_low")+"℃");
			todayTempHigh.setText(forecast.getJSONObject(1).getString("temp_high")+"℃");
			todayTempDesc.setText(forecast.getJSONObject(1).getString("weather"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView fetureWeatherList = (ListView)view.findViewById(R.id.fetureList);

//		forecast.remove(0);
//		forecast.remove(1);
		JSONArray forecaseTempArray = new JSONArray();
		if(forecast != null){
		for(int i=0;i<forecast.length();i++){
			try {
				int dis = DateUtil.daysBetween(new Date(),DateUtil.StringToDate(forecast.getJSONObject(i).getString("days"), "yyyy-MM-dd"));
				if(dis!=0 && dis!=-1){
					forecaseTempArray.put(forecast.getJSONObject(i));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}
		}
		fwAdapter = new FetureWeatherAdapter(getActivity(), forecaseTempArray); 
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