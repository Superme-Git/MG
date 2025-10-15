/***********************************************************************
    filename:   FalIrregularFigure.h
 ***************************************************************************/
#ifndef _FalLinkText_h_
#define _FalLinkText_h_

#include "FalModule.h"
#include "../../elements/CEGUILinkText.h"

// Start of CEGUI namespace section
namespace CEGUI
{
    /*!
    \brief
        Button class for the FalagardBase module.

        This class requires LookNFeel to be assigned.  The LookNFeel should provide the following:

        States (missing states will default to 'Normal'):
            - Normal    - Rendering for when the button is neither pushed or has the mouse hovering over it.
            - Hover     - Rendering for then the button has the mouse hovering over it.
            - Pushed    - Rendering for when the button is pushed and mouse is over it.
            - PushedOff - Rendering for when the button is pushed and mouse is not over it.
            - Disabled  - Rendering for when the button is disabled.
    */
    class FALAGARDBASE_API FalagardLinkText : public WindowRenderer
    {
    public:
        static const utf8   TypeName[];       //!< type name for this widget.

        /*!
        \brief
            Constructor
        */
        FalagardLinkText(const String& type);
		FalagardLinkText(){}

        void render();

		virtual void clone(const WindowRenderer* templateRenderer){ WindowRenderer::clone(templateRenderer); }

    };

} // End of  CEGUI namespace section


#endif  // end of guard _FalButton_h_
