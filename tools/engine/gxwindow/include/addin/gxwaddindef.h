#pragma once


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

#include "gxwaddin.h"


#ifdef GXWINDOW_ENABLE_BATCH_RENDER
#define INVALIDATE() { GXWINDOW::GXWNeedRedraw(); }
#else
#define INVALIDATE(...) {}
#endif



