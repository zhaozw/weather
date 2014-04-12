package com.young.common.util;

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
    	Log.i("WeatherClient", "����Client����ɹ�");   
    	try {
			this.cityParam = URLEncoder.encode(city, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	//�õ�HttpClient���� 
    	getClient = new DefaultHttpClient();   
    }
    
    public WeatherClient(String[] city){
    	Log.i("WeatherClient", "����Client����ɹ�"); 
    	String paramTmp = "";
    	for(int i=0; i<city.length; i++){
    		paramTmp += (city[i] + ",");
    	}
    	try {
			this.cityListParam = URLEncoder.encode(paramTmp.substring(-1), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	//�õ�HttpClient���� 
    	getClient = new DefaultHttpClient();   
    }
    
    public String getWeatherInfo(){
    	Log.i("WeatherClient", "��ȡ������Ϣ");   
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("location", cityParam);
    	return doGet(serverUri, params);
    }
    
    public String getAllWeatherInfo(){
    	Log.i("WeatherClient", "��ȡ���г���������Ϣ");   
    	Map<String, String> params = new HashMap<String, String>();
    	//TODO ��Լ����������л�ȡ����������Ϣ�Ľӿڲ���
    	params.put("location", cityListParam);
    	return doGet(serverUri, params);
    }
    
    @SuppressWarnings("rawtypes")
	private String buildParamString(Map params){
    	/* ����HTTPGet���� */  
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
            //�õ�HttpGet����  
            HttpGet request = new HttpGet(url);  
            //�ͻ���ʹ��GET��ʽִ����̣���÷������˵Ļ�Ӧresponse  
            HttpResponse response = getClient.execute(request);  
            //�ж������Ƿ�ɹ�    
            Log.i("doGet", "resCode = " + response.getStatusLine().getStatusCode()); //��ȡ��Ӧ��  
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
                Log.i("doGet", "����������˳ɹ�");      
                String netResult = EntityUtils.toString(response.getEntity(), "utf-8");
                Log.i("doGet", "result = " + netResult); //��ȡ��Ӧ����   
                return netResult;
            }else {  
                Log.i("doGet", "�����������ʧ��");                
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

            //ʵ��UrlEncodedFormEntity����  
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(  
                    postParameters, "utf-8");  
 
            //ʹ��HttpPost����������UrlEncodedFormEntity��Entity  
            request.setEntity(formEntity);  
            HttpResponse response = getClient.execute(request);
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
            	 Log.i("doGet", "����������˳ɹ�");
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
            	 Log.i("doPost", "�����������ʧ��");
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
