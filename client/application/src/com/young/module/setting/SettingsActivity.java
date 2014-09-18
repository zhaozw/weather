package com.young.module.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.young.modules.R;
import com.young.module.setting.SettingForecaseActivity;;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

		Button supportBtn = (Button) findViewById(R.id.support);
		View forecase = (View) findViewById(R.id.forecase_time);
		View feedback = (View) findViewById(R.id.feedback);
		View versionUpdate = (View) findViewById(R.id.version_update);
		View about = (View) findViewById(R.id.about);

		supportBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		forecase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent settingIntent = new Intent(SettingsActivity.this, SettingForecaseActivity.class);
				startActivity(settingIntent);

			}
		});

		feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		versionUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateManager manager = new UpdateManager(SettingsActivity.this);
				// 检查软件更新
				manager.checkUpdate();
			}
		});

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

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
