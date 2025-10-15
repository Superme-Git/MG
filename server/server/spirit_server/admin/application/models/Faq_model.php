<?php
/**
 *
 * Faq数据模型
 *
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 *
 */
class Faq_model extends MY_Model
{
	protected $table = 'faq';
	protected $primaryKey = 'faq_id';
	//protected $subtableTagCount = 5;
	
	public function __construct()
	{
		parent::__construct();
		
		$this->load->model(array("Faq_content_model", 'Faq_category_model'));
	}
	
	public function getInfoRelation($info)
	{
		$content = $this->Faq_content_model->getInfo($info['faq_id']);
		$info['content'] = htmlspecialchars_decode($content['content'], ENT_QUOTES);
		
		
		$category = $this->Faq_category_model->getInfo($info['category_id']);
		$info['category_info'] = $category;
		
		return $info;
	}
}
