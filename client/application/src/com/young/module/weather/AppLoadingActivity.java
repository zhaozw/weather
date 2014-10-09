package com.young.module.weather;

import java.util.HashMap;
import java.util.Map;

import com.young.common.util.L;
import com.young.common.util.LocationUtil;
import com.young.common.util.SharePreferenceUtil;
import com.young.modules.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class AppLoadingActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
     // TODO Auto-generated method stub 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);


        RelativeLayout loadingIv = (RelativeLayout) this.findViewById(R.id.loading_page);
        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setStartOffset(500);
        animation.setDuration(500);
        loadingIv.setAnimation(animation);
        
        SharePreferenceUtil sp = new SharePreferenceUtil(this);
        String fcTimeStr = sp.getForecaseTime();
        boolean isForecast = sp.getForecase();
        if(fcTimeStr == null){
        	Map<String, String> timeMap = new HashMap<String, String>();
    		timeMap.put("hour", "07");
    		timeMap.put("minute", "30");
    		L.i("LoadSaveForecaseTime", ""+timeMap);
    		sp.setForecaseTime(timeMap.toString());
        }
        if(!isForecast){
        	Map<String, String> timeMap = new HashMap<String, String>();
    		timeMap.put("hour", "07");
    		timeMap.put("minute", "30");
    		L.i("LoadSaveForecaseTime", ""+timeMap);
    		sp.setForecaseTime(timeMap.toString());
        }
        
        LocationUtil.startGetLocation(this);//开启定位
        
        
        Intent intent = new Intent("com.young.common.service.ForecastService"); 
        Bundle bundle = new Bundle();  
        bundle.putInt("op", 0);  
        intent.putExtras(bundle);         
        startService(intent);    
        
        


        // 给animation设置监听器
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                Intent it = new Intent(AppLoadingActivity.this, MainActivity.class);
                startActivity(it);
                finish();
            }
        });
    }
}


