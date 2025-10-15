/***********************************************************************
	filename: 	CEGUIRichEditbox.h
*************************************************************************/

#ifndef _CEGUIRichEditboxJoinTeamLinkComponent_h_
#define _CEGUIRichEditboxJoinTeamLinkComponent_h_

#include "CEGUIRichEditboxLinkTextComponent.h"

#if defined(_MSC_VER)
#	pragma warning(push)
#	pragma warning(disable : 4251)
#endif


// Start of CEGUI namespace section
namespace CEGUI
{
	class CEGUIEXPORT RichEditboxJoinTeamLinkComponent:public RichEditboxLinkTextComponent 
	{
	public:
		//! Destructor.
		virtual ~RichEditboxJoinTeamLinkComponent() {}
		RichEditboxJoinTeamLinkComponent(const String& text, int64_t leaderid);
		bool onMouseButtonUp(MouseEventArgs& e);
		bool canSplit();
		RichEditboxJoinTeamLinkComponent* split(float split_point, bool first_component);
	private:
		int64_t _teamleaderid;
	};
}

#if defined (_MSC_VER)
#	pragma warning(pop)
#endif

#endif // end of guard _CEGUICentredRenderedString_h_
