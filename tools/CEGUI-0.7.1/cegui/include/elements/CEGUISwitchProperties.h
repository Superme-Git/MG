//  CEGUISwitchProperties.h
//  CEGUI

#ifndef CEGUI_CEGUISwitchProperties_h
#define CEGUI_CEGUISwitchProperties_h


#include "../CEGUIProperty.h"


// Start of CEGUI namespace section
namespace CEGUI
{
    
    // Start of CheckboxProperties namespace section
    /*!
     \brief
     Namespace containing all classes that make up the properties interface for the Switch class
     */
    namespace SwitchProperties
    {
        /*!
         \brief
         Property to access the status of the switch.
         
         This property offers access to the status for the switch object.
         
         \par Usage:
         - Name: Status
         - Format: "[text]".
         
         \par Where [Text] is:
         - "ON" to indicate the switch is on .
         - "OFF" to indicate the switch is off.
         */
        class Status : public Property
        {
        public:
            Status() : Property(
                                  "Status",
                                  "Property to get/set the stauts of the Switch.  Value is either \"ON\" or \"OFF\".",
                                  "ON")
            {}
            
            String	get(const PropertyReceiver* receiver) const;
            void	set(PropertyReceiver* receiver, const String& value);
        };
        
    } // End of  SwitchProperties namespace section
    
} // End of  CEGUI namespace section


#endif
