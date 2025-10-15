/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxCommonLinkComponent_h_
#define _CEGUIRichEditboxCommonLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxCommonLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxCommonLinkComponent();

		RichEditboxCommonLinkComponent(const String &text, const String &arg);
		
		 virtual String GenerateParseText();
		 Point GetOffsetOfIndex(size_t index);

		 bool onMouseButtonUp(MouseEventArgs& e);

		 const String& GetArg() { return arg; }

		 bool canSplit(){ return true; }
		 RichEditboxCommonLinkComponent* split(float split_point, bool first_component);

	private:
		String arg;
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
