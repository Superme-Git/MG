

/****************************************************************************
 Copyright (c) 2011 cocos2d-x.org

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
//#include "stdafx.h"
#include "CCLuaEngine.h"
#include "tolua++.h"

extern "C" {
#include "lualib.h"
#include "lauxlib.h"
#include "tolua_fix.h"
}

#include "cocos2d.h"
#include "LuaCocos2d.h"
#include "cocoa/CCArray.h"
#include "CCScheduler.h"
#include "ljfmext.h"
#include "utils/StringUtil.h"
#include "LuaFunctor.h"
#include "LJFMLuaLoader.h"
#include "log/CoreLog.h"


#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#ifndef _3RD_PLATFORM
extern "C" {
#include "luasocket.h"
#include "mime.h"
}
#endif
#endif

#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
//#include "Cocos2dxLuaLoader.h"
#include "CCLuaJavaBridge.h"
#endif
NS_CC_BEGIN

CCLuaEngine::~CCLuaEngine()
{
    lua_close(m_state);
}

bool CCLuaEngine::init(void)
{
    m_state = lua_open();
    luaL_openlibs(m_state);

#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	tolua_Cocos2dWP8_open(m_state);
#else
	tolua_Cocos2d_open(m_state);

    /**
     * 鍙湪ios涓诲共涓婂姞鐫�
     */
#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS
#ifndef _3RD_PLATFORM
    lua_register(m_state, "luaopen_socket_core", luaopen_socket_core);
    lua_register(m_state, "luaopen_mime_core", luaopen_mime_core);
#endif
#endif

#endif
    tolua_prepare_ccobject_table(m_state);
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    //addLuaLoader(loader_Android);
	LuaJavaBridge::luaopen_luaj(m_state);
#endif
	addLuaLoader(loader_LJFM);
    return true;
}

CCLuaEngine* CCLuaEngine::engine()
{
    CCLuaEngine* pEngine = new CCLuaEngine();
    pEngine->init();
    pEngine->autorelease();
    return pEngine;
}

void CCLuaEngine::removeCCObjectByID(int nLuaID)
{
    tolua_remove_ccobject_by_refid(m_state, nLuaID);
}

void CCLuaEngine::removeLuaHandler(int nHandler)
{
    tolua_remove_function_by_refid(m_state, nHandler);
}

void CCLuaEngine::addSearchPath(const char* path)
{
    lua_getglobal(m_state, "package");                              /* stack: package */
    lua_getfield(m_state, -1, "path");            /* get package.path, stack: package path */
    const char* cur_path =  lua_tostring(m_state, -1);
    lua_pop(m_state, 1);                                            /* stack: package */
    lua_pushfstring(m_state, "%s;%s/?.lua", cur_path, path);        /* stack: package newpath */
    lua_setfield(m_state, -2, "path");      /* package.path = newpath, stack: package */
    lua_pop(m_state, 1);                                            /* stack: - */
}

int CCLuaEngine::executeString(const char *codes, bool bGC)
{
	if (codes)
	{
		int nRet = luaL_dostring(m_state, codes);
		if (bGC){
			lua_gc(m_state, LUA_GCCOLLECT, 0);
		}

		if (nRet != 0)
		{
	#if (defined WIN32) && _DEBUG
			std::string strOut = "[LUA ERROR] ";
			strOut += lua_tostring(m_state, -1);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
	#endif
			SDLOG_ERR(L"[LUA ERROR] %s", lua_tostring(m_state, -1));
			lua_pop(m_state, 1);
			return nRet;
		}
		return 0;
	}
	else
	{
		if (bGC){
			lua_gc(m_state, LUA_GCCOLLECT, 0);
		}
	}
}

