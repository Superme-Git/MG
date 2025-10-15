#ifndef FireClient_LuaEffectNotify_h
#define FireClient_LuaEffectNotify_h

class LuaEffectNotify : public Nuclear::IEffectNotify
{
public:
    LuaEffectNotify(int handler)
		: m_iHandler(handler)
	{ }

public:
	virtual void OnDelete(Nuclear::IEffect *pEffect)
	{
		if (pEffect){
			pEffect->RemoveNotify(this);
			delete this;
		}
	}

	virtual void OnEnd(Nuclear::IEffect *pEffect)
	{
        cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeFunctionByHandler(m_iHandler, 0);
		if (pEffect){
			pEffect->RemoveNotify(this);
			delete this;
		}
	}

private:
	const int m_iHandler;
};


#endif
