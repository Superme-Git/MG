<?php
/**
 * Debug
 * 调试业务接口
 * 
 * @author lihuixu@joyogame.com
 * @copyright http://www.locojoy.com/
 * @version v1.0 2016.3.15
 */
class Debug extends MY_Controller
{
    public function __construct()
    {
        parent::__construct();
    }
    
    /**
     * 调试是否通信
     */
    public function status()
    {
    	
    	$this->load->dbutil();
    	
    	//判断某个数据库是否存在
    	if ($this->dbutil->database_exists('database_name'))
    	{
    		// some code...
    	}
    	
    }
    
    
}