int CCLuaEngine::executeScriptFile(const wchar_t* filename)
{
    // load file
    std::wstring FName = L"/script/" + std::wstring(filename);
    
    #ifndef NoPack
        std::transform(FName.begin(),FName.end(),FName.begin(),::tolower);
    #else
    #endif
    
#if (defined WIN32) && _DEBUG
	std::string strOut = "executescriptfile ";
	strOut += SHARE_Wstring2String(FName).c_str();
	strOut += '\n';
	::OutputDebugStringA(strOut.c_str());
#endif
	//LOGD("executescriptfile %s\n", SHARE_Wstring2String(FName).c_str());
    LJFM::LJFMF AFile;
    if( !AFile.Open(FName, LJFM::FM_EXCL, LJFM::FA_RDONLY ))
        return -1;
    int file_size = (int)AFile.GetSize();
    if( file_size == 0 ) return -1;
    LJFM::LJFMID output = AFile.GetImage();

	//LOGD("script found!\n");
    // load code into lua
    int loaderr = luaL_loadbuffer(m_state,
                                  reinterpret_cast<const char*>(output.GetData()),
                                  output.GetSize(), SHARE_Wstring2String(filename).c_str());
    
    int top = lua_gettop(m_state);
    if (loaderr)
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] ";
		strOut += lua_tostring(m_state, -1);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] %s", lua_tostring(m_state, -1));
        //LOGD("[LUA ERROR] %s\n", lua_tostring(m_state, -1));
        lua_settop(m_state,top);
        return loaderr;
    }
    
    int error = 0;
    
    try
    {
        lua_getglobal(m_state, "debug");
        lua_getfield(m_state, -1, "traceback");
        lua_insert(m_state, 1);
        lua_remove(m_state, -1);
        // call it
        error = lua_pcall(m_state, 0, 0, 1);
  
    }
    catch (std::exception& e)
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] lua_pcall ";
		strOut += e.what();
		strOut += "catch C++ exception\n";
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] lua_pcall(%s) catch C++ exception: %s", e.what());
        lua_settop(m_state, 0);
        return 0;
    }
    catch (...)
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] lua_pcall catch C++ unknown exception. \n";
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] lua_pcall catch C++ unknown exception.");
        lua_settop(m_state, 0);
        return 0;
    }
    
    // call it
    if (error)
    {
        std::string strOut = "[LUA ERROR] ";
        strOut += lua_tostring(m_state, -1);
        strOut += '\n';
#if (defined WIN32) && _DEBUG
		::OutputDebugStringA(strOut.c_str());
#else
        CCLOG(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] %s", lua_tostring(m_state, -1));
        //LOGD("[LUA ERROR] %s\n", lua_tostring(m_state, -1));
        lua_settop(m_state,0);
        return error;
    }
    
    lua_settop(m_state,0); 
    
    return 0;
}

std::string CCLuaEngine::executeGlobalFunctionBackString(const char* functionName, int numArgs)
{
	//lua_getglobal(m_state, functionName);  /* query function by name, stack: function */
	LuaFunctor::pushNamedFunction(m_state, functionName);

	if (!lua_isfunction(m_state, -1))
	{
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] name ";
		strOut += functionName;
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] name '%s' does not represent a Lua function", functionName);
		lua_pop(m_state, 1);
		return "";
	}

	if (numArgs > 0)
	{
		lua_insert(m_state, -(numArgs + 1));                        /* stack: ... func arg1 arg2 ... */
	}

	int error = 0;

	try
	{
		lua_getglobal(m_state, "debug");
		lua_getfield(m_state, -1, "traceback");
		lua_insert(m_state, 1);
		lua_remove(m_state, -1);
		lua_pcall(m_state, numArgs, 1, 1);         /* call function, stack: ret */
	}
	catch (std::exception& e)
	{
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] lua_pcall ";
		strOut += functionName;
		strOut += "catch C++ exception";
		strOut += e.what();
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] lua_pcall(%s) catch C++ exception: %s", functionName, e.what());
		lua_settop(m_state, 0);
		return "";
	}
	catch (...)
	{
		SDLOG_ERR(L"[LUA ERROR] lua_pcall(%s) catch C++ unknown exception.", functionName);
		lua_settop(m_state, 0);
		return "";
	}

	if (error)
	{
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] ";
		strOut += lua_tostring(m_state, -1);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] %s", lua_tostring(m_state, -1));
		lua_settop(m_state, 0); // clean error message
		return "";
	}

	// get return value
	if (!lua_isstring(m_state, -1))
	{
		lua_settop(m_state, 0);
		return "";
	}

	std::string ret = lua_tostring(m_state, -1);
	lua_settop(m_state, 0);
	return ret;
}


