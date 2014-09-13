package com.young.module.setting;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.young.common.util.L;
import com.young.module.weather.MainActivity;
import com.young.modules.R;

public class SettingForecaseActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_forecase);

		ImageView forecase = (ImageView) findViewById(R.id.forecase_btn);
		
		String forecaseTimeStr = MainActivity.mSpUtil.getForecaseTime();
		if(forecaseTimeStr == null || forecaseTimeStr.equals("")){
			forecase.setImageResource(R.drawable.kaiqi);
		}else{
			forecase.setImageResource(R.drawable.guanbi);
		}
		
		forecase.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				String forecaseTimeStr = MainActivity.mSpUtil.getForecaseTime();
				if(forecaseTimeStr == null || forecaseTimeStr.equals("")){
					((ImageView) v).setImageResource(R.drawable.guanbi);
					Date currentTime = new Date();
					int currentHour = currentTime.getHours();
					int currentminute = currentTime.getMinutes();
					Map<String, String> timeMap = new HashMap<String, String>();
					timeMap.put("hour", ""+currentHour);
					timeMap.put("minute", ""+currentminute);
					L.i("timeMap", ""+timeMap);
					MainActivity.mSpUtil.setForecaseTime(timeMap.toString());
 				}else{
					((ImageView) v).setImageResource(R.drawable.kaiqi);
					MainActivity.mSpUtil.setForecaseTime(null);
				}
				

			}
		});


	}
}
