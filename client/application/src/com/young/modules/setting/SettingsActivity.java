package com.young.modules.setting;

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
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.young.modules.R;
import com.young.modules.setting.SettingForecaseActivity;
import com.young.modules.weather.MainActivity;

public class SettingsActivity extends Activity {

	FeedbackAgent fb;
	SnsPostListener SnsPostListener;
	
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
		
		//设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		
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
		weixinContent.setShareContent("天气预报就该如此简单，今天，你感受天气了吗？ http://www.toway.in");
		//设置title
		weixinContent.setTitle("好天气，好生活");
		//设置分享内容跳转URL
		weixinContent.setTargetUrl("http://www.toway.in");
		//设置分享图片
		mController.setShareMedia(weixinContent);
		
		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("天气预报就该如此简单，今天，你感受天气了吗？ http://www.toway.in");
		//设置朋友圈title
		circleMedia.setTitle("好天气，好生活");
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
				
//				fb.startFeedbackActivity();
				Intent aboutIntent = new Intent(SettingsActivity.this, FeedbackActivity.class);
				startActivity(aboutIntent);

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
		
		SnsPostListener  = new SnsPostListener() {

	        @Override
		    public void onStart() {
	
		    }

		    @Override
		    public void onComplete(SHARE_MEDIA platform, int stCode,
		        SocializeEntity entity) {
			      if (stCode == 200) {
			        Toast.makeText(SettingsActivity.this, "分享成功", Toast.LENGTH_SHORT)
			            .show();
			      } else {
			        Toast.makeText(SettingsActivity.this,
			            "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
			            .show();
			      }
		    }
		};

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
