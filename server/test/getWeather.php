<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<?php
		$url = "http://m.weather.com.cn/data/101110101.html";
		$opts = array('http'=>array('method'=>"GET",
									'header'=>"Content-Type: text/html; charset=utf-8"));
		$context = stream_context_create($opts);
		$weather = file_get_contents($url, false, $context);
		print_r($weather);
	?>
</body>
</html>