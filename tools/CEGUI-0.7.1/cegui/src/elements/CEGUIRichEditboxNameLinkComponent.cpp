/***********************************************************************
    filename:   CEGUIRichEditboxTextComponent.cpp
 *************************************************************************/

#include "elements/CEGUIRichEditboxNameLinkComponent.h"

#include "CEGUISystem.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIRichEditbox_xmlHandler.h"

// Start of CEGUI namespace section

namespace CEGUI
{

#define NameColor0 0xFFBAFFF6
#define NameColor1 0xFF06db97

RichEditboxNameLinkComponent::~RichEditboxNameLinkComponent()
{

}
//----------------------------------------------------------------------------//
RichEditboxNameLinkComponent::RichEditboxNameLinkComponent(int64_t roleID,const String& roleName) :
d_roleID(roleID),
d_roleName(roleName),
d_NameColorType(0)
{
	d_Type=RichEditboxComponentType_NameLink;
	setColours(NameColor0);
	SetRoleName(roleName);
}

String RichEditboxNameLinkComponent::GenerateParseText()
{
	String parseText;
	parseText+="<";
	parseText+=RichEditbox_xmlHandler::NameLinkElement;
	if (getText().size()>0)
	{
		parseText+=" "+RichEditbox_xmlHandler::ObjRoleID;
		parseText+="=";
		parseText+='"';
		parseText+=PropertyHelper::int64_tToString(d_roleID);
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::RoleName;
		parseText+="=";
		parseText+='"';
		parseText+=d_roleName;
		parseText+='"';

		parseText+=" "+RichEditbox_xmlHandler::ColorAttribute;
		parseText+="=";
		parseText+='"';
		parseText+=PropertyHelper::intToString(d_NameColorType);
		parseText+='"';


	}
	parseText+=">";
	parseText+="</";
	parseText+=RichEditbox_xmlHandler::NameLinkElement;
	parseText+=	">";

	return parseText;
}

String RichEditboxNameLinkComponent::GeneratePureText(size_t startPos,size_t endPos)
{
	return d_roleName;
}



bool RichEditboxNameLinkComponent::onMouseButtonDown(MouseEventArgs& e)
{
	OnNameLinkClick* pFunc=System::getSingleton().GetNameClickFunc();
	if (pFunc)
	{
		(*pFunc)(d_roleID,d_roleName);
	}
	RichEditboxLinkTextComponent::onMouseButtonDown(e);
	return true;
}
bool RichEditboxNameLinkComponent::onMouseButtonUp(MouseEventArgs& e)
{
	
	RichEditboxLinkTextComponent::onMouseButtonUp(e);
	return true;
}

void RichEditboxNameLinkComponent::SetRoleName(const String& name)
{
	d_roleName=name;
	setText("["+d_roleName+"]");
}

void RichEditboxNameLinkComponent::SetNameColorType(int type)
{
	d_NameColorType=type;
	if (type==0)
	{
		setColours(NameColor0);
	}else if (type==1)
	{
		setColours(NameColor1);
	}
}

RichEditboxNameLinkComponent* RichEditboxNameLinkComponent::split(float split_point, bool first_component)
{
	RichEditboxNameLinkComponent* lhs = new RichEditboxNameLinkComponent(d_roleID, d_roleName);
	clone2(lhs, split_point);
	return lhs;
}





//----------------------------------------------------------------------------//

} // End of  CEGUI namespace section
