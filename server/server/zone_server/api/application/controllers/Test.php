<?php
class Test extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
	
		//设置站点
		//setSite('tuiguang');
	}
	
	var $host = 'http://tuiguangapi.locojoy.com/';
	
	public function index()
	{
		$str = '全3D';
		var_dump(strtolower($str));
		
		
		return $this->load->view("test/index", array(
		));
	}
	
	public function auth_token()
	{
		$url = $this->host . 'auth/get_token';
		
		$attributes = array();
		$attributes['app_key'] = '1111111';
		$attributes['app_secret'] = '111111';
		$attributes['app_version'] = '';
		$attributes['device'] = '111111';
		
		$result = request($url, $attributes, 'post');
		log_message('ERROR', __FUNCTION__.' request '. json_encode(array($url, $attributes, $result)));
		var_dump($url);
		echo "<br />";
		var_dump($attributes);
		echo "<br />";
		
		echo json_encode($result['data']);
	}
	
	private function get_token()
	{
		$url = $this->host . 'auth/get_token';
		
		$attributes = array();
		$attributes['app_key'] = '1111111';
		$attributes['app_secret'] = '111111';
		$attributes['app_version'] = '';
		$attributes['device'] = '111111';
		
		$result = request($url, $attributes, 'post');
		log_message('ERROR', __FUNCTION__.' request '. json_encode(array($url, $attributes, $result)));
		//var_dump($result);
		
		
		return $result['data']['data'];
	}
	
	
	public function get_captcha()
	{
		$url = $this->host . 'auth/get_captcha?token='.$this->get_token();
	
		$result = request($url, null, 'post');
		var_dump($url);
		echo "<br />";
		var_dump(null);
		echo "<br />";
		echo json_encode($result['data']);
		
		$result = $result['data'];
		$imgStr = $result['data'];
		
		echo "<img src=\"".$imgStr."\"/>";
	
	}
	
	public function login()
	{
		
	
	
		$url = $this->host . 'auth/get_captcha?token='.$this->get_token();
		
		$result = request($url, null, 'post');
		var_dump($url);
		echo "<br />";
		
		$result = $result['data'];
		$imgStr = $result['data'];
		echo "<img src=\"".$imgStr."\"/>";
		
		$cache_key = cache_key('captcha', '111111');
		$captcha = cache_get($cache_key);
	
		$url = $this->host . 'auth/login?token='.$this->get_token();
		
		$attributes = array();
		$attributes['username'] = 'xulihui002';
		$attributes['password'] = '111111';
		$attributes['captcha'] = $captcha;
	
		$result = request($url, $attributes, 'post');
		log_message('ERROR', __FUNCTION__.' request '. json_encode(array($url, $attributes, $result)));
		var_dump($url);
		echo "<br />";
		var_dump(null);
		echo "<br />";
		response($result['data']['data']);
	
	}
	
	public function register()
	{
		$url = $this->host . 'auth/register?token='.$this->get_token();
		
		
		$cache_key = cache_key('captcha', '111111');
		$captcha = cache_get($cache_key);
	
		$attributes = array();
		$attributes['username'] = 'xulihui002';
		$attributes['password'] = '111111';
		$attributes['captcha'] = $captcha;
		
		$result = request($url, $attributes, 'post');
		log_message('ERROR', __FUNCTION__.' request '. json_encode(array($url, $attributes, $result)));
		var_dump($url);
		echo "<br />";
		var_dump(null);
		echo "<br />";
		var_dump($result['data']);
	
	}
	
	public function register_succ()
	{
		$url = $this->host . 'auth/register?token='.$this->get_token();
	
	
		$cache_key = cache_key('captcha', '111111');
		var_dump($cache_key);
		$captcha = cache_get($cache_key);
	
		$attributes = array();
		$attributes['username'] = 'xulihui'.rand(100, 999);
		$attributes['password'] = '111111';
		$attributes['captcha'] = $captcha;
		var_dump($attributes);
	
		$result = request($url, $attributes, 'post');
		log_message('ERROR', __FUNCTION__.' request '. json_encode(array($url, $attributes, $result)));
		var_dump($url);
		echo "<br />";
		var_dump(null);
		echo "<br />";
		var_dump($result['data']);
	
	}
	
	
	public function int_to_date()
	{
		$date = date('Y-m-d H:i:s', 1450995955);
		var_dump($date);
		
		$date = date('Y-m-d H:i:s', 1450995946);
		var_dump($date);
		
		$date = date('Y-m-d H:i:s', 1450912245);
		var_dump($date);
		
	}
	
	
	public function htmlencode()
	{
		$str = '&lt;p&gt;      　　今日，卓越游戏旗下《死亡日记》首战大捷，不但获得了苹果全球推荐，仅上线四小时便实现了中国区双榜第一。作为一款新游，也是唯一一
款国人独立原创的生存冒险巨作，《死亡日记》不但填补了国产生存类手游在国内市场的空缺，也为全世界的玩家带来了无以伦比的末日求生体验。现在，你无需打开电脑，只要拿>起手中的移动设备即可踏入真实恐怖的废墟世界，释放你心里深埋的不安和焦虑；面对不可能出现的噩梦，战胜你不知道的自己！  &lt;/p&gt;  &lt;p style=&quot;text-align:center;&quot;&gt;      &lt;img src=&quot;http://resource.locojoy.com/www/news/lmzy/C_lmzy01.jpg&quot; /&gt;   &lt;/p&gt;  &lt;p style=&quot;text-align:center;&quot;&gt;      &lt;img src=&quot;http://resource.locojoy.com/www/news/lmzy/C_lmzy02.jpg&quot; /&gt;   &lt;/p&gt;  &lt;br /&gt;  &lt;p&gt;      　　与力求复杂真实
的硬核游戏相比，清新简单的休闲作品更能赢得广大美眉的芳心，而《浪漫庄园》设计理念也是主打轻松休闲，旨在为大家开创温馨浪漫的娱乐方式。它的画面精致可爱，洋溢着满>满的卡通风味；它的世界自由开放，充满着无限的DIY要素。它是一部卓越游戏斥资千万的休闲类手游，也是首开Game3.0时代的庄园式佳作。在这里，每一位入住庄园的女神都将放>飞理想、实现自己美妙的公主梦。  &lt;/p&gt;  &lt;p style=&quot;text-align:center;&quot;&gt;      &lt;img src=&quot;http://resource.locojoy.com/www/news/lmzy/C_lmzy03.jpg&quot; /&gt;   &lt;/p&gt;  &lt;br /&gt;  &lt;p&gt;      　　女性天生善于表达、热爱交流，《浪漫庄园》则为她们提供了一个彰显个性、表达自身想法的交互平台
。游戏中你不但可以自定角色、自立职业，还可以与其他玩家实时交易、好友互助。这就是一个虚拟现实的乌托邦，庄园的每一位住民都不是孤立的存在。通过与帅哥靓妹们携手合>作、分享创意，整个庄园也将在大家一点一滴的努力中变得更加温馨、更加绚丽。  &lt;/p&gt;  &lt;p style=&quot;text-align:center;&quot;&gt;      &lt;img src=&quot;http://resource.locojoy.com/www/news/lmzy/C_lmzy04.jpg&quot; /&gt;   &lt;/p&gt;  &lt;br /&gt;  &lt;p&gt;      　　自由、浪漫是女神的最爱，这恰恰也是《浪漫庄园》所
要传递的内容。卓越游戏旗下《浪漫庄园》已在iOS平台全面上线，崇尚自由、渴望浪漫的朋友可随时入住。这是一座浪漫的爱之家、心灵的避风港，只要拿起手机、轻触指尖，你就
能感受到家的温馨、心的温暖。  &lt;/p&gt;  &lt;p style=&quot;text-align:center;&quot;&gt;      &lt;img src=&quot;http://resource.locojoy.com/www/news/lmzy/C_lmzy05.jpg&quot; /&gt;   &lt;/p&gt;';
		
		var_dump(htmlspecialchars_decode($str, ENT_QUOTES));
		exit();
	}
	
	
	public function tt()
	{
		$item = '43826 21:52:00 php \/opt\/www_new\/cms\/phpcms\/tools\/cms_svn_update_monitor.php';
		preg_match('/\s*([\d]+)\s+([\w\:]+\s*[\d]*)\s+(.+)/', $item, $matches);
		var_dump($matches);
		
	}
	
	//http://59.151.112.45:8821/test/upload_avatar
	public function upload_avatar()
	{
	    //$url = 'http://59.151.112.45:8821/role/upload_avatar?serverkey=1101961001';
	    $img = '/APP/user00/webroot/weibo/admin/resource/images/logo.png';
	    $img_data = file_get_contents($img);
	    var_dump(strlen($img_data));
	
// 	    $attributes = array();
// 	    $attributes['img_type'] = 'jpg';
// 	    $attributes['img_data'] = $img_data;
	
// 	    $result = request($url, $attributes, 'post');
// 	    var_dump($url);
// 	    var_dump($img);
// 	    var_dump($result);
	    
	    
	    
	    include APPPATH.'/third_party/uploadfile/Uploadfile.php';
	    $uploadfile = new Uploadfile();
	    $result = $uploadfile->image('jpg', $img_data);
	    var_dump($result);
	    
	    
	}

}