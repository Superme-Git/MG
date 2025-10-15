<?php
/**
 *
 * 用户数据模型
 *
 * @author xu.lihui@qq.com
 * @copyright http://www.maikesi.org/
 * @version v1.0 2015.8.9
 *
 */
class Faq_record_subtable_model extends MY_Model
{
	protected $table = 'faq_record_subtable';
	protected $primaryKey = 'record_subtable_id';
	
	public function __construct()
	{
		parent::__construct();
	}
	
}
