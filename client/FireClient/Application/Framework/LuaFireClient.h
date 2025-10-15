
#ifndef __LUAFIRECLIENT_H__
#define __LUAFIRECLIENT_H__

#if defined(_WIN32) && defined(_DEBUG)
#pragma warning (disable:4800)
#endif

#define TOLUA_RELEASE

#if !defined(DEBUG)
#define TOLUA_RELEASE
#endif

#include "tolua++.h"
#if (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
int  tolua_engineWp8_open(lua_State* tolua_S);
int  tolua_FireClientWp8_open(lua_State* tolua_S);
#elif defined WIN7_32
int  tolua_engineWin32_open(lua_State* tolua_S);
int  tolua_FireClientWin32_open(lua_State* tolua_S);
#else
int  tolua_engine_open(lua_State* tolua_S);
int  tolua_FireClient_open(lua_State* tolua_S);
#endif

#endif // __LUAFIRECLIENT_H
