#pragma once
//
// 预编译头文件, 用于包含系统或其他较稳定的头文件
//
//#include "vld.h"	// vld头文件, 用于内存泄漏检测, 这个工具的结果报告比_CrtSetDbgFlag详细, 外部库的泄漏也能够定位

// 使用界面动态配置
#define  GXWE_GENERATEMODE_FRAME

// 使用Crash Dump模块
#define CRASHDUMP_USING

// 启用角色精灵速度调节功能
//#define FIND_SPRITE_SPEED

// 指定精灵移动模块的版本
#define SPRITE_MOVE_VERSION_0X 0
#define SPRITE_MOVE_VERSION_1X 1
#define SPRITE_MOVE_VERSION SPRITE_MOVE_VERSION_1X

// 启用日志功能
#define OPEN_LOGGER

#include <assert.h>
#include <set>
#include <list>
#include <map>
#include <vector>
#include <string>
#include <math.h>
#include <algorithm>
#include <time.h>
#include <sstream>
#include <fstream>
#include <deque>
#include <locale>

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include "../include/cauthc.h"
#endif

#include "nuiengine.h"
#include "nuienv.h"
#include "nuiapp.h"

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)

#include "log/CoreLog.h"
#include "Singleton.hpp"
#else
#include "../../../common/platform/log/CoreLog.h"
#include "../../../common/platform/Singleton.hpp"

#include "NetConnection.h"

#endif

#include "common/nuworldlogiccoord.h"
#include "common/nulog.h"
#include "renderer/nurenderer.h"
#include "particlesystem/nuparticlemanager.h"
#include "renderer/nucocos2d_render.h"
#include "common/nuxptypes.h"
#include "common/nuutil.h"
#include "common/nuxbuffer.h"
#ifdef WIN7_32
#include "octets.h"
#include "marshal.h"
#endif
#include "../../../common/ljfm/code/include/ljfm.h"
#include "UICommonHeader.h"
#define mynew DEBUG_NEW


namespace Nuclear
{
	inline int distance(Nuclear::NuclearLocation& pt1, Nuclear::NuclearLocation& pt2)
	{
		return abs(static_cast<int>(pt1.x - pt2.x)) > abs(static_cast<int>(pt1.y - pt2.y)) ? abs(static_cast<int>(pt1.x - pt2.x)) : abs(static_cast<int>(pt1.y - pt2.y));
	}
}
const float PI = 3.14159265f;
