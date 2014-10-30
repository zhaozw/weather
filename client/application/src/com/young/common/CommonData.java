package com.young.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.young.modules.R;

public class CommonData {
	
	public static final Map<String, String> WEEK_PARSE = new HashMap<String, String>();
	public static final Map<String, String> WIND_DIR_MAP = new HashMap<String, String>();
	public static final Map<String, String> WIND_MAP = new HashMap<String, String>();
	public static final Map<String, String> WIND_DESC_MAP = new HashMap<String, String>();
	public static final Map<String, Integer> WEATHER_MAP = new HashMap<String, Integer>();
	public static final String[] HUMIDITY_DESC = {"干燥","干燥","舒适","湿润","潮湿","潮湿"};
	public static final String[] WINTER_FELT_TEMP = {"4","8","13","18","23","29","35"};
	public static List<String> WINTER_FELT_TEMP_LIST = null;
	public static final String[] WINTER_FELT_TEMP_DESC = {"很冷","冷","凉","凉爽","舒适","温暖","暖热"};
	public static final String[] SUMMER_FELT_TEMP = {"13","18","20","25","27","30","33","35","37"};
	public static List<String> SUMMER_FELT_TEMP_LIST = null;
	public static final String[] SUMMER_FELT_TEMP_DESC = {"很冷","冷","有点冷","凉快","热","很热","过热","太热","极热"};
	public static final String[] WEATHER_SORTED = {"强沙尘暴","沙尘暴","暴雪","特大暴雨","大暴雨","大雪","暴雨","中雪","大雨",
		"霾","扬沙","浮尘","冰雹","小雪","阵雪","冻雨","雨夹雪","中雨","小雨","雷阵雨","阵雨","雾","阴","多云","晴"};
	public static List<String> WEATHER_SORTED_LIST = null;
	
	public static final void setMapData(){
		
		WINTER_FELT_TEMP_LIST = Arrays.asList(WINTER_FELT_TEMP);
		SUMMER_FELT_TEMP_LIST = Arrays.asList(SUMMER_FELT_TEMP);
		WEATHER_SORTED_LIST = Arrays.asList(WEATHER_SORTED);
		
		WEEK_PARSE.put("星期一", "周一");
		WEEK_PARSE.put("星期二", "周二");
		WEEK_PARSE.put("星期三", "周三");
		WEEK_PARSE.put("星期四", "周四");
		WEEK_PARSE.put("星期五", "周五");
		WEEK_PARSE.put("星期六", "周六");
		WEEK_PARSE.put("星期日", "周日");
		
		WIND_DIR_MAP.put("0", "无持续风向");
		WIND_DIR_MAP.put("1", "东北风");
		WIND_DIR_MAP.put("2", "东风");
		WIND_DIR_MAP.put("3", "东南风");
		WIND_DIR_MAP.put("4", "南风");
		WIND_DIR_MAP.put("5", "西南风");
		WIND_DIR_MAP.put("6", "西风");
		WIND_DIR_MAP.put("7", "西北风");
		WIND_DIR_MAP.put("8", "北风");
		WIND_DIR_MAP.put("9", "旋转风");

		WIND_MAP.put("0", "微风");
		WIND_MAP.put("1", "3-4级");
		WIND_MAP.put("2", "4-5级");
		WIND_MAP.put("3", "5-6级");
		WIND_MAP.put("4", "6-7级");
		WIND_MAP.put("5", "7-8级");
		WIND_MAP.put("6", "8-9级");
		WIND_MAP.put("7", "9-10级");
		WIND_MAP.put("8", "10-11级");
		WIND_MAP.put("9", "11-12级");
		
		WIND_DESC_MAP.put("0", "微风");
		WIND_DESC_MAP.put("1", "和风");
		WIND_DESC_MAP.put("2", "劲风");
		WIND_DESC_MAP.put("3", "强风");
		WIND_DESC_MAP.put("4", "强风");
		WIND_DESC_MAP.put("5", "烈风");
		WIND_DESC_MAP.put("6", "烈风");
		WIND_DESC_MAP.put("7", "暴风");
		WIND_DESC_MAP.put("8", "暴风");
		WIND_DESC_MAP.put("9", "飓风");
		
//		WIND_DESC_MAP.put("0", "无风");
//		WIND_DESC_MAP.put("1", "轻微");
//		WIND_DESC_MAP.put("2", "轻微");
//		WIND_DESC_MAP.put("3", "和缓");
//		WIND_DESC_MAP.put("4", "和缓");
//		WIND_DESC_MAP.put("5", "清劲");
//		WIND_DESC_MAP.put("6", "强风");
//		WIND_DESC_MAP.put("7", "强风");
//		WIND_DESC_MAP.put("8", "烈风");
//		WIND_DESC_MAP.put("9", "烈风");
//		WIND_DESC_MAP.put("10", "暴风");
//		WIND_DESC_MAP.put("11", "暴风");
//		WIND_DESC_MAP.put("12", "飓风");

		WEATHER_MAP.put("晴", R.drawable.w_qing);
		WEATHER_MAP.put("阴", R.drawable.w_yin);
		WEATHER_MAP.put("多云", R.drawable.w_duoyun);
		WEATHER_MAP.put("晚间多云", R.drawable.w_wanjianduoyun);
		WEATHER_MAP.put("雾", R.drawable.w_wu);
		WEATHER_MAP.put("阵雨", R.drawable.w_zhenyu);
		WEATHER_MAP.put("小雨", R.drawable.w_xiaoyu);
		WEATHER_MAP.put("中雨", R.drawable.w_zhongyu);
		WEATHER_MAP.put("大雨", R.drawable.w_dayu);
		WEATHER_MAP.put("暴雨", R.drawable.w_baoyu);
		WEATHER_MAP.put("大暴雨", R.drawable.w_dabaoyu);
		WEATHER_MAP.put("特大暴雨", R.drawable.w_tedabaoyu);
		WEATHER_MAP.put("雷阵雨", R.drawable.w_leizhenyu);
		WEATHER_MAP.put("雷阵雨伴有冰雹", R.drawable.w_leizhenyubanyoubingbao);
		WEATHER_MAP.put("冰雹", R.drawable.w_bingbao);
		WEATHER_MAP.put("雨夹雪", R.drawable.w_yujiaxue);
		WEATHER_MAP.put("阵雪", R.drawable.w_zhenxue);
		WEATHER_MAP.put("小雪", R.drawable.w_xiaoxue);
		WEATHER_MAP.put("中雪", R.drawable.w_zhongxue);
		WEATHER_MAP.put("大雪", R.drawable.w_daxue);
		WEATHER_MAP.put("暴雪", R.drawable.w_baoxue);
		WEATHER_MAP.put("沙尘暴", R.drawable.w_shachenbao);
		WEATHER_MAP.put("冻雨", R.drawable.w_dongyu);
		WEATHER_MAP.put("浮尘", R.drawable.w_fuchen);
		WEATHER_MAP.put("强沙尘暴", R.drawable.w_qiangshachenbao);
		WEATHER_MAP.put("特大暴雪", R.drawable.w_tedabaoxue);
		WEATHER_MAP.put("扬沙", R.drawable.w_yangsha);
		WEATHER_MAP.put("霾", R.drawable.w_wuai);
		
		
	}

}
