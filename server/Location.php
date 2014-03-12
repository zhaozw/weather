<?php
class Location extends EMongoDocument
{
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