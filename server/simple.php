<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<?php
			try{
				$mongo = new Mongo();
				$db = $mongo->selectDB("weather");
				$collection = $db->selectCollection("location");
				$forecast = $db->selectCollection("forecast");
				$query = array( "location_name" => "北京" );
				$cursor = $collection->find( $query );
				while( $cursor->hasNext() ) {
    				$location = $cursor->getNext();
    				echo $url = "http://m.weather.com.cn/data/".$location['location_id'].".html";
    				$opts = array('http'=>array('method'=>"GET",
									'header'=>"Content-Type: text/html; charset=utf-8"));
					$context = stream_context_create($opts);
					$weather = file_get_contents($url, false, $context);
					$weather_array = json_decode($weather);
					$forecast->insert($weather_array);
					echo "success";
				}
				$mongo->close();
				} catch(Excepion $e){
					die($e->getMessage());
				}
		?>
	</body>