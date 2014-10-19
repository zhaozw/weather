package com.young.module.setting;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.util.Log;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.young.modules.R;
import com.young.module.setting.SettingForecaseActivity;

public class SettingsActivity extends Activity {

	FeedbackAgent fb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Context mContext = this;
		setContentView(R.layout.setting);
		ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
 
        Log.LOG = true;

        fb = new FeedbackAgent(this);
        // check if the app developer has replied to the feedback or not.
        fb.sync();

		Button supportBtn = (Button) findViewById(R.id.support);
		View forecase = (View) findViewById(R.id.forecase_time);
		View feedback = (View) findViewById(R.id.feedback);
		View versionUpdate = (View) findViewById(R.id.version_update);
		View about = (View) findViewById(R.id.about);
		
		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(mContext, 
		                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
		// 设置分享图片，参数2为本地图片的资源引用
		//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
		// 设置分享图片，参数2为本地图片的路径(绝对路径)
		//mController.setShareMedia(new UMImage(getActivity(), 
//		                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));


		mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.WEIXIN);
		supportBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 是否只有已登录用户才能打开分享选择页
		        mController.openShare(SettingsActivity.this, false);
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
				
				fb.startFeedbackActivity();

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
