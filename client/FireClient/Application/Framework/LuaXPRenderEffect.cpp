#include "LuaXPRenderEffect.h"
#include "script_support/CCScriptSupport.h"
#include "CEGUI.h"

LuaXPRenderEffect::LuaXPRenderEffect(int userid, int handler):
m_iID(userid),m_iHandler(handler)
{ }

LuaXPRenderEffect::~LuaXPRenderEffect()
{ }

void LuaXPRenderEffect::performPostRenderFunctions()
{
    CEGUI::System::getSingleton().getRenderer()->endRendering();
    cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeFunctionWithIntegerData(m_iHandler, m_iID);
    CEGUI::System::getSingleton().getRenderer()->beginRendering();
}
