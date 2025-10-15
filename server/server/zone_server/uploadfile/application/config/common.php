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

//图片上传配置
$config['img_upload'] = array(
		'upload_path' => FCPATH . '/resource/images/'.date('Y/m/d/'),//
        'http_path' => 'http://mt3.bbs.locojoy.com/resource/images/'.date('Y/m/d/'),//http://mt3.pengyouquan001.locojoy.com:8811
		'allowed_types' => 'gif|jpg|jpeg|png',
		'file_ext_tolower' => true,//如果设置为 TRUE ，文件后缀名将转换为小写
		'max_size' => '1024',//允许上传文件大小的最大值（单位 B），设置为 0 表示无限制
		'max_width' => '1024',//图片的最大宽度（单位为像素），设置为 0 表示无限制
		'max_height' => '1024',//图片的最大高度（单位为像素），设置为 0 表示无限制
		'encrypt_name' => true //文件名将会转换为一个随机的字符串 如果你不希望上传文件的人知道保存后的文件名，这个参数会很有用
);

$config['sound_upload'] = array(
        'upload_path' => FCPATH . '/resource/sounds/'.date('Y/m/d/'),//
        'http_path' => 'http://mt3.bbs.locojoy.com/resource/sounds/'.date('Y/m/d/'),//http://mt3.pengyouquan001.locojoy.com:8811
        'allowed_types' => 'mp3|wav|ogg',
        'file_ext_tolower' => true,//如果设置为 TRUE ，文件后缀名将转换为小写
        'max_size' => '1024',//允许上传文件大小的最大值（单位 KB），设置为 0 表示无限制
        'max_width' => '1024',//图片的最大宽度（单位为像素），设置为 0 表示无限制
        'max_height' => '1024',//图片的最大高度（单位为像素），设置为 0 表示无限制
        'encrypt_name' => true //文件名将会转换为一个随机的字符串 如果你不希望上传文件的人知道保存后的文件名，这个参数会很有用
);


//说不得大师的角色id
$config['dashi_roleid'] = 9223372036854775807;