int    CCLuaEngine::executeGlobalFunction(const char* functionName, int numArgs)
{
    GlobalFunctionHandler::iterator it = m_GlobalFunctionHandler.find(functionName);
    if (it != m_GlobalFunctionHandler.end()) {
        return this->executeFunctionByHandler(it->second, numArgs);
    }
    
    //lua_getglobal(m_state, functionName);  /* query function by name, stack: function */
    LuaFunctor::pushNamedFunction(m_state, functionName);

    if (!lua_isfunction(m_state, -1))
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] name ";
		strOut += functionName;
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] name '%s' does not represent a Lua function", functionName);
        lua_pop(m_state, 1);
        return 0;
    }

    lua_pushvalue(m_state, -1);
    int handler = luaL_ref(m_state, LUA_REGISTRYINDEX);
    m_GlobalFunctionHandler.insert(GlobalFunctionHandler::value_type(functionName, handler));

    if (numArgs > 0)
    {
        lua_insert(m_state, -(numArgs + 1));                        /* stack: ... func arg1 arg2 ... */
    }
    
    int error = 0;
 
    try
    {
        lua_getglobal(m_state, "debug");
        lua_getfield(m_state, -1, "traceback");
        lua_insert(m_state, 1);
        lua_remove(m_state, -1);
        lua_pcall(m_state, numArgs, 1, 1);         /* call function, stack: ret */
    }
    catch (std::exception& e)
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] lua_pcall ";
		strOut += functionName;
		strOut += "catch C++ exception";
		strOut += e.what();
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] lua_pcall(%s) catch C++ exception: %s", functionName, e.what());
        lua_settop(m_state, 0);
        return 0;
    }
    catch (...)
    {
        SDLOG_ERR(L"[LUA ERROR] lua_pcall(%s) catch C++ unknown exception.",functionName);
        lua_settop(m_state, 0);
        return 0;
    }

    if (error)
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] ";
		strOut += lua_tostring(m_state, -1);
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] %s", lua_tostring(m_state, -1));
        lua_settop(m_state, 0); // clean error message
        return 0;
    }

	// get return value
	int ret = 0;
	if (lua_isnumber(m_state, -1))
	{
		ret = lua_tointeger(m_state, -1);
	}
	else if (lua_isboolean(m_state, -1))
	{
		ret = lua_toboolean(m_state, -1);
	}

	lua_settop(m_state, 0);
	return ret;
}


int CCLuaEngine::executeGlobalFunctionWithIntegerData(const char* functionName, int data)
{
    lua_pushinteger(m_state, data);
    return executeGlobalFunction(functionName, 1);
}

int CCLuaEngine::executeGlobalFunctionWithDoubleData(const char* functionName, double data)
{
    lua_pushnumber(m_state, data);
    return executeGlobalFunction(functionName, 1);
}

int CCLuaEngine::executeGlobalFunctionWithBooleanData(const char* functionName, bool data)
{
    lua_pushboolean(m_state, data);
    return executeGlobalFunction(functionName, 1);
}

int CCLuaEngine::executeGlobalFunctionWithStringData(const char* functionName, const char* data)
{
	lua_pushstring(m_state, data);
	return executeGlobalFunction(functionName, 1);
}

int CCLuaEngine::executeGlobalFunctionWithParamsData(const char* functionName, double param0, double param1, double param2, double param3)
{
    lua_pushnumber(m_state, param0);
    lua_pushnumber(m_state, param1);
    lua_pushnumber(m_state, param2);
    lua_pushnumber(m_state, param3);
    return executeGlobalFunction(functionName, 4);
}

