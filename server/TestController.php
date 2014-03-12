<?php
class TestController extends Controller
{
    public function actionConnectDB()
    { 
        $locations = Location::model()->find();
        var_dump($locations);
    }
 
}
?>