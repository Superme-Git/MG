#include "StdAfx.h"
#include "PPErrorInfo.h"

namespace PP
{
	static struct dummy
	{
		static LPCWSTR GetString( LPCWSTR /*key*/, LPCWSTR value ) 
		{
			return value;
		}
	} _ini;

	// 1		当前使用的操作系统版本不支持
	// 3		请等待游戏完全启动以后再尝试
	// 4和11	请确认游戏没有被杀毒软件或防火墙禁止，并确认您是在管理员帐号下启动游戏
	// 7		请等待游戏完全结束以后再尝试
	// 126		由于操作系统有未完成的操作，如自动更新，请完成这些操作并重启计算机再尝试打开游戏
	// 10		父进程检测失败,请从Launcher启动
	// 23		打开时间过长，请关闭所有游戏客户端，重新尝试
	// 24		多开
	// 25		pp_data0文件非法
	// 26		pp_data0文件不存在
	// 1450		系统内存不足，请注销或重启电脑以后再尝试!
	// 其他值	游戏初始化失败，这时可弹出一个对话框，让玩家给我们发送pp_005.dat。
	static LPCWSTR GetMessage( int ErrorCode )
	{
		static LPCWSTR ErrorMessage[] = 
		{
			/*0 */L"不支持运行在当前操作系统下。",
			/*1 */L"刚刚打开的游戏客户端还没启动完毕，请等待完全启动后重试。",
			/*2 */L"请确认游戏没有被杀毒软件或防火墙禁止，并确认您是以计算机管理员帐户身份启动游戏。",
			/*3 */L"正在关闭的游戏客户端还没完全退出，请等待完全退出后重试。",
			/*4 */L"请检查操作系统是否有未完成的操作，如自动更新，确保完成这些操作并重启计算机之后再尝试打开游戏。",
			/*5 */L"请从桌面快捷方式\"魔幻修仙\"或{游戏目录}/launcher/launcher.exe启动!",
			/*6 */L"程序响应时间过长, 请关闭所有游戏客户端后重新尝试启动。",
			/*7 */L"同时打开的游戏客户端已达上限。",
			/*8 */L"程序文件不完整。请尝试对客户端程序进行版本验证。",
			/*9 */L"缺少必要的程序文件。请尝试对客户端程序进行版本验证。",
			/*10 */L"系统资源不足，请关闭其它占用资源较多的程序或注销/重启电脑以后再尝试。",
			/*11 */L"系统原因导致游戏初始化失败。",
		};
		switch( ErrorCode )
		{
		case 1: return _ini.GetString(L"msg0", ErrorMessage[0]);
		case 3: return _ini.GetString(L"msg1", ErrorMessage[1]);
		case 4: case 11: return _ini.GetString(L"msg2", ErrorMessage[2]);
		case 7: return _ini.GetString(L"msg3", ErrorMessage[3]);
		case 126: return _ini.GetString(L"msg4", ErrorMessage[4]);
		case 10: return _ini.GetString(L"msg5", ErrorMessage[5]);
		case 23: return _ini.GetString(L"msg6", ErrorMessage[6]);
		case 24: return _ini.GetString(L"msg7", ErrorMessage[7]);
		case 25:return _ini.GetString(L"msg8", ErrorMessage[8]);
		case 26:return _ini.GetString(L"msg9", ErrorMessage[9]);
		case 1450: return _ini.GetString(L"msg10", ErrorMessage[10]);
		default: return _ini.GetString(L"msg11", ErrorMessage[11]);
		}
	}

	int GetErrorDescription( LANGID /*langId*/, int ErrorCode, LPWSTR lpBuffer, DWORD nSize )
	{
		// TODO: 
		// langId 暂未实现。
		LPCWSTR pszMessage = GetMessage( ErrorCode );
		int nLen = (int)wcslen( pszMessage );
		if( nLen < (int)nSize )
		{
			wcscpy_s( lpBuffer, nSize, pszMessage );
			return nLen;
		}
		else
		{
			return -nLen;
		}
	}

} // namespace PP