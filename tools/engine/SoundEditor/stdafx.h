// stdafx.h : 标准系统包含文件的包含文件，
// 或是经常使用但不常更改的
// 特定于项目的包含文件

#pragma once

#ifndef _SECURE_ATL
#define _SECURE_ATL 1
#endif

#ifndef VC_EXTRALEAN
#define VC_EXTRALEAN		// 从 Windows 头中排除极少使用的资料
#endif

// 如果您必须使用下列所指定的平台之前的平台，则修改下面的定义。
// 有关不同平台的相应值的最新信息，请参考 MSDN。
#ifndef WINVER				// 允许使用特定于 Windows XP 或更高版本的功能。
#define WINVER 0x0501		// 将此值更改为相应的值，以适用于 Windows 的其他版本。
#endif

#ifndef _WIN32_WINNT		// 允许使用特定于 Windows XP 或更高版本的功能。
#define _WIN32_WINNT 0x0501	// 将此值更改为相应的值，以适用于 Windows 的其他版本。
#endif						

#ifndef _WIN32_WINDOWS		// 允许使用特定于 Windows 98 或更高版本的功能。
#define _WIN32_WINDOWS 0x0410 // 将它更改为适合 Windows Me 或更高版本的相应值。
#endif

#ifndef _WIN32_IE			// 允许使用特定于 IE 6.0 或更高版本的功能。
#define _WIN32_IE 0x0600	// 将此值更改为相应的值，以适用于 IE 的其他版本。值。
#endif

#define _ATL_CSTRING_EXPLICIT_CONSTRUCTORS	// 某些 CString 构造函数将是显式的

// 关闭 MFC 对某些常见但经常可放心忽略的警告消息的隐藏
#define _AFX_ALL_WARNINGS

#include <afxwin.h>         // MFC 核心组件和标准组件
#include <afxext.h>         // MFC 扩展


#include <afxdisp.h>        // MFC 自动化类



#ifndef _AFX_NO_OLE_SUPPORT
#include <afxdtctl.h>		// MFC 对 Internet Explorer 4 公共控件的支持
#endif
#ifndef _AFX_NO_AFXCMN_SUPPORT
#include <afxcmn.h>			// MFC 对 Windows 公共控件的支持
#endif // _AFX_NO_AFXCMN_SUPPORT



#include <string>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
#include <hash_map>
#include "..\engine\common\util.h"
#include "..\engine\common\log.h"
#include "..\engine\common\xptypes.h"
#include <afxdlgs.h>

struct XL_SOUND { static const wchar_t *fn() { return L"sound.log"; } };
#define XPLOG_SOUND xwprint<XL_SOUND>

LPITEMIDLIST GetIDListFromPath(LPCTSTR lpszPath);


enum MOUSESTATE
{
	MOUSE_STATE_NORMAL	= 0,
	LEFT_BUTTON_DOWN	= 1,
	RIGHT_BUTTON_DOWN	= 2,
};

enum EDIT_STATE 
{
	EDIT_STATE_ENV_SOUND = 0,
	EDIT_STATE_BKG_MUSIC,
	EDIT_STATE_BKG_VOL,
	EDIT_STATE_STEP,
	EDIT_STATE_TESTING
};

//声音编辑器里面，时间进度条的值是从0 ~ Nuclear::XP_A_DAY_TIME / TIME_PROSS_SCALE
#define TIME_PROSS_SCALE 5000	

inline int EditorTime2GameTime(int editorTime)
{
	return editorTime * TIME_PROSS_SCALE;
}

inline int GameTime2EditorTime(int gameTime)
{
	return gameTime / TIME_PROSS_SCALE;
}

inline int GameTime2RealTime(int gameTime)
{
	return gameTime * 2;//差了俩倍，游戏里面最大值是Nuclear::XP_A_DAY_TIME（2小时的毫秒数），实际上是4小时
}

inline int RealTime2GameTime(int realTime)
{
	return realTime / 2;
}

inline int EditorTime2RealTime(int editorTime)
{
	return GameTime2RealTime(EditorTime2GameTime(editorTime));
}

inline int RealTime2EditorTime(int realTime)
{
	return GameTime2EditorTime(RealTime2GameTime(realTime));
}

inline int Vol255to100(int v)	//实际上是0~255，不过显示的时候就是0~100
{
	return (int)(v * 100.0f / 255 + 0.5f);
}

inline int Vol100to255(int v)	//实际上是0~255，不过显示的时候就是0~100
{
	return (int)(v * 255.0f / 100 + 0.5f);
}

inline int FreqToShow(int f)	//实际上是0~255，显示为-128~127
{
	return f - 128;
}

inline int FreqToSave(int f)	//实际上是0~255，显示为-128~127
{
	return f + 128;
}

inline COLORREF XPC2REF(Nuclear::XPCOLOR c) { return RGB(c.r, c.g, c.b); }
inline Nuclear::XPCOLOR REF2XPC(COLORREF c) { return Nuclear::XPCOLOR(0xFF, GetRValue(c), GetGValue(c), GetBValue(c)); }

#ifdef _UNICODE
#if defined _M_IX86
#pragma comment(linker,"/manifestdependency:\"type='win32' name='Microsoft.Windows.Common-Controls' version='6.0.0.0' processorArchitecture='x86' publicKeyToken='6595b64144ccf1df' language='*'\"")
#elif defined _M_IA64
#pragma comment(linker,"/manifestdependency:\"type='win32' name='Microsoft.Windows.Common-Controls' version='6.0.0.0' processorArchitecture='ia64' publicKeyToken='6595b64144ccf1df' language='*'\"")
#elif defined _M_X64
#pragma comment(linker,"/manifestdependency:\"type='win32' name='Microsoft.Windows.Common-Controls' version='6.0.0.0' processorArchitecture='amd64' publicKeyToken='6595b64144ccf1df' language='*'\"")
#else
#pragma comment(linker,"/manifestdependency:\"type='win32' name='Microsoft.Windows.Common-Controls' version='6.0.0.0' processorArchitecture='*' publicKeyToken='6595b64144ccf1df' language='*'\"")
#endif
#endif


