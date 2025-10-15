//  CEGUISwitchProperties.cpp
//  CEGUI
#include "elements/CEGUISwitchProperties.h"
#include "elements/CEGUISwitch.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIExceptions.h"
#include <cstdlib>
#include <cstdio>


// Start of CEGUI namespace section
namespace CEGUI
{
    
    // Start of CheckboxProperties namespace section
    namespace SwitchProperties
    {
        String Status::get(const PropertyReceiver* receiver) const
        {
            return static_cast<const Switch*>(receiver)->getStatusString();
        }
        
        
        void Status::set(PropertyReceiver* receiver, const String& value)
        {
            static_cast<Switch*>(receiver)->setStatus(value);
        }
        
    } // End of  CheckboxProperties namespace section
    
} // End of  CEGUI namespace section
