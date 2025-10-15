<?php
defined('BASEPATH') OR exit('No direct script access allowed');
/**
 *
 * 栏目管理
 *
 * @copyright www.locojoy.com
 * @author xinmanpei@joyogame.com
 * @version v1.0 2016.04.01
 *
 */
class Index extends MY_Controller
{
	public function __construct()
	{
		parent::__construct();
	}

	public function index()
	{
		$this->load->view('index/index', array(
		));
	}
	
}
