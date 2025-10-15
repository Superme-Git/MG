#include "stdafx.h"
#include "Utils2.h"

#include "GameTable/EffectPath/CEffectPathNoneDrama.h"
#include "GameTable/message/CMessageTip.h"

#ifdef WIN32
#include "CoreLog.h"
#endif

namespace MHSD_UTILS
{
	const std::wstring GETSTRING(int id)
	{
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushIntegerToLuaStack(id);
		std::string tmp = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("MHSD_UTILS.get_resstring", 1);
		return s2ws(tmp);
	}

	const std::wstring& GetEffectPath(int id)
	{
		return GameTable::EffectPath::GetCEffectPathNoneDramaTableInstance().getRecorder(id).Path;
	}

	const std::wstring& GetTipsMsg(int id)
	{
#ifdef WIN32
		const GameTable::message::CMessageTip &tip = GameTable::message::GetCMessageTipTableInstance().getRecorder(id);
		if (tip.id <= 0)
		{
			SDLOG_WARN(L"table CMessageTip can't find ID: %d", id);
			char tmp[128] = {};
			std::sprintf(tmp, "[WARNNING] table CMessageTip can't find ID: %d\n", id);
			OutputDebugStringA(tmp);
		}
		return tip.msg;
#else
		return GameTable::message::GetCMessageTipTableInstance().getRecorder(id).msg;
#endif
	}
}//namepace MHSD_UTILS
