#if _MSC_VER > 1000
#pragma once
#endif

#ifndef WINVER				// ����ʹ���ض��� Windows 2000 ����߰汾�Ĺ��ܡ�
#define WINVER 0x0500
#endif

#ifndef _WIN32_WINNT		// ����ʹ���ض��� Windows 2000 ����߰汾�Ĺ��ܡ�
#define _WIN32_WINNT 0x0500
#endif						
//
//#ifndef _WIN32_WINDOWS		// ����ʹ���ض��� Windows 98 ����߰汾�Ĺ��ܡ�
//#define _WIN32_WINDOWS 0x0410
//#endif

#include <windows.h>
#include <stdio.h>
#include <tchar.h>
#include <string>
//Assert
#include <assert.h>
//STL
#include <vector>
#include <map>
#include <list>
#include <set>
#include <deque>
#include <hash_map>

#include "util.h"