/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxJoinTeamLinkComponent.h"
#include "CEGUIFontManager.h"
#include "CEGUIFont.h"
#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{
	
RichEditboxJoinTeamLinkComponent::RichEditboxJoinTeamLinkComponent(const String& text, int64_t leaderid)
: RichEditboxLinkTextComponent(text)
, _teamleaderid(leaderid)
{
	d_Type = RichEditboxComponentType_JoinTeam;
}

bool RichEditboxJoinTeamLinkComponent::onMouseButtonUp(MouseEventArgs& e)
{
	if (e.button==LeftButton||e.button==RightButton)
	{
		JoinTeamLinkClicked *func = System::getSingleton().GetDefaultJoinTeamLinkFunc();
		if (func)
		{
			(*func)(_teamleaderid);
		}
	}
	return RichEditboxLinkTextComponent::onMouseButtonUp(e);
}

bool RichEditboxJoinTeamLinkComponent::canSplit()
{
	return true;
}

RichEditboxJoinTeamLinkComponent* RichEditboxJoinTeamLinkComponent::split(float split_point, bool first_component)
{
	Font* fnt = d_font ? d_font : System::getSingleton().getDefaultFont();

	// This is checked, but should never fail, since if we had no font our
	// extent would be 0 and we would never cause a split to be needed here.
	if (!fnt)
		throw InvalidRequestException("RenderedStringTextComponent::split: "
		"unable to split with no font set.");

	// create 'left' side of split and clone our basic configuration
	RichEditboxJoinTeamLinkComponent* lhs = new RichEditboxJoinTeamLinkComponent(getText(),_teamleaderid);

	lhs->d_font = d_font;
	lhs->d_colours = d_colours;

	// calculate the 'best' place to split the text

	size_t cutcount = fnt->getCharAtPixel(d_text, split_point);
	// perform the split.
	lhs->d_text = d_text.substr(cutcount, d_text.length() - cutcount);
	lhs->SetLineSpace(GetLineSpace());
	d_text = d_text.substr(0, cutcount);

	lhs->d_HoverColours = d_HoverColours;
	lhs->d_PushedColours = d_PushedColours;
	lhs->d_Pushed = d_Pushed;
	lhs->d_ClickEventID = d_ClickEventID;
	lhs->dataID = dataID;
	lhs->d_UnderLine = d_UnderLine;
	lhs->d_TextHorizontalCenter = d_TextHorizontalCenter;
	lhs->d_TextVerticalCenter = d_TextHorizontalCenter;
	lhs->SetUserID(GetUserID());

	return lhs;
}

} // End of  CEGUI namespace section
