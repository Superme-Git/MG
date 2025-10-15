#pragma once

enum
{
	PREPARE = 0,				//	准备卸载
	START,						//	开始卸载
	CLOSE,						//	关闭

	APPNAME,					//	魔幻修仙卸载程序
	AFFIRM,						//	确认删除\"魔幻修仙\"及它的所有组件？
	DELETEPATH,					//	正在删除目录 %s
	DELETEFILE,					//	正在删除文件 %s
	DELETELINK,					//	正在删除快捷方式
	SUCCESS,					//	卸载成功
	FAILED,						//	卸载失败，错误原因：%s
};