//////////////////////////////////////////////////////////////////////////
// File:		LocationDetector_crossPlatform.cpp
// Author:	Ñî´ºÀ×
// Date:		2016/05/11
//////////////////////////////////////////////////////////////////////////

#include "LocationDetector.h"
#include "script_support/CCScriptSupport.h"

const LocationDetector::Location LocationDetector::getLocation(int index) const
{
	if (0 <= index && index < (int)mLocations.size())
	{
		return mLocations[index];
	}

	static Location sDummy = {0, 0, "Unknown"};
	return sDummy;
}

void LocationDetector::onFinishDetect(bool bSuccess)
{
	if (!mFinishCallbackFunc.empty())
	{
		cocos2d::CCScriptEngineManager* pScriptEngineMgr = cocos2d::CCScriptEngineManager::sharedManager();
		if (pScriptEngineMgr)
		{
			cocos2d::CCScriptEngineProtocol* pScriptEngine = pScriptEngineMgr->getScriptEngine();
			if (pScriptEngine)
			{
				pScriptEngine->executeGlobalFunctionWithBooleanData(mFinishCallbackFunc.c_str(), bSuccess);
			}
		}
	}
}