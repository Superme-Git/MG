<?php

//token v3
$config['token'] = array(
		//'app_key' => '888999001',
		//'app_secret' => '888999002',
		//'app_version' => '1.0',
		'key' => '806235b87fe148c3706ee3416eb2f01f',
		'tag' => '&^hl$hfody9', // 解密标识
);

//cache_helper 配置
$config['cache'] = array(
		'status' => 1,
		'adapter' => 'redis',
);

//说不得大师的角色id//大师角色id为bigint 最大值
$config['dashi_roleid'] = 9223372036854775807;

//php的运行
$config['php_bin'] = '/APP/user00/modules/php/bin/php';

