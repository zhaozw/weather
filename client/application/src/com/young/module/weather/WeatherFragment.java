package com.young.module.weather;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.young.common.adapter.FetureWeatherAdapter;
import com.young.common.util.DateUtil;
import com.young.common.util.L;
import com.young.entity.City;
import com.young.modules.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WeatherFragment extends Fragment {

	public static final int GET_WEATHER_SCUESS = 1;
	public static final int GET_WEATHER_FAIL = 0;
	public static final String ARG_POSITION = "position";
	public static final String ARG_POSITION_TITLE = "positionTitle";
	private static final Map<String, String> WIND_DIR_MAP = new HashMap<String, String>();
	private static final Map<String, String> WIND_MAP = new HashMap<String, String>();

	private int position;
	private String positionTitle;
	private FetureWeatherAdapter fwAdapter;
	private int width = 0;
	private FragmentActivity fa;
	
	private JSONArray forecast;
	private JSONArray scene;

	@SuppressWarnings("static-access")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loadWeather(positionTitle);
		
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
		
		DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(getActivity().WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		fa = getActivity();
		View view = inflater.inflate(R.layout.weather_current, null);  
		return renderView(view);
	}
	
	
//	@Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//        	citys = loadAllCityFromSharePreference();
//        	loadWeather(citys.get(position).getName());
//        } else {
//        }
//    }
	
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
	
	public View renderView(View viewIn){
		View view = viewIn;
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
			
			L.i("city-paint", forecast.getJSONObject(0).getString("citynm"));
			todayDate.setText(forecast.getJSONObject(0).getString("citynm")+" 今天 "+DateUtil.dateParse(forecast.getJSONObject(1).getString("days")));
			todayTempLow.setText(forecast.getJSONObject(1).getString("temp_low")+"℃");
			todayTempHigh.setText(forecast.getJSONObject(1).getString("temp_high")+"℃");
			todayTempDesc.setText(forecast.getJSONObject(1).getString("weather"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView fetureWeatherList = (ListView)view.findViewById(R.id.fetureList);

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
		fwAdapter = new FetureWeatherAdapter(fa, forecaseTempArray); 
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
        
        return view;
	}
	
	public List<City> loadAllCityFromSharePreference() {		
		List<City> citys = new ArrayList<City>();
		try {
			Gson gson = new Gson();
			Type lt=new TypeToken<List<City>>(){}.getType();
			citys=gson.fromJson(MainActivity.mSpUtil.getAllCity().toString(),lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return citys;
	}
	
	public void loadWeather(String cityName){
		if("添加城市".equals(cityName)){
			return; 
		}
		JSONObject currentCityWeather = new JSONObject();
		try {
			String allWeather = MainActivity.mSpUtil.getAllWeather().toString();
			JSONArray weatherList = new JSONArray(allWeather);
			for(int i=0; i<weatherList.length(); i++){
				if(cityName.equals(weatherList.getJSONObject(i).getJSONArray("forecast").getJSONObject(0).getString("citynm"))){
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