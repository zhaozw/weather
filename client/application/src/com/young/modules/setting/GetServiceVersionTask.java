package com.young.modules.setting;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.young.common.util.HttpUtil;
import com.young.common.util.L;
import com.young.modules.weather.MainActivity;

public class GetServiceVersionTask extends AsyncTask<Void, Void, Integer> {
	private static final int SUCCESS = 0;
	private static final int FAIL = -1;
	private Handler mHandler;

	public GetServiceVersionTask(Handler handler) {
		this.mHandler = handler;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			String netResult = HttpUtil.getRequest(
					"http://106.187.94.192/weather/index.php?r=Version/get");//wClient.getAllWeatherInfo();
			if (!TextUtils.isEmpty(netResult)) {
				JSONObject jObject = new JSONObject(netResult);
				setNewVersion(jObject.getString("code"));
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FAIL;
	}
	
	private void setNewVersion(String version) {
		// TODO Auto-generated method stub
		MainActivity.mSpUtil.setVersion(version);
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result < 0 ){
			mHandler.sendEmptyMessage(-1);
			L.i("get Service Version fail");
		}else{
			mHandler.sendEmptyMessage(0);
			L.i("get Service Version sccuess");
		}
	}
}