int CCLuaEngine::executeGlobalFunctionWithParamsData(const char* functionName, double param0, double param1, double param2, double param3, double param4)
{
    lua_pushnumber(m_state, param0);
    lua_pushnumber(m_state, param1);
    lua_pushnumber(m_state, param2);
    lua_pushnumber(m_state, param3);
    lua_pushnumber(m_state, param4);
    return executeGlobalFunction(functionName, 5);
}

int CCLuaEngine::executeGlobalFunctionWithParamsData(const char* functionName, void* param0, double param1, double param2, double param3)
{
    lua_pushlightuserdata(m_state, param0);
    lua_pushnumber(m_state, param1);
    lua_pushnumber(m_state, param2);
    lua_pushnumber(m_state, param3);
    return executeGlobalFunction(functionName, 4);
}

int CCLuaEngine::executeGlobalFunctionWithParamsData(const char *functionName, void *param0, void *param1, double param2, double param3) {
    lua_pushlightuserdata(m_state, param0);
    lua_pushlightuserdata(m_state, param1);
    lua_pushnumber(m_state, param2);
    lua_pushnumber(m_state, param3);
    return executeGlobalFunction(functionName, 4);
}

int CCLuaEngine::executeGlobalFunctionWithData(const char* functionName, void* param0, int param1)
{
	lua_pushlightuserdata(m_state, param0);
	lua_pushinteger(m_state, param1);
	return executeGlobalFunction(functionName, 2);
}

int CCLuaEngine::executeGlobalFunctionWith2Int(const char* functionName, int param0, int param1)
{
	lua_pushinteger(m_state, param0);
	lua_pushinteger(m_state, param1);
	return executeGlobalFunction(functionName, 2);
}

int CCLuaEngine::executeGlobalFunctionWithParamsData(const char* functionName, int param0, int param1, int param2, int param3) {
    lua_pushinteger(m_state, param0);
    lua_pushinteger(m_state, param1);
    lua_pushinteger(m_state, param2);
    lua_pushinteger(m_state, param3);
    return executeGlobalFunction(functionName, 4);
}

int CCLuaEngine::executeGlobalFunctionWithParamsData(const char* functionName, const char* param0, const char* param1, const char* param2, const char* param3)
{
	lua_pushstring(m_state, param0);
	lua_pushstring(m_state, param1);
	lua_pushstring(m_state, param2);
	lua_pushstring(m_state, param3);
	return executeGlobalFunction(functionName, 4);
}
int CCLuaEngine::executeGlobalFunctionWithParamsData(const char* functionName, const char* param0, int param1, int param2, int param3, int param4)
{
	lua_pushstring(m_state, param0);
	lua_pushinteger(m_state, param1);
	lua_pushinteger(m_state, param2);
	lua_pushinteger(m_state, param3);
	lua_pushinteger(m_state, param4);
	return executeGlobalFunction(functionName, 5);
}

