/***********************************************************************
	filename: 	CEGUIRichEditbox.h
	purpose:	Interface to the Rich Content edit box base class.
*************************************************************************/

#ifndef _CEGUIRichEditboxBreakLineComponent_h_
#define _CEGUIRichEditboxBreakLineComponent_h_

#include "CEGUIRichEditboxComponent.h"
//#include "../CEGUIWindow.h"
//#include "../CEGUIFont.h"
//#include "CEGUIRichEditboxProperties.h"
//
//#include <vector>


#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxBreakLineComponent:public RichEditboxComponent 
	{
	public:
		//! Destructor.
		~RichEditboxBreakLineComponent();
		RichEditboxBreakLineComponent();
		String GenerateParseText();

		bool isOnlySpace() { return true; }
		int GetCharCount() { return 0; }
		
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
