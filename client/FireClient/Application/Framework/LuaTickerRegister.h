#ifndef __FireClient__LuaTickerRegister__
#define __FireClient__LuaTickerRegister__

class LuaTickerRegister
{
public:
    LuaTickerRegister();
    virtual ~LuaTickerRegister();
    
    void LuaTick(int delta);
	void RegisterTickerHandler(int handler);

protected:
    int m_iLuaTickerHandler;
};

#endif /* defined(__FireClient__LuaTickerRegister__) */
