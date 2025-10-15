#ifndef __FireClient__LuaXPRenderEffect__
#define __FireClient__LuaXPRenderEffect__

#include "XPRenderEffect.h"

class LuaXPRenderEffect:public XPRenderEffect
{
public:
    LuaXPRenderEffect(int userid, int handler);
	~LuaXPRenderEffect();
    
    virtual void performPostRenderFunctions();
private:
    int m_iID;
	int m_iHandler;
};

#endif /* defined(__FireClient__LuaXPRenderEffect__) */
