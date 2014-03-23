<?php
class Machine extends EMongoDocument
{
	public $mac;
	public $locations;

	public static function model($className=__CLASS__)
      {
        return parent::model($className);
      }

    public function getCollectionName()
      {
        return 'machine';
      }
}
?>