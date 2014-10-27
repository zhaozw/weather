package com.young.module.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;

import android.widget.TextView;

import com.young.common.util.DeviceUtil;
import com.young.modules.R;

public class AboutUsActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settting_about_us);
		ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("V "+DeviceUtil.getVersionName(this));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case android.R.id.home:  
			finish();
			return true;    
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
