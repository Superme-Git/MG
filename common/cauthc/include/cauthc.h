#ifndef __FIRENET_CAUTHC_H__
#define __FIRENET_CAUTHC_H__
#pragma once

#include <stdint.h>
#include <string>
#include <time.h>
#include <map>
#include <vector>
#include <list>
#include <deque>
#include <iostream>

// boo define
template<bool x> struct STATIC_ASSERTION_FAILURE;
template<> struct STATIC_ASSERTION_FAILURE<true>{};
template<int x> struct static_assert_test{};
#define STATIC_ASSERT(B) typedef static_assert_test<sizeof(STATIC_ASSERTION_FAILURE<(bool)(B)>)> static_assert_typedef_ ## __LINE__

// includes.
#include "cauthc.h"
#include "octets.h"
#include "marshal.h"
#include "streamcompress.h"
#include "security.h"
#include "protocol.h"

#ifdef TARGET_OS_IPHONE
#define OS_IOS
#elif defined( TARGET_IPHONE_SIMULATOR)
#define OS_IOS
#elif defined( ANDROID)
#define OS_ANDROID
#elif defined( __linux__)
#define OS_LINUX
#elif defined( WIN32)
#define OS_WINDOWS
#endif

namespace FireNet{	inline int signum(int64_t a){ return (int)(a >> 63 | ((uint64_t)-a) >> 63);	}	}

#endif