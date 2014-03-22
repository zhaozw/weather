<?php
class TestController extends Controller
{
    public function actionConnectDB($location)
    { 
        //$locations = Location::model()->find();
        $criteria = new EMongoCriteria;
        $criteria->weatherinfo->city = $location;
        $forecast = forecast::model()->find($criteria);
        $arr_forecast = Transform::object_array($forecast["weatherinfo"]);
        print_r(Transform::JSON($arr_forecast));
        //var_dump(Transform::JSON($arr_forecast));
        //var_dump(json_encode($forecast["weatherinfo"]));
        //$this->render('form',$array('location'->$location));
        //echo $location;
    }
 
}
?>