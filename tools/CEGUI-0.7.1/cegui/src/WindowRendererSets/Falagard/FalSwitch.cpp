//  FalSwitch.cpp
//  CEGUI
#include "FalSwitch.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    const utf8 FalagardSwitch::TypeName[] = "Falagard/Switch";
    
    FalagardSwitch::FalagardSwitch(const String& type) :
    WindowRenderer(type)
    {
    }
    
    void FalagardSwitch::render()
    {
        Switch* w = (Switch*)d_window;
        const WidgetLookFeel& wlf = getLookNFeel();
        
        bool norm = false;
        String state;
        
        
		if (w->isDisabled())
		{
		    state = "Disabled";
		}
		else if (w->isPushed())
		{
            state =  "Pushed" ;
		}
        
		else if (w->isHovering())
        {
            state = "Hover";
        }
        else
		{
		    state = "Normal";
		    norm = true;
		}
        
        if (!norm && !wlf.isStateImageryPresent(state))
        {
            state = "Normal";
        }
        
        state = w->getStatusString() + state;
        
		if (!norm||!w->isFlash())
		{
			wlf.getStateImagery(actualStateName(state)).render(*w);
		}
        
    }
    
} // End of  CEGUI namespace section