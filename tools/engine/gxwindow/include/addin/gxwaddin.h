//
// gxwindow扩展库的头文件, 使用库时只需包含该头文件
//
#pragma once


// 如果不使用pfs, 请打开该宏
// 目前暂时针对xmltextdecoder、Generator/xmlioloader
// 以后新增设计时考虑同时支持两种模式
// #define USE_NAIVEFILE



#include "../gxwindow.h"

#include "dbclick.h"
#include "Generator/uiloader.h"
#include "Generator/xmlioloader.h"
#include "qqbar.h"

#include "inc/tableex.h"
#include "inc/textdecoder.h"

#ifdef USE_NAIVEFILE
#include "xmltextdecoder.h"
#else
#include "xmlio/xmltextdecoder.h"
#endif