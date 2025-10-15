Java版机器人运行指南 by dc

	启动参数含义：
	服务器LinkIP 服务器Link端口(例：28993) 起始账号(只能填数字，例如：1) 登录账号个数 账号前缀(2~3个字母，例如：ab)
	例子：192.168.29.161 27000 1 10 sv （此时创建名字为“sv1”~“sv10”10个账号及角色连接到192.168.29.161上27000端口的glink服务器）


	Linux运行：
	修改run_robot.sh内参数，执行run_robot.sh。
	
	Windos运行：
	修改run_robot.bat内参数，执行run_robot.bat。
		 
	备注：	
	1.若robot运行登陆流程中log出现CreateRoleError 3，表明角色名称错误，不符合服务器要求，请更换账号前缀参数后重新启动！

	2.若有角色在运行中掉线（log中出现AuthError），300秒后该角色自动重连。
		
	