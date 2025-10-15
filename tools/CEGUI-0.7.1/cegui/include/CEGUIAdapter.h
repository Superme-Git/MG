//  CEGUIAdapter.h
//  CEGUI

#ifndef CEGUI_CEGUIAdapter_h
#define CEGUI_CEGUIAdapter_h
#include "CEGUIRect.h"
namespace CEGUI {
    class IAdapter
    {
    public:
        virtual int GetLogicWidth() = 0;
        virtual int GetLogicHeight() = 0;
        
        virtual int GetScreenWidth() = 0;
        virtual int GetScreenHeight() = 0;
        
        virtual int GetDisplayOffsetX() = 0;
        virtual int GetDisplayOffsetY() = 0;
        
        virtual int GetDisplayWidth() = 0;
        virtual int GetDisplayHeight() = 0;
        
        Rect LogicToScreen(const Rect& src)
        {
            float s = ((float)GetDisplayHeight())/((float)GetLogicHeight());
			int lw = PixelAligned(src.getWidth()*s);
            int lh = PixelAligned(src.getHeight()*s);
            int lx = PixelAligned(src.d_left * s + GetDisplayOffsetX());
            int ly = PixelAligned(src.d_top * s + GetDisplayOffsetY());
            Rect ret(lx, ly, lw + lx, lh + ly);
            
            return ret;
        }
    };
}

#endif
