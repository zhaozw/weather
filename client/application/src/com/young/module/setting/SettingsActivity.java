package com.young.module.setting;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.util.Log;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.young.common.util.HttpUtil;
import com.young.modules.R;
import com.young.module.setting.SettingForecaseActivity;
import com.young.module.weather.MainActivity;
import com.young.module.weather.UpdateAllWeatherTask;

public class SettingsActivity extends Activity {

	FeedbackAgent fb;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				UpdateManager manager = new UpdateManager(SettingsActivity.this);
				// 检查软件更新
				manager.checkUpdate(MainActivity.mSpUtil.getVersion());
				break;
			case -1:
				
				break;
			default:
				break;
			}
		}
	};
	
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


		//mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.WEIXIN);
		
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx280e54e9704d30a3";
		String appSecret = "ea6582bdf3591005a925433a30533d51";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(mContext,appID,appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(mContext,appID,appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		//设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		//设置分享文字
		weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
		//设置title
		weixinContent.setTitle("友盟社会化分享组件-微信");
		//设置分享内容跳转URL
		weixinContent.setTargetUrl("http://www.toway.in");
		//设置分享图片
		mController.setShareMedia(weixinContent);
		
		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
		//设置朋友圈title
		circleMedia.setTitle("友盟社会化分享组件-朋友圈");
		circleMedia.setTargetUrl("http://www.toway.in");
		mController.setShareMedia(circleMedia);
		
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
				new GetServiceVersionTask(handler).execute();
			}
		});

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent aboutIntent = new Intent(SettingsActivity.this, AboutUsActivity.class);
				startActivity(aboutIntent);
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

}
