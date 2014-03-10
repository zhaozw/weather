<?php
	try{
		$mongo = new Mongo();
		$db = $mongo->listDBs();
		echo '<pre>';
		print_r($db);
		$mongo->close();
	} catch(Excepion $e){
		die($e->getMessage());
	}
?>