#ifndef __MAINROLEDATAMANAGER_H__
#define __MAINROLEDATAMANAGER_H__

#include "CCLuaEngine.h"
#include "CallLuaUtil.h"

//处理主角和主角宠物的数据更新
//服务器发下来有关主角和主宠物的数据更新，直接调用这个类里的函数，再通过事件多播，传给其他需要更新数据的类，比如MainCharacter等
enum ePetState
{
	ePetState_Normal = 0x00,
	ePetState_Death	= 0x01,
	ePetState_Flyout	= 0x02,
	ePetState_AlreadyFight = 0x04,
};

//MainRoleDataManager
template <typename T>
inline T GetMainRoleDataNumValue(const char *name)
{
	CallLuaUtil util;
	util.addArg(name);
	if (util.callLua("MainRoleData_getNumValue"))
	{
		tolua_Error tolua_err;
		lua_State *state = cocos2d::gGetScriptEngine()->getLuaState();
		if (tolua_isnumber(state, -1, 0, &tolua_err))
		{
			T ret(tolua_tonumber(state, -1, 0));
			lua_pop(state, 1);
			return ret;
		}
	}
	return (T)0;
}

extern int GetMainRoleDataAttr(int attrType);

extern std::string GetMainRoleDataStrValue(const char *name);

class CMainRoleDataManager
{
public:
	static void UpdateMainPetAttribute(std::map<int, int>& attribute);
	static void UpdateMainBattlerAttribute(std::map<int, int>& attribute);
};

#endif
