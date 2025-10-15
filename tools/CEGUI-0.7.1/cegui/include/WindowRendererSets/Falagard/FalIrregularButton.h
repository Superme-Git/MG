/***********************************************************************
    filename:   FalIrregularFigure.h
 ***************************************************************************/
#ifndef _FalIrregularButton_h_
#define _FalIrregularButton_h_

#include "FalModule.h"
#include "../../elements/CEGUIIrregularButton.h"

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
    class FALAGARDBASE_API FalagardIrregularButton : public WindowRenderer
    {
    public:
        static const utf8   TypeName[];       //!< type name for this widget.

        /*!
        \brief
            Constructor
        */
        FalagardIrregularButton(const String& type);
		FalagardIrregularButton(){}

        void render();

		virtual void clone(const WindowRenderer* templateRenderer){ WindowRenderer::clone(templateRenderer); }

    };

} // End of  CEGUI namespace section


#endif  // end of guard _FalButton_h_
