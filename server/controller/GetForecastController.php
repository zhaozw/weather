<?php
class GetForecastController extends Controller
{
	public function actionInsertall(){
		$base_url = 'http://webapi.weather.com.cn/data/';
		$appid = '8aa47c217f91edc5';
		$appid_6=substr($appid,0,6);
		$type = 'forecast3d';
		$areaid = '101020100';
		$private_key = '72fff4_SmartWeatherAPI_a42a6a7';
		$public_url = $base_url.'?areaid='.$areaid.'&type='.$type.'&date='.date("YmdHi").'&appid=';
		$key=urlencode(base64_encode(hash_hmac('sha1',$public_url.$appid,$private_key,true)));
		$url = $public_url.$appid_6.'&key='.$key;
		$forecast_json = file_get_contents($url);
		$forecast = new Forecast();
		$forecast->weatherinfo = json_decode($forecast_json);
		$forecast->save();
		echo "success";
	}
}
?>