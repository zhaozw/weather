<?php
class Location extends EMongoDocument
{
	public $location_id;
	public $location_name;

	public static function model($className=__CLASS__)
      {
        return parent::model($className);
      }

    public function getCollectionName()
      {
        return 'location';
      }
}
?>