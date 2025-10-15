#pragma once
#include "../targetver.h"
#define WIN32_LEAN_AND_MEAN             //  从 Windows 头文件中排除极少使用的信息
// Windows 头文件: 
#include <windows.h>
// C 运行时头文件
#include <stdlib.h>
#include <malloc.h>
#include <memory.h>
#include <stdio.h>
#include <tchar.h>
#define _ATL_CSTRING_EXPLICIT_CONSTRUCTORS      // 某些 CString 构造函数将是显式的
#include <atlbase.h>
#include <atlstr.h>
// TODO:  在此处引用程序需要的其他头文件
#include <stdio.h>
#include <io.h>
#include <iosfwd>
#include <algorithm>
#include <direct.h>
#include <vector>
#include <string>
#include <list>
#include <map>
#include <sstream>
#include <fstream>
#include <thread>