<?php
/**
 *
 * Record数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 *
 */
class Faq_record_model extends MY_Model
{
	protected $table = 'faq_record';
	protected $primaryKey = 'record_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
	}
	
	public function create_subtable($month)
	{
		$sql = "CREATE TABLE `faq_record_".$month."` (
  `record_id` int(11) unsigned NOT NULL,
  `userkey` varchar(30) NOT NULL,
  `keyword` varchar(60) NOT NULL DEFAULT '',
  `answer_faq_id` int(11) unsigned NOT NULL DEFAULT '0',
  `feedback_result` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '结果反馈,0未选择,1满意,2不满意',
  `month` varchar(10) NOT NULL DEFAULT '' COMMENT '月份,201603',
  `create_time` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='记录表'";
			
		$query = $this->db()->query($sql);
		log_message('debug', __CLASS__ . ' ' . __FUNCTION__ . ' last_query '.serialize($this->db->last_query()));
			
		return true;
	}
}
