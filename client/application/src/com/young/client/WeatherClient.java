package com.young.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class WeatherClient {
	
	final static String serverUri = "http://106.187.94.192/weather/index.php?r=Test/ConnectDB";
    private HttpClient getClient; 
    private String cityParam;
    private String cityListParam;
    
    public WeatherClient(String city){
    	Log.i("WeatherClient", "创建Client对象成功");   
    	try {
			this.cityParam = URLEncoder.encode(city, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	//得到HttpClient对象 
    	getClient = new DefaultHttpClient();   
    }
    
    public WeatherClient(String[] city){
    	Log.i("WeatherClient", "创建Client对象成功"); 
    	String paramTmp = "";
    	for(int i=0; i<city.length; i++){
    		paramTmp += (city[i] + ",");
    	}
    	try {
			this.cityListParam = URLEncoder.encode(paramTmp.substring(-1), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	//得到HttpClient对象 
    	getClient = new DefaultHttpClient();   
    }
    
    public String getWeatherInfo(){
    	Log.i("WeatherClient", "获取天气信息");   
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("location", cityParam);
    	return doGet(serverUri, params);
    }
    
    public String getAllWeatherInfo(){
    	Log.i("WeatherClient", "获取所有城市天气信息");   
    	Map<String, String> params = new HashMap<String, String>();
    	//TODO 需约定传多个城市获取所有天气信息的接口参数
    	params.put("location", cityListParam);
    	return doGet(serverUri, params);
    }
    
    @SuppressWarnings("rawtypes")
	private String buildParamString(Map params){
    	/* 建立HTTPGet对象 */  
        String paramStr = "";  
        Iterator iter = params.entrySet().iterator();  
        while (iter.hasNext()) {  
            Map.Entry entry = (Map.Entry) iter.next();  
            Object key = entry.getKey();  
            Object val = entry.getValue();  
            paramStr += paramStr = "&" + key + "=" + val;  
        }  
//        if (!paramStr.equals("")) {  
//            paramStr = paramStr.replaceFirst("&", "?");  
//        }  
        return paramStr;
    }
    
    private String doGet(String url, Map params) {
    	String paramString = buildParamString(params);
    	url += paramString;
    	try {             
            //得到HttpGet对象  
            HttpGet request = new HttpGet(url);  
            //客户端使用GET方式执行请教，获得服务器端的回应response  
            HttpResponse response = getClient.execute(request);  
            //判断请求是否成功    
            Log.i("doGet", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码  
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
                Log.i("doGet", "请求服务器端成功");      
                String netResult = EntityUtils.toString(response.getEntity(), "utf-8");
                Log.i("doGet", "result = " + netResult); //获取响应内容   
                return netResult;
            }else {  
                Log.i("doGet", "请求服务器端失败");                
            }             
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    	return null;
    }
    
    @SuppressWarnings("rawtypes")
	private void doPost(String url, Map params) {
    	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();  
        postParameters.add(new BasicNameValuePair("city", (String)params.get("city")));
        BufferedReader in = null;  
        try {  
            HttpPost request = new HttpPost(url);  

            //实例化UrlEncodedFormEntity对象  
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(  
                    postParameters, "utf-8");  
 
            //使用HttpPost对象来设置UrlEncodedFormEntity的Entity  
            request.setEntity(formEntity);  
            HttpResponse response = getClient.execute(request);
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
            	 Log.i("doGet", "请求服务器端成功");
	            in = new BufferedReader(  
	                    new InputStreamReader(  
	                            response.getEntity().getContent()));  
	 
	            StringBuffer string = new StringBuffer("");  
	            String lineStr = "";  
	            while ((lineStr = in.readLine()) != null) {  
	                string.append(lineStr + "\n");  
	            }  
	            in.close();  	 
	            String resultStr = string.toString();  
	            System.out.println(resultStr);  
            }else{
            	 Log.i("doPost", "请求服务器端失败");
            }
        } catch(Exception e) {  
            // Do something about exceptions  
        } finally {  
            if (in != null) {  
                try {  
                    in.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }

}
