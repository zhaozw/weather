<?php
class MachineController extends Controller
{
	public function actionChangeLocation()
	{
		$machine = new Machine();//机器集合
		$location = new Location();//地点集合
		$result = array();
		$in_json = file_get_contents("php://input");

		if(is_null(json_decode($in_json))){
			echo "wrong input";
		}

		else{
			$arr_input = Transform::object_array(json_decode($in_json));
			//print_r($arr_input);
			$mac = $arr_input["mac"];
			$locations = $arr_input["locations"];

			if(empty($mac) || empty($locations)){
				echo "wrong input";
			}

			else{
				$criteria = new EMongoCriteria();
				$criteria->mac = $mac;
				$result = $machine::model()->find($criteria);

				if(empty($result)){
					$machine->mac = $mac;
					$machine->locations = $locations;
					$status = $machine->save();
					//echo $status;
					echo "insert successfully";
				}

				else{
					$modifier = new EMongoModifier();
					$modifier->addModifier('locations', 'set', $locations);
					$status = $machine::model()->updateAll($modifier, $criteria);
					//echo $status;
					echo "update successfully";
				}
			}
		}
	}
}
?>