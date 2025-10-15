//  GlobalFunction.cpp
//  FireClient


#include "GlobalFunction.h"


#include "../../ljfm/code/include/common.h"

#include "utils/StringUtil.h"

#include "UpdateEngine.h"

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	#include "FileDownloader.h"
#endif
