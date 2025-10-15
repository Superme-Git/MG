#pragma once

#define _WIN32_WINNT	0x0400



#include <windows.h>
#include <assert.h>

#include <map>
#include <set> 
#include <list> 
#include <vector>
#include <algorithm>
#include <functional>
#include <string> 
#include <sstream> 
#include <math.h>

#define _GXW_UPDATE_CHECK 

#include "simplestring.h" 
#include "clipboard.h" 
#include "../include/gxwindow.h"
#include "componentnotifyset.h"

#include "container.h"
#include "abstactdesktop.h"
#include "desktop.h"

#ifdef GXWINDOW_ENABLE_BATCH_RENDER
#define INVALIDATE() { GXWNeedRedraw(); }
namespace GXWINDOW {
bool GXWIsNeedRedraw();
void GXWCancelNeedRedraw();
}
#else
#define INVALIDATE(...) {}
#endif


// ÄÚ´æÐ¹Â©¼ì²â
#ifdef _DEBUG
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>
#define new new(_NORMAL_BLOCK, __FILE__, __LINE__)
#endif