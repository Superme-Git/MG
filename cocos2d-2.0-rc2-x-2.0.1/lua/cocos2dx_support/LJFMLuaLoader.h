#ifndef __COCOS2DX_LUA_LJFM_LOADER_H__
#define __COCOS2DX_LUA_LJFM_LOADER_H__


extern "C"
{
#include "lua.h"
#include "lualib.h"
#include "lauxlib.h"

extern int loader_LJFM(lua_State *L);
}

#endif // __COCOS2DX_LUA_LJFM_LOADER_H__
