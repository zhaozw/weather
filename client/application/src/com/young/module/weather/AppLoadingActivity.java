package com.young.module.weather;

import com.young.common.util.LocationUtil;
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
        animation.setStartOffset(1000);
        animation.setDuration(1000);
        loadingIv.setAnimation(animation);
        
        LocationUtil.startGetLocation(this);


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


