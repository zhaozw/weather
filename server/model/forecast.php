<?php
class Forecast extends EMongoDocument
{
	public $weatherinfo;

	public static function model($className=__CLASS__)
      {
        return parent::model($className);
      }

    public function getCollectionName()
      {
        return 'forecast';
      }
}
?>