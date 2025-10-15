/***********************************************************************
filename: 	CEGUIRichEditbox.h
*************************************************************************/

#ifndef _CEGUIRichEditboxOpenDialogComponent_h_
#define _CEGUIRichEditboxOpenDialogComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxOpenDialogComponent :public RichEditboxLinkTextComponent
	{
	public:
		//! Destructor.
		virtual ~RichEditboxOpenDialogComponent() {}
		RichEditboxOpenDialogComponent(const String& text, int64_t dialogID);
		bool onMouseButtonUp(MouseEventArgs& e);

		bool canSplit(){ return true; }
		RichEditboxOpenDialogComponent* split(float split_point, bool first_component);

	private:
		int64_t _targetDialogID;
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
