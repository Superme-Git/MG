/***********************************************************************
    filename:   FalIrregularFigure.h
 ***************************************************************************/
#ifndef _FalAnimateText_h_
#define _FalAnimateText_h_

#include "FalModule.h"
#include "../../elements/CEGUIAnimateText.h"
#include "FalStaticText.h"

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
    class FALAGARDBASE_API FalagardAnimateText : public FalagardStaticText
    {
    public:
        static const utf8   TypeName[];       //!< type name for this widget.

        /*!
        \brief
            Constructor
        */
        FalagardAnimateText(const String& type);
		FalagardAnimateText(){}
		~FalagardAnimateText();


        void render();

		virtual void clone(const WindowRenderer* templateRenderer)
		{
			FalagardStaticText::clone(templateRenderer);
			const FalagardAnimateText* t = dynamic_cast<const FalagardAnimateText*>(templateRenderer);
			assert(t);
			m_lastDrawText = t->m_lastDrawText;
		}

    private:
        String m_lastDrawText;
    };

} // End of  CEGUI namespace section


#endif  // end of guard _FalButton_h_
