package com.young.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.young.util.DeviceUtil;
import com.young.util.L;

public class HttpUtil
{
	private static final String CHARSET = HTTP.UTF_8;
	public static HttpClient httpClient;
	public static final String BASE_URL = 
		"http://106.187.94.192/weather/index.php?r=machine/ChangeLocation";

	
	public static String getRequest(String url)
		throws Exception
	{
		
		HttpGet get = new HttpGet(url);
		
		HttpResponse httpResponse = httpClient.execute(get);
		
		if (httpResponse.getStatusLine()
			.getStatusCode() == 200)
		{
			
			String result = EntityUtils
				.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	public static String postRequest(String url
		, Map<String ,Object> rawParams)throws Exception
	{
		L.i("enter postRequest");
		HttpPost post = new HttpPost(url);
		
		//向服务器写json
        JSONObject json = new JSONObject();
        json.put("mac", DeviceUtil.DEVICE_ID);
        JSONObject citys = new JSONObject();
        citys.put("stay", (String)rawParams.get("stay"));
        JSONArray otherCitys = new JSONArray(new Gson().toJson((List)rawParams.get("other")));       
        citys.put("other", otherCitys);
        json.put("locations", citys);
        StringEntity se = new StringEntity(json.toString());
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(se);
        
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		for(String key : rawParams.keySet())
//		{
//			
//			params.add(new BasicNameValuePair(key , rawParams.get(key)));
//		}
//		
//		post.setEntity(new UrlEncodedFormEntity(
//			params, "utf-8"));
		
		HttpClient client = getHttpClient();
		HttpResponse httpResponse = client.execute(post);
		
		if (httpResponse.getStatusLine()
			.getStatusCode() == 200)
		{
			
			String result = EntityUtils
				.toString(httpResponse.getEntity());
			System.out.println("result = " + result);
			return result;
		}
		return null;
	}
	
	 public static synchronized HttpClient getHttpClient() {
		 if (null == httpClient) {
			 HttpParams params = new BasicHttpParams();
           //设置一些基本参数
			 HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			 HttpProtocolParams.setContentCharset(params,
					 CHARSET);
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams
            .setUserAgent(params,"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                    + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            // 超时设置
            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 1000);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 2000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, 4000);
           
            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg = new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory
                   .getSocketFactory(), 443));

            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                    params, schReg);
            httpClient = new DefaultHttpClient(conMgr, params);
        }
        return httpClient;
    }
	
	
}
