//  FalSwitch.h
//  CEGUI

#ifndef CEGUI_FalSwitch_h
#define CEGUI_FalSwitch_h

#include "FalModule.h"
#include "../../elements/CEGUISwitch.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    /*!
     \brief
     Switch class for the FalagardBase module.
     
     This class requires LookNFeel to be assigned.  The LookNFeel should provide the following:
     
     States (missing states will default to 'Normal'):
     - ONNormal    - Rendering for when the switch is neither pushed or has the mouse hovering over it(status is ON).
     - ONHover     - Rendering for then the switch has the mouse hovering over it(status is ON).
     - ONPushed    - Rendering for when the switch is pushed and mouse is over it(status is ON).
     - ONPushedOff - Rendering for when the switch is pushed and mouse is not over it(status is ON).
     - ONDisabled  - Rendering for when the switch is disabled(status is ON).
     - OFFNormal    - Rendering for when the switch is neither pushed or has the mouse hovering over it(status is OFF).
     - OFFHover     - Rendering for then the switch has the mouse hovering over it(status is OFF).
     - OFFPushed    - Rendering for when the switch is pushed and mouse is over it(status is OFF).
     - OFFPushedOff - Rendering for when the switch is pushed and mouse is not over it(status is OFF).
     - OFFDisabled  - Rendering for when the switch is disabled(status is OFF).
     */
    class FALAGARDBASE_API FalagardSwitch : public WindowRenderer
    {
    public:
        static const utf8   TypeName[];       //!< type name for this widget.
        
        /*!
         \brief
         Constructor
         */
        FalagardSwitch(const String& type);
		FalagardSwitch(){}
        
        void render();
        virtual String actualStateName(const String& name) const   {return name;}

		virtual void clone(const WindowRenderer* templateRenderer){ WindowRenderer::clone(templateRenderer); }

    };
    
} // End of  CEGUI namespace section

#endif
