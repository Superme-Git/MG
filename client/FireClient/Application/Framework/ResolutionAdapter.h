#ifndef FireClient_ResolutionAdapter_h
#define FireClient_ResolutionAdapter_h

#include <CEGUIAdapter.h>
#include <engine/nuiadapter.h>

class ResolutionAdapter : public CEGUI::IAdapter, public Nuclear::iadapter
{
public:
	ResolutionAdapter();
    
    virtual bool init();

	virtual int GetDisplayWidth();
	virtual int GetDisplayHeight();

	virtual int GetDisplayOffsetX();
	virtual int GetDisplayOffsetY();

    virtual int get_display_x();
    virtual int get_display_y();
    
    virtual int GetLogicWidth();
    virtual int GetLogicHeight();
    

	virtual int get_display_w();
	virtual int get_display_h();
	virtual int GetScreenWidth();
    virtual int GetScreenHeight();
    
    virtual void SetScreenHeight(int h);
    
    virtual void SetDisplayOffsetY(int y);

	virtual int get_logic_w();
	virtual int get_logic_h();

	virtual int get_screen_w();
	virtual int get_screen_h();

private:    
    int m_logicW, m_logicH;
    int m_screenW, m_screenH;
    int m_displayX, m_displayY;
    int m_displayW, m_displayH;
    
    int m_displayUIX, m_displayUIY;
    int m_displayUIW, m_displayUIH;
    int m_screenUIW, m_screenUIH;
    int m_logicUIW, m_logicUIH;
    
};

#endif
