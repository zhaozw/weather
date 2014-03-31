<?php
class LoadAreaController extends Controller
{
	public function actionInsertall(){
		$area = file('/opt/nginx/html/weather/area_list.txt');
		foreach ($area as $place) {
			$location = new Location();//地点集合
			$place = rtrim($place);
			$placeSet = explode("	",$place);
			$location->location_id = $placeSet[0];
			$location->location_name = $placeSet[1];
			$location->save();
		}
		echo "insert all";
	}
}