int CCLuaEngine::executeFunctionByHandler(int nHandler, int numArgs)
{
    if (pushFunctionByHandler(nHandler))
    {
        if (numArgs > 0)
        {
            lua_insert(m_state, -(numArgs + 1));                        /* stack: ... func arg1 arg2 ... */
        }

        int error = 0;
         try
         {
             lua_getglobal(m_state, "debug");
             lua_getfield(m_state, -1, "traceback");
             lua_insert(m_state, 1);
             lua_remove(m_state, -1);

            error = lua_pcall(m_state, numArgs, 1, 1);                  /* stack: ... ret */
         }
        catch (std::exception& e)
         {
#if (defined WIN32) && _DEBUG
			 std::string strOut = "[LUA ERROR] lua_pcall ";
			 strOut += nHandler;
			 strOut += '\n';
			 ::OutputDebugStringA(strOut.c_str());
#endif
			 SDLOG_ERR(L"[LUA ERROR] lua_pcall(%d) catch C++ exception: %s", nHandler, e.what());
             lua_settop(m_state, 0);
             return 0;
         }
         catch (...)
         {
#if (defined WIN32) && _DEBUG
			 std::string strOut = "[LUA ERROR] lua_pcall ";
			 strOut += nHandler;
			 strOut += '\n';
			 ::OutputDebugStringA(strOut.c_str());
#endif
			 SDLOG_ERR(L"[LUA ERROR] lua_pcall(%d) catch C++ unknown exception.", nHandler);
             lua_settop(m_state, 0);
             return 0;
         }
        if (error)
        {
#if (defined WIN32) && _DEBUG
			std::string strOut = "[LUA ERROR] lua_pcall ";
			strOut += lua_tostring(m_state, -1);
			strOut += '\n';
			::OutputDebugStringA(strOut.c_str());
#endif
			SDLOG_ERR(L"[LUA ERROR] %s", lua_tostring(m_state, -1));
            lua_settop(m_state, 0);
            return 0;
        }

        // get return value
        int ret = 0;
        if (lua_isnumber(m_state, -1))
        {
            ret = lua_tointeger(m_state, -1);
        }
        else if (lua_isboolean(m_state, -1))
        {
            ret = lua_toboolean(m_state, -1);
        }

        lua_settop(m_state, 0);
        return ret;
    }
    else
    {
        return 0;
    }
}

int CCLuaEngine::executeFunctionWithIntegerData(int nHandler, int data)
{
    lua_pushinteger(m_state, data);
    return executeFunctionByHandler(nHandler, 1);
}

int CCLuaEngine::executeFunctionWithFloatData(int nHandler, float data)
{
    lua_pushnumber(m_state, data);
    return executeFunctionByHandler(nHandler, 1);
}

int CCLuaEngine::executeFunctionWithBooleanData(int nHandler, bool data)
{
    lua_pushboolean(m_state, data);
    return executeFunctionByHandler(nHandler, 1);
}

int CCLuaEngine::executeFunctionWithStringData(int nHandler, const char* data)
{
	lua_pushstring(m_state, data);
    return executeFunctionByHandler(nHandler, 1);
}

int CCLuaEngine::executeFunctionWithCCObject(int nHandler, CCObject* pObject, const char* typeName)
{
    tolua_pushusertype_ccobject(m_state, pObject->m_uID, &pObject->m_nLuaID, pObject, typeName);
    return executeFunctionByHandler(nHandler, 1);
}

int CCLuaEngine::executeFunctionWithParamsData(int nHandler, double param0, double param1, double param2, double param3)
{
    lua_pushnumber(m_state, param0);
    lua_pushnumber(m_state, param1);
    lua_pushnumber(m_state, param2);
    lua_pushnumber(m_state, param3);
    return executeFunctionByHandler(nHandler, 4);
}

int CCLuaEngine::pushIntegerToLuaStack(int data)
{
    lua_pushinteger(m_state, data);
    return lua_gettop(m_state);
}

int CCLuaEngine::pushInt64ToLuaStack(int64_t data)
{
	lua_pushnumber(m_state, data);
	return lua_gettop(m_state);
}

int CCLuaEngine::pushFloatToLuaStack(float data)
{
    lua_pushnumber(m_state, data);
    return lua_gettop(m_state);
}

int CCLuaEngine::pushBooleanToLuaStack(int data)
{
    lua_pushboolean(m_state, data);
    return lua_gettop(m_state);
}

int CCLuaEngine::pushStringToLuaStack(const char* data)
{
	lua_pushstring(m_state, data);
	return lua_gettop(m_state);
}

int CCLuaEngine::pushUserDataToLuaStack(void* data)
{
	lua_pushlightuserdata(m_state, data);
	return lua_gettop(m_state);
}


int CCLuaEngine::pushCCObjectToLuaStack(CCObject* pObject, const char* typeName)
{
    tolua_pushusertype_ccobject(m_state, pObject->m_uID, &pObject->m_nLuaID, pObject, typeName);
    return lua_gettop(m_state);
}

// functions for excute touch event
int CCLuaEngine::executeTouchEvent(int nHandler, int eventType, CCTouch *pTouch)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
	CCPoint pt = CCDirector::sharedDirector()->convertToGL(pTouch->getLocationInView());
