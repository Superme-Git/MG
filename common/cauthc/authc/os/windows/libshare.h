#ifndef __FIRENET_LIBSHARE_H__
#define __FIRENET_LIBSHARE_H__

#if /*(defined WIN7_32) ||*/ (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#include <stdint.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#endif

#include <windows.h>
#include <stddef.h>

#include <string.h>
#include <assert.h>
#include <time.h>

#include <set>
#include <memory>
#include <algorithm>
#include <functional>
#include <stdexcept>
#if !((defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP))
#define __i386__
#endif
#define _REENTRANT_

#include "../../cauthc/include/cauthc.h"

#endif