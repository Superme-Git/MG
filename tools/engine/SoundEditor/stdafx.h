// stdafx.h : ��׼ϵͳ�����ļ��İ����ļ���
// ���Ǿ���ʹ�õ��������ĵ�
// �ض�����Ŀ�İ����ļ�

#pragma once

#ifndef _SECURE_ATL
#define _SECURE_ATL 1
#endif

#ifndef VC_EXTRALEAN
#define VC_EXTRALEAN		// �� Windows ͷ���ų�����ʹ�õ�����
#endif

// ���������ʹ��������ָ����ƽ̨֮ǰ��ƽ̨�����޸�����Ķ��塣
// �йز�ͬƽ̨����Ӧֵ��������Ϣ����ο� MSDN��
#ifndef WINVER				// ����ʹ���ض��� Windows XP ����߰汾�Ĺ��ܡ�
#define WINVER 0x0501		// ����ֵ����Ϊ��Ӧ��ֵ���������� Windows �������汾��
#endif

#ifndef _WIN32_WINNT		// ����ʹ���ض��� Windows XP ����߰汾�Ĺ��ܡ�
#define _WIN32_WINNT 0x0501	// ����ֵ����Ϊ��Ӧ��ֵ���������� Windows �������汾��
#endif						

#ifndef _WIN32_WINDOWS		// ����ʹ���ض��� Windows 98 ����߰汾�Ĺ��ܡ�
#define _WIN32_WINDOWS 0x0410 // ��������Ϊ�ʺ� Windows Me ����߰汾����Ӧֵ��
#endif

#ifndef _WIN32_IE			// ����ʹ���ض��� IE 6.0 ����߰汾�Ĺ��ܡ�
#define _WIN32_IE 0x0600	// ����ֵ����Ϊ��Ӧ��ֵ���������� IE �������汾��ֵ��
#endif

#define _ATL_CSTRING_EXPLICIT_CONSTRUCTORS	// ĳЩ CString ���캯��������ʽ��

// �ر� MFC ��ĳЩ�����������ɷ��ĺ��Եľ�����Ϣ������
#define _AFX_ALL_WARNINGS

#include <afxwin.h>         // MFC ��������ͱ�׼���
#include <afxext.h>         // MFC ��չ


#include <afxdisp.h>        // MFC �Զ�����



#ifndef _AFX_NO_OLE_SUPPORT
#include <afxdtctl.h>		// MFC �� Internet Explorer 4 �����ؼ���֧��
#endif
#ifndef _AFX_NO_AFXCMN_SUPPORT
#include <afxcmn.h>			// MFC �� Windows �����ؼ���֧��
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

//�����༭�����棬ʱ���������ֵ�Ǵ�0 ~ Nuclear::XP_A_DAY_TIME / TIME_PROSS_SCALE
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
	return gameTime * 2;//������������Ϸ�������ֵ��Nuclear::XP_A_DAY_TIME��2Сʱ�ĺ���������ʵ������4Сʱ
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

inline int Vol255to100(int v)	//ʵ������0~255��������ʾ��ʱ�����0~100
{
	return (int)(v * 100.0f / 255 + 0.5f);
}

inline int Vol100to255(int v)	//ʵ������0~255��������ʾ��ʱ�����0~100
{
	return (int)(v * 255.0f / 100 + 0.5f);
}

inline int FreqToShow(int f)	//ʵ������0~255����ʾΪ-128~127
{
	return f - 128;
}

inline int FreqToSave(int f)	//ʵ������0~255����ʾΪ-128~127
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


