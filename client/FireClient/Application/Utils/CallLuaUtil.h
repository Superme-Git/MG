/***********************************************************************
	filename:	CallLuaUtil.h
	author:		Ñî´ºÀ×
***********************************************************************/

#ifndef _CallLuaUtil_H
#define _CallLuaUtil_H

#include "tolua++.h"
#include <vector>

// C++ µ÷ lua º¯Êý¸¨Öú

class LuaArg
{
public:
	LuaArg(){}
	virtual ~LuaArg(){}
	virtual void pushToLua(lua_State* pState) const = 0;
};

class LuaArgBool : public LuaArg
{
private:
	bool mVal;

public:
	LuaArgBool(bool b) : mVal(b) {}
	virtual void pushToLua(lua_State* pState) const { tolua_pushboolean(pState, mVal); }
};

class LuaArgInt : public LuaArg
{
private:
	int mVal;

public:
	LuaArgInt(int i) : mVal(i) {}
	virtual void pushToLua(lua_State* pState) const { tolua_pushnumber(pState, mVal); }
};

class LuaArgInt64 : public LuaArg
{
private:
	int64_t mVal;

public:
	LuaArgInt64(int64_t i) : mVal(i) {}
	virtual void pushToLua(lua_State* pState) const { tolua_pushnumber(pState, mVal); }
};

class LuaArgFloat : public LuaArg
{
private:
	float mVal;

public:
	LuaArgFloat(float f) : mVal(f) {}
	virtual void pushToLua(lua_State* pState) const { tolua_pushnumber(pState, mVal); }
};

class LuaArgCString : public LuaArg
{
private:
	std::string mVal;

public:
	LuaArgCString(const char* str) : mVal(str) {}
	virtual void pushToLua(lua_State* pState) const { tolua_pushstring(pState, mVal.c_str()); }
};

class LuaArgUsertype : public LuaArg
{
private:
	void* mpData;
	const char* mType;
	bool mbRegisterGC;

public:
	LuaArgUsertype(void* pData, const char* szType, bool bRegGC) : mpData(pData), mType(szType), mbRegisterGC(bRegGC){}
	virtual void pushToLua(lua_State* pState) const;
};

class CallLuaUtil
{
protected:
	std::vector<LuaArg*> mArgs;
	lua_State* mpState;
	int mOriginalTop;

public:
	CallLuaUtil() : mpState(NULL), mOriginalTop(0) {}
	virtual ~CallLuaUtil();

	void addArg(bool val);
	void addArg(int val);
	void addArg(int64_t val);
	void addArg(float val);
	void addArg(const char* val);
	void addArg(void* val, const char* szType, bool bRegGC);

	bool callLua(lua_State* pState, const char* fname, int nresults = 1);
	bool callLua(const char* fname, int nresults = 1);

	bool getRetBool(bool def = false);
	int64_t getRetInt(int64_t def = 0);
	float getRetFloat(float def = 0);
	const char* getRetCString(const char* def = "");
	void* getRetUsertype(const char* szType);

	void reset();

protected:
	int pushToLua(lua_State* pState);
};

#endif
