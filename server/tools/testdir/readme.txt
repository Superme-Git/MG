
eclipse 目录组织要求
	把当前目录设置为workspace。
	每个项目作为子目录存在这个目录内。

项目要求
	1 项目独立维护。
	2 不要提交项目运行环境需要的库，如db_x86.dll,libdb_amd64.so,xdb.jar等。

autorun
	1 可重复执行的测试加到自动运行脚本中。
	  支持哪个版本就加到相应的脚本中。 autorun.bat(windows版本) autorun.sh(linux版本)
	2 自动运行脚本能自动建立运行环境，不需要手动设置。

