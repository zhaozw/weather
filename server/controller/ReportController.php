<?php
class ReportController extends Controller
{
    public function actionSendForecast()
    { 
        $mac = Yii::app()->request->getParam('mac');
        $m_criteria = new EMongoCriteria;
        $f_criteria = new EMongoCriteria;
        $machine = new Machine();
        $m_criteria->mac = $mac;
        $locations = $machine->findall($m_criteria);
        $data = Transform::object_array($locations[0]);
        //print_r($data);
        $stay = $data["locations"]["stay"];
        $other = $data["locations"]["other"];
        foreach ($other as $location) {
            //echo $location;
            $f_criteria->weatherinfo->c->c3 = $location;
            $forecast = Forecast::model()->findall($f_criteria);
            if(empty($forecast)){
                echo 'no data';
                echo '<br>';
            }
            else{
                print_r(json_encode($forecast));
            }
        }
        
        
        //print_r(Transform::JSON($arr_forecast));
        //var_dump(Transform::JSON($arr_forecast));
        //var_dump(json_encode($forecast["weatherinfo"]));
        //$this->render('form',$array('location'->$location));
        //echo $location;
    }
 
}
?>