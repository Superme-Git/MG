#pragma once

enum
{
	TIP_ERROR =						1,			// 错误
	ERR_START =						2,			// 启动程序过程中发生错误. 拷贝文件失败(%d):\n%s -> %s\n%s\n请尝试重新启动程序!
	ERR_START_MISSING_FILE =		3,			// 无法启动程序！\n原因：缺少必要文件或目录 %s! \n请尝试重新安装程序!
	ERR_START_NOT_DELETE_FILE =		4,			// 启动程序过程中发生错误！无法删除陈旧版本程序。\n错误信息 %d:\n%s\n请尝试重新安装程序!
	ERR_START_NOT_MOVE_FILE =		5,			// 启动程序过程中发生错误！无法替换陈旧版本程序。\n错误信息 %d:\n%s\n请尝试重新安装程序!
	ERR_START_FILE_DAMAGE =			6,			// 无法启动程序！\n原因：目录 %s 中的文件被破坏! \n请尝试重新安装程序!
	ERR_START_NOT_CREATE_FILE =		7,			// 启动程序过程中发生错误！无法创建文件。\n错误信息 %d:\n%s\n请重新尝试启动程序或重新安装程序!
	ERR_START_NOT_BACKUP_FILE =		8,			// 启动程序过程中发生错误！无法备份程序。\n%1!s!(%2!d!)\n请重新尝试启动程序或重新安装程序!
	ERR_LOAD_PROGRAME =				9,			// 加载程序失败:%s. \n错误 %d:\n%s
	QST_OLD_PATCHER_NOT_EXIT =		10,			// 旧的更新程序还没完全退出。\n选择&quot;重试&quot;继续等待。\n选择&quot;取消&quot;先退出本程序，稍后手工启动更新程序进行更新！
	TIP_TIP =						11,			// 提示
	ERR_OPEN_PROCESS =				12,			// 打开进程失败
	ERR_START_OPERATE_REGISTER =	13,			// 修仙启动过程中发生问题：\n\n    注册表操作失败。\n\n错误信息：%1!s!错误代码：%2!d!\n\n请确认你是以管理员账户身份启动程序！
	
	TOK_PRODUCT_NAME				= 40,		//修仙
	TOK_PRODUCT_LAUNCHER			= 41,		//修仙启动程序
	TOK_PRODUCT_PATCHER				= 42,		//修仙更新程序
	TOK_PRODUCT_REPAIR				= 43,		//修仙版本验证程序
	TOK_PRODUCT_CLIENT				= 44,		//修仙客户端程序
};