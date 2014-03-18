<?php
class forecast extends EMongoDocument
{
	public $weatherinfo;
	public $city;

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