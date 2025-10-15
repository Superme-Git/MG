<?php
//$config['status']=true;
//$config['socket_type'] ='tcp';
//$config['host']='192.168.34.48';
//$config['port']=32478;
//主
$config['master'] = array(
	'status' => true,
    'socket_type' => 'tcp',
    'host' => '192.168.34.48',
    'port' => 35478,
	'timeout' => 1200
);

//从
$config['slave'] = array(
    array(
    	'status' => true,
        'socket_type' => 'tcp',
        'host' => '192.168.34.47',
        'port' => 35478,
    	'timeout' => 1200
    )
);

