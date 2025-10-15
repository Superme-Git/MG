#ifndef __FIRENET_SYSCONF_H__
#define __FIRENET_SYSCONF_H__

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

#endif // #ifndef __FIRENET_SYSCONF_H__
