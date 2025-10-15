/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxFamilyRecruitLinkComponent.h"

#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section
namespace CEGUI
{

RichEditboxFamilyRecruitLinkComponent::~RichEditboxFamilyRecruitLinkComponent()
{

}
//----------------------------------------------------------------------------//
RichEditboxFamilyRecruitLinkComponent::RichEditboxFamilyRecruitLinkComponent(int familyID,const String& familyName,
																			 int64_t leaderID,const String& leader,int rank,int level,const String& aim):
d_FamilyID(familyID),
d_FamilyName(familyName),
d_LeaderName(leader),
d_FamilyRank(rank),
d_FamilyLevel(level),
d_FamilyAim(aim),
d_LeaderID(leaderID)
{
	d_Type=RichEditboxComponentType_NameLink;
	setColours(0xffffff33);
	setText("["+d_FamilyName+"]");
	//EnableUnderLine(true);
}

String RichEditboxFamilyRecruitLinkComponent::GenerateParseText()
{
	String parseText;
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::FamilyRecruitElement;
	if (getText().size()>0)
	{
		parseText+=" "+RichEditbox_xmlHandler::FamilyID;
		parseText+="=";
		parseText+='"';
		parseText+=PropertyHelper::intToString(d_FamilyID);
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::FamilyName;
		parseText+="=";
		parseText+='"';
		parseText+=d_FamilyName;
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::FamilyLeaderID;
		parseText+="=";
		parseText+='"';
		parseText+=PropertyHelper::int64_tToString(d_LeaderID);
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::FamilyLeader;
		parseText+="=";
		parseText+='"';
		parseText+=d_LeaderName;
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::FamilyLevel;
		parseText+="=";
		parseText+='"';
		parseText+=PropertyHelper::intToString(d_FamilyLevel);
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::FamilyRank;
		parseText+="=";
		parseText+='"';
		parseText+=PropertyHelper::intToString(d_FamilyRank);
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::FamilyAim;
		parseText+="=";
		parseText+='"';
		parseText+=d_FamilyAim;
		parseText+='"';
	}
	parseText+=">";
	parseText+="</";
	parseText+=RichEditbox_xmlHandler::FamilyRecruitElement;
	parseText+=	">";

	return parseText;
}

String RichEditboxFamilyRecruitLinkComponent::GeneratePureText(size_t startPos,size_t endPos)
{
	return d_FamilyName;
}



bool RichEditboxFamilyRecruitLinkComponent::onMouseButtonDown(MouseEventArgs& e)
{
	OnFamilyRecruitClick* pFunc=System::getSingleton().GetFamilyRecruitFunc();
	if (pFunc)
	{
		(*pFunc)(d_FamilyID,d_FamilyName,d_LeaderID,d_LeaderName,d_FamilyRank,d_FamilyLevel,d_FamilyAim);
	}
	RichEditboxLinkTextComponent::onMouseButtonDown(e);
	return true;
}
bool RichEditboxFamilyRecruitLinkComponent::onMouseButtonUp(MouseEventArgs& e)
{
	
	RichEditboxLinkTextComponent::onMouseButtonUp(e);
	return true;
}

RichEditboxFamilyRecruitLinkComponent* RichEditboxFamilyRecruitLinkComponent::split(float split_point, bool first_component)
{
	RichEditboxFamilyRecruitLinkComponent* lhs = new RichEditboxFamilyRecruitLinkComponent(d_FamilyID,d_FamilyName,d_LeaderID,d_LeaderName,d_FamilyRank,d_FamilyLevel,d_FamilyAim);
	clone2(lhs, split_point);
	return lhs;
}







//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
