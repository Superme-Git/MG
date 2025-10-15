/***********************************************************************
filename: 	CEGUIRichEditbox.h
*************************************************************************/

#ifndef _CEGUIRichEditboxRequestOtherQuestComponent_h_
#define _CEGUIRichEditboxRequestOtherQuestComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif

// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxRequestOtherQuestComponent :public RichEditboxLinkTextComponent
	{
	public:
		//! Destructor.
		virtual ~RichEditboxRequestOtherQuestComponent() {}
		RichEditboxRequestOtherQuestComponent(const String& text, int64_t dialogID);
		bool onMouseButtonUp(MouseEventArgs& e);
		bool canSplit(){ return true; }
		RichEditboxRequestOtherQuestComponent* split(float split_point, bool first_component);
	private:
		int64_t _targetQuestID;
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
