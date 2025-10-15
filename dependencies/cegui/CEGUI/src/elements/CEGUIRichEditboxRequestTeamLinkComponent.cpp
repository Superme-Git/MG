/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxRequestTeamLinkComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	
	RichEditboxRequestTeamLinkComponent::RichEditboxRequestTeamLinkComponent(const String& text, int64_t teamid)
	: RichEditboxLinkTextComponent(text)
	, _teamid(teamid)
{
	d_Type = RichEditboxComponentType_RequestTeam;
}

bool RichEditboxRequestTeamLinkComponent::onMouseButtonDown(MouseEventArgs& e)
{
	mDownPos = e.position;
	return RichEditboxLinkTextComponent::onMouseButtonDown(e);
}

bool RichEditboxRequestTeamLinkComponent::onMouseButtonUp(MouseEventArgs& e)
{
	if (e.button==LeftButton||e.button==RightButton)
	{
		RequestTeamLinkClicked *func = System::getSingleton().GetDefaultRequestTeamLinkFunc();
		if (func)
		{
			(*func)(_teamid);
		}
	}
	return RichEditboxLinkTextComponent::onMouseButtonUp(e);
}

RichEditboxRequestTeamLinkComponent* RichEditboxRequestTeamLinkComponent::split(float split_point, bool first_component)
{
	RichEditboxRequestTeamLinkComponent* lhs = new RichEditboxRequestTeamLinkComponent(getText(), _teamid);
	clone2(lhs, split_point);
	return lhs;
}

} // End of  CEGUI namespace section
