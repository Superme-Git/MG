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


//迅搜sdk目录
$config['xunsearch_sdk'] = '/APP/user00/xunsearch/sdk';

//xunsearch索引文件同步
$config['sync_cron'] = array(
    'rsync -zvaP --port 40212 --delete /APP/user00/xunsearch/data/mt3_jingling/db/ rsync@192.168.34.46::xunserarchdata  --log-file=/var/log/rsync.log',
    'rsync -zvaP --port 40212 --delete /APP/user00/xunsearch/data/mt3_jingling/db/ rsync@192.168.34.45::xunserarchdata  --log-file=/var/log/rsync.log',
    'rsync -zvaP --port 40212 --delete /APP/user00/xunsearch/data/mt3_jingling_keyword/db/ rsync@192.168.34.46::xunserarchdata_keyword  --log-file=/var/log/rsync.log',
    'rsync -zvaP --port 40212 --delete /APP/user00/xunsearch/data/mt3_jingling_keyword/db/ rsync@192.168.34.45::xunserarchdata_keyword  --log-file=/var/log/rsync.log'
);