#else
    CCPoint pt = CCDirector::sharedDirector()->convertToGL(pTouch->locationInView());
#endif
    lua_pushinteger(m_state, eventType);
    lua_pushnumber(m_state, pt.x);
    lua_pushnumber(m_state, pt.y);
    return executeFunctionByHandler(nHandler, 3);
}

int CCLuaEngine::executeTouchesEvent(int nHandler, int eventType, CCSet *pTouches)
{
    lua_pushinteger(m_state, eventType);
    lua_newtable(m_state);

    CCDirector* pDirector = CCDirector::sharedDirector();
    CCSetIterator it = pTouches->begin();
    CCTouch* pTouch;
    int n = 1;
    while (it != pTouches->end())
    {
        pTouch = (CCTouch*)*it;
#if CC_TARGET_PLATFORM == CC_PLATFORM_WP8
		CCPoint pt = pDirector->convertToGL(pTouch->getLocationInView());
#else
        CCPoint pt = pDirector->convertToGL(pTouch->locationInView());
#endif
        lua_pushnumber(m_state, pt.x);
        lua_rawseti(m_state, -2, n++);
        lua_pushnumber(m_state, pt.y);
        lua_rawseti(m_state, -2, n++);
        ++it;
    }

    return executeFunctionByHandler(nHandler, 2);
}

int CCLuaEngine::executeSchedule(int nHandler, float dt)
{
    return executeFunctionWithFloatData(nHandler, dt);
}

void CCLuaEngine::addLuaLoader(lua_CFunction func)
{
    if (!func) return;

    // stack content after the invoking of the function
    // get loader table
    lua_getglobal(m_state, "package");                     // package
    lua_getfield(m_state, -1, "loaders");                  // package, loaders

    // insert loader into index 2
    lua_pushcfunction(m_state, func);                      // package, loaders, func
    for (int i = lua_objlen(m_state, -2) + 1; i > 2; --i)
    {
        lua_rawgeti(m_state, -2, i - 1);                   // package, loaders, func, function
                                                           // we call lua_rawgeti, so the loader table now is at -3
        lua_rawseti(m_state, -3, i);                       // package, loaders, func
    }
    lua_rawseti(m_state, -2, 2);                           // package, loaders

    // set loaders into package
    lua_setfield(m_state, -2, "loaders");                  // package

    lua_pop(m_state, 1);
}

bool CCLuaEngine::pushFunctionByHandler(int nHandler)
{
    lua_rawgeti(m_state, LUA_REGISTRYINDEX, nHandler);  /* stack: ... func */
    if (!lua_isfunction(m_state, -1))
    {
#if (defined WIN32) && _DEBUG
		std::string strOut = "[LUA ERROR] function refid ";
		strOut += nHandler;
		strOut += '\n';
		::OutputDebugStringA(strOut.c_str());
#endif
		SDLOG_ERR(L"[LUA ERROR] function refid '%d' does not reference a Lua function", nHandler);
        lua_pop(m_state, 1);
        return false;
    }
    return true;
}

bool CCLuaEngine::executeProtocolHandler(int nHandler,
                            const aio::Protocol& e)
{
    // push Protocol as the first parameter
    tolua_pushusertype(m_state, (void*)&e, "const aio::Protocol");
    return (bool)executeFunctionByHandler(nHandler, 1);
}

void CCLuaEngine::executeLuaProtocolHandler(int nHandler,
                                       const aio::LuaProtocol& lp)
{
    // push LuaProtocol as the first parameter
    tolua_pushusertype(m_state, (void*)&lp, "const aio::LuaProtocol");
    executeFunctionByHandler(nHandler, 1);
}

void CCLuaEngine::collectMemory()
{
	lua_gc(m_state, LUA_GCCOLLECT, 0);
}

CCScriptEngineProtocol* gGetScriptEngine()
{
	return CCScriptEngineManager::sharedManager()->getScriptEngine();
}

NS_CC_END
