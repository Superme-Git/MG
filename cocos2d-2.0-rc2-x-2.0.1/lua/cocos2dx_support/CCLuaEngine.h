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

#ifndef __CC_LUA_ENGINE_H__
#define __CC_LUA_ENGINE_H__

extern "C" {
#include "lua.h"
}

#include "ccTypes.h"
#include "cocoa/CCObject.h"
#include "touch_dispatcher/CCTouch.h"
#include "cocoa/CCSet.h"
#include "base_nodes/CCNode.h"
#include "script_support/CCScriptSupport.h"

#include <map>
#include <string>

NS_CC_BEGIN

// Lua support for cocos2d-x
class CCLuaEngine : public CCScriptEngineProtocol
{
public:
    ~CCLuaEngine();
    
    /**
     @brief Method used to get a pointer to the lua_State that the script module is attached to.
     @return A pointer to the lua_State that the script module is attached to.
     */
    virtual lua_State* getLuaState(void) {
        return m_state;
    }
    
    /**
     @brief Remove CCObject from lua state
     @param object to remove
     */
    virtual void removeCCObjectByID(int nLuaID);
    
    /**
     @brief Remove Lua function reference
     */
    virtual void removeLuaHandler(int nHandler);
    
    /**
     @brief Add a path to find lua files in
     @param path to be added to the Lua path
     */
    virtual void addSearchPath(const char* path);
    
    /**
     @brief Execute script code contained in the given string.
     @param codes holding the valid script code that should be executed.
     @return 0 if the string is excuted correctly.
     @return other if the string is excuted wrongly.
     */
	virtual int executeString(const char* codes, bool bGC = false);
    
    /**
     @brief Execute a script file.
     @param filename String object holding the filename of the script file that is to be executed
     */
  //  virtual int executeScriptFile(const char* filename);
    
    virtual int executeScriptFile(const wchar_t* filename);
    
    /**
     @brief Execute a scripted global function.
     @brief The function should not take any parameters and should return an integer.
     @param functionName String object holding the name of the function, in the global script environment, that is to be executed.
     @return The integer value returned from the script function.
     */
	virtual std::string executeGlobalFunctionBackString(const char* functionName, int numArgs = 0);
    virtual int executeGlobalFunction(const char* functionName, int numArgs = 0);
    virtual int executeGlobalFunctionWithIntegerData(const char* functionName, int data);
    virtual int executeGlobalFunctionWithDoubleData(const char* functionName, double data);
    virtual int executeGlobalFunctionWithBooleanData(const char* functionName, bool data);
    virtual int executeGlobalFunctionWithStringData(const char* functionName, const char* data);
    virtual int executeGlobalFunctionWithParamsData(const char* functionName, double param0, double param1, double param2 = 0, double param3 = 0);
    virtual int executeGlobalFunctionWithParamsData(const char* functionName, double param0, double param1, double param2, double param3, double param4);//xiaolong
    virtual int executeGlobalFunctionWithParamsData(const char* functionName, void* param0, double param1, double param2 = 0, double param3 = 0);
    virtual int executeGlobalFunctionWithParamsData(const char* functionName, void* param0, void* param1, double param2 = 0, double param3 = 0);
    virtual int executeGlobalFunctionWithParamsData(const char* functionName, int param0, int param1, int param2 = 0, int param3 = 0);
	virtual int executeGlobalFunctionWithParamsData(const char* functionName, const char* param0, const char* param1, const char* param2, const char* param3);
	virtual int executeGlobalFunctionWithParamsData(const char* functionName, const char* param0, int param1, int param2, int param3, int param4 = 0);
	virtual int executeGlobalFunctionWithData(const char* functionName, void* param0, int param1);
	virtual int executeGlobalFunctionWith2Int(const char* functionName, int param0, int param1);
    
    
    ///////////////////////////////////////////////////////////////////
    virtual bool executeProtocolHandler(int nHandler, const aio::Protocol& e);
    
    virtual void executeLuaProtocolHandler(int nHandler, const aio::LuaProtocol& lp);
    
    //////////////////////////////////////////////////////////////////
    
    /**
     @brief Execute a function by ref id
     @param The function ref id
     @param Number of parameters
     @return The integer value returned from the script function.
     */
    virtual int executeFunctionByHandler(int nHandler, int numArgs = 0);
    virtual int executeFunctionWithIntegerData(int nHandler, int data);
    virtual int executeFunctionWithFloatData(int nHandler, float data);
    virtual int executeFunctionWithBooleanData(int nHandler, bool data);
    virtual int executeFunctionWithStringData(int nHandler, const char* data);
    virtual int executeFunctionWithCCObject(int nHandler, CCObject* pObject, const char* typeName);
    virtual int executeFunctionWithParamsData(int nHandler, double param0, double param1, double param2 = 0, double param3 = 0);
    virtual int pushIntegerToLuaStack(int data);
	virtual int pushInt64ToLuaStack(int64_t data);
	virtual int pushFloatToLuaStack(float data);
    virtual int pushBooleanToLuaStack(int data);
	virtual int pushStringToLuaStack(const char* data);
	virtual int pushUserDataToLuaStack(void* data);
    virtual int pushCCObjectToLuaStack(CCObject* pObject, const char* typeName);
    
    // functions for excute touch event
    virtual int executeTouchEvent(int nHandler, int eventType, cocos2d::CCTouch *pTouch);
    virtual int executeTouchesEvent(int nHandler, int eventType, cocos2d::CCSet *pTouches);
    
    // execute a schedule function
    virtual int executeSchedule(int nHandler, float dt);
    
    // Add lua loader, now it is used on android
    virtual void addLuaLoader(lua_CFunction func);

	//collect garbage memory
	virtual void collectMemory();
    
	static  CCLuaEngine* engine();
    
    
    //////////////////////////////////////////////////////////////////////
private:
    CCLuaEngine(void)
    : m_state(NULL)
    {
    }
    
    bool init(void);
    bool pushFunctionByHandler(int nHandler);
    
    lua_State* m_state;
    
    typedef std::map<std::string, unsigned int> GlobalFunctionHandler;
    GlobalFunctionHandler m_GlobalFunctionHandler;
};

extern CCScriptEngineProtocol* gGetScriptEngine();

NS_CC_END

#endif // __CC_LUA_ENGINE_H__
