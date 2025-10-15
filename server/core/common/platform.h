
#ifndef _SHARE_PLATFORM_H_
#define _SHARE_PLATFORM_H_

#ifdef WIN32

#ifdef _WIN64
//#error "__i386__ only"
#endif

#include <windows.h>
#include <stddef.h>

#ifndef __i386__
#define __i386__ 
#endif

#ifndef _REENTRANT_
#define _REENTRANT_
#endif

#ifndef int64_t
typedef __int64 int64_t;
#endif

#ifndef uint64_t
typedef unsigned __int64 uint64_t;
#endif

#else

#include <stdint.h>

#endif

namespace GNET {

inline int signum(int64_t a)                 { return (int)(a >> 63 | ((uint64_t)-a) >> 63); } // see java.lang.Long.signum
inline int compareTo(int64_t a, int64_t b)   { return signum(a-b); }
inline int compareTo(uint64_t a, uint64_t b) { return signum(a-b); }

}

#endif // _SHARE_PLATFORM_H_
