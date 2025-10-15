/***********************************************************************
	filename: 	CEGUIRichEditbox.h
*************************************************************************/

#ifndef _CEGUIRichEditboxRequestTeamLinkComponent_h_
#define _CEGUIRichEditboxRequestTeamLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxRequestTeamLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxRequestTeamLinkComponent() {}
		RichEditboxRequestTeamLinkComponent(const String& text, int64_t teamid);
		bool onMouseButtonDown(MouseEventArgs& e);
		bool onMouseButtonUp(MouseEventArgs& e);
		bool canSplit(){ return true; }
		RichEditboxRequestTeamLinkComponent* split(float split_point, bool first_component);
	private:
		int64_t _teamid;
		Point mDownPos;
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
