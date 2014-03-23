<?php
   function http_post_data($url, $data_string) {

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);
		curl_setopt($ch, CURLOPT_HTTPHEADER, array(
			'Content-Type: application/json; charset=utf-8',
			'Content-Length: ' . strlen($data_string))
		);
        ob_start();
        curl_exec($ch);
        $return_content = ob_get_contents();
        ob_end_clean();

        $return_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        return array($return_code, $return_content);
    }

$url  = "http://106.187.94.192/weather/index.php?r=machine/ChangeLocation";
$arr = array();
$arr["mac"] = "0A-FF-13-54-C1-34";
$arr["locations"]["stay"] = "北京";
$arr["locations"]["other"] = array("上海","深圳");
$data = json_encode($arr);
echo $data;
list($return_code, $return_content) = http_post_data($url, $data);
echo $return_code;
echo "<br>";
echo $return_content;
?>