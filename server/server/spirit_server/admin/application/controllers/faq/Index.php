<?php
/**
 * 管理用户管理
 * 
 * @copyright www.locojoy.com
 * @author lihuixu@joyogame.com
 * @version v1.0 2015.02.05
 */
class Index extends MY_Controller
{
    function __construct()
    {
        parent::__construct();
        
    }
    
    public function index()
    {
    	
    	return $this->load->view("faq/index", array(
    	));
    }
	
}
?>